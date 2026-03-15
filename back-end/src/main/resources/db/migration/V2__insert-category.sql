INSERT INTO CATEGORY_TYPE(id, type, description) VALUES (1, 'DESPESA', 'Saída de recursos financeiros que reduz o saldo disponível.');
INSERT INTO CATEGORY_TYPE(id, type, description) VALUES (2, 'RECEITA', 'Entrada de recursos financeiros que aumenta o saldo disponível.');

INSERT INTO CATEGORY (id, title, description, color, client_id, type_id) VALUES
(1, 'Lazer', 'Gastos com entretenimento e atividades recreativas.', '#8E44AD', null, 1),
(2, 'Casa', 'Despesas relacionadas à manutenção e itens da casa.', '#E67E22', null, 1),
(3, 'Alimentação', 'Gastos com supermercado e refeições.', '#27AE60', null, 1),
(4, 'Transporte', 'Gastos com combustível, transporte público e aplicativos.', '#2980B9', null, 1),
(5, 'Saúde', 'Despesas médicas, medicamentos e plano de saúde.', '#E74C3C', null, 1);


INSERT INTO CATEGORY (id, title, description, color, client_id, type_id) VALUES
(6, 'Salário', 'Receita mensal proveniente do trabalho.', '#2ECC71', null, 2),
(7, 'Freelance', 'Receita obtida com trabalhos autônomos.', '#1ABC9C', null, 2),
(8, 'Investimentos', 'Rendimentos provenientes de aplicações financeiras.', '#16A085', null, 2),
(9, 'Outras Receitas', 'Outras entradas financeiras não classificadas.', '#3498DB', null, 2);

