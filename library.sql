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

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `library` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `library`;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `barcode` char(10) NOT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('00000002','9787560662701','Floor1 SectionB Shelf10 P10-1','in library'),('00000003','9787302561941','Floor2 Section2 Shelf2 P10-1','in library'),('00000004','9787302147510','Floor1 Section2 Shef2 P1-10','borrowed'),('00000006','9787111573692','Floor3 SectionC Shelf10 P5-5','borrowed'),('00000007','9787111599715','Floor4 SectionC Shelf8 P16-3','borrowed');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_info`
--

DROP TABLE IF EXISTS `book_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_info` (
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
  `publish_time` date DEFAULT NULL COMMENT '出版时间',
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_info`
--

LOCK TABLES `book_info` WRITE;
/*!40000 ALTER TABLE `book_info` DISABLE KEYS */;
INSERT INTO `book_info` VALUES ('9787111573692','现代操作系统-原书第4版',89.00,'塔嫩鲍姆 博斯 陈向群 马洪兵','机械工业出版社','2017-07-01'),('9787111599715','机械工业出版社计算机科学丛书计算机网络:自顶向下方法(原书第7版)',89.00,'库罗斯 陈鸣','机械工业出版社','2018-06-01'),('9787302147510','数据结构',10.00,NULL,NULL,NULL),('9787302561941','Java语言程序设计（第4版）（本科教材）',69.80,'郎波','清华大学出版社','2021-01-01'),('9787560662701','Python程序设计，',39.00,'李光夏','西安电子科技大学出版社','2022-03-01');
/*!40000 ALTER TABLE `book_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_record`
--

DROP TABLE IF EXISTS `borrow_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_barcode` varchar(255) DEFAULT NULL,
  `book_name` varchar(255) DEFAULT NULL,
  `borrower_id` bigint DEFAULT NULL,
  `borrower_name` varchar(255) DEFAULT NULL,
  `borrow_time` datetime DEFAULT NULL,
  `dead_time` datetime DEFAULT NULL,
  `return_time` datetime DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_record`
--

LOCK TABLES `borrow_record` WRITE;
/*!40000 ALTER TABLE `borrow_record` DISABLE KEYS */;
INSERT INTO `borrow_record` VALUES (34,'00000002','Python程序设计',2,'123','2024-05-13 17:30:55','2024-06-12 17:30:55','2024-05-13 17:31:07','returned'),(35,'00000001','数据结构(C语言版)(配光盘)',2,'123','2024-05-13 17:34:53','2024-06-12 17:34:53','2024-05-15 08:26:12','returned'),(36,'00000003','Java语言程序设计（第4版）（本科教材）',2,'123','2024-05-13 17:34:53','2024-06-12 17:34:53','2024-05-13 17:35:22','returned'),(38,'00000001','数据结构(C语言版)(配光盘)',2,'123','2024-05-15 08:27:38','2024-06-14 08:27:38','2024-05-15 08:27:48','returned'),(39,'00000002','Python程序设计，',7,'yao','2024-03-21 11:08:23','2024-04-20 11:08:23','2024-05-21 11:55:19','returned'),(40,'00000003','Java语言程序设计（第4版）（本科教材）',7,'yao','2024-04-15 11:34:22','2024-05-15 11:34:22','2024-05-21 11:55:19','returned'),(41,'00000006','现代操作系统-原书第4版',7,'yao','2024-03-21 11:57:59','2024-04-20 11:57:59',NULL,'borrowing'),(42,'00000007','机械工业出版社计算机科学丛书计算机网络:自顶向下方法(原书第7版)',7,'yao','2024-04-15 11:57:59','2024-05-15 11:57:59',NULL,'borrowing'),(43,'00000002','Python程序设计',6,'Kelei','2024-05-21 13:34:08','2024-06-20 13:34:08','2024-05-21 13:35:35','returned'),(44,'00000001','数据结构(C语言版)(配光盘)',6,'Kelei','2024-05-21 13:34:08','2024-06-20 13:34:08','2024-05-21 13:34:26','returned'),(45,'00000002','Python程序设计',6,'Kelei','2024-05-21 13:39:21','2024-06-20 13:39:21','2024-05-21 13:39:38','returned'),(46,'00000001','数据结构(C语言版)(配光盘)',6,'Kelei','2024-05-21 13:49:22','2024-06-20 13:49:22','2024-05-21 13:49:34','returned'),(47,'00000004','数据结构(C语言版)(配光盘)',6,'Kelei','2024-05-21 13:49:22','2024-06-20 13:49:22','2024-05-21 13:50:09','returned'),(48,'00000004','数据结构',6,'Kelei','2024-05-21 17:07:23','2024-06-20 17:07:23','2024-05-21 17:07:28','confirming');
/*!40000 ALTER TABLE `borrow_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_record`
--

DROP TABLE IF EXISTS `income_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_record` (
  `id` varchar(50) NOT NULL,
  `payer_id` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_record`
--

LOCK TABLES `income_record` WRITE;
/*!40000 ALTER TABLE `income_record` DISABLE KEYS */;
INSERT INTO `income_record` VALUES ('7-2024-06-01-18-58-53',7,2,'2024-06-01 18:59:10');
/*!40000 ALTER TABLE `income_record` ENABLE KEYS */;
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
  `debt` int DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','123','15988961695','Male',1,0),(2,'123','123','15988961555','Male',2,0),(6,'Kelei','123456','123456','Male',2,0),(7,'yao','123','18637047511','Male',2,0);
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

-- Dump completed on 2024-06-01 19:01:02
