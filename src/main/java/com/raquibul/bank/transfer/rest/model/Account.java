package com.raquibul.bank.transfer.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author Raquibul Hasan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "account";
	@ApiModelProperty(notes = "The database generated product ID")
	private Long id;
	@ApiModelProperty(notes = "The name of the account holder")
	private String name;
	@ApiModelProperty(notes = "The account balance", required = true)
	private BigDecimal balance;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
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
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	@JsonIgnore
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).add("balance", balance).toString();
	}
}
