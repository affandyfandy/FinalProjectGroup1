CREATE TABLE User (
    id BINARY(16) NOT NULL PRIMARY KEY, 
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password BINARY(64) NOT NULL, 
    role CHAR(20) NOT NULL,
    created_time DATETIME NOT NULL,
    updated_time DATETIME,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE TABLE Patient (
    id BINARY(16) NOT NULL PRIMARY KEY, 
    user_id BINARY(16) NOT NULL,
    nik INT NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    address TEXT,
    gender CHAR(15),
    date_of_birth DATE,
    created_time DATETIME NOT NULL,
    updated_time DATETIME,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES User(id)
);
