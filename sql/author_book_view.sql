SELECT a.id as author_id, a.first_name, a.last_name, b.id as book_id, b.title, b.publisher, b.isbn
FROM author a
LEFT OUTER JOIN book b ON b.author_id = a.id