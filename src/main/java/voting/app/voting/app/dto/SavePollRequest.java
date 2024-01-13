package voting.app.voting.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SavePollRequest {
    @NotEmpty
    private String title;

    private String description;

    @Size(min = 2, max = 10)
    private List<String> items;

    @NotNull
    private Boolean isPrivate;

    private Instant closedDate;

    @NotNull
    private Boolean isAnonymous;
}
