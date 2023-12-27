DROP DATABASE RevSpeed;
CREATE DATABASE RevSpeed;

USE RevSpeed;

DROP TABLE User;
CREATE TABLE User(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL,
phone_number BIGINT(13) NOT NULL,
address VARCHAR (50),
email_id VARCHAR(35) NOT NULL UNIQUE,
password VARCHAR(15) NOT NULL,
is_broadband BOOLEAN DEFAULT FALSE,
is_dth BOOLEAN DEFAULT FALSE,
role VARCHAR(15) NOT NULL DEFAULT "user"
);

select * from User;

DROP PROCEDURE insertUser;
DELIMITER $$
CREATE PROCEDURE insertUser(name VARCHAR(30), phone_number BIGINT(13), address VARCHAR (50), email_id VARCHAR(35), password VARCHAR(15))
BEGIN
	INSERT INTO User (name, phone_number, address, email_id, password) VALUES (name, phone_number, address, email_id, password);
END $$
DELIMITER ;
CALL insertUser('RUPA', 7895632780, 'Wardha', 'rupa@gmail.com', '123456789');

DELIMITER //
CREATE PROCEDURE GetUserCountByEmail(IN emailParam VARCHAR(255), OUT userCount INT)
BEGIN
    SELECT COUNT(*) INTO userCount
    FROM user
    WHERE email_id = emailParam;
END //
DELIMITER ;
CALL GetUserCountByEmail('krunal@gmail.com', @userCount);
SELECT @userCount;


DROP PROCEDURE loginUser;
DELIMITER //
CREATE PROCEDURE loginUser(
    IN p_email VARCHAR(255),
    IN p_password VARCHAR(255)
)
BEGIN
    DECLARE userCount INT;

    -- Check if the user with the provided email and password exists
    SELECT COUNT(*) INTO userCount
    FROM User
    WHERE email_id = p_email AND password = p_password;

    -- If a user is found, return user details; otherwise, return 0
    IF userCount > 0 THEN
        SELECT 
            1 AS success,
            id,
            name,
            phone_number,
            address,
            email_id,
            password,
            role
        FROM User
        WHERE email_id = p_email AND password = p_password;
    ELSE
        SELECT 0 AS success;
    END IF;
END //

DROP PROCEDURE updateUserProfile;
DELIMITER //
CREATE PROCEDURE updateUserProfile(
    IN p_user_id INT,
    IN p_name VARCHAR(255),
    IN p_phone_number BIGINT,
    IN p_address VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_password VARCHAR(255)
)
BEGIN
    UPDATE User
    SET
        name = IFNULL(p_name, name),
        phone_number = IFNULL(p_phone_number, phone_number),
        address = IFNULL(p_address, address),
        email_id = IFNULL(p_email, email_id),
        password = IFNULL(p_password, password)
    WHERE id = p_user_id;
END //
DELIMITER ;

DROP TABLE Services;
CREATE TABLE Services(
sr_nm_id INT PRIMARY KEY AUTO_INCREMENT,
service_name VARCHAR(25) UNIQUE
);

INSERT INTO Services VALUES(1, 'Broadband Service'), (2, 'DTH Service');

SELECT * FROM Services;

DROP TABLE Broadband_service;
CREATE TABLE Broadband_service(
br_sr_id INT PRIMARY KEY AUTO_INCREMENT,
service_name_id INT,
broadband_service_plans VARCHAR(10) UNIQUE,
FOREIGN KEY (service_name_id) REFERENCES Services(sr_nm_id)
);

INSERT INTO Broadband_service (br_sr_id, service_name_id, broadband_service_plans) VALUES 
(1, 1, 'Monthly'), 
(2, 1, 'Quaterly'), 
(3, 1, 'Yearly');

SELECT * FROM Broadband_service;

DROP TABLE Broadband_service_plans_details;
CREATE TABLE Broadband_service_plans_details(
br_sr_pl_dt_id INT PRIMARY KEY AUTO_INCREMENT,
br_sr_id INT,
plan_name VARCHAR(50) UNIQUE,
price DECIMAL(10, 2),
FOREIGN KEY (br_sr_id) REFERENCES Broadband_service(br_sr_id)
);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(1, 1, 'Monthly Basic Plan / 28 dasy / 20 MBPS', 300),
(2, 1, 'Monthly Standard Plan / 28 days / 25 MBPS', 350),
(3, 1, 'Monthly Entertainment Plan / 28 days / 40 MBPS', 450);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(4, 2, 'Quaterly Basic Plan / 84 days / 22 MBPS', 800),
(5, 2, 'Quaterly Standard Plan / 84 days / 27 MBPS',900),
(6, 2, 'Quaterly Entertainment Plan / 84 days / 42 MBPS', 1000);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(7, 3, 'Yearly Basic Plan / 360 days / 27 MBPS', 3000),
(8, 3, 'Yearly Standard Plan / 360 days / 32 MBPS',3500),
(9, 3, 'Yearly Entertainment Plan / 360 days / 50 MBPS', 4500);

SELECT * FROM Broadband_service_plans_details where br_sr_id = 1;

DROP TABLE OTT_platform;
CREATE TABLE OTT_platform(
ott_platform_id INT PRIMARY KEY AUTO_INCREMENT,
platform_name VARCHAR(30) UNIQUE
);

INSERT INTO OTT_platform VALUES 
(1, 'Netflix'), 
(2, 'Sling'), 
(3, 'IBM watson'), 
(4, 'Hulu'), 
(5, 'Amazon prime video');

SELECT * FROM OTT_platform;

DROP TABLE Broadband_OTT_Mapping;
CREATE TABLE Broadband_OTT_Mapping(
br_ott_map_id INT PRIMARY KEY AUTO_INCREMENT,
br_sr_id INT,
ott_platform_id INT,
FOREIGN KEY (br_sr_id) REFERENCES Broadband_service (br_sr_id),
FOREIGN KEY (ott_platform_id) REFERENCES OTT_platform(ott_platform_id)
);

-- Associate monthly broadband plan with ott
INSERT INTO Broadband_OTT_Mapping VALUES(1, 1, 1);

-- Associate quaterly broadband plan with ott
INSERT INTO Broadband_OTT_Mapping VALUES(2, 2, 1),(3, 2, 2);

-- Associate quaterly broadband plan with ott
INSERT INTO Broadband_OTT_Mapping VALUES(4, 3, 1),(5, 3, 2),(6, 3, 3),(7, 3, 4),(8, 3, 5);

DELIMITER //

CREATE PROCEDURE GetPlatformNameByBrSrId(IN br_sr_id_param INT)
BEGIN
    SELECT OP.platform_name
    FROM Broadband_OTT_Mapping BOM
    JOIN OTT_Platform OP ON BOM.ott_platform_id = OP.ott_platform_id
    WHERE BOM.br_sr_id = br_sr_id_param;
END //

DELIMITER ;
call GetPlatformNameByBrSrId(3);
SELECT OP.platform_name
FROM Broadband_OTT_Mapping BOM
JOIN OTT_Platform OP ON BOM.ott_platform_id = OP.ott_platform_id
WHERE BOM.br_sr_id = 1; -- Here 1 represents the monthly broadband plan, 2 represents the quaterly broadband plan, 3 represents the yearly broadband plan


DROP TABLE Dth_service;
CREATE TABLE Dth_service (
    dth_sr_id INT PRIMARY KEY AUTO_INCREMENT,
    service_name_id INT,
    dth_service_plans VARCHAR(50) UNIQUE,
    FOREIGN KEY (service_name_id) REFERENCES Services(sr_nm_id)
);

INSERT INTO Dth_service (dth_sr_id, service_name_id, dth_service_plans) VALUES 
(1, 2, 'Monthly'), 
(2, 2, 'Quaterly'), 
(3, 2, 'Yearly');
SELECT * FROM Dth_service;	

drop table Dth_service_plans;
CREATE TABLE Dth_service_plans (
    dth_sr_pl_dt_id INT PRIMARY KEY AUTO_INCREMENT,
    dth_sr_id INT,
    language VARCHAR(25),
    channel_category VARCHAR(25),
    UNIQUE KEY (dth_sr_id, language, channel_category),
    FOREIGN KEY (dth_sr_id) REFERENCES Dth_service(dth_sr_id)
);

select * from Dth_service_plans;

-- Insert data into Dth_service_plans table
INSERT INTO Dth_service_plans (dth_sr_id, language, channel_category) VALUES
(1, 'Hindi', 'Entertainment Plan'), (1, 'Hindi', 'Sports Plan'), (1, 'Hindi', 'News Plan'),
(2, 'Tamil', 'Entertainment Plan'), (2, 'Tamil', 'Sports Plan'), (2, 'Tamil', 'News Plan');
select * from Dth_service_plans;

-- Create DTH_channel_details table
drop table DTH_channel_details;
CREATE TABLE DTH_channel_details (
    dth_chnl_dt_id INT PRIMARY KEY AUTO_INCREMENT,
    dth_sr_pl_dt_id INT,
    channel_name VARCHAR(50),
    price DECIMAL(10, 2),
    FOREIGN KEY (dth_sr_pl_dt_id) REFERENCES Dth_service_plans(dth_sr_pl_dt_id) 
);


-- Insert data into DTH_channel_details table
INSERT INTO DTH_channel_details (dth_sr_pl_dt_id, channel_name, price) VALUES
(1, 'Hindi Entertainment Channel 1', 15.00), (1, 'Hindi Entertainment Channel 2', 20.00), (1, 'Hindi Entertainment Channel 3', 25.00),
(2, 'Hindi Sports Channel 1', 12.00), (2, 'Hindi Sports Channel 2', 18.00), (2, 'Hindi Sports Channel 3', 22.00),
(3, 'Hindi News Channel 1', 8.00), (3, 'Hindi News Channel 2', 10.00), (3, 'Hindi News Channel 3', 12.00),
(4, 'Tamil Entertainment Channel 1', 18.00), (4, 'Tamil Entertainment Channel 2', 22.00), (4, 'Tamil Entertainment Channel 3', 25.00),
(5, 'Tamil Sports Channel 1', 15.00), (5, 'Tamil Sports Channel 2', 20.00), (5, 'Tamil Sports Channel 3', 25.00),
(6, 'Tamil News Channel 1', 10.00), (6, 'Tamil News Channel 2', 15.00), (6, 'Tamil News Channel 3', 18.00);
select * from DTH_channel_details;

drop table User_Service_Link;
CREATE TABLE User_Service_Link (
    user_service_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    br_sr_dt_id INT,
    dth_service_plan_id  INT,
    subscription_start_date DATE,
    subscription_end_date DATE,
    user_status boolean default false,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (br_sr_dt_id) REFERENCES Broadband_service_plans_details(br_sr_pl_dt_id),
    FOREIGN KEY (dth_service_plan_id ) REFERENCES Dth_service_plans(dth_sr_pl_dt_id)
);
select * from User_Service_Link;
Alter table User_Service_Link add column user_status boolean;
-- ALL GET QUERIES

-- Querry to get user_name, phone_number, address, email_id, subscription_plan, subscription_start_date, subscription_end_date of perticular user
SELECT
    U.name AS user_name,
    U.phone_number,
    U.address,
    U.email_id,
    BSLD.plan_name AS subscription_plan,
    USL.subscription_start_date,
    USL.subscription_end_date
FROM
    User AS U
LEFT JOIN User_Service_Link AS USL ON U.id = USL.user_id
LEFT JOIN Broadband_service_plans_details AS BSLD ON USL.br_sr_dt_id = BSLD.br_sr_pl_dt_id
WHERE U.id = 1;

-- Included total price of purchased plan

SELECT
    U.name AS user_name,
    U.phone_number,
    U.address,
    U.email_id,
    BSLD.plan_name AS subscription_plan,
    USL.subscription_start_date,
    USL.subscription_end_date,
    SUM(BSLD.price) AS total_purchased_price
FROM
    User AS U
LEFT JOIN User_Service_Link AS USL ON U.id = USL.user_id
LEFT JOIN Broadband_service_plans_details AS BSLD ON USL.br_sr_dt_id = BSLD.br_sr_pl_dt_id
WHERE U.id = 1
GROUP BY
    U.id, U.name, U.phone_number, U.address, U.email_id, BSLD.plan_name, USL.subscription_start_date, USL.subscription_end_date;
    
    DELIMITER //

CREATE PROCEDURE GetUserBroadbandDetails(IN userId INT)
BEGIN
    SELECT
        U.name AS user_name,
        U.phone_number,
        U.address,
        U.email_id,
        BSLD.plan_name AS subscription_plan,
        USL.subscription_start_date,
        USL.subscription_end_date,
        SUM(BSLD.price) AS total_purchased_price
    FROM
        User AS U
    LEFT JOIN User_Service_Link AS USL ON U.id = USL.user_id
    LEFT JOIN Broadband_service_plans_details AS BSLD ON USL.br_sr_dt_id = BSLD.br_sr_pl_dt_id
    WHERE U.id = userId and user_status = 1
    GROUP BY
        U.id, U.name, U.phone_number, U.address, U.email_id, BSLD.plan_name, USL.subscription_start_date, USL.subscription_end_date;
END //

DELIMITER ;
call GetUserBroadbandDetails(4);





-- Querry to get OTT platform of perticular user
SELECT 
	OP.platform_name
FROM 
	User_Service_Link USL
JOIN 
	Broadband_service_plans_details BSLD 
    ON USL.br_sr_dt_id = BSLD.br_sr_pl_dt_id
JOIN 
	Broadband_OTT_Mapping BOM 
    ON BSLD.br_sr_id = BOM.br_sr_id
JOIN 
	OTT_platform OP 
    ON BOM.ott_platform_id = OP.ott_platform_id
WHERE USL.user_id = 1;

DELIMITER //

DROP PROCEDURE GetOTTPlatformsForUser;
CREATE PROCEDURE GetOTTPlatformsForUser(IN userId INT)
BEGIN
    SELECT DISTINCT
        OP.platform_name
    FROM 
        User_Service_Link USL
    JOIN 
        Broadband_service_plans_details BSLD 
        ON USL.br_sr_dt_id = BSLD.br_sr_pl_dt_id
    JOIN 
        Broadband_OTT_Mapping BOM 
        ON BSLD.br_sr_id = BOM.br_sr_id
    JOIN 
        OTT_platform OP 
        ON BOM.ott_platform_id = OP.ott_platform_id
    WHERE USL.user_id = userId;
END //

DELIMITER ;
call GetOTTPlatformsForUser(1);

-- Procedure to get language options
DELIMITER //
CREATE PROCEDURE GetLanguageOptions()
BEGIN
    SELECT DISTINCT dth_sr_id, language FROM Dth_service_plans;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE GetDTHChannelsByLanguageAndCategory(
    IN p_language VARCHAR(25),
    IN p_channel_category VARCHAR(25)
)
BEGIN
    SELECT dc.channel_name
    FROM DTH_channel_details dc
    JOIN Dth_service_plans dsp ON dc.dth_sr_pl_dt_id = dsp.dth_sr_pl_dt_id
    JOIN Dth_service ds ON dsp.dth_sr_id = ds.dth_sr_id
    WHERE dsp.language = p_language AND dsp.channel_category = p_channel_category;
END //

DELIMITER ;
CALL GetDTHChannelsByLanguageAndCategory('Tamil', 'Entertainment');

DELIMITER //
CREATE PROCEDURE GetPlansForLanguage(IN selectedLanguage VARCHAR(25))
BEGIN
    SELECT dth_sr_pl_dt_id, channel_category FROM Dth_service_plans WHERE language = selectedLanguage;
END // 
DELIMITER ;
call GetPlansForLanguage('Tamil');

DELIMITER //
CREATE PROCEDURE CalculateDTHServicePlanPrice(
    IN p_language VARCHAR(25),
    IN p_channel_category VARCHAR(25),
    IN p_duration VARCHAR(10)
)
BEGIN
    DECLARE total_price DECIMAL(10, 2);

    -- Calculate the total price based on the given language and channel category
    SELECT SUM(price) INTO total_price
    FROM DTH_channel_details dc
    JOIN Dth_service_plans dsp ON dc.dth_sr_pl_dt_id = dsp.dth_sr_pl_dt_id
    WHERE dsp.language = p_language AND dsp.channel_category = p_channel_category;

    -- Adjust the total price based on the duration
    IF p_duration = 'Monthly' THEN
        SET total_price = total_price * 1;  -- No adjustment for monthly
    ELSEIF p_duration = 'Quaterly' THEN
        SET total_price = total_price * 3;  -- Assuming a quarter has three months
    ELSEIF p_duration = 'Yearly' THEN
        SET total_price = total_price * 12; -- Assuming a year has twelve months
    END IF;

    -- Display the calculated total price
    SELECT total_price AS calculated_total_price;
END //

DELIMITER ;

-- Call the stored procedure for a specific language, channel category, and duration
CALL CalculateDTHServicePlanPrice('Hindi', 'Entertainment', 'Quaterly');
