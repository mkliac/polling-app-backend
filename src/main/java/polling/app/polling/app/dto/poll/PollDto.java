package polling.app.polling.app.dto.poll;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import polling.app.polling.app.dto.user.UserDto;
import polling.app.polling.app.model.poll.PollPriority;

@Data
public class PollDto {
    @Schema(description = "Id of the poll")
    private String id;

    @Schema(description = "Title of the poll")
    private String title;

    @Schema(description = "Description of the poll")
    private String description;

    @Schema(description = "List of options for the poll")
    private List<PollItemDto> items;

    @Schema(description = "Priority of the poll")
    private PollPriority priority;

    @Schema(description = "Visibility of the poll")
    private boolean isPrivate;

    @Schema(description = "Is Anonymous voting")
    private boolean isAnonymous;

    @Schema(description = "Is the poll bookmarked by the current user")
    private boolean isBookmarked;

    @Schema(description = "Is the poll owned by the current user")
    private boolean isOwner;

    @Schema(description = "Closed date of the poll")
    private Instant closedDate;

    @Schema(description = "Creator of the poll")
    private UserDto createdBy;

    @Schema(description = "Creation date of the poll")
    private Instant createdAt;
}
