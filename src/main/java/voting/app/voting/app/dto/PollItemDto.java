package voting.app.voting.app.dto;

import lombok.Data;

@Data
public class PollItemDto {
    private String id;

    private String text;

    private Integer voteCount;
}
