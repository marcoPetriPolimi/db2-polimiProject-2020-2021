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
-- TABLE FOR PRODUCT
--
CREATE TABLE IF NOT EXISTS Product (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	image BLOB NOT NULL,
	UNIQUE KEY(name)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONNAIRES
--
CREATE TABLE IF NOT EXISTS Questionnaire (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	creatorId INT UNSIGNED,
	name VARCHAR(50) NOT NULL,
	date DATE NOT NULL,
	product INT UNSIGNED NOT NULL,
	UNIQUE KEY(name),
    FOREIGN KEY(creatorId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(product) REFERENCES Product(id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1;

--
-- TABLE FOR QUESTIONS
-- Additional info: questions can be checkbox (1), selection (2), string (3) or comment (4). Numbers represent this order
--
CREATE TABLE IF NOT EXISTS Question (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	question VARCHAR(50) NOT NULL,
	type SMALLINT NOT NULL,
	CHECK(type = 1 OR type = 2 OR type = 3 OR type = 4)
) AUTO_INCREMENT = 1;

--
-- TABLE FOR OFFENSIVE WORDS
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
-- TABLE FOR QUESTIONNAIRE SUBMISSIONS
--
CREATE TABLE IF NOT EXISTS Submission (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	userId INT UNSIGNED,
	questionnaireId INT UNSIGNED,
	submitted BOOL NOT NULL,
	points INT UNSIGNED NOT NULL,
	date DATETIME NOT NULL,
    UNIQUE KEY(questionnaireId,userId),
	FOREIGN KEY (questionnaireId) REFERENCES Questionnaire(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR POSSIBLE ANSWERS
--
CREATE TABLE IF NOT EXISTS PossibleAnswer (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	questionId INT UNSIGNED,
	answerText VARCHAR(50) NOT NULL,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1;

--
-- TABLE FOR PRODUCT ANSWERS
--
CREATE TABLE IF NOT EXISTS ProductAnswer (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	submissionId INT UNSIGNED,
	questionId INT UNSIGNED,
	userId INT UNSIGNED NOT NULL,
	word VARCHAR(50) NOT NULL,
	FOREIGN KEY (questionId) REFERENCES Question(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (submissionId) REFERENCES Submission(id) ON UPDATE CASCADE ON DELETE CASCADE
);

--
-- TABLE FOR PERSONAL ANSWERS
-- Additional info sex: M = male, F = female, U = undefined
-- Additional info Expertise: 0 = none, 1 = low, 2 = medium, 3 = high
--
CREATE TABLE IF NOT EXISTS PersonalAnswer (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	submissionId INT UNSIGNED,
	age SMALLINT UNSIGNED,
	sex CHAR(1),
	expertise SMALLINT UNSIGNED,
	FOREIGN KEY (submissionId) REFERENCES Submission(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CHECK(sex = 'M' OR sex = 'F' OR sex = 'U'),
	CHECK(expertise = 0 OR expertise = 1 OR expertise = 2 OR expertise = 3)
) AUTO_INCREMENT = 1;

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

--
-- TABLE FOR REVIEW
--
CREATE TABLE IF NOT EXISTS Review (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	userId INT UNSIGNED NOT NULL,
	productId INT UNSIGNED NOT NULL,
	productReview VARCHAR(50) NOT NULL,
	UNIQUE KEY(userId,productId),
	FOREIGN KEY (userId) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (productId) REFERENCES Product(id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1;


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
    
    	SELECT role into isAdmin FROM User WHERE id = NEW.creatorId;
    	
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
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Users cannot fill questionnaires before registration.';
	END IF;
END$$

DELIMITER ;


DELIMITER $$
-- @author MARCO
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
-- @author MARCO
CREATE TRIGGER ResponseNumberForAQuestionIsPositiveAndLessThanMaximum
AFTER INSERT ON ProductAnswer
FOR EACH ROW
BEGIN
	DECLARE maximumResponses, givenResponses, responseType INT;

	SELECT count(*) INTO maximumResponses FROM PossibleAnswer WHERE questionId = NEW.questionId;
	SELECT count(*) INTO givenResponses FROM ProductAnswer WHERE questionId = NEW.questionId AND submissionId = NEW.submissionId;
	SELECT type INTO responseType FROM Question WHERE id = NEW.questionId;

	IF (responseType = 1) THEN
		IF (givenResponses < 1 OR givenResponses > maximumResponses) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Responses to a multiple question must be less than the 	maximum number of responses and at least one.';
		END IF;
	ELSE
		IF (givenResponses <> 1) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Response to a single question must be unique.';
		END IF;
	END IF;
END$$

DELIMITER ;


DELIMITER $$

-- @author ETION
CREATE TRIGGER NicknamesDoNotContainOffensiveWordOnUpdate
BEFORE INSERT ON User -- //( change )//
FOR EACH ROW
BEGIN
		DECLARE iteration INT DEFAULT 1; 
        DECLARE offence VARCHAR(50);
        DECLARE potentialOffence VARCHAR(50);
        
        SELECT LCASE(new.nickname) INTO potentialOffence; 
        SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = 1;
        
        LABEL1: WHILE (offence IS NOT NULL) DO
					IF ( LOCATE(offence, potentialOffence) <> 0 ) THEN -- function locate is position sensitive
						SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nickname should not contain offensive words. -UPD473';
					END IF;
                    
                    SET iteration = iteration + 1;
                    SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = iteration;
                    
				END WHILE LABEL1;
END$$

DELIMITER ;


DELIMITER $$

-- @author ETION
CREATE TRIGGER NicknamesDoNotContainOffensiveWordOnCreation
BEFORE INSERT ON user -- //( change )//
FOR EACH ROW
BEGIN
		DECLARE iteration INT DEFAULT 1; 
        DECLARE offence VARCHAR(50);
        DECLARE potentialOffence VARCHAR(50);
        
        SELECT LCASE(new.nickname) INTO potentialOffence; 
        SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = 1;
        
        LABEL1: WHILE (offence IS NOT NULL) DO
					IF ( LOCATE(offence, potentialOffence) <> 0 ) THEN -- function locate is position sensitive
						SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nicknames should not contain offensive words. -1N53R7';
					END IF;
                    
                    SET iteration = iteration + 1;
                    SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = iteration;
                    
				END WHILE LABEL1;
END$$


DELIMITER ;


DELIMITER $$

-- @author ETION
CREATE TRIGGER QuestionsDoNotContainOffensiveWordOnCreation
BEFORE INSERT ON question -- //( change )//
FOR EACH ROW
BEGIN
		DECLARE iteration INT DEFAULT 1; 
        DECLARE offence VARCHAR(50);
        DECLARE potentialOffence VARCHAR(50);
        
        SELECT LCASE(new.question) INTO potentialOffence; 
        SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = 1;
        
        LABEL1: WHILE (offence IS NOT NULL) DO
					IF ( LOCATE(offence, potentialOffence) <> 0 ) THEN -- function locate is position sensitive
						SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Questions should not contain offensive words. Check specifications.';
					END IF;
                    
                    SET iteration = iteration + 1;
                    SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = iteration;
                    
				END WHILE LABEL1;
END$$


DELIMITER ;


DELIMITER $$

-- @author ETION
CREATE TRIGGER ReviewDoNotContainOffensiveWordOnCreation
BEFORE INSERT ON review -- //( change )//
FOR EACH ROW
BEGIN
	DECLARE iteration INT DEFAULT 1; 
        DECLARE offence VARCHAR(50);
        DECLARE potentialOffence VARCHAR(50);
        
        SELECT LCASE(new.productReview) INTO potentialOffence; 
        SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = 1;
        
        LABEL1: WHILE (offence IS NOT NULL) DO
					IF ( LOCATE(offence, potentialOffence) <> 0 ) THEN -- function locate is position sensitive
						SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Review must not contain offensive words.';
					END IF;
                    
                    SET iteration = iteration + 1;
                    SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = iteration;
                    
				END WHILE LABEL1;
END$$

DELIMITER ;


DELIMITER $$

-- @author ETION
CREATE TRIGGER QuestionnairesResponsesDoNotContainOffensiveWordOnCreation
BEFORE INSERT ON productAnswer-- //( change )//
FOR EACH ROW
BEGIN
	DECLARE iteration INT DEFAULT 1; 
        DECLARE offence VARCHAR(50);
        DECLARE potentialOffence VARCHAR(50);
        
        SELECT LCASE(new.word) INTO potentialOffence; 
        SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = 1;
        
        LABEL1: WHILE (offence IS NOT NULL) DO
					IF ( LOCATE(offence, potentialOffence) <> 0 ) THEN -- function locate is position sensitive
						SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Answers must not contain offensive words.';
					END IF;
                    
                    SET iteration = iteration + 1;
                    SELECT LCASE(word) INTO offence FROM offensiveWord where offensiveWord.id = iteration;
                    
				END WHILE LABEL1;
END$$

DELIMITER ;

DELIMITER $$

-- @AUTHOR ETION
CREATE TRIGGER QuestionnairesResponsesAreEqualToQuestions
BEFORE INSERT ON ProductAnswer
FOR EACH ROW
BEGIN
	DECLARE numOfResponses, numOfQuestions INT;
	SELECT count(*) INTO numOfResponses 
	FROM ProductAnswer as P, Questionnaire as Q 
	WHERE P.questionnaireId = Q.id;

	SELECT count(*) INTO numOfQuestions 
	FROM Questionnaire as Q, Inclusion as I, Question as QT
	WHERE I.questionnaireId = Q.id and I.questionID = QT.id ;

	IF(numOfResponses <> numOfQuestions) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Number of answers must be equal to number of questions in questionnaire.';
	END IF;
END$$


DELIMITER ;