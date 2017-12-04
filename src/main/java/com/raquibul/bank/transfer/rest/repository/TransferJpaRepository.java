package com.raquibul.bank.transfer.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.raquibul.bank.transfer.rest.jpa.entity.Transfer;


/**
 * Repository used to perform CRUD operations for the Account Entity. 
 * It uses conventional Spring framework provided Repository (CrudRepository) API
 * @author Raquibul Hasan
 * @see CrudRepository
 * @see Transfer
 */
public interface TransferJpaRepository extends CrudRepository<Transfer, Long> {
	List<Transfer> findByTransferDateBetween(Date start, Date end);
}
