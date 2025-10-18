package com.ApiFactory.ApiFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ApiFactory.ApiFactory.dto.ClientCreateDto;
import com.ApiFactory.ApiFactory.dto.ContractCreateDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiFactoryApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static Long clientId;
	private static Long contractId1;
	private static Long contractId2;

	@Test
	@Order(1)
	void createPersonClient() throws Exception {
		ClientCreateDto dto = new ClientCreateDto();
		dto.setType("PERSON");
		dto.setName("John Doe");
		dto.setEmail("john@example.com");
		dto.setPhone("+41791234567");
		dto.setBirthDate(LocalDate.of(1990, 4, 15));

		MvcResult result = mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		JsonNode body = objectMapper.readTree(response);
		clientId = body.get("id").asLong();

		System.out.println(" Created client:");
		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));

		assertThat(clientId).isPositive();
	}

	@Test
	@Order(2)
	void updateClient_shouldIgnoreImmutableFields() throws Exception {
		ClientCreateDto update = new ClientCreateDto();
		update.setType("PERSON");
		update.setName("John Updated");
		update.setEmail("john.updated@example.com");
		update.setPhone("+41790009999");
		update.setBirthDate(LocalDate.of(2000, 1, 1)); // should be ignored

		MvcResult result = mockMvc.perform(put("/clients/" + clientId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Updated"))
				.andExpect(jsonPath("$.email").value("john.updated@example.com"))
				.andExpect(jsonPath("$.phone").value("+41790009999"))
				.andExpect(jsonPath("$.birthDate").value("1990-04-15")) // original
				.andReturn();

		System.out.println(" Updated client (immutables unchanged):");
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	@Order(3)
	void createContracts() throws Exception {
		ContractCreateDto contract1 = new ContractCreateDto();
		contract1.setCostAmount(new BigDecimal("99.99"));

		MvcResult res1 = mockMvc.perform(post("/clients/" + clientId + "/contracts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contract1)))
				.andExpect(status().isOk())
				.andReturn();

		String body1 = res1.getResponse().getContentAsString();
		contractId1 = objectMapper.readTree(body1).get("id").asLong();
		System.out.println("Created contract 1 (active):");
		System.out.println(body1);

		ContractCreateDto contract2 = new ContractCreateDto();
		contract2.setCostAmount(new BigDecimal("49.99"));
		contract2.setEndDate(LocalDate.now().minusDays(1)); // expired

		MvcResult res2 = mockMvc.perform(post("/clients/" + clientId + "/contracts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contract2)))
				.andExpect(status().isOk())
				.andReturn();

		String body2 = res2.getResponse().getContentAsString();
		contractId2 = objectMapper.readTree(body2).get("id").asLong();
		System.out.println(" Created contract 2 (expired):");
		System.out.println(body2);

		assertThat(contractId1).isPositive();
		assertThat(contractId2).isPositive();
	}

	@Test
	@Order(4)
	void updateContractCost() throws Exception {
		MvcResult result = mockMvc.perform(patch("/contracts/" + contractId1 + "/cost")
				.param("cost", "123.45"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.costAmount").value("123.45"))
				.andReturn();

		System.out.println("Updated contract 1 cost to 123.45:");
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	@Order(5)
	void getActiveContracts_withAndWithoutUpdatedFilter() throws Exception {
		MvcResult activeRes = mockMvc.perform(get("/clients/" + clientId + "/contracts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)) // Only contract 1 is active
				.andExpect(jsonPath("$[0].id").value(contractId1))
				.andReturn();

		System.out.println(" Active contracts:");
		System.out.println(activeRes.getResponse().getContentAsString());

		String since = OffsetDateTime.now().minusMinutes(2)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		MvcResult filteredRes = mockMvc.perform(get("/clients/" + clientId + "/contracts")
				.param("updatedSince", since))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andReturn();

		System.out.println(" Active contracts filtered by updatedSince:");
		System.out.println(filteredRes.getResponse().getContentAsString());
	}

	@Test
	@Order(6)
	void getSumOfActiveContracts() throws Exception {
		MvcResult result = mockMvc.perform(get("/clients/" + clientId + "/contracts/sum"))
				.andExpect(status().isOk())
				.andExpect(content().string("123.45"))
				.andReturn();

		System.out.println("Sum of active contracts = 123.45");
		System.out.println(result.getResponse().getContentAsString());
	}



	@Test
	@Order(7)
	void createCompanyClient() throws Exception {
		ClientCreateDto dto = new ClientCreateDto();
		dto.setType("COMPANY");
		dto.setName("Test Company AG");
		dto.setEmail("contact@testcompany.com");
		dto.setPhone("+41791165233");
		dto.setCompanyIdentifier("abc-911");

		MvcResult result = mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.companyIdentifier").value("abc-911"))
				.andReturn();

		System.out.println("Created company client:");
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	@Order(8)
	void readClientDetails() throws Exception {
		MvcResult result = mockMvc.perform(get("/clients/" + clientId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(clientId))
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.birthDate").exists())
				.andReturn();

		System.out.println("Fetched client details:");
		System.out.println(result.getResponse().getContentAsString());
	}

	@ParameterizedTest
	@Order(9)
	@ValueSource(strings = { "123", "abcd", "+41abcdef" })
	void createClientWithInvalidPhone(String phone) throws Exception {
		ClientCreateDto dto = new ClientCreateDto();
		dto.setType("PERSON");
		dto.setName("Invalid Phone User");
		dto.setEmail("valid@example.com");
		dto.setPhone(phone);
		dto.setBirthDate(LocalDate.of(1990, 1, 1));

		mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(10)
	void createClientWithInvalidEmail() throws Exception {
		ClientCreateDto dto = new ClientCreateDto();
		dto.setType("PERSON");
		dto.setName("Invalid Email User");
		dto.setEmail("not-an-email");
		dto.setPhone("+41791234567");
		dto.setBirthDate(LocalDate.of(1990, 1, 1));

		mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(11)
	void createContractWithNegativeCost() throws Exception {
		ContractCreateDto dto = new ContractCreateDto();
		dto.setCostAmount(new BigDecimal("-99.99"));

		mockMvc.perform(post("/clients/" + clientId + "/contracts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest());
	}


	@Test
	@Order(12)
	void createClientWithMissingRequiredFields() throws Exception {
		ClientCreateDto dto = new ClientCreateDto(); // No fields set

		mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest());
	}
	@Test
	@Order(13)
	void deleteClient_shouldEndContracts() throws Exception {
		mockMvc.perform(delete("/clients/" + clientId))
				.andExpect(status().isNoContent());

		MvcResult result = mockMvc.perform(get("/clients/" + clientId + "/contracts/sum"))
				.andExpect(status().isOk())
				.andExpect(content().string("0"))
				.andReturn();

		System.out.println(" Client deleted, all contracts ended. Sum after delete = 0");
		System.out.println(result.getResponse().getContentAsString());
	}

}
