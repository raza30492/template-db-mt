package com.jazasoft.mt.repository.master;

import com.jazasoft.mt.entity.master.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findOneByName(String name);
}
