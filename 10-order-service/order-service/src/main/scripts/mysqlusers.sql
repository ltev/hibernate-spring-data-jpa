DROP DATABASE IF EXISTS order_service_db;
DROP USER IF EXISTS `orderserviceadmin`@`%`;
DROP USER IF EXISTS `orderserviceuser`@`%`;

CREATE DATABASE IF NOT EXISTS order_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS `orderserviceadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `order_service_db`.* TO `orderserviceadmin`@`%`;

CREATE USER IF NOT EXISTS `orderserviceuser`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, SHOW VIEW ON `order_service_db`.* TO `orderserviceuser`@`%`;

FLUSH PRIVILEGES;