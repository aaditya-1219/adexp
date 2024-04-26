-- Create sales_fact table
CREATE TABLE sales_fact (
    time_id INT,
    product_id INT,
    customer_id INT,
    sales_amount DECIMAL(10, 2)
);

-- Create dimension tables
CREATE TABLE time_dim (
    time_id INT PRIMARY KEY,
    year INT,
    month INT
);

CREATE TABLE product_dim (
    product_id INT PRIMARY KEY,
    category VARCHAR(50)
);

CREATE TABLE customer_dim (
    customer_id INT PRIMARY KEY,
    region VARCHAR(50)
);

-- Insert sample data into time_dim
INSERT INTO time_dim VALUES (1, 2023, 8);
INSERT INTO time_dim VALUES (2, 2023, 12);
INSERT INTO time_dim VALUES (3, 2023, 3);

-- Insert sample data into product_dim
INSERT INTO product_dim VALUES (1, 'Electronics');
INSERT INTO product_dim VALUES (2, 'Clothing');

-- Insert sample data into customer_dim
INSERT INTO customer_dim VALUES (1, 'Sangli');
INSERT INTO customer_dim VALUES (2, 'Mumbai');

-- Insert sample data into sales_fact
INSERT INTO sales_fact VALUES (1, 1, 1, 100.00);
INSERT INTO sales_fact VALUES (2, 2, 2, 150.00);
INSERT INTO sales_fact VALUES (3, 1, 2, 200.00);
INSERT INTO sales_fact VALUES (1, 2, 1, 250.00);

-- Query
SELECT
    t.year,
    p.category,
    c.region,
    SUM(s.sales_amount) AS total_sales
FROM
    sales_fact s
JOIN
    time_dim t ON s.time_id = t.time_id
JOIN
    product_dim p ON s.product_id = p.product_id
JOIN
    customer_dim c ON s.customer_id = c.customer_id
GROUP BY
    CUBE(t.year, p.category, c.region);
