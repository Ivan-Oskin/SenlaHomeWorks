CREATE TABLE Places (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Masters (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Orders (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    timeCreate TIMESTAMP NOT NULL,
    timeStart TIMESTAMP NOT NULL,
    timeComplete TIMESTAMP NOT NULL,
    cost INT NOT NULL,
    place_id INTEGER
);


CREATE TABLE OrdersByMaster (
    id INT PRIMARY KEY,
    master_id INT,
    order_id INT
);
