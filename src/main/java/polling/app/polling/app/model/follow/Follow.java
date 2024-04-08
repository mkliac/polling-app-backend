package polling.app.polling.app.model.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "follow")
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    @Id private FollowId followId;
}
