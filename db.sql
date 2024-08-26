
drop database ecommerce_web;
create  database ecommerce_web;
use ecommerce_web;
create table role (
    id int auto_increment primary key,
    name varchar(30)  not null
);
CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    address TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    facebook_account_id VARCHAR(255),
    google_account_id VARCHAR(255),
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES Role(id)
);
CREATE TABLE Product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE Product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    image_url VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);
CREATE TABLE `order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    total_money DECIMAL(10, 2),
    status ENUM('Pending', 'Shipped', 'Delivered', 'Canceled'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    phone_number VARCHAR(50),
	full_name VARCHAR(50),
    shipping_method VARCHAR(255),
    shipping_date DATE,
    shipping_address TEXT,
    tracking_number VARCHAR(255),
    payment_method VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES User(id)
);
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    order_id INT,
    quantity INT,
    price DECIMAL(10, 2),
    color VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES Product(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE ON UPDATE CASCADE 
);
CREATE TABLE inventory_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(id)
);
CREATE TABLE inventory_transactions (
    id int PRIMARY KEY AUTO_INCREMENT,
    inventory_item_id int  NOT NULL,
    quantity_change INT NOT NULL,
    transaction_type ENUM('STOCK_IN', 'STOCK_OUT', 'ADJUSTMENT') NOT NULL,
    reason VARCHAR(255),
    reference_id VARCHAR(50),
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
);


CREATE TABLE Notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES User(id)
);
CREATE TABLE Payment_methods (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);
CREATE TABLE Transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    order_id INT,
    payment_method_id INT,
    amount DECIMAL(10, 2),
    status VARCHAR(50),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (payment_method_id) REFERENCES Payment_methods(id)
);
CREATE TABLE promotions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    discount_type VARCHAR(50),
    discount_value DECIMAL(10, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE promotion_products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_id INT,
    product_id INT,
    FOREIGN KEY (promotion_id) REFERENCES promotions(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);
CREATE TABLE promotion_users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_id INT,
    user_id INT,
    is_notified BOOLEAN DEFAULT FALSE,
    notified_at TIMESTAMP,
    FOREIGN KEY (promotion_id) REFERENCES promotions(id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Thêm dữ liệu vào bảng role
INSERT INTO role (name) VALUES
('Admin'),
('User'),
('Manager');

-- Thêm dữ liệu vào bảng User
INSERT INTO User (fullname, phone_number, address, email, password, role_id) VALUES
('John Doe', '1234567890', '123 Main St, City', 'john@example.com', 'hashed_password_1', 1),
('Jane Smith', '0987654321', '456 Elm St, Town', 'jane@example.com', 'hashed_password_2', 2),
('Bob Johnson', '1122334455', '789 Oak St, Village', 'bob@example.com', 'hashed_password_3', 2);

-- Thêm dữ liệu vào bảng Product
INSERT INTO Product (name, price, description) VALUES
('Smartphone X', 999.99, 'Latest smartphone with advanced features'),
('Laptop Pro', 1499.99, 'High-performance laptop for professionals'),
('Wireless Earbuds', 149.99, 'True wireless earbuds with noise cancellation');

-- Thêm dữ liệu vào bảng Product_images
INSERT INTO Product_images (product_id, image_url) VALUES
(1, 'http://example.com/images/smartphone_x_1.jpg'),
(1, 'http://example.com/images/smartphone_x_2.jpg'),
(2, 'http://example.com/images/laptop_pro_1.jpg'),
(3, 'http://example.com/images/earbuds_1.jpg');

-- Thêm dữ liệu vào bảng `Order`
INSERT INTO `order` (user_id, total_money, status, shipping_method, shipping_date, shipping_address, payment_method) VALUES
(2, 1149.98, 'Shipped', 'Express', '2024-08-05', '456 Elm St, Town', 'Credit Card'),
(3, 149.99, 'Pending', 'Standard', '2024-08-10', '789 Oak St, Village', 'PayPal');

-- Thêm dữ liệu vào bảng order_details
INSERT INTO order_details (product_id, order_id, quantity, price, color) VALUES
(1, 1, 1, 999.99, 'Black'),
(3, 1, 1, 149.99, 'White'),
(3, 2, 1, 149.99, 'Black');

-- Thêm dữ liệu vào bảng inventory_items
INSERT INTO inventory_items (product_id, quantity) VALUES
(1, 50),
(2, 30),
(3, 100);

-- Thêm dữ liệu vào bảng Notifications
INSERT INTO Notifications (user_id, message) VALUES
(2, 'Your order has been shipped!'),
(3, 'New product available that you might like.');

-- Thêm dữ liệu vào bảng Payment_methods
INSERT INTO Payment_methods (name) VALUES
('Credit Card'),
('PayPal'),
('Bank Transfer');

-- Thêm dữ liệu vào bảng Transactions
INSERT INTO Transactions (user_id, order_id, payment_method_id, amount, status) VALUES
(2, 1, 1, 1149.98, 'Completed'),
(3, 2, 2, 149.99, 'Pending');

-- Thêm dữ liệu vào bảng promotions
INSERT INTO promotions (name, description, start_date, end_date, discount_type, discount_value) VALUES
('Summer Sale', '20% off on all electronics', '2024-07-01', '2024-08-31', 'Percentage', 20.00),
('Back to School', '$50 off on laptops', '2024-08-15', '2024-09-15', 'Fixed', 50.00);

-- Thêm dữ liệu vào bảng promotion_products
INSERT INTO promotion_products (promotion_id, product_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2);

-- Thêm dữ liệu vào bảng promotion_users
INSERT INTO promotion_users (promotion_id, user_id, is_notified) VALUES
(1, 2, TRUE),
(1, 3, FALSE),
(2, 2, TRUE);
-- Thêm dữ liệu mẫu
INSERT INTO inventory_transactions 
(inventory_item_id, quantity_change, transaction_type, reason, reference_id, created_by, created_at) 
VALUES
-- Nhập kho
(1, 100, 'STOCK_IN', 'Initial stock', 'PO-001', 'system', '2024-08-01 09:00:00'),
(2, 150, 'STOCK_IN', 'Restocking', 'PO-002', 'john_doe', '2024-08-02 10:30:00'),

-- Xuất kho
(1, -2, 'STOCK_OUT', 'Order fulfillment', 'ORDER-001', 'system', '2024-08-05 14:30:00'),
(2, -1, 'STOCK_OUT', 'Order fulfillment', 'ORDER-002', 'jane_smith', '2024-08-05 15:45:00'),

-- Điều chỉnh kho
(2, -5, 'ADJUSTMENT', 'Inventory count correction', 'ADJ-001', 'inventory_manager', '2024-08-07 16:00:00'),
(1, 3, 'ADJUSTMENT', 'Found misplaced items', 'ADJ-002', 'store_manager', '2024-08-08 09:15:00'),

-- Thêm giao dịch nhập kho
(2, 50, 'STOCK_IN', 'Emergency restocking', 'PO-003', 'procurement_team', '2024-08-10 08:45:00'),

-- Thêm giao dịch xuất kho
(2, -3, 'STOCK_OUT', 'Order fulfillment', 'ORDER-003', 'sales_team', '2024-08-11 13:20:00'),

-- Thêm giao dịch điều chỉnh
(1, -2, 'ADJUSTMENT', 'Damaged goods write-off', 'ADJ-003', 'quality_control', '2024-08-12 10:00:00');