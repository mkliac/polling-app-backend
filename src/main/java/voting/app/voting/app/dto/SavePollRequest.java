package voting.app.voting.app.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SavePollRequest {
    private String title;

    private String description;

    private List<String> items;

    private Boolean isPrivate;

    private Instant closedDate;

    private Boolean isAnonymous;
}
