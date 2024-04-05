package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import voting.app.voting.app.dto.poll.PollItemDto;
import voting.app.voting.app.model.poll.PollItem;
import voting.app.voting.app.repository.VoteRepository;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PollItemMapper {
    @Autowired private VoteRepository voteRepository;

    public abstract PollItemDto toPollItemDto(PollItem pollItem);

    public abstract List<PollItemDto> toPollItemDtos(List<PollItem> pollItems);
}
