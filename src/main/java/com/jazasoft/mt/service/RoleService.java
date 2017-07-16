package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.Role;
import com.jazasoft.mt.repository.master.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class RoleService {
    private final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return roleRepository.findOne(id);
    }

    public Role findOneByName(String role) {
        return roleRepository.findOneByName("ROLE_" + role).orElse(null);
    }

    public List<Role> findAll(){
        LOGGER.debug("findAll");
        return roleRepository.findAll();
    }

    public Role save(Role role) {
        LOGGER.debug("save: role = {}", role);
        return roleRepository.save(role);
    }

    public long count() {
        return roleRepository.count();
    }

    public boolean exists(Long id) {
        return roleRepository.exists(id);
    }

    public boolean exists(String role) {
        return roleRepository.findOneByName("ROLE_" + role).isPresent();
    }
}
