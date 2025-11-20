

package com.roberto.gestor_despesa.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.roberto.gestor_despesa.repository.ClientRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.clientRepository.findByEmail(username).map(UserAuth::new).orElseThrow(() -> new UsernameNotFoundException("User "+ username +" not found"));
    }

    
}