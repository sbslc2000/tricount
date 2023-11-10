package goorm.tricount.domain.user;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.domain.user.dto.CreateUserRequestDto;
import goorm.tricount.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(CreateUserRequestDto dto) {
        return UserDto.of(userRepository.save(new User(dto.getUsername(), dto.getNickname(), dto.getPassword())));
    }

    public UserDto getUser(Long userId) {
        return UserDto.of(userRepository.findById(userId));
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);

        if(!user.getPassword().equals(password)) {
            throw new ClientFaultException(ErrorCode.LOGIN_FAILED);
        }

        return user;
    }
}
