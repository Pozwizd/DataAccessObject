DELETE FROM users;
DELETE FROM product;
DELETE FROM shopping_cart;
DELETE FROM orders;
ALTER TABLE shopping_cart AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE product AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;