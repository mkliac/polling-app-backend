package polling.app.polling.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import polling.app.polling.app.model.poll.PollItem;
import polling.app.polling.app.dto.poll.PollItemDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PollItemMapper {

    PollItemDto toPollItemDto(PollItem pollItem);

    List<PollItemDto> toPollItemDtos(List<PollItem> pollItems);
}
