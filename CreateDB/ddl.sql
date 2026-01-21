CREATE TABLE places (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE masters (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    timeCreate TIMESTAMP NOT NULL,
    timeStart TIMESTAMP NOT NULL,
    timeComplete TIMESTAMP NOT NULL,
    cost INT NOT NULL,
    place_id INTEGER
);


CREATE TABLE order_master (
    id INT PRIMARY KEY,
    master_id INT,
    order_id INT
);
