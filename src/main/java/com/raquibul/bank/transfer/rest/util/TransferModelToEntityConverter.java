package com.raquibul.bank.transfer.rest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.raquibul.bank.transfer.rest.jpa.entity.Transfer;

/**
 * Converter used to convert from Model to Entity
 * @author Raquibul Hasan
 *
 */
@Component
public class TransferModelToEntityConverter implements Converter<com.raquibul.bank.transfer.rest.model.Transfer, Transfer> {

	@Override
	public Transfer convert(com.raquibul.bank.transfer.rest.model.Transfer source) {
		if (source == null) {
			throw new IllegalArgumentException("The provided Model Account is null");
		}
		Transfer transfer = new Transfer();
		transfer.setId(source.getId());
		transfer.setFromAccount(source.getFromAccount());
		transfer.setToAccount(source.getToAccount());
		transfer.setAmount(source.getAmount());
		transfer.setTransferDate(source.getTransferDate());
		return transfer;
	}
}
