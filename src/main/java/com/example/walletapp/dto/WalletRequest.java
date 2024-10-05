package com.example.walletapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class WalletRequest {

    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Amount must be greater than zero")
    private BigDecimal amount;

    // Конструктор по умолчанию
    public WalletRequest() {
    }

    // Конструктор с параметрами
    public WalletRequest(UUID walletId, BigDecimal amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    // Геттеры и сеттеры
    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
