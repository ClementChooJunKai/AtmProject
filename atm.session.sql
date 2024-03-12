CREATE TABLE User (
    UserID INT NOT NULL UNIQUE AUTO_INCREMENT,
    PIN INT,
    username VARCHAR(50) UNIQUE,
    BankName VARCHAR(50),
    BankID INT,
    fullName VARCHAR(50),
    admin BOOLEAN,
    BalanceSavings FLOAT,
    BalanceCurrent FLOAT,
    newUserStatus BOOLEAN,
    withdrawLimit FLOAT,
    transferLimit FLOAT,
    PRIMARY KEY (UserID),
    FOREIGN KEY (bankID) REFERENCES Bank(bankID)
);

CREATE TABLE Transaction (
    TransactionNo INT NOT NULL AUTO_INCREMENT,
    UserID INT,
    Remarks VARCHAR(255),
    CheqNo INT,
    TransactionDate DATE,
    TransactionType INT,
    AccountType INT,
    FromUserName VARCHAR(50),
    ToUserName VARCHAR(50),
    Amount FLOAT,
    PRIMARY KEY (TransactionNo),
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

-- create bank table 
use atm;
CREATE TABLE Bank (
    BankName VARCHAR(50),
    BankID INT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (bankID),
    exchangeRate FLOAT,
    currency VARCHAR(20)
);
--delete bank name column from User 
use atm;
ALTER TABLE User
DROP COLUMN BankName
--add bankID to user 
alter table User
ADD COLUMN bankID INT;
-- add bankname column as foregin key to user
use atm;
ALTER TABLE User
ADD FOREIGN KEY (bankID) REFERENCES Bank(bankID);

--inserting bank details 
use atm;
insert into Bank(bankName,exchangeRate,currency)
values ('DBS', 1, 'SGD');
insert into Bank(bankName,exchangeRate,currency)
values ('Bank of China', 5.15, 'CNY');
insert into Bank(bankName,exchangeRate,currency)
values ('Maybank', 3.0, 'MYR');
insert into Bank(bankName,exchangeRate,currency)
values ('Bank Australia',0.9,'AUD');

insert into User(username, pin, bankID, fullName, admin, BalanceCurrent, BalanceSavings,newUserStatus,withdrawLimit,transferLimit)
values ('idiot',1234,2,'dog',0,1000,0,0,0,0);

UPDATE user set email = 'clementchoo78@hotmail.com' WHERE UserID=3;


UPDATE user set AccountNoSavings = FLOOR(RAND()*(999999-100000+1)+100000);
UPDATE user set AccountNoCurrent = FLOOR(RAND()*(999999-100000+1)+100000);

alter table user
ADD COLUMN AccountNoSavings INT;
alter table user
ADD COLUMN AccountNoCurrent INT;

ALTER table user
add column Email VARCHAR(255)

update 
drop table bank;