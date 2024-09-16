CREATE TABLE User (
    id BINARY(16) NOT NULL PRIMARY KEY,  -- UUID as a 16-byte binary field
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password BINARY(64) NOT NULL,  -- Store password as binary (assuming hashed passwords, length adjusted accordingly)
    role CHAR(20) NOT NULL,
    created_time DATETIME NOT NULL,
    updated_time DATETIME,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);
