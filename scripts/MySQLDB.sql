-- MySQL dump 10.13  Distrib 8.1.0, for Linux (x86_64)
--
-- Host: localhost    Database: keynotes
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `id` int NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `theme` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `links` varchar(255) DEFAULT NULL,
  `createdOn` date NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (-1299883982,'A new design plan to cooperate with FileNote and folder structure organize.','Design plan','Plan','','2023-09-07'),(15,'A simple test','sample','Test run','','2023-09-07'),(89952595,'How to use lombok builder annotation','Download lombok plugin in Intellij','Java',';https://www.baeldung.com/lombok-builder;https://stackoverflow.com/questions/56151870/lombok-builder-not-recognised-by-intellij','2023-09-07'),(125524586,'How to design a use case diagram','Use Case diagram','UML',';https://www.ibm.com/docs/en/rationalsoftarch/9.5?topic=diagramsrelationshipsinusecase','2023-09-07'),(133363683,'Set of rules to write good unit tests','Best practice for writting unit tests','java',';https://www.baeldung.com/java-unit-testing-best-practices;https://www.baeldung.com/jacoco','2023-09-07'),(300525489,'Congratulations! You\'ve Advanced to the Semi-Finals of the Hackathon.\nEach time has 15 minutes: {5 for presentation, 10 for debate and QAs with the judges}','Prepare for the Semi-Finals','Hackathon',';https://wowdao.ai/competition/generativeai.html;AI21;Recycling','2023-09-13'),(564680907,'Desgin pattern used for creating only one object, helpful when you don\'t want to manage bunch of DAOs','Singletons design pattern','Java',';https://www.baeldung.com/java-singleton;https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples','2023-09-07'),(615438365,'You can reassign System.in with defined input through ByteArrayInputStream','Test input method','java',';https://www.tutorialkart.com/java/java-system-setin/#gsc.tab=0;https://www.logicbig.com/how-to/junit/java-test-user-command-line-input.html','2023-09-07'),(676454480,'Identifies processes using a file or file structure.','fuser command in Linux','Linux',';https://www.ibm.com/docs/en/aix/7.2?topic=f-fuser-command','2023-09-07');
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-13  8:13:39
