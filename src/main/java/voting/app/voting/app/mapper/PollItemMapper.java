package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import voting.app.voting.app.dto.poll.PollItemDto;
import voting.app.voting.app.model.poll.PollItem;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PollItemMapper {

    PollItemDto toPollItemDto(PollItem pollItem);

    List<PollItemDto> toPollItemDtos(List<PollItem> pollItems);
}
