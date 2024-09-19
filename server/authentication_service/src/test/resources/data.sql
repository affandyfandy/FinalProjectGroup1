CREATE TABLE User (
    id BINARY(16) NOT NULL PRIMARY KEY,  -- UUID as a 16-byte binary field
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARBINARY(64) NOT NULL,  -- Store password as binary (adjusted to VARBINARY for H2 compatibility)
    role VARCHAR(20) NOT NULL,  -- CHAR(20) changed to VARCHAR(20) for H2 compatibility
    created_time TIMESTAMP NOT NULL,  -- Changed DATETIME to TIMESTAMP for H2
    updated_time TIMESTAMP,  -- Changed DATETIME to TIMESTAMP for H2
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);
