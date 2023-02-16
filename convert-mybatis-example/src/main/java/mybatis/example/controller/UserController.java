package mybatis.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybatis.example.entity.User;
import mybatis.example.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserMapper userMapper;

    @PostMapping
    public User saveUser(@RequestBody User user) {
        userMapper.insert(user);
        log.info("saveUser:{}", user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        User user = userMapper.findUserById(id);
        log.info("getUser:{}", user);
        return user;
    }
}
