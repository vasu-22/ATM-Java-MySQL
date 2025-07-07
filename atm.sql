create database atm;
use atm;
CREATE TABLE accounts (
    account_number VARCHAR(255) PRIMARY KEY,
    balance DOUBLE,
    pin VARCHAR(255)
);
INSERT INTO accounts (account_number, balance, pin) VALUES ('123456', 1000.00, 'password');
