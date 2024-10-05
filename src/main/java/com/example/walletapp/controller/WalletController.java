package com.example.walletapp.controller;

import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.model.Wallet;
import com.example.walletapp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@Validated
public class WalletController {

    @Autowired
    private WalletService walletService;

    // Эндпоинт для получения баланса кошелька по ID
    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWalletBalance(@PathVariable UUID walletId) {
        Wallet wallet = walletService.findWalletById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with ID: " + walletId));

        return ResponseEntity.ok(wallet);
    }

    // Эндпоинт для пополнения кошелька
    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@RequestBody @Valid WalletRequest request) {
        try {
            walletService.deposit(request.getWalletId(), request.getAmount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Deposit successful");
    }

    // Эндпоинт для снятия средств с кошелька
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@RequestBody @Valid WalletRequest request) {
        try {
            walletService.withdraw(request.getWalletId(), request.getAmount());
            return ResponseEntity.ok("Withdrawal successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Эндпоинт для создания нового кошелька
    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(@RequestBody BigDecimal initialBalance) {
        Wallet wallet = walletService.createWallet(initialBalance);
        return ResponseEntity.ok(wallet);
    }
}
