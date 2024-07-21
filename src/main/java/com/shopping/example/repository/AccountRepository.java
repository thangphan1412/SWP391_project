package com.shopping.example.repository;

import com.shopping.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Account findByVerificationCode(String code);
    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.roles")
    List<Account> findAllWithRoles();

    @Query("SELECT DISTINCT a FROM Account a " +
            "JOIN a.roles r " +
            "WHERE r.name = :roleName")
    List<Account> findAllByRole(@Param("roleName") String roleName);

    @Query("SELECT DISTINCT a FROM Account a " +
            "JOIN a.roles r " +
            "WHERE r.name = :roleName AND a.email LIKE %:email%")
    List<Account> findAllByRoleAndEmail(@Param("roleName") String roleName, @Param("email") String email);



}
