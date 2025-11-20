package com.roberto.gestor_despesa.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.roberto.gestor_despesa.entities.Client;

public class UserAuth implements UserDetails {

    private final Client client;

    public UserAuth(Client client) {
        this.client = client;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Collections.emptyList();
    }

    @Override
    public String getPassword() {
       return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getUsername();
    }
    
}
