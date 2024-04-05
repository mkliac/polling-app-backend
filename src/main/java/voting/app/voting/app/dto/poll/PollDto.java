package voting.app.voting.app.dto.poll;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import voting.app.voting.app.dto.user.UserDto;
import voting.app.voting.app.model.poll.PollPriority;

@Data
public class PollDto {
    private String id;

    private String title;

    private String description;

    private List<PollItemDto> items;

    private PollPriority priority;

    private boolean isPrivate;

    private boolean isAnonymous;

    private boolean isBookmarked;

    private boolean isOwner;

    private Instant closedDate;

    private UserDto createdBy;

    private Instant createdAt;
}