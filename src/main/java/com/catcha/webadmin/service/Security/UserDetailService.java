package com.catcha.webadmin.service.Security;

import com.catcha.webadmin.model.Role;
import com.catcha.webadmin.model.User;
import com.catcha.webadmin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserDetailService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.warn("username input: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        Collection<Role> listRole = new ArrayList<>(user.getRoles());
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                user.isEnabled(), true, true, true,
                getAuthorities(listRole));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
