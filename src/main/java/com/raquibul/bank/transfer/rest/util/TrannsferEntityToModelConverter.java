package com.raquibul.bank.transfer.rest.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.raquibul.bank.transfer.rest.model.Transfer;

/**
 * Converter used to convert from Entity to Model.
 * @author Raquibul Hasan
 * @see ConversionService
 */
@Component
public class TrannsferEntityToModelConverter implements Converter<com.raquibul.bank.transfer.rest.jpa.entity.Transfer, Transfer>{
	@Override
	public Transfer convert(com.raquibul.bank.transfer.rest.jpa.entity.Transfer source) {
		if (source == null) {
			throw new IllegalArgumentException("The provided domain Transfer is null");
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
