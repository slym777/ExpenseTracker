package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findExpenseById(Long id);
    List<Expense> findAll();

//    Optional<Expense> findDebtByIdAndSettledUp(Long id, Boolean settledUp);
//    List<Expense> findAllByHubIdAndSettledUp(Long hub_id, Boolean settledUp);

//    @Modifying
//    @Transactional
//    @Query(value = "update Expense e set settled_up=true where d.debitor_id=:debitorId and d.creditor_id=:creditorId and d.hub_id=:hubId",
//        nativeQuery = true)
//    void settleUpDebtsByDCHId(Long debitorId, Long creditorId, Long hubId);
//
//    @Query(value = "select * from debts d where (d.creditor_id=:userId or d.debitor_id=:userId) and d.settled_up=false", nativeQuery = true)
//     List<Debt> getDebtsByUserId(Long userId);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update debts d set d.settled_up=true where d.id=:debtId",
//            nativeQuery = true)
//    void settleUpById(Long debtId);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update debts d set d.settled_up=true where d.creditor_id=:creditorId and hub_Id=:hubId",
//            nativeQuery = true)
//    void settleUpDebtByCreditorIdAndHubId(Long creditorId, Long hubId);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update debts d set d.settled_up=true where d.creditor_id=:creditorId and debitor_id=:debitorId",
//            nativeQuery = true)
//    void settleUpUserCreditsWithFriend(Long creditorId, Long debitorId);
//
//    @Query(value = "select * from debts d where d.creditor_id=:creditorId and debitor_id=:debitorId and d.settled_up=false", nativeQuery = true)
//    List<Debt> getActiveDebtsByCreditorIdAndDebtorId(Long creditorId, Long debitorId);
//
//    @Query(value = "select * from debts d where (d.creditor_id=:userId or d.debitor_id=:userId) and d.settled_up=true", nativeQuery = true)
//    List<Debt> getSettledUpDebtsByUserId(Long userId);
//
//    @Override
//    void deleteById(Long aLong);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update Debts d set d.amount=:debtAmount, d.description=:debtDescription, d.currency=:currencyIndex, d.debitor_id=:debtorId where d.id=:debtId",
//            nativeQuery = true)
//    void updateDebtById(Double debtAmount, String debtDescription, Integer currencyIndex, Long debtorId, Long debtId);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update Debt d set d.createdAt=:debtDate where d.id=:debtId")
//    void updateDebtDateById(Instant debtDate, Long debtId);
//
//    @Query(value = "select count(*) from debts d where d.hub_id=:hubId and d.settled_up=false", nativeQuery = true)
//    int getNrOfDebtsFromHub(Long hubId);

}

