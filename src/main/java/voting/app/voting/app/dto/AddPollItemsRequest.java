package voting.app.voting.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AddPollItemsRequest {
    @Size(min = 1)
    @Schema(description = "List of texts for poll items", required = true)
    private List<String> texts;
}
