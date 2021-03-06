package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.Constants;
import com.jazasoft.mt.assember.RoleAssembler;
import com.jazasoft.mt.dto.RestError;
import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.entity.master.Role;
import com.jazasoft.mt.service.CompanyService;
import com.jazasoft.mt.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(ApiUrls.ROOT_URL_ROLES)
public class RoleRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(RoleRestController.class);

    @Autowired RoleService roleService;

    @Autowired RoleAssembler roleAssembler;

    @Autowired CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getAllRoles(HttpServletRequest req) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("getAllRoles(): tenant = {}", company != null ? company.getName() : "");
        List<Role> roles = roleService.findAll(company);
        Resources resources = new Resources(roleAssembler.toResources(roles), linkTo(RoleRestController.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_ROLES_ROLE)
    public ResponseEntity<?> getRole(HttpServletRequest req, @PathVariable("roleId") Long roleId) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("getRole(): tenant = {},  roleId = {}", company != null ? company.getName() : "", roleId);
        if (!roleService.exists(roleId)) {
            return ResponseEntity.notFound().build();
        }
        if (company != null && !roleService.exists(company,roleId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Role role = roleService.findOne(roleId);
        return ResponseEntity.ok(roleAssembler.toResource(role));
    }

    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest req, @Valid @RequestBody Role role){
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("save(): tenant = {}", company != null ? company.getName() : "");
        if (company != null) {
            role.setCompany(company);
        }else {
            if (role.getCompanyId() != null && !companyService.exists(role.getCompanyId())) {
                RestError error = new RestError(404, 40401,"Company with Id=" + role.getCompanyId() + " not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }
        role = roleService.save(role);
        Link selfLink = linkTo(RoleRestController.class).slash(role.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }

    @PutMapping(ApiUrls.URL_ROLES_ROLE)
    public ResponseEntity<?> update(HttpServletRequest req, @PathVariable("roleId") Long roleId, @Valid @RequestBody Role role){
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("update(): tenant = {}, roleId = {}", company != null ? company.getName() : "", roleId);
        if (!roleService.exists(roleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (company != null && !roleService.exists(company, roleId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        role.setId(roleId);
        role = roleService.update(role);
        return ResponseEntity.ok(roleAssembler.toResource(role));
    }
}
