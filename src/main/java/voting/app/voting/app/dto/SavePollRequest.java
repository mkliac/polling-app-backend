package voting.app.voting.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SavePollRequest {
    @NotEmpty
    @Schema(description = "Title of the poll", required = true)
    private String title;

    @Schema(description = "Description of the poll")
    private String description = "";

    @Size(min = 2)
    @Schema(description = "List of items for the poll", required = true)
    private List<String> items;

    @NotNull
    @Schema(description = "Is the poll private", required = true)
    private Boolean isPrivate;

    @Schema(description = "Date when the poll will be closed")
    private Instant closedDate;

    @NotNull
    @Schema(description = "Is the poll anonymous", required = true)
    private Boolean isAnonymous;
}
