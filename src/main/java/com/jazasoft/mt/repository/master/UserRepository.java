package com.jazasoft.mt.repository.master;

import com.jazasoft.mt.entity.master.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public interface UserRepository extends RevisionRepository<User,Long,Integer>, JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByName(String name);

    User findByUsername(String username);

    Optional<User> findOneByUsername(String username);

    Optional<User> findOneByEmail(String email);

    List<User> findByModifiedAtGreaterThan(Date updatedAt);
}
