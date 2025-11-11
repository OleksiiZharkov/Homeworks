INSERT INTO employers (name, address) VALUES ('Google', '1600 Amphitheatre Parkway');
INSERT INTO employers (name, address) VALUES ('Meta', '1 Hacker Way');

INSERT INTO customers (name, email, age) VALUES ('Іван Франко', 'ivan@example.com', 40);
INSERT INTO customers (name, email, age) VALUES ('Леся Українка', 'lesya@example.com', 30);

INSERT INTO accounts (number, currency, balance, customer_id) VALUES ('uuid-ivan-usd', 'USD', 1000.0, 1);
INSERT INTO accounts (number, currency, balance, customer_id) VALUES ('uuid-ivan-uah', 'UAH', 50000.0, 1);

INSERT INTO accounts (number, currency, balance, customer_id) VALUES ('uuid-lesya-eur', 'EUR', 2500.0, 2);

INSERT INTO customer_employer (customer_id, employer_id) VALUES (1, 1);
INSERT INTO customer_employer (customer_id, employer_id) VALUES (2, 1);
INSERT INTO customer_employer (customer_id, employer_id) VALUES (2, 2);