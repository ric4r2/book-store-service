-- =====================================================
-- Book Store Service - Database Initialization Script
-- =====================================================
-- This script creates default users and sample books
-- Run this after starting the application for the first time
-- 
-- Usage:
--   Get-Content src/main/resources/db/init-data.sql | docker exec -i bookstore-postgres psql -U postgres -d bookstore
-- =====================================================

-- Create admin employee user (password: password123)
INSERT INTO users (email, name, password, user_type, is_blocked, created_at, updated_at)
VALUES (
  'admin@bookstore.com',
  'Admin User',
  '$2a$10$tftckvwgXVncVniZUAdoded961.gzhacEAuLrnGqofdQp9KljLmpG',
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

-- Create sample client users (password: password123)
INSERT INTO users (email, name, password, user_type, is_blocked, created_at, updated_at)
VALUES 
  ('john@example.com', 'John Doe', '$2a$10$tftckvwgXVncVniZUAdoded961.gzhacEAuLrnGqofdQp9KljLmpG', 'CLIENT', false, NOW(), NOW()),
  ('jane@example.com', 'Jane Smith', '$2a$10$tftckvwgXVncVniZUAdoded961.gzhacEAuLrnGqofdQp9KljLmpG', 'CLIENT', false, NOW(), NOW());

-- Create client records
INSERT INTO clients (id, address, phone)
SELECT id, '123 Main St', '+1234567890'
FROM users
WHERE email = 'john@example.com';

INSERT INTO clients (id, address, phone)
SELECT id, '456 Oak Ave', '+0987654321'
FROM users
WHERE email = 'jane@example.com';

-- Add sample books
INSERT INTO books (name, author, genre, age_group, price, publication_year, number_of_pages, description, language, created_at, updated_at, deleted_at)
VALUES
  ('The Great Gatsby', 'F. Scott Fitzgerald', 'Classic Fiction', 'ADULT', 15.99, '1925-04-10', 180, 'A classic American novel set in the Jazz Age', 'ENGLISH', NOW(), NOW(), NULL),
  ('To Kill a Mockingbird', 'Harper Lee', 'Classic Fiction', 'YOUNG_ADULT', 18.99, '1960-07-11', 324, 'A gripping tale of racial injustice and childhood innocence', 'ENGLISH', NOW(), NOW(), NULL),
  ('1984', 'George Orwell', 'Dystopian Fiction', 'ADULT', 16.99, '1949-06-08', 328, 'A dystopian social science fiction novel', 'ENGLISH', NOW(), NOW(), NULL),
  ('Pride and Prejudice', 'Jane Austen', 'Romance', 'ADULT', 14.99, '1813-01-28', 432, 'A romantic novel of manners', 'ENGLISH', NOW(), NOW(), NULL),
  ('The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 'YOUNG_ADULT', 17.99, '1951-07-16', 277, 'A story about teenage rebellion and angst', 'ENGLISH', NOW(), NOW(), NULL),
  ('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 'Fantasy', 'CHILDREN', 12.99, '1997-06-26', 309, 'A young wizard begins his magical education', 'ENGLISH', NOW(), NOW(), NULL);
