package voting.app.voting.app.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import voting.app.voting.app.dto.PollDto;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.model.*;
import voting.app.voting.app.repository.BookmarkRepository;
import voting.app.voting.app.repository.VoteRepository;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, PollItemMapper.class})
public abstract class PollMapper {
    @Autowired private VoteRepository voteRepository;

    @Autowired private BookmarkRepository bookmarkRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createdBy", target = "createdBy")
    public abstract Poll fromSavePollRequest(User createdBy, SavePollRequest request);

    @Mapping(source = "name", target = "text")
    public abstract PollItem fromItemName(String name);

    public abstract List<PollItem> fromItemNames(List<String> names);

    public abstract PollDto toPollDto(Poll poll);

    public abstract List<PollDto> toPollDtos(List<Poll> polls);

    public PollDto toPollDto(Poll poll, User user) {
        PollDto pollDto = toPollDto(poll);
        pollDto.setBookmarked(
                bookmarkRepository
                        .findById(new BookmarkId(user.getId(), pollDto.getId()))
                        .isPresent());

        String voteItemId =
                voteRepository
                        .findAllByVoteIdIn(
                                pollDto.getItems().stream()
                                        .map(item -> new VoteId(user.getId(), item.getId()))
                                        .toList())
                        .stream()
                        .findFirst()
                        .map(Vote::getVoteId)
                        .map(VoteId::getPollItemId)
                        .orElse(null);
        System.out.println(voteItemId);
        pollDto.getItems().forEach(item -> item.setVoted(item.getId().equals(voteItemId)));

        return pollDto;
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
        pollDtos.forEach(
                pollDto -> pollDto.setBookmarked(bookmarkedPollIds.contains(pollDto.getId())));

        Set<String> votedPollItemIds =
                voteRepository
                        .findAllByVoteIdIn(
                                pollDtos.stream()
                                        .flatMap(
                                                pollDto ->
                                                        pollDto.getItems().stream()
                                                                .map(
                                                                        item ->
                                                                                new VoteId(
                                                                                        user
                                                                                                .getId(),
                                                                                        item
                                                                                                .getId())))
                                        .toList())
                        .stream()
                        .map(Vote::getVoteId)
                        .map(VoteId::getPollItemId)
                        .collect(Collectors.toSet());
        pollDtos.forEach(
                pollDto ->
                        pollDto.getItems()
                                .forEach(
                                        item ->
                                                item.setVoted(
                                                        votedPollItemIds.contains(item.getId()))));

        return pollDtos;
    }
}
