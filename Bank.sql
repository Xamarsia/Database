-- Database: Bank

-- DROP DATABASE IF EXISTS "Bank";

-- CREATE TABLE Accounts(
-- 	accountPK SERIAL PRIMARY KEY,
-- 	money NUMERIC(9,2),
-- 	currency VARCHAR(10) UNIQUE CONSTRAINT allowed_currency_types 
--     CHECK (currency IN ('USD', 'EUR', 'UAH'))
-- );

-- CREATE TABLE users( 
--   userPK SERIAL PRIMARY KEY,
-- 	 name VARCHAR(50) NOT NULL,
--   accountFK INTEGER REFERENCES accounts(accountPK) ON DELETE CASCADE,
--   CONSTRAINT account_for_user_unique UNIQUE (userPK, accountFK)
-- );

-- ALTER TABLE Accounts
-- ADD COLUMN userFK INTEGER REFERENCES USERS(userPK) ON DELETE CASCADE;

-- CREATE TABLE CurrencyRates( 
--   currencyPK SERIAL PRIMARY KEY,
-- 	currency VARCHAR(10) CONSTRAINT allowed_currency_types 
--     CHECK (currency IN ('USD', 'EUR', 'UAH')),
-- 	moneyInUSD NUMERIC(9,2),
-- 	currencyUpdateDate DATE NOT NULL DEFAULT now()
-- );

-- CREATE TABLE Transaction( 
-- 	senderAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL,
-- 	recipientAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL,
-- 	currencyRate NUMERIC(9,2),
-- 	transactionDate DATE NOT NULL DEFAULT now(),
-- 	money NUMERIC(9,2),
-- 	CONSTRAINT accounts_should_be_different CHECK(senderAccountsFK != recipientAccountsFK)
-- );



-- DROP TABLE Transaction, CurrencyRates, Accounts, users;
-- 

-- Select * from CurrencyRates;

