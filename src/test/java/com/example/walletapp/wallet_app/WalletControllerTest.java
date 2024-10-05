package com.example.walletapp.wallet_app;

import com.example.walletapp.controller.WalletController;
import com.example.walletapp.model.Wallet;
import com.example.walletapp.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private Wallet testWallet;
    private UUID walletId;

    @BeforeEach
    public void setUp() {
        walletId = UUID.randomUUID();
        testWallet = new Wallet();
        testWallet.setId(walletId);
        testWallet.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    public void testCreateWallet() throws Exception {
        when(walletService.createWallet(any(BigDecimal.class))).thenReturn(testWallet);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"initialBalance\": 1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("1000.00"));
    }

    @Test
    public void testGetWalletBalance() throws Exception {
        when(walletService.getBalance(walletId)).thenReturn(testWallet.getBalance());

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1000.00"));
    }

    @Test
    public void testDepositFunds() throws Exception {
        doNothing().when(walletService).deposit(any(UUID.class), any(BigDecimal.class));

        mockMvc.perform(post("/api/v1/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\": \"" + walletId.toString() + "\", \"amount\": 500}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdrawFunds() throws Exception {
        doNothing().when(walletService).withdraw(any(UUID.class), any(BigDecimal.class));

        mockMvc.perform(post("/api/v1/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\": \"" + walletId.toString() + "\", \"amount\": 200}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetWalletNotFound() throws Exception {
        when(walletService.findWalletById(walletId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testWithdrawInsufficientFunds() throws Exception {
        Mockito.doThrow(new Exception("Insufficient funds")).when(walletService)
                .withdraw(any(UUID.class), any(BigDecimal.class));

        mockMvc.perform(post("/api/v1/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\": \"" + walletId.toString() + "\", \"amount\": 2000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient funds"));
    }
}
