package voting.app.voting.app.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiError {
    private Integer status;

    private String error;

    private String message;

    private String path;
}
