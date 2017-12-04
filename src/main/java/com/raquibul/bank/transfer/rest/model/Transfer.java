package com.raquibul.bank.transfer.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;	
	@ApiModelProperty(notes = "fromAccount", required = true)
	private String fromAccount;
	@ApiModelProperty(notes = "toAccount", required = true)
	private String toAccount;
	@ApiModelProperty(notes = "The amount to be transferred", required = true)
	private BigDecimal amount;

	@ApiModelProperty(notes = "Transaction Date", required = false)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Amsterdam")
	private Date transferDate;

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
	 * @return the transferDate
	 */
	public Date getTransferDate() {
		return transferDate;
	}
	/**
	 * @param transferDate the transferDate to set
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	
	/**
	 * @return the fromAccount
	 */
	public String getFromAccount() {
		return fromAccount;
	}
	/**
	 * @param fromAccount the fromAccount to set
	 */
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	/**
	 * @return the toAccount
	 */
	public String getToAccount() {
		return toAccount;
	}
	/**
	 * @param toAccount the toAccount to set
	 */
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	@JsonIgnore
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("fromAccount", fromAccount)
				.add("toAccount", toAccount)
				.add("amount", amount)
				.add("transferDate", transferDate)
				.toString();
	}
}
