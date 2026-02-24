package com.roberto.gestor_despesa.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ConfirmAccountToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private LocalDateTime expiryDate;

    @OneToOne
    private Client client;

    public ConfirmAccountToken(Client client) {
        this.id = null;
        this.client = client;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusMinutes(60);
    }
}
