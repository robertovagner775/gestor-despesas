package com.roberto.gestor_despesa.testData.builder;

import com.roberto.gestor_despesa.entities.Client;

import java.time.LocalDate;

public  class ClientTestBuilder {

    private Integer id;
    private String username;
    private LocalDate birthDate;
    private String name;
    private String email;
    private String password;
    private Boolean enabled;


    public ClientTestBuilder() {
        this.id = 1;
        this.username = "userDefault";
        this.birthDate = LocalDate.of(2000, 1, 1);
        this.name = "Default Name";
        this.email = "user@email.com";
        this.password = "123456";
        this.enabled = true;
    }

    public ClientTestBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ClientTestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ClientTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ClientTestBuilder disabled() {
        this.enabled = false;
        return this;
    }

    public Client build() {
        return new Client(id, username, birthDate, name, email, password, enabled);
    }
}
