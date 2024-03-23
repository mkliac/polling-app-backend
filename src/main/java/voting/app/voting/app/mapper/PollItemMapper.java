package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import voting.app.voting.app.dto.PollItemDto;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.repository.VoteRepository;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PollItemMapper {
    @Autowired private VoteRepository voteRepository;

    @Mapping(source = "id", target = "voteCount", qualifiedByName = "countVotes")
    public abstract PollItemDto toPollItemDto(PollItem pollItem);

    public abstract List<PollItemDto> toPollItemDtos(List<PollItem> pollItems);

    @Named("countVotes")
    public Integer countVotes(String id) {
        return voteRepository.countByVoteIdPollItemId(id);
    }
}
