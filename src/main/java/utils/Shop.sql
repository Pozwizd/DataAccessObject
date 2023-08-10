CREATE SCHEMA shop;
USE shop;

CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(128) NOT NULL,
  email VARCHAR(50) NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
  );
  
  CREATE TABLE user_details (
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  gender ENUM('male', 'female') NOT NULL,
  date_of_birth DATE NOT NULL,
  address VARCHAR(255) NOT NULL,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE CASCADE
);

CREATE TABLE orders (
  order_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  order_list TINYTEXT NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (order_id),
  FOREIGN KEY (user_id)
  REFERENCES users(id)
  ON DELETE CASCADE
    );

CREATE TABLE product (
  id INT NOT NULL AUTO_INCREMENT,
  product_name VARCHAR(100) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE IF NOT EXISTS shopping_cart (
  user_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  FOREIGN KEY (product_id)
  REFERENCES shop.product (id)
  ON DELETE CASCADE,
  FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE CASCADE
    );

