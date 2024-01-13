package voting.app.voting.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AddPollItemsRequest {
    @Size(min = 1)
    private List<String> texts;
}
