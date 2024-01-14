package voting.app.voting.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<User> voters = new HashSet<>();

    private Integer voteCount = 0;

    public void addVoter(User user) {
        voters.add(user);
        voteCount = voters.size();
    }

    public void deleteVoter(User user) {
        voters.remove(user);
        voteCount = voters.size();
    }
}
