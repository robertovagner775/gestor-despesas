package com.roberto.gestor_despesa.entities;


import jakarta.persistence.*;

import java.time.LocalDate;

@Table
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public Client() {
    }

    public Client(Integer id, String password, String email, String name, LocalDate birthDate, String username) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.username = username;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
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
