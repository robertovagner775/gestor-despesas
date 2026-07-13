package com.roberto.gestor_despesa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.dtos.response.CategoryResponse;
import com.roberto.gestor_despesa.dtos.response.IncomeResponse;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.services.IncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(IncomeController.class)
public class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IncomeService incomeService;

    private Income income0;

    private IncomeRequest request0;

    @BeforeEach
    public void setup() {
        this.income0 = new Income();
        income0.setAmount(new BigDecimal("5000.00"));
        income0.setReceivedDate(LocalDate.now());
        this.request0 = new IncomeRequest("Salary", LocalDate.now(), new BigDecimal("5000.00"), 1);
    }

    @Nested
    class createIncomeTests {

        @Test
        void shouldCreateIncomeWithSuccess() throws Exception {
            given(incomeService.createIncome(any(IncomeRequest.class), eq(1))).willReturn(income0);

            ResultActions response = mockMvc.perform((post("/api/incomes"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request0))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            response.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(result -> assertNotNull(result.getResponse().getHeader("Location"), "Location header should not be null"));
        }

        @Test
        void shouldThrowExceptionWhenCategoryNotFound() throws Exception {
            given(incomeService.createIncome(any(IncomeRequest.class), eq(1))).willThrow(new NotFoundException(request0.category()));

            ResultActions response = mockMvc.perform((post("/api/incomes"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request0))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            String expectedErrorField = "Not Found";
            String expectedErrorMessage = String.format("Resource with ID %s not found", request0.category());
            String expectedPath = "/api/incomes";

            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(expectedErrorField)))
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }

    }

    @Nested
    class findByIdTests {

        private IncomeResponse incomeResponse0;

        @BeforeEach
        void setup() {
            incomeResponse0 = new IncomeResponse(1, "Dinheiro rebebido de vendas", LocalDate.now(), new BigDecimal("5000.00"), new CategoryResponse(1, "Vendas"));
        }

        @Test
        void shouldReturnIncomeWhenFindById() throws Exception {
            Integer idIncome = 1;
            Integer idClient = 1;

            given(incomeService.findById(idClient, idIncome)).willReturn(incomeResponse0);

            ResultActions response = mockMvc.perform((get("/api/incomes/{id}", idIncome))
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(incomeResponse0.id())))
                    .andExpect(jsonPath("$.description", is(incomeResponse0.description())))
                    .andExpect(jsonPath("$.receivedDate", is(incomeResponse0.receivedDate().toString())))
                    .andExpect(jsonPath("$.amount", is(incomeResponse0.amount().doubleValue())))
                    .andExpect(jsonPath("$.category.id", is(incomeResponse0.category().id())))
                    .andExpect(jsonPath("$.category.category", is(incomeResponse0.category().category())));
        }

        @Test
        void shouldThrowExceptionWhenIncomeNotFound() throws Exception {
            Integer idIncome = 1;
            Integer idClient = 1;

            given(incomeService.findById(idClient, idIncome)).willThrow(new NotFoundException(idIncome));

            ResultActions response = mockMvc.perform((get("/api/incomes/{id}", idIncome))
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            String expectedErrorField = "Not Found";
            String expectedErrorMessage = String.format("Resource with ID %s not found", idIncome);
            String expectedPath = String.format("/api/incomes/%s", idIncome);

            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(expectedErrorField)))
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }
    }

    @Nested
    class incomeUpdateTests {

        @Test
        void shouldUpdateIncomeWithSuccess() throws Exception {
            Integer idIncome = 1;
            Integer idClient = 1;

            IncomeResponse incomeResponse = new IncomeResponse(idIncome, request0.description(), request0.receivedDate(), request0.amount(), new CategoryResponse(request0.category(), "Vendas"));

            given(incomeService.updateIncome(any(IncomeRequest.class), eq(idIncome), eq(idClient))).willReturn(incomeResponse);

            ResultActions result = mockMvc.perform((put("/api/incomes/{id}", idIncome))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request0))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(incomeResponse.id())))
                    .andExpect(jsonPath("$.description", is(incomeResponse.description())))
                    .andExpect(jsonPath("$.receivedDate", is(incomeResponse.receivedDate().toString())))
                    .andExpect(jsonPath("$.amount", is(incomeResponse.amount().doubleValue())))
                    .andExpect(jsonPath("$.category.id", is(incomeResponse.category().id())))
                    .andExpect(jsonPath("$.category.category", is(incomeResponse.category().category())));
        }

        @Test
        void shouldMethodArgumentNotValidExceptionWhenFieldNotValid() throws Exception {
            Integer idIncome = 1;

            IncomeRequest request1 = new IncomeRequest("", LocalDate.now(), new BigDecimal("-5000.00"), 1);

            ResultActions result = mockMvc.perform((put("/api/incomes/{id}", idIncome))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request1))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            String expectedErrorMessage = "Method Argument Not Valid Exception";
            String expectedPath = String.format("/api/incomes/%s", idIncome);

            result.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        void shouldThrowExceptionWhenIncomeNotFound() throws Exception {
            Integer idIncome = 1;
            Integer idClient = 1;

            given(incomeService.updateIncome(request0, idIncome, idClient)).willThrow(new NotFoundException(idIncome));

            ResultActions response = mockMvc.perform((put("/api/incomes/{id}", idIncome))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request0))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            String expectedErrorField = "Not Found";
            String expectedErrorMessage = String.format("Resource with ID %s not found", idIncome);
            String expectedPath = String.format("/api/incomes/%s", idIncome);

            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.error", is(expectedErrorField)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }

    }

    @Nested
    class findAll {

        @Test
        void shouldFindAllIncomesByClientWithPaginination() throws Exception {
            Integer clientId = 1;
            String description = "Test Income";
            LocalDate dateStart = LocalDate.now().minusDays(7);
            LocalDate dateEnd = LocalDate.now();
            BigDecimal valueStart = new BigDecimal("100.00");
            BigDecimal valueEnd = new BigDecimal("1000.00");
            String category = "Test Category";
            Pageable pageable = PageRequest.of(0, 10);;

            given(incomeService.findAll(eq(clientId), eq(valueStart), eq(valueEnd), eq(description), eq(dateStart), eq(dateEnd), eq(category), eq(pageable)))
                    .willReturn(Page.empty(pageable));

            ResultActions response = mockMvc.perform((get("/api/incomes")
                            .param("description", description)
                            .param("dateStart", dateStart.toString())
                            .param("dateEnd", dateEnd.toString())
                            .param("valueStart", valueStart.toString())
                            .param("valueEnd", valueEnd.toString())
                            .param("category", category)
                            .param("page", "0")
                            .param("size", "10")
                    )
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0))
                    .andExpect(jsonPath("$.pageable.pageSize").value(10));

            verify(incomeService).findAll(eq(clientId), eq(valueStart), eq(valueEnd), eq(description), eq(dateStart), eq(dateEnd), eq(category), eq(pageable));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteIncomeWithSuccess() throws Exception {
            Integer idIncome = 1;

            ResultActions response = mockMvc.perform((delete("/api/incomes/{id}", idIncome))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            response.andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldThrowExceptionWhenIncomeNotFound() throws Exception {

            Integer idIncome = 1;
            Integer idClient = 1;

            doThrow(new NotFoundException(idIncome))
                    .when(incomeService)
                    .deleteIncome(idClient, idIncome);

            ResultActions response = mockMvc.perform((delete("/api/incomes/{id}", idIncome))
                    .with(csrf())
                    .with(jwt().jwt(jwt -> jwt.claim("clientId", 1L))));

            String expectedErrorField = "Not Found";
            String expectedErrorMessage = String.format("Resource with ID %s not found", idIncome);
            String expectedPath = String.format("/api/incomes/%s", idIncome);

            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.error", is(expectedErrorField)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timeStamp").exists());
        }

    }
}
