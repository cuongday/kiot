-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: posbe
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (5,'2025-03-16 07:54:04.115519','cuongnd3','Bao gồm các loại thực phẩm được đóng hộp nhằm bảo quản lâu dài, tiện lợi khi sử dụng.\nVí dụ: cá hộp, thịt hộp, pate, rau củ đóng hộp, trái cây đóng hộp, súp đóng hộp.\nĐược chế biến và đóng gói theo quy trình đảm bảo vệ sinh an toàn thực phẩm, giữ được hương vị và giá trị dinh dưỡng.\nPhù hợp với những người bận rộn, có thể dùng ngay hoặc chế biến nhanh chóng.','http://res.cloudinary.com/posbe/image/upload/v1742111721/xbqjas6rocw5ckfan2qi.jpg','Đồ hộp',NULL,NULL),(6,'2025-03-16 07:54:52.844088','cuongnd3','Bao gồm các loại thực phẩm chế biến sẵn, chỉ cần pha chế đơn giản hoặc ăn ngay mà không cần nấu nướng cầu kỳ.\nVí dụ: mì tôm, cháo ăn liền, phở ăn liền, ngũ cốc ăn liền, xôi đóng gói, bún khô.\nGiúp tiết kiệm thời gian nấu nướng, tiện lợi cho học sinh, sinh viên, nhân viên văn phòng, người thường xuyên di chuyển.\nMột số sản phẩm được bổ sung dưỡng chất nhằm đảm bảo sức khỏe cho người tiêu dùng.','http://res.cloudinary.com/posbe/image/upload/v1742111770/tkgwg2mzcwgdte54n5md.png','Đồ ăn liền',NULL,NULL),(7,'2025-03-16 07:55:28.664112','cuongnd3','Bao gồm các loại nước giúp giải khát, cung cấp năng lượng hoặc bổ sung vitamin cho cơ thể.\nVí dụ: nước khoáng, nước ép trái cây, nước dừa, sữa chua uống, trà xanh đóng chai, nước ngọt có gas.\nĐược đóng chai hoặc hộp tiện dụng, phù hợp với mọi lứa tuổi.\nMột số sản phẩm có chứa thành phần thảo dược giúp thanh nhiệt, giải độc cơ thể.','http://res.cloudinary.com/posbe/image/upload/v1742111806/jejzoucf4utzss5ccj8q.webp','Đồ uống giải khát',NULL,NULL),(8,'2025-03-16 07:56:09.708925','cuongnd3','Bao gồm các loại gia vị dùng để nêm nếm, tạo hương vị đặc trưng cho món ăn.\nVí dụ: muối, đường, bột ngọt, nước mắm, nước tương, dầu ăn, giấm, tiêu, ớt bột, bột nghệ, hạt nêm.\nGiúp nâng cao chất lượng món ăn, tạo hương vị hấp dẫn và phù hợp với khẩu vị từng vùng miền.\nĐược đóng gói đa dạng như lọ, gói, chai giúp bảo quản dễ dàng.','http://res.cloudinary.com/posbe/image/upload/v1742111845/s7fmddgem2h1hssfztxv.jpg','Gia vị',NULL,NULL),(9,'2025-03-16 07:56:45.572916','cuongnd3','Bao gồm các sản phẩm vệ sinh cá nhân, chăm sóc sức khỏe và sắc đẹp.\nVí dụ: dầu gội, sữa tắm, kem đánh răng, nước hoa, kem dưỡng da, kem chống nắng, sữa rửa mặt, lăn khử mùi, dao cạo râu.\nSử dụng các thành phần an toàn cho da, có nguồn gốc rõ ràng và được kiểm định chất lượng.\nĐáp ứng nhu cầu chăm sóc cá nhân hàng ngày, giúp cơ thể sạch sẽ và khỏe mạnh.','http://res.cloudinary.com/posbe/image/upload/v1742111883/inalxpormyblo1hz1eut.png','Chăm sóc cá nhân',NULL,NULL),(10,'2025-03-16 07:57:15.614674','cuongnd3','Bao gồm các vật dụng phục vụ nhu cầu sinh hoạt trong gia đình, từ nhà bếp đến phòng ngủ.\nVí dụ: nồi, chảo, bếp điện, lò vi sóng, máy xay sinh tố, quạt điện, máy hút bụi, đèn bàn, bộ chén đĩa.\nChất lượng bền bỉ, thiết kế tiện lợi, giúp tối ưu hóa không gian sống.\nMột số sản phẩm hiện đại tích hợp công nghệ thông minh giúp tiết kiệm năng lượng.','http://res.cloudinary.com/posbe/image/upload/v1742111913/ecvmk4jetecd33khvwc7.jpg','Đồ gia dụng',NULL,NULL),(11,'2025-03-16 07:58:58.533785','cuongnd3','Bao gồm các món ăn nhẹ, dùng để nhâm nhi, giải trí hoặc bổ sung năng lượng nhanh chóng.\nVí dụ: bánh quy, snack khoai tây, hạt dẻ, kẹo, socola, trái cây sấy, rong biển sấy, bắp rang bơ.\nPhù hợp với mọi lứa tuổi, có nhiều hương vị đa dạng từ ngọt, mặn đến cay.\nMột số sản phẩm có lợi cho sức khỏe như hạt dinh dưỡng, thanh protein.','http://res.cloudinary.com/posbe/image/upload/v1742112016/b8ksrlaysu2uxwgatxwa.jpg','Đồ ăn vặt',NULL,NULL),(12,'2025-03-16 07:59:40.316969','cuongnd3','Bao gồm các loại đồ uống chứa cồn, phục vụ nhu cầu giải trí, tiệc tùng hoặc thưởng thức cá nhân.\nVí dụ: bia, rượu vang, rượu mạnh (whisky, vodka, brandy), cocktail đóng chai, sake, soju.\nĐược sản xuất theo quy trình chưng cất, lên men hoặc pha chế, đảm bảo hương vị độc đáo.\nMột số sản phẩm được kết hợp với trái cây hoặc thảo mộc để tăng cường mùi vị và giảm nồng độ cồn.','http://res.cloudinary.com/posbe/image/upload/v1742112058/ete1yryj1ujpzwvsf2wp.png','Đồ uống có cồn',NULL,NULL),(13,'2025-03-22 13:34:22.105113','cuongnd3','Thuốc lá điếu: Có đầu lọc, không đầu lọc, nhiều hương vị.\nXì gà (Cigar): Nhập khẩu cao cấp.\nThuốc lá cuốn tay: Lá thuốc và giấy cuốn.\nThuốc lá thảo dược: Không nicotine, từ thảo mộc tự nhiên.\n⚠️ Sử dụng có trách nhiệm, không dành cho người dưới 18 tuổi.','http://res.cloudinary.com/posbe/image/upload/v1742650547/nwsamgmtqznobp5o321g.png','Thuốc lá',NULL,NULL),(14,'2025-03-22 13:35:01.203494','cuongnd3','Danh mục Sữa bao gồm các sản phẩm sữa tươi, sữa đặc, sữa bột và sữa thực vật từ các thương hiệu uy tín. Sản phẩm đảm bảo chất lượng, giàu dinh dưỡng, phù hợp cho mọi đối tượng từ trẻ em, người lớn đến người cao tuổi. Sữa không chỉ cung cấp canxi, protein mà còn giúp tăng cường sức khỏe và phát triển toàn diện.','http://res.cloudinary.com/posbe/image/upload/v1742650586/y7zo0dvkbel0u5ptdf1b.webp','Sữa',NULL,NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `point` bigint NOT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (4,'2025-03-19 18:17:58.686428','cuongnd3','Nguyễn Thanh Hoàng','0987654321',NULL,NULL,0,_binary ''),(5,'2025-03-19 18:18:31.234963','cuongnd3','Quản Anh Đức','0987654312',NULL,NULL,0,_binary ''),(6,'2025-03-19 18:18:57.272317','cuongnd3','Nguyễn ĐỨc Cường','0123488888',NULL,NULL,0,_binary '');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_details`
--

DROP TABLE IF EXISTS `import_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `price` bigint NOT NULL,
  `quantity` int NOT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `import_history_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr1hcpolyqs81es4pu714ju3hi` (`import_history_id`),
  KEY `FK13vxgtbffkj8acalfah5sxe1b` (`product_id`),
  CONSTRAINT `FK13vxgtbffkj8acalfah5sxe1b` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKr1hcpolyqs81es4pu714ju3hi` FOREIGN KEY (`import_history_id`) REFERENCES `import_histories` (`id`),
  CONSTRAINT `import_details_chk_1` CHECK ((`price` >= 0)),
  CONSTRAINT `import_details_chk_2` CHECK ((`quantity` >= 0)),
  CONSTRAINT `import_details_chk_3` CHECK ((`total_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_details`
--

LOCK TABLES `import_details` WRITE;
/*!40000 ALTER TABLE `import_details` DISABLE KEYS */;
INSERT INTO `import_details` VALUES (1,'2025-03-25 04:52:59.492312','cuongnd3',100000,10,1000000,NULL,NULL,2,15),(2,'2025-03-25 04:52:59.528066','cuongnd3',150000,2,300000,NULL,NULL,2,16),(3,'2025-03-25 12:03:47.543708','cuongnd3',100000,10,1000000,NULL,NULL,3,15),(4,'2025-03-25 12:03:47.550247','cuongnd3',150000,2,300000,NULL,NULL,3,16);
/*!40000 ALTER TABLE `import_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_histories`
--

DROP TABLE IF EXISTS `import_histories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_histories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg7xhjlx27ye0xxyrmf4abkeo5` (`supplier_id`),
  KEY `FKrjvndkv24qeqnlv2x5woru8wa` (`user_id`),
  CONSTRAINT `FKg7xhjlx27ye0xxyrmf4abkeo5` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKrjvndkv24qeqnlv2x5woru8wa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_histories`
--

LOCK TABLES `import_histories` WRITE;
/*!40000 ALTER TABLE `import_histories` DISABLE KEYS */;
INSERT INTO `import_histories` VALUES (2,'2025-03-25 04:52:59.484265','cuongnd3',1300000,'2025-03-25 04:52:59.531065','cuongnd3',9,1),(3,'2025-03-25 12:03:47.524258','cuongnd3',1300000,'2025-03-25 12:03:47.552246','cuongnd3',10,1);
/*!40000 ALTER TABLE `import_histories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `price` bigint NOT NULL,
  `quantity` int NOT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`),
  CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_details_chk_1` CHECK ((`price` >= 0)),
  CONSTRAINT `order_details_chk_2` CHECK ((`quantity` >= 0)),
  CONSTRAINT `order_details_chk_3` CHECK ((`total_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (27,'2025-03-23 14:50:47.745126','cuongnd3',1000,2,2000,NULL,NULL,14,4),(28,'2025-03-23 14:50:47.766131','cuongnd3',3000,1,3000,NULL,NULL,14,6),(29,'2025-03-23 14:58:50.610398','cuongnd3',1000,2,2000,NULL,NULL,15,4),(30,'2025-03-23 14:58:50.614391','cuongnd3',3000,1,3000,NULL,NULL,15,6),(31,'2025-03-23 15:12:28.745616','cuongnd3',1000,2,2000,NULL,NULL,16,4),(32,'2025-03-23 15:12:28.749143','cuongnd3',3000,1,3000,NULL,NULL,16,6),(33,'2025-03-23 15:22:02.153315','cuongnd3',1000,2,2000,NULL,NULL,17,4),(34,'2025-03-23 15:22:02.157338','cuongnd3',3000,1,3000,NULL,NULL,17,6);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `payment_method` enum('CASH','TRANSFER') DEFAULT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `payment_status` enum('FAILED','PAID','PENDING','REFUNDED') DEFAULT NULL,
  `payment_url` mediumtext,
  `transaction_no` varchar(255) DEFAULT NULL,
  `payment_message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `orders_chk_1` CHECK ((`total_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (14,'2025-03-23 14:50:47.670947','cuongnd3','TRANSFER',5000,'2025-03-23 14:50:47.815132','cuongnd3',4,1,'PENDING','https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=500000&vnp_Command=pay&vnp_CreateDate=20250323215047&vnp_CurrCode=VND&vnp_ExpireDate=20250323220547&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A+14&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8081%2Fapi%2Fpayment%2Fvnpay-return&vnp_TmnCode=4A9Q28OV&vnp_TxnRef=14&vnp_Version=2.1.0&vnp_SecureHash=dc457990481e17613c19af3cfb383ce1da341bb320192ae6cb0c07eb8fe60f1f2306157be77a9d36e97bb91c7267686be166ea1c9b0b2b8f1d2a2350b5797fe2',NULL,NULL),(15,'2025-03-23 14:58:50.605394','cuongnd3','TRANSFER',5000,'2025-03-23 14:58:50.628388','cuongnd3',4,1,'PENDING','https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=500000&vnp_Command=pay&vnp_CreateDate=20250323215850&vnp_CurrCode=VND&vnp_ExpireDate=20250323221350&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A+15&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8081%2Fapi%2Fpayment%2Fvnpay-return&vnp_TmnCode=E41HWAPX&vnp_TxnRef=15&vnp_Version=2.1.0&vnp_SecureHash=edb29dc552b24dac7bceac80dbb9a53bc4fe60342bd58994e1d12afb0d9075a52e01d7e806ae2573e0b364061146618fa7ec454bbe24f8b192f28bcb5d991ff4',NULL,NULL),(16,'2025-03-23 15:12:28.742611','cuongnd3','TRANSFER',5000,'2025-03-23 15:12:28.761707','cuongnd3',4,1,'PENDING','https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=500000&vnp_Command=pay&vnp_CreateDate=20250323221228&vnp_CurrCode=VND&vnp_ExpireDate=20250323222728&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A+16&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8081%2Fapi%2Fpayment%2Fvnpay-return&vnp_TmnCode=E41HWAPX&vnp_TxnRef=16&vnp_Version=2.1.0&vnp_SecureHash=09c6314171e975b49cde297818b7abb81e65114bf4f39a203cece6002b3827a947736bbe1465651af29d9e6dfa62f1af1321340144215ac0f7270c73dc01df83',NULL,NULL),(17,'2025-03-23 15:22:02.147793','cuongnd3','TRANSFER',5000,'2025-03-23 15:22:02.173138','cuongnd3',4,1,'PENDING','https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=500000&vnp_Command=pay&vnp_CreateDate=20250323222202&vnp_CurrCode=VND&vnp_ExpireDate=20250323223702&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A+17&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8081%2Fapi%2Fpayment%2Fvnpay-return&vnp_TmnCode=E41HWAPX&vnp_TxnRef=17&vnp_Version=2.1.0&vnp_SecureHash=2ca3ec38faa6c4fc7d2aaa9e04909ec853700f807c086b19ab75dc8d782fa2c360fb88e8f8cde0e8d4a344475ff49461fe2085a75c069c60bb7ba6300b383787',NULL,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `buy_price` bigint NOT NULL,
  `sell_price` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  KEY `FK6i174ixi9087gcvvut45em7fd` (`supplier_id`),
  CONSTRAINT `FK6i174ixi9087gcvvut45em7fd` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `products_chk_2` CHECK ((`buy_price` >= 0)),
  CONSTRAINT `products_chk_3` CHECK ((`sell_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (4,'2025-03-22 14:22:15.869949','cuongnd3','Patê Gan 3 Bông Mai được sản xuất trên dây chuyền công nghệ hiện đại và tuân thủ quy trình khép kín, đảm bảo các tiêu chuẩn nghiêm ngặt về an toàn thực phẩm. Với hương vị thơm ngon, béo ngậy cùng kết cấu mịn màng, sản phẩm hứa hẹn sẽ là lựa chọn lý tưởng cho những bữa ăn nhanh gọn, mà vẫn đảm bảo chất lượng cao và giàu dinh dưỡng..\n\nQuy cách đóng gói: 150g/hộp, 4 hộp/lốc, 72 hộp/thùng\n\nThành phần: Gan heo, mỡ heo, nạc heo, hành, tiêu, bột tỏi ngũ vị hương, chất điều vị (621, 627, 631, 635), chất ổn định (1422, 471)\n\nHướng dẫn sử dụng: Dùng ngay hoặc chế biến thành các món ăn khác.\n\nHướng dẫn bảo quản: Nơi thoáng mát, không để nơi nóng, ẩm.\n\n','http://res.cloudinary.com/posbe/image/upload/v1742653421/ojpbajjocohbwkaqpclx.jpg','Pate Gan Ba Bông Mai',38,'Còn hàng','2025-03-23 15:22:02.160614','cuongnd3',5,9,'2028-03-28 00:00:00.000000',17500,23000),(5,'2025-03-22 14:27:54.804205','cuongnd3','Cá ngừ xốt cay được sản xuất trên dây chuyền công nghệ hiện đại và tuân thủ quy trình khép kín, đảm bảo các tiêu chuẩn nghiêm ngặt về an toàn thực phẩm. Với hương vị cá ngừ thơm ngon cùng xốt cay đậm đà hứa hẹn sẽ là lựa chọn lý tưởng cho những bữa ăn nhanh gọn, mà vẫn đảm bảo chất lượng cao và giàu dinh dưỡng..\n\nQuy cách đóng gói: 170g/hộp, 4 hộp/lốc, 72 hộp/thùng\n\nThành phần: Cá ngừ (60%), nước, ớt tươi, đường, muối, chất điều vị (621, 627, 631, 635), dầu đậu nành, nước tương, bột hành, bột tỏi, chất ổn định (1422), chất làm dày (415), chiết xuất ớt.\n\nHướng dẫn sử dụng: Dùng ngay hoặc có thể chế biến thành các món ăn khác. Có thể làm nóng trước khi dùng, không làm nóng sản phẩm trực tiếp trong lò vi sóng.\n\nHướng dẫn bảo quản: Nơi thoáng mát, không để nơi nóng ẩm.','http://res.cloudinary.com/posbe/image/upload/v1742653760/liyakpqvhd4oydz1iiji.jpg','Cá Ngừ Xốt Cay',72,'Còn hàng','2025-03-22 14:30:16.626270','cuongnd3',5,9,'2028-03-28 00:00:00.000000',45000,50000),(6,'2025-03-22 14:33:34.025050','cuongnd3','Quy cách đóng gói: 170g/hộp (lon in).\nThành phần: Nạc bò (55%), mỡ heo, nước, đường, muối i-ốt, tiêu, hành, tỏi, gừng, chất ổn định (1422), chất giữ ẩm (451i, 452i), nước mắm, chất điều vị (621), chất bảo quản (249).\nHướng dẫn sử dụng: Dùng ngay, có thể làm nóng lại trước khi dùng hoặc chế biến thành các món ăn khác.\nHướng dẫn bảo quản: Nơi thoáng mát, không để nơi nóng, ẩm.','http://res.cloudinary.com/posbe/image/upload/v1742654099/gi3p0ndecsetknrtlbrt.jpg','Bò 2 Lát',71,'Còn hàng','2025-03-23 15:22:02.160614','cuongnd3',5,9,'2028-03-28 00:00:00.000000',31000,36000),(7,'2025-03-22 14:35:34.993139','cuongnd3','Heo hầm được sản xuất và chế biến theo dây chuyền công nghệ hiện đại của châu Âu. Tất cả các khâu chế biến đều nằm trong quy trình tuyệt đối khép kín, đảm bảo an toàn vệ sinh thực phẩm. Bên cạnh yếu tố chất lượng, Heo hầm còn là món ăn tiện lợi, phù hợp với khẩu vị của người tiêu dùng Việt Nam. Sản phẩm có thể dùng ngay hoặc chế biến thành các món ăn khác.\n\n','http://res.cloudinary.com/posbe/image/upload/v1742654220/azfz6yfr2fdnutoxbl3y.png','Heo hầm',50,'Còn hàng',NULL,NULL,5,9,'2028-03-28 00:00:00.000000',24000,30000),(8,'2025-03-23 09:29:31.054166','cuongnd3','Patê gan heo được sản xuất và chế biến theo dây chuyền công nghệ hiện đại của châu Âu. Tất cả các khâu chế biến đều nằm trong quy trình tuyệt đối khép kín, đảm bảo an toàn vệ sinh thực phẩm. Bên cạnh yếu tố chất lượng, Patê gan heo còn là món ăn tiện lợi, phù hợp với khẩu vị của người tiêu dùng Việt Nam. Sản phẩm có thể dùng ngay hoặc chế biến thành các món ăn khác.\n\nQuy cách đóng gói: 4 hộp/lốc, 72 hộp/thùng','http://res.cloudinary.com/posbe/image/upload/v1742722256/zhafqoqk5sxt1pyuetdv.jpg','Pate Gan Heo Vissan',72,'Còn hàng',NULL,NULL,5,9,'2028-03-28 00:00:00.000000',32000,37000),(9,'2025-03-23 09:31:08.080485','cuongnd3','Tại VISSAN, bí quyết để làm nên nước xương hầm chất lượng đó là nguồn nguyên liệu xương heo phải đảm bảo tươi ngon và an toàn. Theo đó, VISSAN sử dụng nguyên liệu xương heo VietGAP được giết mổ tại chính công ty, sau đó ninh trong nhiều giờ cùng các loại rau củ như hành tây, hành tím tạo nên hương thơm nồng nàn và vị ngọt thanh cho món ăn. Đặc biệt, trong quá trình ninh xương, những dưỡng chất Canxi, Collagen có trong xương sẽ được giải phóng và hòa tan trong nước. Nhờ vậy, nước hầm xương chứa nhiều chất dinh dưỡng và khoáng chất quan trọng, tốt cho sức khỏe người dùng.','http://res.cloudinary.com/posbe/image/upload/v1742722353/xotn18o5xksweulxzgye.png','Nước xương hầm 820ml',24,'Còn hàng',NULL,NULL,5,9,'2028-03-28 00:00:00.000000',40000,45000),(10,'2025-03-23 09:31:44.994629','cuongnd3','Quy cách đóng gói: 150g/hộp (lon in).\n\nThành phần: Nạc heo (55%), mỡ heo, nước, protein đậu nành, đường, muối i-ốt, tiêu, hành, tỏi, chất ổn định (1422), chất giữ ẩm (451i, 452i), nước mắm, chất điều vị (621), chất bảo quản (249).\n\nHướng dẫn sử dụng: Dùng ngay, có thể làm nóng lại trước khi dùng hoặc chế biến thành các món ăn khác.\n\nHướng dẫn bảo quản: Nơi thoáng mát, không để nơi nóng, ẩm','http://res.cloudinary.com/posbe/image/upload/v1742722390/sivlko6bdg7lukx0nerz.png','Heo 2 Lát 74g',72,'Còn hàng',NULL,NULL,5,9,'2028-03-28 00:00:00.000000',23000,28000),(11,'2025-03-23 09:33:29.095818','cuongnd3','Được sản xuất theo đúng tiêu chuẩn, công nghệ Nhật Bản, tâm huyết trong mỗi gói mì. Hảo Hảo sẽ là một món ăn không thể thiếu trên bàn ăn Việt Nam, làm phong phú, hoàn thiện bàn ăn Việt và cần thiết cho mọi người như một món ăn quốc dân, đồng thời cung cấp 333 mg canxi đáp ứng hơn 1/3 nhu cầu hàng ngày cho người tiêu dùng, thêm dinh dưỡng và hỗ trợ xương chắc khỏe.','http://res.cloudinary.com/posbe/image/upload/v1742722493/qxawfql1jio6kuhmhgjy.png','Mì Hảo Hảo hương vị Mì Tôm Chua Cay 74g',600,'Còn hàng',NULL,NULL,6,15,'2028-03-28 00:00:00.000000',3000,4000),(12,'2025-03-23 09:34:24.569128','cuongnd3','Sợi mì vàng dai ngon hòa quyện trong nước súp sườn heo thơm lừng, đậm đà thấm đẫm từng sợi mì sóng sánh cùng hương tỏi phi quyến rũ ngất ngây. Mì Hảo Hảo sườn heo tỏi phi gói 73g chính hãng mì Hảo Hảo là lựa chọn hấp dẫn không thể bỏ qua cho những bữa ăn nhanh chóng đơn giản mà vẫn đủ chất','http://res.cloudinary.com/posbe/image/upload/v1742724756/ytw0wot1cbqssxhlxsok.jpg','Mì Hảo Hảo sườn heo tỏi phi 74g',600,'Còn hàng','2025-03-23 10:11:11.866932','cuongnd3',6,15,'2028-03-28 00:00:00.000000',4000,5000),(13,'2025-03-23 10:12:03.585975','cuongnd3','Sợi mì vàng dai ngon hòa quyện trong nước súp sa tế thơm lừng, đậm đà thấm đẫm từng sợi mì sóng sánh cùng hương hành phi quyến rũ ngất ngây. Mì Hảo Hảo sa tế hành tím gói 75g chính hãng mì Hảo Hảo là lựa chọn hấp dẫn không thể bỏ qua cho những bữa ăn nhanh chóng đơn giản','http://res.cloudinary.com/posbe/image/upload/v1742724808/cmusait4bmdnlgja5co3.jpg','Mì Hảo Hảo sa tế hành tím 74g',600,'Còn hàng',NULL,NULL,6,15,'2028-03-28 00:00:00.000000',4000,5000),(14,'2025-03-23 10:12:53.439964','cuongnd3','Sợi mì vàng dai ngon hòa quyện trong nước súp vị gà thơm lừng, đậm đà thấm đẫm từng sợi mì sóng sánh cùng hương thơm quyến rũ ngất ngây. Mì Hảo Hảo gà vàng gói 74g chính hãng mì Hảo Hảo là lựa chọn hấp dẫn không thể bỏ qua cho những bữa ăn nhanh chóng đơn giản mà vẫn đủ chất\n','http://res.cloudinary.com/posbe/image/upload/v1742724858/m6tnu8pmkmwwwz4gtpge.jpg','Mì Hảo Hảo gà vàng 74g',600,'Còn hàng',NULL,NULL,6,15,'2028-03-28 00:00:00.000000',4000,5000),(15,'2025-03-23 10:13:50.584464','cuongnd3','Sợi mì vàng dai ngon hòa quyện trong nước sốt mì xào vị tôm chua ngọt thơm lừng, thấm đậm đà từng sợi cùng hương thơm quyến rũ ngất ngây. Mì xào Hảo Hảo tôm xào chua ngọt gói 75g chính hãng mì Hảo Hảo  là lựa chọn hấp dẫn không thể bỏ qua cho những bữa ăn nhanh chóng đơn giản mà vẫn đủ chất','http://res.cloudinary.com/posbe/image/upload/v1742724915/irhorxjdo1txzmbadw2k.jpg','Mì xào Hảo Hảo tôm xào chua ngọt 74g',620,'Còn hàng','2025-03-25 12:03:47.552246','cuongnd3',6,15,'2028-03-28 00:00:00.000000',4000,5000),(16,'2025-03-23 10:15:39.241977','cuongnd3','Sợi mì vàng dai ngon hòa quyện trong nước sốt mì Omachi Spaghetti đậm đà gồm bò bằm và cà chua tươi mát cùng hương thơm ngất ngây tạo ra siêu phẩm mì với sự kết hợp hương vị hài hòa, độc đáo. Mì trộn Omachi xốt Spaghetti phô mai 90g tiện lợi, thơm ngon hấp dẫn','http://res.cloudinary.com/posbe/image/upload/v1742725024/s0y6oa2njgoi5uvjhzkg.jpg','Mì trộn Omachi xốt Spaghetti 74g',604,'Còn hàng','2025-03-25 12:03:47.552246','cuongnd3',6,14,'2028-03-28 00:00:00.000000',8000,10000),(17,'2025-03-23 10:16:34.719698','cuongnd3','Sản phẩm mì ăn liền sợi khoai tây dinh dưỡng chính hãng thương hiệu mì Omachi được nhiều người yêu thích. Mì khoai tây Omachi tôm chua cay Thái gói 80g được làm từ nước cốt đậm đà, sợi mì vàng dai và hấp dẫn, hương vị chua cay khoái khẩu của nhiều người, phù hợp khẩu vị cả nhà','http://res.cloudinary.com/posbe/image/upload/v1742725079/ofaoy5a3phiwb6gfiymd.jpg','Mì khoai tây Omachi tôm chua cay Thái 74g',600,'Còn hàng',NULL,NULL,6,14,'2028-03-28 00:00:00.000000',8000,10000),(18,'2025-03-23 10:19:14.648720','cuongnd3','Bia ngon chất lượng quen thuộc với người dân Việt Nam đặc biệt là người dân miền Nam. Thùng 24 lon bia Sài Gòn Special Sleek 330ml chính hãng bia Sài Gòn với hương vị truyền thống thơm ngon, cân bằng, dễ uống, thiết kế lon cao thanh lịch sang trọng, hiện đại\nThương hiệu        \nSài Gòn (Việt Nam)\nSản xuất tại        \nViệt Nam\nLoại sản phẩm        \nBia các loại\nNồng độ cồn        \n4.9%\nThể tích        \n330ml\nĐóng gói        \nLon\nSố lượng        \nThùng 24 lon\nLưu ý        \nSản phẩm dành cho người trên 18 tuổi và không dành cho phụ nữ mang thai. Thưởng thức có trách nhiệm, đã uống đồ uống có cồn thì không lái xe!','http://res.cloudinary.com/posbe/image/upload/v1742725239/spsaiyc3zliewi53tcg0.jpg','Bia Sài Gòn Special Sleek 330ml',480,'Còn hàng',NULL,NULL,12,11,'2028-03-28 00:00:00.000000',12000,16000),(19,'2025-03-23 10:20:02.806780','cuongnd3','Được sản xuất tại Việt Nam từ nước, malt đại mạch, ngũ cốc và hoa bia, chính hãng thương hiệu bia Sài Gòn. Thùng 24 lon bia Sài Gòn Lager 330ml có hương vị đậm đà, thơm ngon, cùng hương thơm ngũ cốc dễ chịu giúp bạn thăng hoa hơn, sảng khoái hơn trong những cuộc vui cùng gia đình và bạn bè.\nThương hiệu        \nSài Gòn (Việt Nam)\nNồng độ cồn        \n4.3%\nThể tích        \n330ml\nSố lượng        \nThùng 24 lon\nSản xuất tại        \nViệt Nam\nLưu ý        \nSản phẩm dành cho người trên 18 tuổi và không dành cho phụ nữ mang thai. Thưởng thức có trách nhiệm, đã uống đồ uống có cồn thì không lái xe!','http://res.cloudinary.com/posbe/image/upload/v1742725287/dpjsv9wvqsusy6fj4sfu.jpg','Bia Sài Gòn Lager 330ml',480,'Còn hàng',NULL,NULL,12,11,'2028-03-28 00:00:00.000000',10000,14000),(20,'2025-03-23 10:21:18.516346','cuongnd3','Thông tin sản phẩm\nBia Tiger nổi tiếng được nhiều người yêu thích, lên men tự nhiên từ các thành phần chính nước, đại mạch, ngũ cốc và hoa bia. Thùng 24 lon bia Tiger nâu 330ml đậm đà thơm ngon cho bạn ly bia hấp dẫn và cảm giác uống sảng khoái\nThương hiệu        \nTiger (Singapore)\nSản xuất tại        \nViệt Nam\nLoại sản phẩm        \nBia các loại\nNồng độ cồn        \n5%\nThể tích        \n330ml\nĐóng gói        \nLon\nSố lượng        \nThùng 24 lon\nLưu ý        \nSản phẩm dành cho người trên 18 tuổi và không dành cho phụ nữ mang thai. Thưởng thức có trách nhiệm, đã uống đồ uống có cồn thì không lái xe!','http://res.cloudinary.com/posbe/image/upload/v1742725363/us0cabixkc9jkmr4mt4o.jpg','Bia 333 330ml',480,'Còn hàng',NULL,NULL,12,11,'2028-03-28 00:00:00.000000',8000,13000);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'2025-03-10 11:49:32.665780','cuongnd3',NULL,'admin',NULL,NULL),(2,'2025-03-10 11:49:32.665780','cuongnd3',NULL,'employee',NULL,NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `close_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `open_time` datetime(6) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_categories`
--

DROP TABLE IF EXISTS `supplier_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt9g8i2hefv870klfalk7bupte` (`category_id`),
  KEY `FK3b2hix6smuvlm3e3bmce6p098` (`supplier_id`),
  CONSTRAINT `FK3b2hix6smuvlm3e3bmce6p098` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKt9g8i2hefv870klfalk7bupte` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_categories`
--

LOCK TABLES `supplier_categories` WRITE;
/*!40000 ALTER TABLE `supplier_categories` DISABLE KEYS */;
INSERT INTO `supplier_categories` VALUES (29,'2025-03-17 15:30:20.460975','cuongnd3',NULL,NULL,5,9),(30,'2025-03-17 15:30:20.474562','cuongnd3',NULL,NULL,6,9),(31,'2025-03-17 16:34:11.566840','cuongnd3',NULL,NULL,5,10),(35,'2025-03-17 16:36:19.067705','cuongnd3',NULL,NULL,7,11),(36,'2025-03-17 16:36:19.071721','cuongnd3',NULL,NULL,12,11),(37,'2025-03-17 16:37:19.524687','cuongnd3',NULL,NULL,7,12),(38,'2025-03-17 16:37:19.530693','cuongnd3',NULL,NULL,12,12),(40,'2025-03-17 16:39:45.624324','cuongnd3',NULL,NULL,7,13),(41,'2025-03-17 16:41:33.052785','cuongnd3',NULL,NULL,5,14),(42,'2025-03-17 16:41:33.058780','cuongnd3',NULL,NULL,6,14),(43,'2025-03-17 16:42:12.556515','cuongnd3',NULL,NULL,5,15),(44,'2025-03-17 16:42:12.561509','cuongnd3',NULL,NULL,6,15),(45,'2025-03-17 16:42:12.567514','cuongnd3',NULL,NULL,8,15),(46,'2025-03-17 16:43:01.096051','cuongnd3',NULL,NULL,9,16),(47,'2025-03-17 16:43:01.102057','cuongnd3',NULL,NULL,10,16),(48,'2025-03-17 16:43:39.525330','cuongnd3',NULL,NULL,9,17),(49,'2025-03-17 16:43:39.531334','cuongnd3',NULL,NULL,10,17),(50,'2025-03-17 16:44:18.229530','cuongnd3',NULL,NULL,11,18),(51,'2025-03-17 16:44:50.863142','cuongnd3',NULL,NULL,11,19),(52,'2025-03-22 13:39:40.232185','cuongnd3',NULL,NULL,13,20);
/*!40000 ALTER TABLE `supplier_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (9,'2025-03-17 15:30:20.308484','cuongnd3','Mô tả: Công ty cổ phần Việt Nam Kỹ Nghệ Súc Sản – Vissan được thành lập vào ngày 20/11/1970, hoạt động trong lĩnh vực chăn nuôi, sản xuất, kinh doanh thực phẩm tươi sống và thực phẩm chế biến. Đến nay, Vissan là một trong những doanh nghiệp dẫn đầu ngành thực phẩm của cả nước với hệ thống phân phối rộng khắp trên toàn quốc, xuất khẩu đến các nước trong khu vực và trên thế giới.\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: 420 Nơ Trang Long, Phường 13, Quận Bình Thạnh, TP. HCM\n\nĐiện thoại: 028 3553 3999\n\nEmail: vissanco@vissan.com.vn\nWebsite: https://vissanmart.com/\nFanpage: www.facebook.com/VISSAN1970/','http://res.cloudinary.com/posbe/image/upload/v1742225501/zpak2ogfhmvpik8banw3.jpg','Công ty Cổ phần Việt Nam Kỹ Nghệ Súc Sản (Vissan)',NULL,NULL),(10,'2025-03-17 16:34:11.560838','cuongnd3','Mô tả: Công ty Cổ phần đồ hộp Hạ Long (Halong Canfoco) là một công ty có lịch sử lâu đời và uy tín trong ngành sản xuất đồ hộp thực phẩm tại Việt Nam. Được thành lập vào năm 1957 dưới tên Nhà máy Cá hộp Hạ Long, Halong Canfoco đã trải qua một quá trình phát triển dài hơi và đáng kể. Thương hiệu Halong Canfoco được biết đến như một trong những đơn vị đầu tiên sản xuất đồ hộp thực phẩm tại Việt Nam, và ngày nay họ đã trở thành một trong những công ty đầu tiên niêm yết trên thị trường chứng khoán. Với gần 1.000 lao động và một hệ thống sản xuất bao gồm 2 nhà máy chế biến, 2 văn phòng đại diện, và 1 địa điểm thu mua nguyên liệu nông sản trên khắp cả nước, Halong Canfoco có một tầm ảnh hưởng lớn trong ngành thực phẩm tại Việt Nam.\n\nTHÔNG TIN LIÊN HỆ:\n\nĐịa chỉ: 71 Lê Lai, Máy Chai, Ngô Quyền, Hải Phòng\n\nĐiện thoại: 022 5383 6692\nEmail: halong@canfoco.com.vn\nWebsite: https://canfoco.com.vn/\nFanpage: www.facebook.com/canfoco','http://res.cloudinary.com/posbe/image/upload/v1742229332/cb5ktt97xoueybowl0qf.jpg','Công ty CP đồ hộp Hạ Long',NULL,NULL),(11,'2025-03-17 16:35:03.539841','cuongnd3','Mô tả: Người tiêu dùng Việt Nam từ lâu đã quen thuộc với thương hiệu Bia Sài Gòn của Tổng Công ty Cổ phần Bia - Rượu - Nước giải khát Sài Gòn - SABECO. Với lịch sử gần 150 năm đồng hành cùng những đổi thay của đất nước và con người Việt Nam, dòng chảy vàng óng của Bia đã và sẽ luôn được nỗ lực gìn giữ để  tiếp nối  dài đến tương lai, luôn tồn tại trong cảm xúc của những người dân Việt tự hào về sản phẩm Việt.\n\nTHÔNG TIN LIÊN HỆ:\nVăn phòng đại diện tại Hà Nội:\nTầng 6, 97 Trần Hưng Đạo, P. Cửa Nam, Q. Hoàn Kiếm, Hà Nội\n(+84) 24 39 745 877\n(+84) 24 39 745 878','http://res.cloudinary.com/posbe/image/upload/v1742229384/woymryaqxlweblomiask.png','Tổng Công ty Bia - Rượu - Nước giải khát Sài Gòn (SABECO)','2025-03-17 16:36:19.054194','cuongnd3'),(12,'2025-03-17 16:37:19.520691','cuongnd3','Mô tả: Tổng Công ty Cổ phần Bia - Rượu - Nước giải khát Hà Nội (HABECO) là một trong những doanh nghiệp hàng đầu trong ngành đồ uống có cồn tại Việt Nam. Với lịch sử hơn 130 năm hình thành và phát triển, thương hiệu Bia Hà Nội đã trở thành một biểu tượng trong ngành bia Việt Nam. Sản phẩm của HABECO gồm Bia Hà Nội 450ml, Bia Trúc Bạch, Hanoi Beer Premium, nước giải khát...\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: 183 Hoàng Hoa Thám, Ba Đình, Hà Nội\nĐiện thoại: (024) 3845 4419\nEmail: info@habeco.com.vn\nWebsite: https://www.habeco.com.vn\nFanpage: facebook.com/HABECO','http://res.cloudinary.com/posbe/image/upload/v1742229520/l2pcb1uc8lcjdphqdjoj.jpg','Tổng Công ty Bia - Rượu - Nước giải khát Hà Nội (HABECO): Nổi tiếng với thương hiệu Bia Hà Nội và các sản phẩm nước giải khát khác.',NULL,NULL),(13,'2025-03-17 16:38:13.146227','cuongnd3','Mô tả: Công ty Cổ phần Nước Giải Khát Chương Dương là một trong những thương hiệu nước giải khát lâu đời tại Việt Nam. Thành lập năm 1952, công ty nổi tiếng với sản phẩm nước ngọt có ga mang thương hiệu \"Xá Xị Chương Dương\". Các sản phẩm khác bao gồm soda chanh, cam, sữa hột gà, nước tăng lực...\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: 2 Bis Nguyễn Thị Minh Khai, Phường Đa Kao, Quận 1, TP. HCM\nĐiện thoại: (028) 3910 2636\nEmail: info@chuongduongbeverage.com.vn\nWebsite: http://chuongduongbeverage.com.vn\nFanpage: facebook.com/chuongduongbeverage','http://res.cloudinary.com/posbe/image/upload/v1742229667/ekckpmspm2e0oiatvsdp.png','Công ty Cổ phần Nước Giải Khát Chương Dương:','2025-03-17 16:39:45.615324','cuongnd3'),(14,'2025-03-17 16:41:33.047783','cuongnd3','Mô tả: Masan Consumer là một công ty thành viên của Tập đoàn Masan, chuyên sản xuất và kinh doanh các sản phẩm tiêu dùng nhanh như nước mắm, nước tương, mì ăn liền, cà phê hòa tan và thực phẩm đóng hộp. Các thương hiệu nổi bật gồm Nam Ngư, Chin-su, Omachi, Wake-up 247…\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: Số 10 đường 2A, KCN Biên Hòa II, Biên Hòa, Đồng Nai\nĐiện thoại: 028 6256 3333\nEmail: info@masanconsumer.com\nWebsite: https://masanconsumer.com\nFanpage: facebook.com/MasanConsumer','http://res.cloudinary.com/posbe/image/upload/v1742229774/wppezlbn9qbfgj9z01yb.png','Công ty Cổ phần Hàng tiêu dùng Masan (Masan Consumer):',NULL,NULL),(15,'2025-03-17 16:42:12.552518','cuongnd3','Mô tả: Acecook Việt Nam là một công ty hàng đầu trong lĩnh vực sản xuất mì ăn liền tại Việt Nam. Được thành lập từ năm 1995, công ty hiện nắm giữ thị phần lớn với các thương hiệu nổi tiếng như Hảo Hảo, Đệ Nhất, Lẩu Thái, Phở Đệ Nhất…\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: Lô II-3, Đường số 11, KCN Tân Bình, Quận Tân Phú, TP. HCM\nĐiện thoại: (028) 3815 4064\nEmail: contact@acecookvietnam.com\nWebsite: https://acecookvietnam.vn\nFanpage: facebook.com/acecookvietnam','http://res.cloudinary.com/posbe/image/upload/v1742229813/r9u7dnburmacqcqzkck3.png','Công ty Cổ phần Acecook Việt Nam:',NULL,NULL),(16,'2025-03-17 16:43:01.091050','cuongnd3','Mô tả: Unilever Việt Nam là công ty đa quốc gia chuyên sản xuất và kinh doanh các sản phẩm chăm sóc cá nhân, gia dụng và thực phẩm. Các thương hiệu nổi tiếng gồm OMO, Sunlight, Dove, Lifebuoy, Knorr, P/S…\n\nTHÔNG TIN LIÊN HỆ\nĐịa chỉ: Lô A2-3, KCN Tây Bắc Củ Chi, TP. HCM\nĐiện thoại: (028) 3892 1818\nEmail: customer@unilever.com\nWebsite: https://www.unilever.com.vn\nFanpage: facebook.com/UnileverVietnam','http://res.cloudinary.com/posbe/image/upload/v1742229862/yvf9kuebas66kk7lx37l.png','Công ty TNHH Quốc tế Unilever Việt Nam',NULL,NULL),(17,'2025-03-17 16:43:39.520330','cuongnd3','Mô tả: P&G Việt Nam là chi nhánh của tập đoàn đa quốc gia Procter & Gamble (P&G), chuyên sản xuất các sản phẩm tiêu dùng như dầu gội Head & Shoulders, Pantene, bột giặt Tide, kem đánh răng Oral-B…\n\nTHÔNG TIN LIÊN HỆ\nĐịa chỉ: Lô A2, KCN Đồng An, Bình Dương\nĐiện thoại: (0274) 3756 333\nEmail: info@pg.com\nWebsite: https://www.pg.com.vn\nFanpage: facebook.com/PGVietnam\n','http://res.cloudinary.com/posbe/image/upload/v1742229900/pgtrp4x53zybpxd09lvp.jpg','Công ty TNHH Procter & Gamble Việt Nam (P&G):',NULL,NULL),(18,'2025-03-17 16:44:18.224540','cuongnd3','Mô tả: Bibica là một trong những công ty bánh kẹo hàng đầu tại Việt Nam, nổi bật với các sản phẩm bánh trung thu, kẹo cứng, kẹo mềm, socola… Công ty hiện có nhiều nhà máy sản xuất lớn và mạng lưới phân phối rộng khắp cả nước.\n\nTHÔNG TIN LIÊN HỆ\nĐịa chỉ: 443 Lý Thường Kiệt, Phường 8, Quận Tân Bình, TP. HCM\nĐiện thoại: (028) 3864 1024\nEmail: info@bibica.com.vn\nWebsite: https://bibica.com.vn\nFanpage: facebook.com/BibicaVietNam','http://res.cloudinary.com/posbe/image/upload/v1742229939/kwdgm7jiaoviajjoaonk.png','Công ty Cổ phần Bibica:',NULL,NULL),(19,'2025-03-17 16:44:50.857152','cuongnd3','Mô tả: Orion Food Vina là một chi nhánh của tập đoàn Orion (Hàn Quốc) tại Việt Nam, chuyên sản xuất bánh ChocoPie, bánh Custas, bánh Goute, bánh Marine Boy…\n\nTHÔNG TIN LIÊN HỆ\nĐịa chỉ: Số 15, Đường 17A, KCN Biên Hòa II, Đồng Nai\nĐiện thoại: (0251) 3991 900\nEmail: info@orionvn.com\nWebsite: https://www.orionvn.com\nFanpage: facebook.com/orionvietnam','http://res.cloudinary.com/posbe/image/upload/v1742229972/iu5uj6e0ngk0llcxdp8e.png','Công ty TNHH Orion Food Vina:',NULL,NULL),(20,'2025-03-22 13:39:40.212158','cuongnd3','MÔ TẢ:\nLĩnh vực sản xuất và kinh doanh chính của công ty bao gồm: sản xuất và kinh doanh thuốc lá điếu; chế tạo, gia công sửa chữa thiết bị chuyên ngành thuốc lá; xuất, nhập khẩu các mặt hàng Công ty kinh doanh và một số ngành nghề khác. Trong suốt 57 năm, Công ty luôn thực hiện nghiêm túc quy trình công nghệ, đảm bảo và không ngừng nâng cao chất lượng 100% sản phẩm đầu ra, thỏa mãn nhu cầu ngày càng cao của thị trường trong nước và quốc tế.\n\nTHÔNG TIN LIÊN HỆ:\nĐịa chỉ: 235 Đường Nguyễn Trãi, Quận Thanh Xuân, TP Hà Nội\nĐiện thoại: 0243 8.584.441 – 0243 8.584.342\nEmail: contact@thanglongtabac.vn','http://res.cloudinary.com/posbe/image/upload/v1742650865/hjlctmqr45fvu82ldnni.png','Công ty TNHH MTV Thuốc lá Thăng Long',NULL,NULL);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `refresh_token` mediumtext,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Hà Nội','http://res.cloudinary.com/posbe/image/upload/v1741789571/ayrypainkjmynbswkafo.png','2025-03-10 11:49:32.665780','anonymousUser','MALE','Nguyễn Đức Cường','$2a$10$ehjBciQMAIqmxOnBNpB7x.KU2.IdvjB.jqz4vz8qtZIrwFQhh9O6G','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdW9uZ25kMyIsImV4cCI6MTc1MTU5ODE0OSwiaWF0IjoxNzQyOTU4MTQ5LCJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6ImN1b25nbmQzIiwibmFtZSI6Ik5ndXnhu4VuIMSQ4bupYyBDxrDhu51uZyJ9fQ.iuA-cepAINsVPFSO32Fqkk8-K4nJKGSjk_KYTSskzwj6sC0ooVreuCi6Cc-hbDIeDLU9Ey1fh7V0jIMHGamyOA','2025-03-26 03:02:29.964422','cuongnd3','cuongnd3',1),(2,'Trần Cung, Bắc Từ Liêm, Hà Nội','http://res.cloudinary.com/posbe/image/upload/v1741787589/w1yvrixffefsz59j8iws.png','2025-03-12 13:52:20.847224','cuongnd3','MALE','Nguyễn Thanh Hoàng','$2a$10$fvcqNSRtnuavZwOn4ALWK.03ZBmgjtz1kZOQ18H77bHXxym8fCp9C',NULL,NULL,NULL,'hoangnt',2),(3,'Hà Nội','http://res.cloudinary.com/posbe/image/upload/v1743478270/il6zsauthowtrcavvnzf.jpg','2025-04-01 03:29:34.774632','cuongnd3','MALE','Nguyễn Văn Tuấn','$2a$10$Mtg28BWjaOkwcF7SPXqH3.9mq2IptmODQ0/PGdAiDc2aQdHdpZdLy',NULL,NULL,NULL,'tuannv',1),(4,'Hà Nội','http://res.cloudinary.com/posbe/image/upload/v1743478538/c7v1fxhwnahfhrqfruzx.jpg','2025-04-01 03:34:01.903524','cuongnd3','MALE','Nguyễn Văn Huấn','$2a$10$iWbnQzxVmEfcxkdXSHl3YOnhOhqtfhS/f8OSd8Rt9PByJhs6L6zaS',NULL,NULL,NULL,'huannv',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-06 21:52:40
