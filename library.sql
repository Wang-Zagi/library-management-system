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
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
  `publish_time` date DEFAULT NULL COMMENT '出版时间',
  `status` char(3) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1',
  `borrow_num` int NOT NULL DEFAULT '0' COMMENT '此书被借阅次数',
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('ISBN-20243880100','图书管理系统',188.00,'聂家辉','温州大学出版社','2022-08-10','在库中',0),('ISBN-20243880106','一万个为什么',16.00,'丁奕中','温州大学出版社','2022-12-02','在库中',0),('ISBN-20243880109','格林童话',20.00,'张超祥','温州大学出版社','2022-12-05','在库中',0),('ISBN-20243880114','计算机操作系统',32.00,'凌浩晨','温州大学出版社','2022-12-03','在库中',0),('ISBN-20243880115','十万个为什么',20.00,'苏格与','温州大学出版社','2022-12-05','在库中',0),('ISBN-20243880118','伊索寓言',25.00,'沈益威','温州大学出版社','2022-12-04','在库中',0),('ISBN-20243880119','五万个为什么',22.00,'聂家辉','温州大学出版社','2022-12-02','在库中',0),('ISBN-20243880128','和另一个自己谈谈心',12.00,'武志红','中国友谊出版公司','2021-01-01','在库中',0),('ISBN-20243880145','简单减肥餐, 好吃不反弹',20.00,'萨巴蒂娜','中国轻工业出版社','2022-07-07','在库中',0),('ISBN-20243880155','其实她真的好喜欢你',25.00,'莫妮打','时代文艺出版社','2022-02-02','在库中',0),('ISBN-20243880156','导游业务. 第7版',24.00,'全国导游人员资格考试教材编写组','旅游教育出版社','2022-08-22','在库中',0),('ISBN-20243880166','法医秦明. 幸存者',22.00,'法医秦明','北京联合出版公司','2022-02-22','在库中',0),('ISBN-20243880199','实用百草治百病',22.00,'宋纬文','福建科学技术出版社','2021-09-22','在库中',0);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
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
  `status` char(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2055241731 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lend_record`
--

LOCK TABLES `lend_record` WRITE;
/*!40000 ALTER TABLE `lend_record` DISABLE KEYS */;
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','123',NULL,'男',1),(2,'123','123','123','女',2),(3,'321','123','15988961695','男',2);
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

-- Dump completed on 2024-04-08 17:49:25
