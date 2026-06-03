INSERT INTO CLIENT(id, username, name, email, password, birth_date, enabled)
VALUES (1,'User12', 'User', 'user@email.com', '123456', '2004-01-01', true);

INSERT INTO CATEGORY(id, title, description, color, client_id, type_id) VALUES
(10, 'COMPRAS', 'Compras em Geral', '#8E44AD', 1, 1),
(11, 'Hobby', 'Dinheiro gasto com hobbys', '#E67E22', 1, 1);

INSERT INTO BUDGET(id, description, total_planned, period_reference, period_type, status, client_id) VALUES
(1, 'ORCAMENTO OUTUBRO', 0.00, '2024-10-01', 'MONTH', 'ACTIVE', 1);

INSERT INTO BUDGET_CATEGORY(budget_id, category_id, planned_value) VALUES
(1, 10, 500.00),
(1, 11, 600.00);

INSERT INTO EXPENSE(id, description, payment_method, paid_date, amount, client_id, category_id, budget_id) VALUES
(1, 'compras de materiais de construção', 'DEBIT_CARD', '2024-10-14', 240.00, 1, 10, 1);

INSERT INTO EXPENSE(id, description, payment_method, paid_date, amount, client_id, category_id, budget_id) VALUES
(2, 'gastos com jogos de video game', 'PIX', '2024-10-14', 540.00, 1, 11, 1);