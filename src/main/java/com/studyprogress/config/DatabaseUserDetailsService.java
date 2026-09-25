package com.studyprogress.config;

import com.studyprogress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(user.getEmail()).password(user.getPassword())
                .authorities(user.getRole().name()).disabled(!user.isEnabled()).build();
    }
}
