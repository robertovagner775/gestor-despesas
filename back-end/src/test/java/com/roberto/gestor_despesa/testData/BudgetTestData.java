package com.roberto.gestor_despesa.testData;

import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequest;
import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class BudgetTestData {

    public static Client createClient(Integer id) {
        return new Client(id, "robertovagner", LocalDate.now(), "Roberto Vagner",
                "robertovagner@email.com", "12131", true);
    }

    public static List<Category> createCategoryList() {
        CategoryType type = new CategoryType(1, "DESPESA", "Saída");
        Category category0 = new Category(1, "Compras", "Compras em Geral", type, null);
        Category category1 = new Category(2, "Alimentação", "Gastos com alimentação", type, null);
        return List.of(category0, category1);
    }

    public static BudgetRequest createBudgetRequest() {
        var req0 = new BudgetCategoryRequest(1, new BigDecimal("1500"));
        var req1 = new BudgetCategoryRequest(2, new BigDecimal("1200"));
        return new BudgetRequest("Orcamento Abril", YearMonth.of(2026, 4),
                PeriodType.MONTH, List.of(req0, req1));
    }
}
