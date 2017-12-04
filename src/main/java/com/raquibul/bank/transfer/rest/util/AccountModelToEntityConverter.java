package com.raquibul.bank.transfer.rest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.raquibul.bank.transfer.rest.jpa.entity.Account;
/**
 * Converter used to convert from Model to Entity
 * @author Raquibul Hasan
 *
 */
@Component
public class AccountModelToEntityConverter implements Converter<com.raquibul.bank.transfer.rest.model.Account, Account> {

	@Override
	public Account convert(com.raquibul.bank.transfer.rest.model.Account source) {
		if (source == null) {
			throw new IllegalArgumentException("The provided Model Account is null");
		}
		Account account = new Account();
		account.setId(source.getId());
		account.setName(source.getName());
		account.setBalance(source.getBalance());
		return account;
	}
}
