package voting.app.voting.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "vote")
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id private VoteId voteId;
}
