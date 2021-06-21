-- STUDENTS TABLE
CREATE TABLE student (
	id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	age INT,
	biography_url VARCHAR(500),
	
	CONSTRAINT pk_student PRIMARY KEY (id)
);

COMMIT;