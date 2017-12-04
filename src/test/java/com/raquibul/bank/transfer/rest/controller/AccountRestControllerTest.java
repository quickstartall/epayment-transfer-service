package com.raquibul.bank.transfer.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raquibul.bank.transfer.rest.controller.AccountRestController;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.repository.AccountJpaRepository;
import com.raquibul.bank.transfer.rest.service.AccountService;

/**
 *
 * @author Raquibul Hasan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:testdata.properties")
public class AccountRestControllerTest {
	private MockMvc mockMvc;
	@Value("${payment.rest.api.base.uri}")
	private String accountRestBaseUri;
	@Value("${payment.rest.api.account.get.uri}")
	private String accountGetUri;
	@Value("${payment.rest.api.account.add.uri}")
	private String accountAddUri;
	@Value("${payment.rest.api.account.update.uri}")
	private String accountUpdateUri;
	@Value("${payment.rest.api.account.delete.uri}")
	private String accountDeleteUri;
	@Value("${payment.rest.api.accounts.uri}")
	private String accountsUri;
	
	@Mock
	private AccountService accountService;
	
    @InjectMocks
    private AccountRestController accountController;
    
    @MockBean
    private AccountJpaRepository accountJpaRepository;
    
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .addPlaceholderValue("payment.rest.api.base.uri", accountRestBaseUri)
                .addPlaceholderValue("payment.rest.api.account.get.uri", accountGetUri)
                .addPlaceholderValue("payment.rest.api.account.add.uri", accountAddUri)
                .addPlaceholderValue("payment.rest.api.account.update.uri", accountUpdateUri)
                .addPlaceholderValue("payment.rest.api.account.delete.uri", accountDeleteUri)
                .addPlaceholderValue("payment.rest.api.accounts.uri", accountsUri)
                .build();
    }
    
    @Test
	public void testStatusForAccountAddRequest() throws Exception {
    	// Add Account
		Mockito.when(accountService.addAccount(Mockito.any())).thenReturn(getAccount());
		this.mockMvc.perform(MockMvcRequestBuilders.post(accountRestBaseUri + accountAddUri)
			.content(getRequestJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print()).andExpect(status().isCreated());
		
    }
    
    //Find Accounts
    @Test
	public void testStatusForAccountsFetchAllRequest() throws Exception {
		Mockito.when(accountService.getAllAccounts()).thenReturn(Arrays.asList(getAccount()));
		this.mockMvc.perform(MockMvcRequestBuilders.get(accountRestBaseUri + accountsUri)).andDo(print())
			.andExpect(status().isOk());
	}
	
    //Check Response is JSON Array
	@Test
	public void testContentIsArrayForAccountsFetchAllRequest() throws Exception {
		Mockito.when(accountService.getAllAccounts()).thenReturn(Arrays.asList(getAccount()));
		this.mockMvc.perform(MockMvcRequestBuilders.get(accountRestBaseUri + accountsUri)).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}
	
	//Update Account
	@Test
	@Rollback(false)
	public void testStatusForAccountUpdateRequest() throws Exception {
		Mockito.when(accountService.getAccount(Mockito.anyLong())).thenReturn(getAccount());
		Mockito.doNothing().when(accountService).updateAccount(Mockito.any());
		this.mockMvc.perform(MockMvcRequestBuilders.put(accountRestBaseUri + accountUpdateUri)
			.content(getRequestJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print()).andExpect(status().isCreated());
		
		//Negative Balance
		Mockito.when(accountService.getAccount(Mockito.anyLong())).thenReturn(getAccount());
		Mockito.doNothing().when(accountService).updateAccount(Mockito.any());
		this.mockMvc.perform(MockMvcRequestBuilders.put(accountRestBaseUri + accountUpdateUri)
			.content(getRequestJsonNegativeBalanace()).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print()).andExpect(status().isBadRequest());
	}

	private String getRequestJson() throws JsonProcessingException {
		String jsonString = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> account = new HashMap<>();
		account.put("id", 1);
		account.put("name", "Some name");
		account.put("balance", 500);
		jsonString = objectMapper.writeValueAsString(account);
		return jsonString;
	}
	
	private String getRequestJsonNegativeBalanace() throws JsonProcessingException {
		String jsonString = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> account = new HashMap<>();
		account.put("id", 987);
		account.put("name", "Some Name");
		account.put("balance", -123);
		jsonString = objectMapper.writeValueAsString(account);
		return jsonString;
	}
	
	private Account getAccount() {
		Account account = new Account();
		account.setId(1l);
		return account;
	}
	
	@After
	public void cleanup() {
		mockMvc = null;
	}
}
