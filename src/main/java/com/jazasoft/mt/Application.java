package com.jazasoft.mt;

import com.jazasoft.mt.entity.master.Role;
import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.service.CompanyService;
import com.jazasoft.mt.service.RoleService;
import com.jazasoft.mt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/**
 * Created by mdzahidraza on 26/06/17.
 */
@SpringBootApplication
public class Application {

    private final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

    @Bean
    CommandLineRunner init(
            UserService userService,
            RoleService roleService,
            CompanyService companyService) {

        return (args) -> {
            if (roleService.count() == 0) {
                LOGGER.info("No of roles = " + 0);
                roleService.save(new Role("MASTER","Super user for Tennant management"));
            }
            if(userService.count() == 0){
                LOGGER.info("No of users = " + 0);
                Role role = roleService.findOneByName("MASTER");
                System.out.println(role);
                User user = new User("Md Zahid Raza","zahid7292","zahid7292@gmail.com","admin","8987525008",true,false,false,false);
                user.addRole(role);
                userService.save(user);
            }
        };
    }

}
