package com.roberto.gestor_despesa.entities;


import jakarta.persistence.*;

import java.time.LocalDate;

@Table
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private LocalDate birthDate;
    private String cpf;
    private String name;
    private String email;
    private String password;

    public Client() {
    }

    public Client(Integer id, String password, String email, String name, String cpf, LocalDate birthDate, String username) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
