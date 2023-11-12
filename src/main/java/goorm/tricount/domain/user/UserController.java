package goorm.tricount.domain.user;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.api.response.BaseResponseTemplate;
import goorm.tricount.domain.user.UserService;
import goorm.tricount.domain.user.dto.CreateUserRequestDto;
import goorm.tricount.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequestDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }
}
