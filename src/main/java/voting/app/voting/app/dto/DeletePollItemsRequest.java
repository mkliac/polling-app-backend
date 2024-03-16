package voting.app.voting.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeletePollItemsRequest {
    @Size(min = 1, message = "At least one item is required")
    @Schema(description = "List of ids for poll items", required = true)
    private List<String> ids;
}
