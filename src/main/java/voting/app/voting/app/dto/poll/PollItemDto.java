package voting.app.voting.app.dto.poll;

import lombok.Data;

@Data
public class PollItemDto {
    private String id;

    private String text;

    private Integer voteCount;

    private boolean isVoted;
}
