package com.jazasoft.mt.test;

import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.repository.master.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Created by mdzahidraza on 04/07/17.
 */
@Configuration
@Profile("test")
public class WebSecurityGlobalConfigTest extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    UserRepository userRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return (username) -> {
            try {
                Optional<User> user = userRepository.findOneByUsername(username);
                if (!user.isPresent()) {
                    user = userRepository.findOneByEmail(username);
                    if (!user.isPresent()) {
                        throw new UsernameNotFoundException("user not found");
                    }
                }

                return user.get();
            } catch (Exception e) {
            }
            return null;
        };
    }

}