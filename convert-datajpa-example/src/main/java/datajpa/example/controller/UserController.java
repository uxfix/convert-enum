package datajpa.example.controller;

import datajpa.example.entity.User;
import datajpa.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    public User saveUser(@RequestBody User user) {
        userRepository.save(user);
        log.info("saveUser:{}", user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        User user = userRepository.findById(id).get();
        log.info("getUser:{}", user);
        return user;
    }
}
