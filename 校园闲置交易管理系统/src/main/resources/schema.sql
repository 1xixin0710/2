DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    display_name VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    category VARCHAR(40) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    item_condition VARCHAR(20) NOT NULL,
    trade_location VARCHAR(40) NOT NULL,
    delivery_options VARCHAR(120) NOT NULL,
    publish_time TIMESTAMP NOT NULL,
    seller_name VARCHAR(40) NOT NULL,
    contact VARCHAR(80) NOT NULL,
    status VARCHAR(20) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_status ON items(status);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    item_id BIGINT NOT NULL,
    item_title VARCHAR(100) NOT NULL,
    item_image_url VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    buyer_username VARCHAR(40) NOT NULL,
    buyer_name VARCHAR(40) NOT NULL,
    seller_name VARCHAR(40) NOT NULL,
    seller_contact VARCHAR(80) NOT NULL,
    delivery_method VARCHAR(40) NOT NULL,
    trade_location VARCHAR(40) NOT NULL,
    payment_method VARCHAR(40) NOT NULL,
    contact VARCHAR(80) NOT NULL,
    note VARCHAR(300),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX idx_orders_buyer ON orders(buyer_username);
CREATE INDEX idx_orders_status ON orders(status);
