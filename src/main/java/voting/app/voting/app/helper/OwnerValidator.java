package voting.app.voting.app.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.model.User;
import voting.app.voting.app.model.common.CreatableEntity;

@Component
public class OwnerValidator {
    public <T extends CreatableEntity> Boolean isOwner(User user, T object) {
        return user.getId().equals(object.getCreatedBy().getId());
    }

    public <T extends CreatableEntity> void isOwnerOrElseThrow(User user, T object) {
        if (!isOwner(user, object)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner");
        }
    }
}
