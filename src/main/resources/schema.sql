CREATE TABLE test_table
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    age   INT,
    email VARCHAR(255) UNIQUE
);
