package polling.app.polling.app.dto.poll;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import lombok.Data;
import polling.app.polling.app.validation.NonEmptyFields;
import polling.app.polling.app.validation.ValidDate;

@Data
public class SavePollRequest {
    @NotEmpty(message = "Title is required")
    @Schema(description = "Title of the poll", required = true)
    private String title;

    @Schema(description = "Description of the poll")
    private String description = "";

    @Size(min = 2, message = "At least two items are required")
    @NonEmptyFields(message = "Item cannot be empty or null")
    @Schema(description = "List of items for the poll", required = true)
    private Set<String> items;

    @NotNull(message = "isPrivate is required")
    @Schema(description = "Is the poll private", required = true)
    private boolean isPrivate;

    @ValidDate(isFuture = true, nullable = true, message = "Closed date must be in the future")
    @Schema(description = "Date when the poll will be closed")
    private Instant closedDate;

    @NotNull(message = "isAnonymous is required")
    @Schema(description = "Is the poll anonymous", required = true)
    private boolean isAnonymous;
}
