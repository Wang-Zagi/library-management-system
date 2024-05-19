-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Current Database: `library`
--

DROP DATABASE library;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `library` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `library`;

--
-- Table structure for table `bookInfo`
--

DROP TABLE IF EXISTS `bookInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookInfo` (
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
  `publish_time` date DEFAULT NULL COMMENT '出版时间',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1',
  `borrow_num` int NOT NULL DEFAULT '0' COMMENT '此书被借阅次数',
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookInfo`
--

LOCK TABLES `bookInfo` WRITE;
/*!40000 ALTER TABLE `bookInfo` DISABLE KEYS */;
INSERT INTO `bookInfo` VALUES ('ISBN-20243880100','The C Programming Language',188.00,'Brian W. Kernighan','CHEP','1983-08-10','in library',3),('ISBN-20243880106','The C++ Programming Language',16.00,'Bjarne Stroustrup','PEP','1990-12-02','in library',0),('ISBN-20243880109','Data Structure',20.00,'Mark Allen Weiss','CHEP','1994-12-05','in library',0),('ISBN-20243880114','Introduction to Algorithms',32.00,'Thomas H. Cormen','PEP','1988-12-03','in library',0),('ISBN-20243880115','The Python Programming Language',20.00,'Cay Horstmann','MIT','2001-12-05','in library',1),('ISBN-20243880118','Object-Oriented Analysis',25.00,'Grady Booch','CHEP','1999-12-04','in library',0),('ISBN-20243880119','Software Engineering',22.00,'Ian Sommerville','PEP','2008-12-02','in library',0),('ISBN-20243880128','Database System Concepts',12.00,'Abraham Silberschat','CMP','1998-01-01','in library',0),('ISBN-20243880145','Modern Operating Systems',20.00,'Andrew S. Tanenbaum','MIT','2007-07-07','in library',0),('ISBN-20243880155','Compilers: Principles',25.00,'Alfred V. Aho','PEP','1987-02-02','in library',0),('ISBN-20243880156','Computer Networks',24.00,'Andrew S. Tanenbaum','MIT','2004-08-22','in library',0),('ISBN-20243880166','TCP/IP Illustrated',22.00,'W. Richard Stevens','CHEP','1976-02-22','in library',0),('ISBN-20243880199','The Java Programming Language',22.00,'Cay Horstmann','CMP','1990-09-22','in library',0);
/*!40000 ALTER TABLE `bookInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lend_record`
--

DROP TABLE IF EXISTS `lend_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lend_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` varchar(255) DEFAULT NULL,
  `book_name` varchar(255) DEFAULT NULL,
  `borrower_name` varchar(255) DEFAULT NULL,
  `borrower_id` bigint DEFAULT NULL,
  `lend_time` datetime DEFAULT NULL,
  `dead_time` datetime DEFAULT NULL,
  `return_time` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lend_record`
--

LOCK TABLES `lend_record` WRITE;
/*!40000 ALTER TABLE `lend_record` DISABLE KEYS */;
INSERT INTO `lend_record` VALUES (6,'ISBN-20243880100','The C Programming Language','123',2,'2024-04-22 17:20:19','2024-05-22 17:20:19','2024-04-22 17:20:27','returned'),(7,'ISBN-20243880115','The Python Programming Language','123',2,'2024-04-22 17:20:35','2024-06-21 17:20:35','2024-04-22 17:24:02','returned'),(8,'ISBN-20243880100','The C Programming Language','123',2,'2024-04-22 17:20:45','2024-05-22 17:20:45','2024-04-22 17:23:58','returned'),(9,'ISBN-20243880100','The C Programming Language','123',2,'2024-04-22 17:47:56','2024-05-22 17:47:56','2024-04-22 17:48:00','returned');
/*!40000 ALTER TABLE `lend_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话号码',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `role` int NOT NULL COMMENT '角色、1：管理员 2：普通用户',
    `dept` double DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','123',NULL,'Male',1,0),(2,'123','123','123','Female',2,0),(3,'321','123','15988961695','Male',2,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-22 17:51:45
