package voting.app.voting.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class SavePollRequest {
    @NotEmpty(message = "Title is required")
    @Schema(description = "Title of the poll", required = true)
    private String title;

    @Schema(description = "Description of the poll")
    private String description = "";

    @Size(min = 2, message = "At least two items are required")
    @Schema(description = "List of items for the poll", required = true)
    private List<String> items;

    @NotNull(message = "isPrivate is required")
    @Schema(description = "Is the poll private", required = true)
    private Boolean isPrivate;

    @Schema(description = "Date when the poll will be closed")
    private Instant closedDate;

    @NotNull(message = "isAnonymous is required")
    @Schema(description = "Is the poll anonymous", required = true)
    private Boolean isAnonymous;
}
