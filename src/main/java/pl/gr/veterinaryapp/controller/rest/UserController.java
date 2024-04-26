package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gr.veterinaryapp.mapper.VetAppUserMapper;
import pl.gr.veterinaryapp.model.dto.UserDto;
import pl.gr.veterinaryapp.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final VetAppUserMapper vetAppUserMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return vetAppUserMapper.toUserDtoList(userService.getAllUsers());
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        return vetAppUserMapper.toUserDto(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return vetAppUserMapper.toUserDto(userService.getUser(id));
    }
}
