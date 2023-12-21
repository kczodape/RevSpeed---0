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

INSERT INTO User (id, name, phone_number, address, email_id, password, is_broadband) VALUES (1, 'Krunal Zodape', '1234567890', 'Nepal, India', 'krunal@gmail.com', 'RevSpeed01', TRUE), (3, 'Paresh Zodape', '1254789653', 'Gujrat, India', 'paresh@gmail.com', 'RevSpeed03', TRUE), (5, 'Jhon Sharma', '8532145986', 'Chennai, India', 'jhon@gmail.com', 'RevSpeed05', TRUE);

INSERT INTO User (id, name, phone_number, address, email_id, password, is_dth) VALUES (2, 'Rupali Wadkar', '3265789423', 'Rajsthan, India', 'rupali@gmail.com', 'RevSpeed02', TRUE), (4, 'Aakash Solanke', '6598742326', 'Uttarpradesh, India', 'aakash@gmail.com', 'RevSpeed04', TRUE);

INSERT INTO User (id, name, phone_number, address, email_id, password, role) VALUES (6, 'Krunal', '8530728729', 'USA', 'kradmin@gmail.com', "admin@1234", 'admin');
SELECT * FROM User;

DROP TABLE Services;
CREATE TABLE Services(
sr_nm_id INT PRIMARY KEY,
service_name VARCHAR(25) UNIQUE
);

INSERT INTO Services VALUES(1, 'broadband service'), (2, 'dth service');

SELECT * FROM Services;

DROP TABLE Broadband_service;
CREATE TABLE Broadband_service(
br_sr_id INT PRIMARY KEY,
service_name_id INT,
broadband_service_plans VARCHAR(10) UNIQUE,
FOREIGN KEY (service_name_id) REFERENCES Services(sr_nm_id)
);

INSERT INTO Broadband_service (br_sr_id, service_name_id, broadband_service_plans) VALUES 
(1, 1, 'monthly'), 
(2, 1, 'quaterly'), 
(3, 1, 'yearly');

SELECT * FROM Broadband_service;

DROP TABLE Broadband_service_plans_details;
CREATE TABLE Broadband_service_plans_details(
br_sr_pl_dt_id INT PRIMARY KEY,
br_sr_id INT,
plan_name VARCHAR(50) UNIQUE,
price DECIMAL(10, 2),
FOREIGN KEY (br_sr_id) REFERENCES Broadband_service(br_sr_id)
);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(1, 1, 'monthly basic plan', 300),
(2, 1, 'mpnthly standard plan',350),
(3, 1, 'monthly entertainment plan', 450);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(4, 2, 'quaterly basic plan', 800),
(5, 2, 'quaterly standard plan',900),
(6, 2, 'quaterly entertainment plan', 1000);

INSERT INTO Broadband_service_plans_details (br_sr_pl_dt_id, br_sr_id, plan_name, price) VALUES 
(7, 3, 'yearly basic plan', 3000),
(8, 3, 'yearly standard plan',3500),
(9, 3, 'yearly entertainment plan', 4500);

SELECT * FROM Broadband_service_plans_details;

DROP TABLE OTT_platform;
CREATE TABLE OTT_platform(
ott_platform_id INT PRIMARY KEY,
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
br_ott_map_id INT PRIMARY KEY,
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

SELECT OP.platform_name
FROM Broadband_OTT_Mapping BOM
JOIN OTT_Platform OP ON BOM.ott_platform_id = OP.ott_platform_id
WHERE BOM.br_sr_id = 3; -- Here 1 represents the monthly broadband plan, 2 represents the quaterly broadband plan, 3 represents the yearly broadband plan


DROP TABLE Dth_service;
CREATE TABLE Dth_service (
    dth_sr_id INT PRIMARY KEY,
    service_name_id INT,
    dth_service_plans VARCHAR(50) UNIQUE,
    FOREIGN KEY (service_name_id) REFERENCES Services(sr_nm_id)
);

INSERT INTO Dth_service (dth_sr_id, service_name_id, dth_service_plans) VALUES 
(1, 2, 'monthly'), 
(2, 2, 'quaterly'), 
(3, 2, 'yearly');
SELECT * FROM Dth_service;	

DROP TABLE Dth_service_plans;
CREATE TABLE Dth_service_plans (
    dth_sr_pl_dt_id INT PRIMARY KEY,
    dth_sr_id INT,
    language VARCHAR(25),
    channel_category VARCHAR(25),
    price DECIMAL(10, 2),
    FOREIGN KEY (dth_sr_id) REFERENCES Dth_service(dth_sr_id)
);

-- Insert data into DTH_channels (Hindi channels)
INSERT INTO Dth_service_plans (dth_sr_pl_dt_id, dth_sr_id, language, channel_category, price) VALUES
(1, 2, 'hindi', 'entertainment', 50),
(2, 2, 'hindi', 'sports', 50),
(3, 2, 'hindi', 'news', 50);

-- Insert data into DTH_channels (Tamil channels)
INSERT INTO Dth_service_plans (dth_sr_pl_dt_id, dth_sr_id, language, channel_category, price) VALUES
(4, 2, 'tamil', 'entertainment', 60),
(5, 2, 'tamil', 'sports', 60),
(6, 2, 'tamil', 'news', 60);

-- Create DTH_channel_details table
DROP TABLE DTH_channel_details;
CREATE TABLE DTH_channel_details (
    dth_chnl_dt_id INT PRIMARY KEY,
    dth_chnl_id INT,
    channel_name VARCHAR(50),
    price DECIMAL(10, 2),
    FOREIGN KEY (dth_chnl_id) REFERENCES Dth_service_plans(dth_sr_pl_dt_id)
);

-- Insert data into DTH_channel_details (Hindi entertainment channels)
INSERT INTO DTH_channel_details VALUES
(1, 1, 'Hindi_Entertainment_1', 5),
(2, 1, 'Hindi_Entertainment_2', 5),
(3, 1, 'Hindi_Entertainment_3', 5),
(4, 1, 'Hindi_Entertainment_4', 5);

-- Insert data into DTH_channel_details (Hindi sports channels)
INSERT INTO DTH_channel_details VALUES
(5, 2, 'Hindi_Sports_1', 8),
(6, 2, 'Hindi_Sports_2', 8),
(7, 2, 'Hindi_Sports_3', 8);

-- Insert data into DTH_channel_details (Hindi news channels)
INSERT INTO DTH_channel_details VALUES
(8, 3, 'Hindi_News_1', 3),
(9, 3, 'Hindi_News_2', 3);

-- Insert data into DTH_channel_details (Tamil entertainment channels)
INSERT INTO DTH_channel_details VALUES
(10, 4, 'Tamil_Entertainment_1', 4),
(11, 4, 'Tamil_Entertainment_2', 4);

-- Insert data into DTH_channel_details (Tamil sports channels)
INSERT INTO DTH_channel_details VALUES
(12, 5, 'Tamil_Sports_1', 6),
(13, 5, 'Tamil_Sports_2', 6),
(14, 5, 'Tamil_Sports_3', 6);

-- Insert data into DTH_channel_details (Tamil news channels)
INSERT INTO DTH_channel_details VALUES
(15, 6, 'Tamil_News_1', 2),
(16, 6, 'Tamil_News_2', 2);


drop table User_Service_Link;
CREATE TABLE User_Service_Link (
    user_service_id INT PRIMARY KEY,
    user_id INT,
    br_sr_dt_id INT,
    dth_sr_dt_id INT,
    subscription_start_date DATE,
    subscription_end_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (br_sr_dt_id) REFERENCES Broadband_service_plans_details(br_sr_pl_dt_id),
    FOREIGN KEY (dth_sr_dt_id) REFERENCES Dth_service_plans(dth_sr_pl_dt_id)
);

INSERT INTO User_Service_Link (user_service_id, user_id, br_sr_dt_id, subscription_start_date, subscription_end_date)
VALUES (2, 1, 5, '2023-12-16', '2024-03-15');

select * from User_Service_Link;

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


