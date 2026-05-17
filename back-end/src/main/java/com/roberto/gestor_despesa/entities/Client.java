package com.roberto.gestor_despesa.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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

    private Boolean enabled;

    public Client(String username, LocalDate birthDate, String name, String email, String password, Boolean enabled) {
        this.username = username;
        this.birthDate = birthDate;
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }
}
