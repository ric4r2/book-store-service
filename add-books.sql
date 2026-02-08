-- Add some sample books (using actual Book entity fields)
INSERT INTO books (name, author, genre, age_group, price, publication_year, number_of_pages, description, language, created_at, updated_at, deleted_at)
VALUES
  ('The Great Gatsby', 'F. Scott Fitzgerald', 'Classic Fiction', 'ADULT', 15.99, '1925-04-10', 180, 'A classic American novel set in the Jazz Age', 'ENGLISH', NOW(), NOW(), NULL),
  ('To Kill a Mockingbird', 'Harper Lee', 'Classic Fiction', 'YOUNG_ADULT', 18.99, '1960-07-11', 324, 'A gripping tale of racial injustice and childhood innocence', 'ENGLISH', NOW(), NOW(), NULL),
  ('1984', 'George Orwell', 'Dystopian Fiction', 'ADULT', 16.99, '1949-06-08', 328, 'A dystopian social science fiction novel', 'ENGLISH', NOW(), NOW(), NULL),
  ('Pride and Prejudice', 'Jane Austen', 'Romance', 'ADULT', 14.99, '1813-01-28', 432, 'A romantic novel of manners', 'ENGLISH', NOW(), NOW(), NULL),
  ('The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 'YOUNG_ADULT', 17.99, '1951-07-16', 277, 'A story about teenage rebellion and angst', 'ENGLISH', NOW(), NOW(), NULL),
  ('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 'Fantasy', 'CHILDREN', 12.99, '1997-06-26', 309, 'A young wizard begins his magical education', 'ENGLISH', NOW(), NOW(), NULL);
