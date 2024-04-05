package voting.app.voting.app.model.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "single_vote")
@AllArgsConstructor
@NoArgsConstructor
public class SingleVote {
    @Id private VoteId voteId;

    @Indexed private String pollItemId;
}
