-- Sample data for development and testing
-- Version: 1.2.0

-- Insert sample employees (password: password123)
INSERT INTO users (email, password, name, user_type, is_blocked) VALUES
('admin@bookstore.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', 'EMPLOYEE', false),
('employee1@bookstore.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Manager', 'EMPLOYEE', false);

INSERT INTO employees (id, position, department) VALUES
((SELECT id FROM users WHERE email = 'admin@bookstore.com'), 'Administrator', 'Management'),
((SELECT id FROM users WHERE email = 'employee1@bookstore.com'), 'Sales Manager', 'Sales');

-- Insert sample clients (password: password123)
INSERT INTO users (email, password, name, user_type, is_blocked) VALUES
('client1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alice Johnson', 'CLIENT', false),
('client2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob Smith', 'CLIENT', false);

INSERT INTO clients (id, phone, address) VALUES
((SELECT id FROM users WHERE email = 'client1@example.com'), '+1234567890', '123 Main St, City, Country'),
((SELECT id FROM users WHERE email = 'client2@example.com'), '+0987654321', '456 Oak Ave, Town, Country');

-- Insert sample books
INSERT INTO books (name, genre, age_group, price, publication_year, author, number_of_pages, characteristics, description, language) VALUES
('The Great Gatsby', 'Classic Fiction', 'ADULT', 12.99, '1925-04-10', 'F. Scott Fitzgerald', 180, 'Hardcover, First Edition Style', 'A story of decadence and excess in the Jazz Age', 'ENGLISH'),
('To Kill a Mockingbird', 'Classic Fiction', 'YOUNG_ADULT', 14.99, '1960-07-11', 'Harper Lee', 324, 'Paperback', 'A gripping tale of racial injustice and childhood innocence', 'ENGLISH'),
('1984', 'Dystopian Fiction', 'ADULT', 13.99, '1949-06-08', 'George Orwell', 328, 'Paperback', 'A dystopian social science fiction novel', 'ENGLISH'),
('Harry Potter and the Philosopher''s Stone', 'Fantasy', 'CHILDREN', 19.99, '1997-06-26', 'J.K. Rowling', 223, 'Hardcover', 'The first book in the Harry Potter series', 'ENGLISH'),
('The Hobbit', 'Fantasy', 'CHILDREN', 15.99, '1937-09-21', 'J.R.R. Tolkien', 310, 'Paperback', 'A fantasy novel and children''s book', 'ENGLISH'),
('Pride and Prejudice', 'Romance', 'ADULT', 11.99, '1813-01-28', 'Jane Austen', 432, 'Paperback', 'A romantic novel of manners', 'ENGLISH'),
('The Catcher in the Rye', 'Coming-of-age Fiction', 'YOUNG_ADULT', 13.49, '1951-07-16', 'J.D. Salinger', 277, 'Paperback', 'A story about teenage rebellion', 'ENGLISH'),
('Brave New World', 'Dystopian Fiction', 'ADULT', 14.49, '1932-01-01', 'Aldous Huxley', 311, 'Paperback', 'A dystopian novel set in a futuristic World State', 'ENGLISH');
