CREATE TABLE laptop (
	code int NOT NULL PRIMARY KEY,
	model varchar(50) NOT NULL,
	speed smallint NOT NULL,
	ram smallint NOT NULL,
	hd real NOT NULL, 
	price money,
	screen smallint NOT NULL
)