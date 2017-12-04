package com.raquibul.bank.transfer.rest.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import com.raquibul.bank.transfer.rest.jpa.entity.Account;

/**
 * Repository used to perform CRUD operations for the Account Entity. 
 * It uses conventional Spring framework provided Repository (CrudRepository) API
 * @author Raquibul Hasan
 * @see CrudRepository
 * @see Account
 */
public interface AccountJpaRepository extends CrudRepository<Account, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Account findByName(String name);
}