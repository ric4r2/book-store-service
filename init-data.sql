-- Create default admin employee user
-- Password: password123 (BCrypt hash)
INSERT INTO users (email, name, password, user_type, is_blocked, created_at, updated_at)
VALUES (
  'admin@bookstore.com',
  'Admin User',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'EMPLOYEE',
  false,
  NOW(),
  NOW()
);

-- Create employee record
INSERT INTO employees (id, department, position)
SELECT id, 'Management', 'Store Manager'
FROM users
WHERE email = 'admin@bookstore.com';

-- Add some sample books
INSERT INTO books (name, author, isbn, publisher, publication_year, genre, price, stock_quantity, description, created_at, updated_at, deleted_at)
VALUES
  ('The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5', 'Scribner', 1925, 'Classic Fiction', 15.99, 50, 'A classic American novel set in the Jazz Age', NOW(), NOW(), NULL),
  ('To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4', 'J. B. Lippincott & Co.', 1960, 'Classic Fiction', 18.99, 45, 'A gripping tale of racial injustice and childhood innocence', NOW(), NOW(), NULL),
  ('1984', 'George Orwell', '978-0-452-28423-4', 'Secker & Warburg', 1949, 'Dystopian Fiction', 16.99, 60, 'A dystopian social science fiction novel', NOW(), NOW(), NULL),
  ('Pride and Prejudice', 'Jane Austen', '978-0-14-143951-8', 'T. Egerton', 1813, 'Romance', 14.99, 40, 'A romantic novel of manners', NOW(), NOW(), NULL),
  ('The Catcher in the Rye', 'J.D. Salinger', '978-0-316-76948-0', 'Little, Brown and Company', 1951, 'Fiction', 17.99, 35, 'A story about teenage rebellion and angst', NOW(), NOW(), NULL);
