CREATE TABLE pc (
	code int NOT NULL PRIMARY KEY,
	model varchar(50) NOT NULL,
	speed smallint NOT NULL,
	ram smallint NOT NULL,
	hd real NOT NULL,
	cd varchar(10) NOT NULL,
	price money
)