package polling.app.polling.app.model.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteId {
    private String userId;

    private String pollId;
}
