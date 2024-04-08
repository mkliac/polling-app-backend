package polling.app.polling.app.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import polling.app.polling.app.dto.poll.PollDto;
import polling.app.polling.app.dto.poll.PollItemDto;
import polling.app.polling.app.dto.poll.SavePollRequest;
import polling.app.polling.app.dto.poll.UpdatePollRequest;
import polling.app.polling.app.model.bookmark.BookmarkId;
import polling.app.polling.app.model.common.CountByIdResult;
import polling.app.polling.app.model.poll.Poll;
import polling.app.polling.app.model.poll.PollItem;
import polling.app.polling.app.model.user.User;
import polling.app.polling.app.model.vote.SingleVote;
import polling.app.polling.app.model.vote.VoteId;
import polling.app.polling.app.repository.BookmarkRepository;
import polling.app.polling.app.repository.VoteRepository;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, PollItemMapper.class})
public abstract class PollMapper {
    @Autowired private VoteRepository voteRepository;

    @Autowired private BookmarkRepository bookmarkRepository;

    @Autowired private MongoTemplate mongoTemplate;

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createdBy", target = "createdBy")
    public abstract Poll fromSavePollRequest(User createdBy, SavePollRequest request);

    public void updatePoll(UpdatePollRequest request, Poll poll) {
        poll.setTitle(request.getTitle());
        poll.setDescription(request.getDescription());
        poll.setClosedDate(request.getClosedDate());
        poll.getItems().removeIf(item -> request.getRemoveItemIds().contains(item.getId()));
        poll.getItems().addAll(fromItemNames(request.getAddItemTexts().stream().toList()));
    }

    @Mapping(source = "name", target = "text")
    public abstract PollItem fromItemName(String name);

    public abstract List<PollItem> fromItemNames(List<String> names);

    public abstract PollDto toPollDto(Poll poll);

    public abstract List<PollDto> toPollDtos(List<Poll> polls);

    // Add details to the pollDto
    public PollDto toPollDto(Poll poll, User user) {
        return toPollDtos(List.of(poll), user).get(0);
    }

    public List<PollDto> toPollDtos(List<Poll> polls, User user) {
        List<PollDto> pollDtos = toPollDtos(polls);
        Set<String> bookmarkedPollIds =
                bookmarkRepository
                        .findAllByBookmarkIdIn(
                                pollDtos.stream()
                                        .map(
                                                pollDto ->
                                                        new BookmarkId(
                                                                user.getId(), pollDto.getId()))
                                        .toList())
                        .stream()
                        .map(bookmark -> bookmark.getBookmarkId().getPollId())
                        .collect(Collectors.toSet());
        Set<String> votedPollItemIds =
                voteRepository
                        .findAllByVoteIdIn(
                                pollDtos.stream()
                                        .map(pollDto -> new VoteId(user.getId(), pollDto.getId()))
                                        .toList())
                        .stream()
                        .map(SingleVote::getPollItemId)
                        .collect(Collectors.toSet());

        Aggregation aggregation =
                Aggregation.newAggregation(
                        Aggregation.match(
                                Criteria.where("pollItemId")
                                        .in(
                                                pollDtos.stream()
                                                        .map(PollDto::getItems)
                                                        .flatMap(List::stream)
                                                        .map(PollItemDto::getId)
                                                        .toList())),
                        Aggregation.group("pollItemId").count().as("count"));
        AggregationResults<CountByIdResult> results =
                mongoTemplate.aggregate(aggregation, "single_vote", CountByIdResult.class);
        Map<String, Integer> voteCounts =
                results.getMappedResults().stream()
                        .collect(
                                Collectors.toMap(
                                        CountByIdResult::getId, CountByIdResult::getCount));

        pollDtos.forEach(
                pollDto -> {
                    // Set owner to true if the poll is created by the current user
                    pollDto.setOwner(pollDto.getCreatedBy().getEmail().equals(user.getId()));

                    // Set createdBy to null if the poll is anonymous
                    if (pollDto.isAnonymous()) {
                        pollDto.setCreatedBy(null);
                    }

                    pollDto.setBookmarked(bookmarkedPollIds.contains(pollDto.getId()));
                    pollDto.getItems()
                            .forEach(
                                    item -> {
                                        item.setVoteCount(voteCounts.getOrDefault(item.getId(), 0));
                                        item.setVoted(votedPollItemIds.contains(item.getId()));
                                    });
                });
        return pollDtos;
    }
}
