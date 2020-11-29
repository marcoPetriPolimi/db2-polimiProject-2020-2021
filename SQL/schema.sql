-- CREATE SCHEMA db2_project DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

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
	nickname VARCHAR(25) NOT NULL,
	password VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	registration DATE NOT NULL,
	points INT UNSIGNED,
	blocked BOOL NOT NULL,
	role INT UNSIGNED NOT NULL,
	UNIQUE KEY(nickname),
	UNIQUE KEY(email),
	CHECK(role = 1 OR role = 2)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONNAIRES
--
CREATE TABLE IF NOT EXISTS Questionnaire (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	date DATE NOT NULL,
	name VARCHAR(50) NOT NULL,
	creatorId INT UNSIGNED,
    	FOREIGN KEY(creatorId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE KEY(name)
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
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionnaireId INT UNSIGNED NOT NULL,
	questionId INT UNSIGNED NOT NULL,
	userId INT UNSIGNED NOT NULL,
	word VARCHAR(50) NOT NULL,
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR PRODUCT ANSWERS
--
CREATE TABLE IF NOT EXISTS OffensiveWord (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	word VARCHAR(50) NOT NULL,
	UNIQUE KEY(word)
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
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionId INT UNSIGNED,
	word VARCHAR(50) NOT NULL,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1;

--
-- TABLE FOR PERSONAL ANSWERS
-- Additional info sex: M = male, F = female, U = undefined
-- Additional info Expertise: 0 = none, 1 = low, 2 = medium, 3 = high
--
CREATE TABLE IF NOT EXISTS PersonalAnswer (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionnaireId INT UNSIGNED,
	userId INT UNSIGNED,
	age SMALLINT UNSIGNED,
	sex CHAR(1),
	expertise SMALLINT UNSIGNED,
    UNIQUE KEY(questionnaireId,userId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CHECK(sex = 'M' OR sex = 'F' OR sex = 'U'),
	CHECK(expertise = 0 OR expertise = 1 OR expertise = 2 OR expertise = 3)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONNAIRE SUBMISSIONS
--
CREATE TABLE IF NOT EXISTS Submission (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionnaireId INT UNSIGNED,
	userId INT UNSIGNED,
	submitted BOOL NOT NULL,
	points INT UNSIGNED NOT NULL,
	date DATETIME NOT NULL,
    UNIQUE KEY(questionnaireId,userId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR INCLUSION RELATION BETWEEN QUESTIONS AND QUESTIONNAIRES
--
CREATE TABLE IF NOT EXISTS Inclusion (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionnaireId INT UNSIGNED,
	questionId INT UNSIGNED,
    	UNIQUE KEY(questionnaireId,questionId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE
);


--------------------------------------------------
--												--
--												--
--	TRIGGERS ON THE DATABASE ARE HERE BELOW		--
--												--
--												--
--------------------------------------------------

DELIMITER $$

CREATE TRIGGER QuestionnairesCreatorIsAdministrator
BEFORE INSERT ON Questionnaire
FOR EACH ROW
BEGIN
	DECLARE isAdmin INT;
    
    	SELECT `role` into isAdmin FROM User WHERE id = NEW.creatorId;
    	
	IF (isAdmin <> 2) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only admins can create questionnaires';
	END IF;
END$$
    
DELIMITER ;

DELIMITER $$

CREATE TRIGGER UserDoesQuestionnairesAfterRegistration
AFTER INSERT ON Submission
FOR EACH ROW
BEGIN
	DECLARE regDate DATE;

	SELECT registration into regDate FROM User WHERE id = NEW.userId;

	IF (NEW.date < regDate) THEN
		SIGNAL SQLSTATE 'HV000' SET MESSAGE_TEXT = 'Users cannot fill questionnaires before registration.';
	END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER AdministratorCreatesQuestionnairesAfterRegistration
BEFORE INSERT ON Questionnaire
FOR EACH ROW
BEGIN
	DECLARE regDate DATE;

	SELECT registration into regDate FROM User WHERE id = NEW.creatorId;

	IF (NEW.date < regDate) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Admins cannot create questionnaires before registration.';
	END IF;
END$$

DELIMITER ;

DELIMITER $$

-- CREATE TRIGGER QuestionnairesResponsesAreEqualToQuestions
-- TODO
-- BEGIN
	-- TODO
-- END$$

DELIMITER ;

DELIMITER $$

-- CREATE TRIGGER QuestionnairesResponsesDoNotContainOffensiveWord
-- BEFORE INSERT ON productAnswer
-- BEGIN
-- 	-- TODO
-- END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER ResponseNumberForAQuestionIsPositiveAndLessThanMaximum
AFTER INSERT ON ProductAnswer
FOR EACH ROW
BEGIN
	DECLARE maximumResponses, givenResponses, responseType INT;

	SELECT count(*) INTO maximumResponses FROM PossibleAnswer WHERE questionId = NEW.questionId;
	SELECT count(*) INTO givenResponses FROM ProductAnswer WHERE questionId = NEW.questionId AND questionnaireId = 	NEW.questionnaireId AND userId = NEW.userId;
	SELECT type INTO responseType FROM Question WHERE id = NEW.questionId;

	IF (responseType = 1) THEN
		IF (givenResponses < 1 OR givenResponses > maximumResponses) THEN
			SIGNAL SQLSTATE 'HV000' SET MESSAGE_TEXT = 'Responses to a multiple question must be less than the 	maximum number of responses and at least one.';
		END IF;
	ELSE
		IF (givenResponses <> 1) THEN
			SIGNAL SQLSTATE 'HV000' SET MESSAGE_TEXT = 'Response to a single question must be unique.';
		END IF;
	END IF;
END$$

DELIMITER ;
DELIMITER $$

-- @author ETION
CREATE TRIGGER QuestionnairesHaveOneCreator
BEFORE INSERT on Questionnaire
FOR EACH ROW
WHEN (
		1 <> (	SELECT count(c.id) -- count all the id's in table creation that
		FROM user AS u, creation AS c  	
		WHERE u.id = c.creatorId AND c.questionnaireId = new.id ) -- that join user to our questionnaire
        )
raise_application_error(-20000, 'Please insert only ONE admin as creator');

DELIMITER ;

DELIMITER $$


-- @author ETION
CREATE TRIGGER NicknamesDoNotContainOffensiveWord
AFTER UPDATE OF nickname ON user -- //( change )//
FOR EACH ROW

WHEN (new.nickname IN ( SELECT word FROM offensiveWord ) )
UPDATE user 
SET user.nickname =  old.nickname
WHERE user.id = new.id;
raise_application_error(-20000, 'No offensive words are allowed as nicknames');

DELIMITER ;

