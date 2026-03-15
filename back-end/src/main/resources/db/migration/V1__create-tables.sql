create table client (
	id SERIAL primary key,
	username VARCHAR(100),
	birth_date DATE,
	name VARCHAR(100),
	email VARCHAR(75) unique,
	password VARCHAR(75)
);
create table budget (
	id SERIAL primary key,
	description VARCHAR(150) not null,
	status VARCHAR(15) not null,
	total_planned NUMERIC(19, 2) not null,
	total_spent NUMERIC(19, 2) not null,
	total_remaining NUMERIC(19, 2) not null,
	period_type VARCHAR(15) not null,
	period_reference DATE not null,
	client_id INTEGER not null,
	constraint fk_client foreign key (client_id) references client(id)
);
create table category_type (
    id SERIAL primary key,
    type VARCHAR(150) not null UNIQUE,
    description VARCHAR(250)
);

create table category(
	id SERIAL primary key,
	title VARCHAR(150) not null,
	description VARCHAR(200) not null,
	color VARCHAR(7),
	type_id INTEGER not null,
	client_id INTEGER null,
	constraint fk_type foreign key (type_id) references category_type(id),
	constraint fk_client foreign key (client_id) references client(id)
);

create table budget_category(
  budget_id INTEGER not null,
  category_id INTEGER not null,
  planned_value numeric(9, 2) not null,
  spent_value numeric(9, 2) not null,
  remaining_value numeric(9, 2) not null,
  constraint fk_budget foreign key (budget_id) references budget(id),
  constraint fk_category foreign key (category_id) references category(id),
  primary key (budget_id, category_id)
);

create table expense(
	id SERIAL primary key,
	description VARCHAR(150) not null,
    value NUMERIC(19, 2) not null,
    payment_method VARCHAR(50),
    paid_date DATE not null,
    category_id INTEGER not null,
    client_id INTEGER not null,
    constraint fk_client foreign key (client_id) references client(id),
    constraint fk_category foreign key (category_id) references category(id)
);

create table income(
	id SERIAL primary key,
    value NUMERIC(19, 2) not null,
    description VARCHAR(255) not null,
	category_id INTEGER not null,
    received_date DATE not null,
    client_id INTEGER not null,
	constraint fk_category foreign key (category_id) references category(id),
    constraint fk_client foreign key (client_id) references client(id)
);
