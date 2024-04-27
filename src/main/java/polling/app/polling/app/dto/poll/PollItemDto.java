package polling.app.polling.app.dto.poll;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PollItemDto {
    @Schema(description = "Id of the poll item")
    private String id;

    @Schema(description = "Text of the poll item")
    private String text;

    @Schema(description = "Vote count of the poll item")
    private Integer voteCount;

    @Schema(description = "Is the poll item voted by the current user")
    private boolean isVoted;
}
