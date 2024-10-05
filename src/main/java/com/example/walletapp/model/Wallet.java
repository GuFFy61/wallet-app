package com.example.walletapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {

    // Первичный ключ UUID
    @Id
    @GeneratedValue
    private UUID id;

    // Поле для хранения баланса
    @Column(nullable = false)
    private BigDecimal balance;

    // Конструктор по умолчанию (требуется для JPA)
    public Wallet() {
    }

    // Конструктор для создания нового кошелька с начальными параметрами
    public Wallet(BigDecimal balance) {
        this.balance = balance;
    }

    // Геттеры и сеттеры для полей
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
