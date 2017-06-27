package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.repository.master.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOne(Long id){
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        if (user.getPassword() == null) {
            user.setPassword(user.getMobile());
        }
        return userRepository.save(user);
    }

    public long count() {
        return userRepository.count();
    }
}
