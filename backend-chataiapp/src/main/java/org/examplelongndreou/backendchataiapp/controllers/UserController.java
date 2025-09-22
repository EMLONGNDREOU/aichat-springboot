package org.examplelongndreou.backendchataiapp.controllers;

import org.examplelongndreou.backendchataiapp.exceptions.ExcStatus;
import org.examplelongndreou.backendchataiapp.models.User;
import org.examplelongndreou.backendchataiapp.models.dtos.JwtDTO;
import org.examplelongndreou.backendchataiapp.services.Singleton;
import org.examplelongndreou.backendchataiapp.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private JwtEncoder jwtEncoder;

    @Autowired
    public UserController(UserService userService,
                          JwtEncoder jwtEncoder
    ) {
        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
//        this.chatController = chatController;

        logger.debug("UserController initialized with ChatService: " + userService);
        Singleton Singleton0 = Singleton.getInstance();
        Singleton Singleton1 = Singleton.getInstance();

    }


    public UserController() {

    }

    @GetMapping("/users")
    public List<User> getAllUsers(Authentication authentication) throws ExcStatus {

        User loggedInUser = userService.getLoggedInUser(authentication);

        if (!loggedInUser.getRole_id().equals(1)){//getRole().equals("3")) {
            throw new ExcStatus(HttpStatus.FORBIDDEN, "You are not allowed to access this resource.");
        }

        return userService.getAll();
    }

    @GetMapping("/users/{username}")
    public User getUserByUserName(@PathVariable String username, Authentication authentication) throws ExcStatus {
        User loggedInUser = userService.getLoggedInUser(authentication);

        boolean isAdmin = "1".equals(loggedInUser.getRole_id());
        boolean isSelf = username.equals(loggedInUser.getUsername());

        if (!(isAdmin || isSelf)) {
            throw new ExcStatus(HttpStatus.FORBIDDEN, "You are not allowed to access this resource.");
        }

        return userService.getUserByUsername(username);
    }


    @GetMapping("/login")
    public JwtDTO login(Authentication authentication) {

        JwtDTO jwtDTO = userService.generateJwtForAuthUser(authentication);

        return jwtDTO;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws Exception {

        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
        user.setId(id);
        return userService.updateUser(user);
    }

  /*
    @DeleteMapping("/users/{id}")

    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserAndRelatedData(id);
        return ResponseEntity.ok().build();
    }
    */


}