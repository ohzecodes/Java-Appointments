CREATE DATABASE  IF NOT EXISTS `Capstone_Project_DB` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `Capstone_Project_DB`;
-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: 127.0.0.1    Database: Capstone_Project_DB
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `APPOINTMENT`
--

DROP TABLE IF EXISTS `APPOINTMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPOINTMENT` (
  `AppointmentID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(50) DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Location` varchar(50) DEFAULT NULL,
  `StartTime` datetime DEFAULT NULL,
  `EndTime` datetime DEFAULT NULL,
  `ClientID` int NOT NULL,
  `UserID` int NOT NULL,
  `CreateDateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`AppointmentID`),
  KEY `fk_customer_id` (`ClientID`),
  KEY `fk_user_id` (`UserID`),
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`ClientID`) REFERENCES `CLIENTS` (`ClientID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`UserID`) REFERENCES `USERS` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APPOINTMENT`
--

LOCK TABLES `APPOINTMENT` WRITE;
/*!40000 ALTER TABLE `APPOINTMENT` DISABLE KEYS */;
INSERT INTO `APPOINTMENT` VALUES (37,'Mac OS X ','Product review meeting','Cupertino meeting room','2023-04-24 14:00:00','2023-04-24 15:00:00',21,1,'2023-04-22 23:45:53'),(39,'IOS review Meeting','Product review meeting','Cuppertino metting room','2023-04-27 16:00:00','2023-04-27 18:00:00',21,1,'2023-04-27 16:10:21');
/*!40000 ALTER TABLE `APPOINTMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENTS`
--

DROP TABLE IF EXISTS `CLIENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENTS` (
  `ClientID` int NOT NULL AUTO_INCREMENT,
  `ClientName` varchar(50) DEFAULT NULL,
  `PostalCode` char(6) DEFAULT NULL,
  `StateName` char(2) DEFAULT NULL,
  `CountryName` char(3) DEFAULT NULL,
  `RepresentativeID` int DEFAULT NULL,
  PRIMARY KEY (`ClientID`),
  UNIQUE KEY `UQ_Client_RepresentativeID` (`RepresentativeID`),
  KEY `fk_State_id` (`StateName`,`CountryName`),
  CONSTRAINT `fk_Rep_id` FOREIGN KEY (`RepresentativeID`) REFERENCES `REPRESENTATIVE` (`RepresentativeID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENTS`
--

LOCK TABLES `CLIENTS` WRITE;
/*!40000 ALTER TABLE `CLIENTS` DISABLE KEYS */;
INSERT INTO `CLIENTS` VALUES (21,'Apple','95014','CA','USA',18),(23,'Microsoft','98042','WA','USA',20),(24,'AMZN','10011','NY','USA',21);
/*!40000 ALTER TABLE `CLIENTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPRESENTATIVE`
--

DROP TABLE IF EXISTS `REPRESENTATIVE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REPRESENTATIVE` (
  `RepresentativeID` int NOT NULL AUTO_INCREMENT,
  `RepName` varchar(20) DEFAULT NULL,
  `Department` varchar(20) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`RepresentativeID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPRESENTATIVE`
--

LOCK TABLES `REPRESENTATIVE` WRITE;
/*!40000 ALTER TABLE `REPRESENTATIVE` DISABLE KEYS */;
INSERT INTO `REPRESENTATIVE` VALUES (18,'Mike Scott','SWE','124567890','mikescott@apple.com'),(19,'Brian Green','','1234567890','greenb@ms.com'),(20,'Allen Poe','SWE','123456789','poe@ms.com'),(21,'Wilson L','SWE','123456789','wilson@a.co');
/*!40000 ALTER TABLE `REPRESENTATIVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERS`
--

DROP TABLE IF EXISTS `USERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERS` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `UserName` char(5) DEFAULT NULL,
  `Password` text,
  `Role` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `UserName` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERS`
--

LOCK TABLES `USERS` WRITE;
/*!40000 ALTER TABLE `USERS` DISABLE KEYS */;
INSERT INTO `USERS` VALUES (1,'test','test','user'),(2,'test5','test5','test');
/*!40000 ALTER TABLE `USERS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-28  4:09:51
