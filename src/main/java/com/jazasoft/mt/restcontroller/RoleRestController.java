package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.entity.master.Role;
import com.jazasoft.mt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_ROLES)
public class RoleRestController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping(ApiUrls.URL_ROLES_ROLE)
    public ResponseEntity<?> getRole(@PathVariable("roleId") Long roleId) {
        Role role = roleService.findOne(roleId);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Role role){
        role = roleService.save(role);
        return ResponseEntity.ok(role);
    }
}
