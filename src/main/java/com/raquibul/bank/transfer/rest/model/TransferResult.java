package com.raquibul.bank.transfer.rest.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean isSuccess;
	private String transferDescription;
	private Transfer transfer;
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * @return the transferDescription
	 */
	public String getTransferDescription() {
		return transferDescription;
	}
	/**
	 * @param transferDescription the transferDescription to set
	 */
	public void setTransferDescription(String transferDescription) {
		this.transferDescription = transferDescription;
	}
	/**
	 * @return the transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}
	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}
	
	@Override
	@JsonIgnore
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("isSuccess", isSuccess)
				.add("transferDescription", transferDescription)
				.add("transfer", transfer)
				.toString();
	}
}
