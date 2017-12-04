package com.raquibul.bank.transfer.rest.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

/**
 * The Entity class for the table account
 * @author Raquibul Hasan
 *
 */
@Entity
@Table(name = Account.TABLE_NAME)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "account";
	
	@Id
	@SequenceGenerator(name = "account_sequence", sequenceName = "account_id_seq")
	@GeneratedValue(generator = "account_sequence")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "balance", nullable = false)
	private BigDecimal balance;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("balance", balance)
				.toString();
	}
}
