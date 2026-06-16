package com.roberto.gestor_despesa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.services.IncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            String expectedErrorMessage = String.format("Resource with ID %s not found", request0.category());;
            String expectedPath = "/api/incomes";

            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(expectedErrorField)))
                    .andExpect(jsonPath("$.message", is(expectedErrorMessage)))
                    .andExpect(jsonPath("$.path", is(expectedPath)))
                    .andExpect(jsonPath("$.timeStamp").exists());

        }

    }
}