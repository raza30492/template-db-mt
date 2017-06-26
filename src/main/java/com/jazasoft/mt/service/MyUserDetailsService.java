package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.repository.master.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager", readOnly = true)
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);
    @Autowired
    private UserRepository userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.trace("Looking for user for {}", username);
        try {
            Optional<User> user = userDao.findOneByUsername(username);
            if (!user.isPresent()) {
                user = userDao.findOneByEmail(username);
                if (!user.isPresent()) {
                    LOGGER.info("USER NOT PRESENT for {} {}", username, user);
                    throw new UsernameNotFoundException("user not found");
                }
            }
            LOGGER.trace("Found user for {} {}", username, user);
            return user.get();
        } catch (Exception e) {
            LOGGER.error("Error loading user {}", username, e);
        }
        return null;
    }
}
