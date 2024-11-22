CREATE TABLE users (
	id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(64) UNIQUE NOT NULL,
	password VARCHAR(320) NOT NULL,
	role VARCHAR(64) NOT NULL,
	created_date Timestamp,
    updated_date Timestamp
);

INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('ada@kth.se', '{bcrypt}$2a$10$OKAWS5qvYqp.23sfCA9t8OxuDmJ4UItAvW3HYICfzOVC2p3xS7j8G', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('admin@kth.se', '{bcrypt}$2a$10$cLGN89RZEFLmpGQtR0Y2feFAt4D3oktNnMkPsrsGGZl1C.V5kDPkO', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('beda@kth.se', '{bcrypt}$2a$10$o78B4NYWChcVH86bA4ORKuP0xk8r0qi52bkjjdKY5PlFs95o8pQlG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('user@kth.se', '{bcrypt}$2a$10$/67Knhyrp24MdIKBxuwY7uxarw2VEIqnkUxhwLNcMJF6LpgYHDpQu', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('user1@kth.se', '{bcrypt}$2a$10$LA5Uw35NwVxFj6yhJJtZTewWG7iR9PjTHjSmYhtyH4N658N3LlI5.', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('user2@kth.se', '{bcrypt}$2a$10$ZvkGgqRxaSlhD378.wk.uum/79iFkPXFYBaNqhTNTONFK2io2dDOK', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (username, password, role, created_date, updated_date)
VALUES ('user3@kth.se', '{bcrypt}$2a$10$kjlScRlU6Q5JAWgME4nHbeunvhSLCn5PK6m9cvGa7LTjWMk4Flj0C', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--INSERT INTO users (username, password) VALUES ( 'ada@kth.se', 'qwerty');
--INSERT INTO users (username, password) VALUES ( 'beda@kth.se', 'qwerty');


CREATE TABLE courses(
	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	title VARCHAR(64) NOT NULL,
	status int NOT NULL
);

INSERT INTO courses (title,status) VALUES ('ID1212',0); -- skapar en kö för ID1212
INSERT INTO courses (title,status) VALUES ('DD1331',0); -- skapar kö för DD1331
INSERT INTO courses (title,status) VALUES ('IX1501',0);
INSERT INTO courses (title,status) VALUES ('ID1206',0);
INSERT INTO courses (title,status) VALUES ('IV1201',0);
INSERT INTO courses (title,status) VALUES ('IE1204',1);
INSERT INTO courses (title,status) VALUES ('ID1217',1);

CREATE TABLE queueitem (
	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	user_id INT NOT NULL REFERENCES users(id),
	course_id INT NOT NULL REFERENCES courses(id),
	location VARCHAR(64) NOT NULL, -- t ex "Ka-209" eller "zoom"
	active BOOLEAN NOT NULL, -- sätts till True när assistent påbörjar hjälp/redovisning.
	comment VARCHAR(64) NOT NULL
);
INSERT INTO queueitem (user_id, course_id, location, active, comment) VALUES (1, 1, 'Ka-209', False, 'L3'); -- ada köar till ID1212 för att redovisa L3

CREATE TABLE administrators(
	user_id INT NOT NULL REFERENCES users(id),
	course_id INT NOT NULL REFERENCES courses(id)
);

INSERT INTO administrators (user_id, course_id) VALUES (1,1); -- ada är assistent på ID1212
INSERT INTO administrators (user_id, course_id) VALUES (1,2); -- ada är (även) assistent på DD1331
INSERT INTO administrators (user_id, course_id) VALUES (1,6);

CREATE TABLE access (
    user_id INT NOT NULL REFERENCES users(id),
    course_id INT NOT NULL REFERENCES courses(id)
);


-- Granting access to users for specific courses
INSERT INTO access (user_id, course_id) VALUES (3, 1); -- beda has access to ID1212
INSERT INTO access (user_id, course_id) VALUES (3, 2); -- beda has access to DD1331
INSERT INTO access (user_id, course_id) VALUES (3, 3); -- beda has access to IX1501
INSERT INTO access (user_id, course_id) VALUES (4, 4); -- user has access to ID1206
INSERT INTO access (user_id, course_id) VALUES (4, 6); -- user has access to IE1204
INSERT INTO access (user_id, course_id) VALUES (3, 6);
INSERT INTO access (user_id, course_id) VALUES (3, 7);


CREATE TABLE messages(
	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	from_id INT NOT NULL REFERENCES users(id),
	to_id INT NOT NULL REFERENCES users(id),
	time TIMESTAMP NOT NULL,
	text VARCHAR(64) NOT NULL
);
INSERT INTO messages (from_id, to_id, time, text) VALUES (1,2,CURRENT_TIMESTAMP,'Ange vilken lab det gäller!'); -- ada skickar meddelande till beda