DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  birth_date date NOT NULL
);

CREATE TABLE accounts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_id int not null,
  iban VARCHAR(250) NOT NULL,
  currency varchar(3) NOT NULL,
  balance DECIMAL(20,2) DEFAULT 0,
  last_updated TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
  foreign key (user_id) references users(id)
);

INSERT INTO users (id, name, birth_date) VALUES
  (1, 'Aliko', TO_DATE( '2-DEC-2006', 'DD-MON-YYYY' )),
  (2, 'Bill', TO_DATE( '2-DEC-2006', 'DD-MON-YYYY' )),
  (3, 'Colrunsho', TO_DATE( '2-DEC-2006', 'DD-MON-YYYY' ));

INSERT INTO accounts (iban, user_id, currency, balance, last_updated) VALUES
  ('AAA123', 1, 'CAD','1000', TO_DATE( '01-FEB-2020', 'DD-MON-YYYY' )),
  ('BBB222', 2, 'MYR','100',TO_DATE( '01-JAN-2020', 'DD-MON-YYYY' )),
  ('CCC333', 3, 'PLN','10',TO_DATE( '21-MAR-2020', 'DD-MON-YYYY' ));