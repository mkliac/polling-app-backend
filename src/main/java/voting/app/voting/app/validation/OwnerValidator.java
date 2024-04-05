package voting.app.voting.app.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.model.common.CreatableEntity;
import voting.app.voting.app.model.user.User;

@Component
public class OwnerValidator {
    public <T extends CreatableEntity> boolean isOwner(User user, T object) {
        return user.getId().equals(object.getCreatedBy().getId());
    }

    public <T extends CreatableEntity> void isOwnerOrElseThrow(User user, T object) {
        if (!isOwner(user, object)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner");
        }
    }
}
