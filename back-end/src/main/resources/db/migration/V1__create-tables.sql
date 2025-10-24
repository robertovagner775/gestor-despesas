create table client (
	id INTEGER primary key,
	username VARCHAR(100),
	birth_date DATE,
	name VARCHAR(100),
	email VARCHAR(75) unique,
	password VARCHAR(75)
);

create table budget (
	id INTEGER primary key,
	description VARCHAR(150) not null,
	value NUMERIC(19, 2) not null,
	date_start DATE not null,
	date_end DATE not null,
	client_id INTEGER not null,
	constraint fk_client foreign key (client_id) references client(id)
);

create table category(
	id INTEGER primary key,
	title VARCHAR(150) not null,
	description VARCHAR(200) not null,
	client_id INTEGER,
	budget_id INTEGER,
	constraint fk_client foreign key (client_id) references client(id),
	constraint fk_budget foreign key (budget_id) references budget(id)

);

create table expense(
	id INTEGER primary key,
	description VARCHAR(150) not null,
    value NUMERIC(19, 2) not null,
    data DATE not null,
    category_id INTEGER not null,
    client_id INTEGER not null,
    constraint fk_client foreign key (client_id) references client(id),
    constraint fk_category foreign key (category_id) references category(id)
);

create table type_income(
	id INTEGER primary key,
	title VARCHAR(100) not null,
	description VARCHAR(200) not null,
	client_id INTEGER not null,
	constraint fk_client foreign key (client_id) references client(id)
);

create table income(
	id INTEGER primary key,
    value NUMERIC(19, 2) not null,
    data DATE not null,
    type_income_id INTEGER not null,
    client_id INTEGER not null,
    constraint fk_client foreign key (client_id) references client(id),
    constraint fk_type_income_id foreign key (type_income_id) references type_income(id)

);
