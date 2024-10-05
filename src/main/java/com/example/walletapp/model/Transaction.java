package com.example.walletapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    // Первичный ключ UUID для транзакции
    @Id
    @GeneratedValue
    private UUID id;

    // Ссылка на кошелек (многие к одному)
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    // Поле для хранения суммы транзакции
    @Column(nullable = false)
    private BigDecimal amount;

    // Поле для хранения типа операции (DEPOSIT или WITHDRAW)
    @Column(nullable = false)
    private String operationType;

    // Конструктор по умолчанию (требуется для JPA)
    public Transaction() {
    }

    // Конструктор для создания новой транзакции
    public Transaction(Wallet wallet, BigDecimal amount, String operationType) {
        this.wallet = wallet;
        this.amount = amount;
        this.operationType = operationType;
    }

    // Геттеры и сеттеры для полей
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
