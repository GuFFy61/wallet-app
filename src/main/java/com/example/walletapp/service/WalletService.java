package com.example.walletapp.service;

import com.example.walletapp.model.Wallet;
import com.example.walletapp.repository.WalletRepository;
import com.example.walletapp.repository.TransactionRepository;
import com.example.walletapp.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Создание нового кошелька с начальными параметрами.
     * @param initialBalance Начальный баланс кошелька.
     * @return Созданный кошелек.
     */
    public Wallet createWallet(BigDecimal initialBalance) {
        Wallet newWallet = new Wallet(initialBalance);
        return walletRepository.save(newWallet);
    }

    /**
     * Поиск кошелька по идентификатору.
     * @param walletId Идентификатор кошелька.
     * @return Optional с найденным кошельком или пустым значением, если кошелек не найден.
     */
    public Optional<Wallet> findWalletById(UUID walletId) {
        return walletRepository.findById(walletId);
    }

    /**
     * Пополнение баланса кошелька.
     * @param walletId Идентификатор кошелька.
     * @param amount Сумма пополнения.
     * @throws Exception Если кошелек не найден.
     */
    @Transactional
    public void deposit(UUID walletId, BigDecimal amount) throws Exception {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new Exception("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        // Сохраняем транзакцию в базе данных
        Transaction transaction = new Transaction(wallet, amount, "DEPOSIT");
        transactionRepository.save(transaction);
    }

    /**
     * Снятие средств с кошелька.
     * @param walletId Идентификатор кошелька.
     * @param amount Сумма для снятия.
     * @throws Exception Если кошелек не найден или недостаточно средств на счете.
     */
    @Transactional
    public void withdraw(UUID walletId, BigDecimal amount) throws Exception {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new Exception("Wallet not found"));

        // Проверяем, достаточно ли средств на счете
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        // Сохраняем транзакцию в базе данных
        Transaction transaction = new Transaction(wallet, amount, "WITHDRAW");
        transactionRepository.save(transaction);
    }

    /**
     * Получение текущего баланса кошелька.
     * @param walletId Идентификатор кошелька.
     * @return Текущий баланс кошелька.
     * @throws Exception Если кошелек не найден.
     */
    public BigDecimal getBalance(UUID walletId) throws Exception {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new Exception("Wallet not found"));

        return wallet.getBalance();
    }
}
