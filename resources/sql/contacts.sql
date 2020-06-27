-- :name create-contacts-table
-- :command :execute
-- :result :raw
-- :doc creates contacts table
CREATE TABLE contacts (
  id SERIAL PRIMARY KEY,
  first_name TEXT,
  last_name TEXT,
  email TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);

-- :name get-contacts :? :*
SELECT * FROM contacts;

-- :name get-contact-by-id :? :1
SELECT * FROM contacts
WHERE id = :id;

-- :name insert-contact :? :1
INSERT INTO contacts (first_name, last_name, email)
VALUES (:first-name, :last-name, :email)
RETURNING id;

-- :name update-contact-by-id :! :1
UPDATE contacts
SET first_name = :first-name, last_name = :last-name, email = :email
WHERE id = :id;

-- :name delete-contact-by-id :! :1
DELETE FROM contacts WHERE id = :id;