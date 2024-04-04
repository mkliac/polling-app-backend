package voting.app.voting.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class UpdatePollRequest {
    @NotEmpty
    @Schema(description = "New title of the poll", required = true)
    private String title;

    @Schema(description = "New description of the poll")
    private String description;

    @Schema(description = "Set of items to be remove for the poll")
    private Set<String> removeItemIds = new HashSet<>();

    @Schema(description = "Set of items to be add for the poll")
    private Set<String> addItemTexts = new HashSet<>();

    @Schema(description = "New date when the poll will be closed")
    private Instant closedDate;
}
