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
    public BaseResponse<UserDto> createUser(@RequestBody CreateUserRequestDto dto) {
        UserDto user = userService.createUser(dto);
        return BaseResponse.ok(user);
    }

    @GetMapping("/{userId}")
    public BaseResponse<UserDto> getUser(@PathVariable Long userId) {
        UserDto user = userService.getUser(userId);
        return BaseResponse.ok(user);
    }
}
