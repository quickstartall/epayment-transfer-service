package com.raquibul.bank.transfer.rest.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.raquibul.bank.transfer.rest.model.Account;

/**
 * Converter used to convert from Entity to Model.
 * @author Raquibul Hasan
 * @see ConversionService
 */
@Component
public class AccountEntityToModelConverter implements Converter<com.raquibul.bank.transfer.rest.jpa.entity.Account, Account>{
	@Override
	public Account convert(com.raquibul.bank.transfer.rest.jpa.entity.Account source) {
		if (source == null) {
			throw new IllegalArgumentException("The provided domain Account is null");
		}
		Account account = new Account();
		account.setId(source.getId());
		account.setName(source.getName());
		account.setBalance(source.getBalance());
		return account;
	}
}
