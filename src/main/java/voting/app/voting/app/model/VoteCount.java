package voting.app.voting.app.model;

import lombok.Data;

@Data
public class VoteCount {
    private String pollItemId;

    private Long count;
}
