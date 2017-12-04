package com.raquibul.bank.transfer.rest.util;

import java.math.BigDecimal;

import com.raquibul.bank.transfer.rest.model.Account;

public final class TransferRestApiUtil {
	private TransferRestApiUtil() {
		// blank intentional - util class should not be instantiated
	}

	/**
	 * Checks if the balance is negative/null
	 * @param balance balance to be check
	 * @return
	 */
	public static boolean isNegativeBalance(BigDecimal balance) {
		if (balance == null || balance.doubleValue() < 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there is sufficient balance in the account to transfser
	 * @param account - the account to be checked for the sufficient balance
	 * @param amountToTransfer - the amount to be transferred
	 * @return true in case of sufficnet balance, false otherwise
	 */
	public static boolean isSufficientBalance(Account account, BigDecimal amountToTransfer) {
		if (account == null) {
			throw new IllegalArgumentException("provided account is null");
		}
		if (amountToTransfer == null) {
			throw new IllegalArgumentException("provided amountToTransfer is null");
		}
		if (account.getBalance() == null || account.getBalance().doubleValue() < amountToTransfer.doubleValue()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the provided account is overdrawn
	 * @param account - the  account to be checked for overdrawn
	 * @return true if the account is overdrawn, else false
	 */
	public static boolean isAccountOverdrawn(Account account) {
		if (account == null) {
			throw new IllegalArgumentException("provided account is null");
		}
		return isNegativeBalance(account.getBalance());
	}
}
