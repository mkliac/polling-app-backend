package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
