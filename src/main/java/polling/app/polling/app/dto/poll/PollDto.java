package polling.app.polling.app.dto.poll;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import polling.app.polling.app.dto.user.UserDto;
import polling.app.polling.app.model.poll.PollPriority;

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
