package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.Constants;
import com.jazasoft.mt.assember.UserAssembler;
import com.jazasoft.mt.dto.RestError;
import com.jazasoft.mt.dto.UserDto;
import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.service.CompanyService;
import com.jazasoft.mt.service.RoleService;
import com.jazasoft.mt.service.UserService;
import com.jazasoft.mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_USERS)
public class UserRestController {

    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired UserService userService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    CompanyService companyService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserAssembler userAssembler;

    @GetMapping
    public ResponseEntity<?> listAllUsers(HttpServletRequest req, @RequestParam(value = "after", defaultValue = "0") Long after) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        logger.debug("listAllUsers(): tenant = {}", company != null ? company.getName() : "");
        List<User> users;
        if (company != null) {
            users = userService.findAllByCompanyAfter(company, after);
        }else {
            users = userService.findAllAfter(after);
        }
        Resources resources = new Resources(userAssembler.toResources(users), linkTo(UserRestController.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> getUser(@PathVariable("userId") long id) {
        logger.debug("getUser(): id = {}",id);
        User user = userService.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        logger.debug("createUser():\n {}", user.toString());
        RestError error;
        if (user.getCompanyId() != null && !companyService.exists(user.getCompanyId())) {
            error = new RestError(404, 40401,"Company with Id=" + user.getCompanyId() + " not found","","");
            return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        }
        else if (user.getRoles() != null) {
            StringBuilder builder = new StringBuilder();
            for(String role: Utils.getRoleList(user.getRoles())){
                if (!roleService.exists(role)){
                    builder.append(role).append(",");
                }
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length()-1);
                error = new RestError(404, 40401,"["+builder.toString() +"] not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }

        user = userService.save(user);
        Link selfLink = linkTo(UserRestController.class).slash(user.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }

    @PatchMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> updateUser(@PathVariable("userId") long id,@Validated @RequestBody UserDto userDto) {
        logger.debug("updateUser(): id = {}",id);
        if (!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        User user = userService.update(userDto);
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long id) {
        logger.debug("deleteUser(): id = {}",id);
        if (!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(ApiUrls.URL_USERS_USER_SEARCH_BY_USERNAME)
    public ResponseEntity<?> searchByName(@RequestParam("username") String username){
        logger.debug("searchByUsername(): username = {}",username);
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_USERS_USER_SEARCH_BY_EMAIL)
    public ResponseEntity<?> searchByEmail(@RequestParam("email") String email){
        logger.debug("searchByName(): name = {}",email);
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @PutMapping(ApiUrls.URL_USERS_USER_PROFILE)
    public ResponseEntity<?> updateProfile() {
        return null;
    }
}
