CREATE SCHEMA db2_project DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

--------------------------------------------------
--												--
--												--
--		MAIN TABLES ARE DEFINED HERE BELOW		--
--												--
--												--
--------------------------------------------------


--
-- TABLE FOR USERS AND ADMINISTRATORS
-- Additional info: role is 1 if the user is a normal user, it is 2 if it is an administrator
--
CREATE TABLE IF NOT EXISTS User (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	nickname VARCHAR(25) NOT NULL UNIQUE KEY,
	password VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	points INT UNSIGNED,
	blocked BOOL NOT NULL,
	role INT UNSIGNED NOT NULL,
	CHECK(role = 1 OR role = 2)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONNAIRES
--
CREATE TABLE IF NOT EXISTS Questionnaire (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	date DATE NOT NULL,
	name VARCHAR(50) NOT NULL UNIQUE KEY
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONS
-- Additional info: questions can be checkbox, selection, string or comment. Numbers represent this order
--
CREATE TABLE IF NOT EXISTS Question (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	question VARCHAR(50) NOT NULL,
	type SMALLINT NOT NULL,
	CHECK(type = 1 OR type = 2 OR type = 3 OR type = 4)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR PRODUCT ANSWERS
--
CREATE TABLE IF NOT EXISTS ProductAnswer (
	questionnaireId INT UNSIGNED,
	questionId INT UNSIGNED,
	userId INT UNSIGNED,
	number INT UNSIGNED,
	word VARCHAR(50) NOT NULL,
	PRIMARY KEY (questionnaireId,questionId,userId,number),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR PRODUCT ANSWERS
--
CREATE TABLE IF NOT EXISTS OffensiveWord (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	word VARCHAR(50) NOT NULL UNIQUE KEY
) AUTO_INCREMENT = 1;


--------------------------------------------------
--												--
--												--
--	RELATIONS TABLES ARE DEFINED HERE BELOW		--
--												--
--												--
--------------------------------------------------

--
-- TABLE FOR POSSIBLE ANSWERS
--
CREATE TABLE IF NOT EXISTS PossibleAnswer (
	id INT UNSIGNED AUTO_INCREMENT,
	questionId INT UNSIGNED,
	word VARCHAR(50) NOT NULL,
	PRIMARY KEY (id,questionId),
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1;

--
-- TABLE FOR PERSONAL ANSWERS
-- Additional info sex: M = male, F = female, U = undefined
-- Additional info Expertise: 0 = none, 1 = low, 2 = medium, 3 = high
--
CREATE TABLE IF NOT EXISTS PersonalAnswer (
	questionnaireId INT UNSIGNED,
	userId INT UNSIGNED,
	age SMALLINT UNSIGNED,
	sex CHAR(1),
	expertise SMALLINT UNSIGNED,
	PRIMARY KEY (questionnaireId,userId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CHECK(sex = 'M' OR sex = 'F' OR sex = 'U'),
	CHECK(expertise = 0 OR expertise = 1 OR expertise = 2 OR expertise = 3)
);

--
-- TABLE FOR QUESTIONNAIRES CREATIONS
--
CREATE TABLE IF NOT EXISTS Creation (
	questionnaireId INT UNSIGNED,
	creatorId INT UNSIGNED,
	PRIMARY KEY (questionnaireId,creatorId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (creatorId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR QUESTIONNAIRE SUBMISSIONS
--
CREATE TABLE IF NOT EXISTS Submission (
	questionnaireId INT UNSIGNED,
	userId INT UNSIGNED,
	submitted BOOL NOT NULL,
	points INT UNSIGNED NOT NULL,
	date DATETIME NOT NULL,
	PRIMARY KEY (questionnaireId,userId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR INCLUSION RELATION BETWEEN QUESTIONS AND QUESTIONNAIRES
--
CREATE TABLE IF NOT EXISTS Inclusion (
	questionnaireId INT UNSIGNED,
	questionId INT UNSIGNED,
	PRIMARY KEY (questionnaireId,questionId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE
);