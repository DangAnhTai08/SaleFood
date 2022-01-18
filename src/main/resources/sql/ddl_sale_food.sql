CREATE TABLE sale_category (
    category_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    category_name varchar(50) NOT NULL DEFAULT '',
    category_url varchar(500) NOT NULL,
    category_status bool NOT null default true,
    CONSTRAINT sale_category_pkey PRIMARY KEY (category_id)
);

create table sale_product(
    product_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_name varchar(500) NOT NULL DEFAULT '',
    product_img varchar(500) not NULL,
    product_url varchar(500) NOT NULL,
    product_price decimal(25,3) not null default 0,
    product_qty int default 0,
    product_view int default 0,
    product_discount decimal default 0,
    product_description text default '',
    product_status bool default true,
    category_id int4 not null,
    CONSTRAINT product_pkey PRIMARY KEY (product_id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES sale_category(category_id)
);

CREATE TABLE public.sale_user (
    user_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_name varchar NOT NULL,
    user_email varchar NOT NULL,
    user_phone varchar,
    user_address varchar,
    user_secret varchar,
    user_password varchar(50) NOT NULL,
    user_balance decimal(20,4) default 50000,
    user_created timestamp NOT NULL DEFAULT now(),
    user_last_login timestamp NOT NULL DEFAULT now(),
    CONSTRAINT sale_user_pkey PRIMARY KEY (user_id)
);

CREATE TABLE sale_transaction (
    tran_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    tran_status int4 NOT NULL DEFAULT 0,
    user_id int4 NOT NULL DEFAULT 0,
    tran_user_name varchar NOT NULL,
    tran_user_email varchar(50) NOT NULL,
    tran_user_phone varchar(20) NOT NULL,
    tran_amount numeric NOT NULL DEFAULT 0.0000,
    tran_payment_info text NOT NULL,
    tran_message varchar NOT NULL,
    tran_security varchar NOT NULL DEFAULT 0,
    tran_created timestamp NOT NULL DEFAULT now(),
    CONSTRAINT sale_transaction_pkey PRIMARY KEY (tran_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES sale_user(user_id)
);

CREATE TABLE sale_order (
    order_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    tran_id int4 NOT NULL,
    product_id int4 NOT NULL,
    order_qty int4 NOT NULL DEFAULT 0,
    order_amount numeric NOT NULL DEFAULT 0.0000,
    order_description text NOT NULL,
    order_status int4 NULL DEFAULT 0,
    order_created timestamp NOT NULL DEFAULT now(),
    CONSTRAINT sale_order_pkey PRIMARY KEY (order_id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES sale_product(product_id),
    CONSTRAINT fk_tran_id FOREIGN KEY (tran_id) REFERENCES sale_transaction(tran_id)
);

CREATE TABLE sale_roles (
    role_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar default 'ROLE_USER',
    CONSTRAINT sale_roles_pkey PRIMARY KEY (role_id)
);

CREATE TABLE users_roles (
     user_id int4 NOT NULL,
     role_id int4 NOT NULL,
     CONSTRAINT fk_users_roles FOREIGN KEY (user_id) REFERENCES sale_user(user_id),
     CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES sale_roles(role_id)
);

INSERT INTO public.sale_roles
(role_id, "name")
VALUES(1, 'ROLE_USER');
INSERT INTO public.sale_roles
(role_id, "name")
VALUES(2, 'ROLE_MODERATOR');
INSERT INTO public.sale_roles
(role_id, "name")
VALUES(3, 'ROLE_ADMIN');
