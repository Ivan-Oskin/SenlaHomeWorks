CREATE TABLE printer(
	code int NOT NULL PRIMARY KEY,
	model varchar(50) NOT NULL,
	color char(1) NOT NULL,
	type varchar(10) NOT NULL,
	price money
)