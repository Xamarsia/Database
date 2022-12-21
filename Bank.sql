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
-- 	 name VARCHAR(50) NOT NULL UNIQUE
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
-- 	isTransactionSuccessful BOOL NOT NULL,
-- 	transactionDate DATE NOT NULL DEFAULT now(),
-- 	money NUMERIC(9,2),
-- 	CONSTRAINT accounts_should_be_different CHECK(senderAccountsFK != recipientAccountsFK)
-- );

-- DROP TABLE Transaction, CurrencyRates, Accounts, users;

-- Select * from CurrencyRates;

-- UPDATE Accounts
-- SET money = money + (SELECT moneyInUSD::float FROM CurrencyRates WHERE 
-- 			 (Accounts.currency = CurrencyRates.currency))  *
-- (SELECT (56::float/CurrencyRates.moneyInUSD::float) FROM CurrencyRates WHERE CurrencyRates.currency = 'EUR') 
-- WHERE accountPK = 1 ;


-- SELECT 50 * (second.moneyInUSD::float / first.moneyInUSD::float)
-- FROM CurrencyRates first, CurrencyRates second
-- WHERE first.currency = 'USD' AND second.currency = 'EUR';


-- CREATE TABLE Transaction( 
-- 	senderAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL,
-- 	recipientAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL,
-- 	isTransactionSuccessful BOOL NOT NULL,
-- 	transactionDate DATE NOT NULL DEFAULT now(),
-- 	money NUMERIC(9,2),
-- 	CONSTRAINT accounts_should_be_different CHECK(senderAccountsFK != recipientAccountsFK)
-- );

-- UPDATE Transaction 
-- SET isTransactionSuccessful

    
-- SELECT 50 * (second.moneyInUSD::float / first.moneyInUSD::float)
-- FROM CurrencyRates first, CurrencyRates second
-- WHERE first.currency = 'USD' AND second.currency = 'EUR'


Select * From accounts
Where accountPK IN(3, 4);


Select * From Transaction
Where senderAccountsFK IN(3)


-- (SELECT currency FROM Accounts WHERE Accounts.accountPK = recipientAccountsFK) As recipientCurrency
-- SELECT money, currency FROM Accounts WHERE Accounts.accountPK = 3

-- Select accountPK, Accounts.money, isTransactionSuccessful,Transaction.money  FROM Transaction
-- JOIN accounts On accountPK = Transaction.senderAccountsFK;

-- SELECT users.userPK, accountPK, money, currency FROM users
-- JOIN Accounts ON Accounts.userFK = users.userPK
-- WHERE userPK = 2 