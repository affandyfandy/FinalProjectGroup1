CREATE TABLE user (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    fullName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(64) NOT NULL,
    role CHAR(20) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE TABLE patient (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    nik INT NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    address TEXT,
    gender CHAR(15),
    date_of_birth DATE,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
