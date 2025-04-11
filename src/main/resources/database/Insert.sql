-- INGREDIENTS
INSERT INTO ingredient (id, name) VALUES
                                      (1, 'Tomate'),
                                      (2, 'Pâte'),
                                      (3, 'Fromage'),
                                      (4, 'Poulet'),
                                      (5, 'Crème');

-- PRICE
INSERT INTO price (id_ingredient, amount, date_value) VALUES
                                                          (1, 2.50, '2024-08-01'),
                                                          (1, 2.50, '2024-08-01'),
                                                          (2, 1.20, '2024-08-01'),
                                                          (3, 3.00, '2024-08-01'),
                                                          (4, 5.00, '2024-08-01'),
                                                          (5, 2.80, '2024-08-01');

-- STOCK_MOVEMENT
INSERT INTO stock_movement (id_ingredient, quantity, unit, movement_type, creation_datetime) VALUES
                                                                                                 (1, 1000, 'G', 'IN', '2024-08-01 08:00:00'),
                                                                                                 (2, 10, 'U', 'IN', '2024-08-01 08:05:00'),
                                                                                                 (3, 500, 'G', 'IN', '2024-08-01 08:10:00'),
                                                                                                 (4, 2, 'U', 'IN', '2024-08-01 08:15:00'),
                                                                                                 (5, 3, 'L', 'IN', '2024-08-01 08:20:00'),
                                                                                                 (1, 200, 'G', 'OUT', '2024-08-02 10:00:00');

-- DISH
INSERT INTO dish (name, available_quantity, actual_price) VALUES
                                                              ('Pizza Margherita', 10, 8.50),
                                                              ('Poulet à la crème', 5, 12.00);

-- DISH_INGREDIENT
INSERT INTO dish_ingredient (dish_id, id_ingredient, required_quantity, unit) VALUES
                                                                                  (1, 1, 150, 'G'),  -- Tomate
                                                                                  (1, 2, 1, 'U'),    -- Pâte
                                                                                  (1, 3, 100, 'G'),  -- Fromage
                                                                                  (2, 4, 1, 'U'),    -- Poulet
                                                                                  (2, 5, 0.3, 'L');  -- Crème

CREATE TABLE orders (
                        reference VARCHAR(50) PRIMARY KEY,
                        status VARCHAR(20) NOT NULL CHECK (status IN ('CREATED', 'CONFIRMED')),
                        total_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE dish_order (
                            id BIGSERIAL PRIMARY KEY,
                            order_reference VARCHAR(50) REFERENCES orders(reference) ON DELETE CASCADE,
                            dish_id BIGINT NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
                            quantity INTEGER NOT NULL,
                            status VARCHAR(20) NOT NULL CHECK (status IN ('IN_PROGRESS', 'FINISHED', 'DELIVERED'))
);
