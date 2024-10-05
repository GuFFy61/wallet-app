package com.example.walletapp.repository;

import com.example.walletapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Получить все транзакции, связанные с определенным кошельком.
     * @param walletId Идентификатор кошелька.
     * @return Список транзакций, связанных с этим кошельком.
     */
    List<Transaction> findByWalletId(UUID walletId);
}
