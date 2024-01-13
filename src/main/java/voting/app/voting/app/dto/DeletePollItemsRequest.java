package voting.app.voting.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeletePollItemsRequest {
    @Size(min = 1)
    private List<String> ids;
}
