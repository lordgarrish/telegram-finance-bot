package lordgarrish.telegramfinancebot.service;

import lombok.extern.slf4j.Slf4j;
import lordgarrish.telegramfinancebot.entity.User;
import lordgarrish.telegramfinancebot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean saveUser(User user) {
        if(!userRepository.existsById(user.getChatId())) {
            userRepository.save(user);
            log.info(user + " saved to database");
            return true;
        }
        log.warn(user + " is already saved to database");
        return false;
    }
}
