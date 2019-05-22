package com.catcha.webadmin.Config;

import com.catcha.webadmin.model.Role;
import com.catcha.webadmin.model.User;
import com.catcha.webadmin.repository.RoleRepository;
import com.catcha.webadmin.repository.UserRepository;
import com.catcha.webadmin.service.Security.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(UserDetailService.class);
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public InitData(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //init role data
        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(roleAdmin);
        Role roleSPV = createRoleIfNotFound("ROLE_SPV");
        List<Role> spvRoles = new ArrayList<>();
        spvRoles.add(roleSPV);

        //init user data
        createUserIfNotFound("admin", "admin@mail.com", "Catcha123", adminRoles, true);
        createUserIfNotFound("supervisor", "supervisor@mail.com", "Catcha123", spvRoles, true);
    }

    private User createUserIfNotFound(String username, String email, String password, List<Role> roles, Boolean enable){
        User userByUsername = userRepository.findByUsername(username);
        if (userByUsername == null){
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRoles(roles);
            user.setEnabled(enable);
            return userRepository.save(user);
        }
        return userByUsername;
    }

    private Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            return roleRepository.save(role);
        }
        return role;
    }
}
