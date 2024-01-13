package voting.app.voting.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "poll_item")
public class PollItem {
    @Id
    private String id;

    private String text;

    @DocumentReference(lazy = true)
    private Set<User> voters = new HashSet<>();
}
