package com.roberto.gestor_despesa.testData.scenario;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.Expense;
import com.roberto.gestor_despesa.entities.enums.PaymentMethod;
import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.repository.BudgetCategoryRepository;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.CategoryTypeRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.ExpenseRepository;


@Component
public class BudgetScenario {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    public Client createBudgetWithExpensesForMonthlyTotals() {

        Client client = new Client("user", LocalDate.of(2004, 1, 1), "User alt", "user@email.com", "123456", true);
        Client clientcreated = clientRepository.save(client);
        
        CategoryType categoryType1 = categoryTypeRepository.getReferenceById(1);

        Category category0 = new Category("COMPRAS", "Compras em Geral", categoryType1, client);
        Category category1 = new Category("Hobby", "Dinheiro gasto com hobbys", categoryType1, client);

        categoryRepository.save(category0);
        categoryRepository.save(category1);

        Budget budget0 = new Budget("ORCAMENTO OUTUBRO", BigDecimal.ZERO, LocalDate.of(2024, 10, 1), PeriodType.MONTH, Status.ACTIVE, client);

        BudgetCategory budgetCategory0 = new BudgetCategory(budget0, category0);
        BudgetCategory budgetCategory1 = new BudgetCategory(budget0, category1);

        budgetRepository.save(budget0);
        budgetCategoryRepository.save(budgetCategory0);
        budgetCategoryRepository.save(budgetCategory1);

        Expense expense0 = new Expense("compras de materiais de construção",PaymentMethod.DEBIT_CARD, LocalDate.of(2024, 10, 14), BigDecimal.valueOf(340.0),  client, category0, budget0);
        expenseRepository.save(expense0);

        return clientcreated;
    }
}
