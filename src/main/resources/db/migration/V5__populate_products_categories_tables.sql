-- Insert categories
INSERT INTO categories (name)
VALUES ('Fruits'),
       ('Vegetables'),
       ('Dairy'),
       ('Bakery'),
       ('Beverages'),
       ('Snacks'),
       ('Meat'),
       ('Seafood'),
       ('Frozen Foods'),
       ('Pantry');

-- Insert products
INSERT INTO products (name, price, description, category_id)
VALUES ('Bananas (1kg)', 1.50, 'Fresh ripe bananas, rich in potassium and perfect for daily consumption.', 1),
       ('Tomatoes (1kg)', 2.20, 'Juicy red tomatoes ideal for salads, cooking, and sauces.', 2),
       ('Whole Milk (1L)', 1.80, 'Fresh whole milk with high calcium content, sourced from local farms.', 3),
       ('White Bread Loaf', 2.50, 'Soft and fluffy white bread, perfect for sandwiches and toast.', 4),
       ('Orange Juice (1L)', 3.20, '100% pure orange juice with no added sugar or preservatives.', 5),
       ('Potato Chips (200g)', 2.00, 'Crispy salted potato chips, a perfect snack for any time.', 6),
       ('Chicken Breast (1kg)', 6.50, 'Fresh boneless chicken breast, high in protein and low in fat.', 7),
       ('Salmon Fillet (500g)', 8.90, 'Premium fresh salmon fillet, rich in omega-3 fatty acids.', 8),
       ('Frozen Peas (500g)', 2.75, 'Quick-frozen green peas to retain freshness and nutrients.', 9),
       ('Basmati Rice (1kg)', 4.00, 'Long-grain aromatic basmati rice, ideal for a variety of dishes.', 10);