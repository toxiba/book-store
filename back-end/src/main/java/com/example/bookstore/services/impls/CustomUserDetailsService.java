package com.example.bookstore.services.impls;

import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> opt = repository.findById(username);

        if (!opt.isPresent()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserEntity entity = opt.get();

        GrantedAuthority authority = null;
        if (Objects.nonNull(entity.getRole()) && !entity.getRole().isEmpty()) {
            authority = new SimpleGrantedAuthority(entity.getRole());
        }

        return User.withUsername(entity.getUsername())
                .password(entity.getPassword())
                .authorities(authority)
                .build();
    }
}
