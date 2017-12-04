package com.raquibul.bank.transfer.rest.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.MoreObjects;
/**
 * The Entity class for the the Table transfer
 * @author Raquibul Hasan
 *
 */
@Entity
@Table(name = Transfer.TABLE_NAME)
public class Transfer implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "transfer";
	@Id
	@SequenceGenerator(name = "transfer_sequence", sequenceName = "transfer_id_seq")
	@GeneratedValue(generator = "transfer_sequence")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "from_account", nullable = false)
	private String fromAccount;
	
	@Column(name = "to_account", nullable = false)
	private String toAccount;
	
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	
	@Column(name = "transaction_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
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
	 * @return the date
	 */
	
	
	/**
	 * @return the fromAccount
	 */
	public String getFromAccount() {
		return fromAccount;
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
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("from", fromAccount)
				.add("to", toAccount)
				.add("amount", amount)
				.add("date", transferDate)
				.toString();
	}
}
