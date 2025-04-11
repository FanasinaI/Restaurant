-- DROP TABLES IF EXIST
DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS stock_movement CASCADE;
DROP TABLE IF EXISTS price CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;

-- INGREDIENT table
CREATE TABLE ingredient (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

-- PRICE table
CREATE TABLE price (
                       id BIGSERIAL PRIMARY KEY,
                       id_ingredient BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                       amount NUMERIC(10, 2) NOT NULL,
                       date_value DATE NOT NULL
);

-- STOCK_MOVEMENT table
CREATE TABLE stock_movement (
                                id BIGSERIAL PRIMARY KEY,
                                id_ingredient BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                                quantity NUMERIC(10, 2) NOT NULL,
                                unit VARCHAR(10) NOT NULL CHECK (unit IN ('G', 'U', 'L')),
                                movement_type VARCHAR(10) NOT NULL CHECK (movement_type IN ('IN', 'OUT')),
                                creation_datetime TIMESTAMP NOT NULL
);

-- DISH table
CREATE TABLE dish (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      available_quantity INT NOT NULL,
                      actual_price NUMERIC(10, 2) NOT NULL
);

-- DISH_INGREDIENT table (note: includes id for easier mapping in ResultSet)
CREATE TABLE dish_ingredient (
                                 id BIGSERIAL PRIMARY KEY,
                                 dish_id BIGINT NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
                                 id_ingredient BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                                 required_quantity NUMERIC(10, 2) NOT NULL,
                                 unit VARCHAR(10) NOT NULL CHECK (unit IN ('G', 'L', 'U')),
                                 UNIQUE (dish_id, id_ingredient)
);
