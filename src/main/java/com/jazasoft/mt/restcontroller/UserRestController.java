package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_USERS)
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId){
        User user = userService.findOne(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User user){
        user = userService.save(user);
        return ResponseEntity.ok(user);
    }


}
