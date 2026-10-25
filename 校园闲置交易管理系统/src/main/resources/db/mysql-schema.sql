CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(40) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    display_name VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS items (
    id BIGINT NOT NULL AUTO_INCREMENT,
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
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_items_category (category),
    KEY idx_items_status (status),
    KEY idx_items_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL,
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
    paid_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_no (order_no),
    KEY idx_orders_buyer (buyer_username),
    KEY idx_orders_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
