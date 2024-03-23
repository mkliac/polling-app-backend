package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import voting.app.voting.app.dto.PollDto;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.User;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, PollItemMapper.class})
public interface PollMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createdBy", target = "createdBy")
    Poll fromSavePollRequest(User createdBy, SavePollRequest request);

    @Mapping(source = "name", target = "text")
    PollItem fromItemName(String name);

    List<PollItem> fromItemNames(List<String> names);

    PollDto toPollDto(Poll poll);

    List<PollDto> toPollDtos(List<Poll> polls);
}
