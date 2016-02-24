-- phpMyAdmin SQL Dump
-- version 4.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 24, 2016 at 01:06 PM
-- Server version: 5.6.23
-- PHP Version: 5.5.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bnizona`
--
CREATE DATABASE IF NOT EXISTS `bnizona` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `bnizona`;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_account_cs`
--

CREATE TABLE IF NOT EXISTS `tbl_account_cs` (
  `cs_id` int(11) NOT NULL,
  `cs_employee_code` varchar(10) NOT NULL,
  `cs_fullname` varchar(30) NOT NULL,
  `cs_twitter` varchar(40) NOT NULL DEFAULT 'cs_twitter',
  `cs_point` int(11) NOT NULL DEFAULT '0',
  `deleted` int(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_account_cs`
--

INSERT INTO `tbl_account_cs` (`cs_id`, `cs_employee_code`, `cs_fullname`, `cs_twitter`, `cs_point`, `deleted`, `created_at`) VALUES
(1, 'NPP30000', 'Emilia Fontessa', 'cs_twitter', 100, 0, '2016-02-24 10:52:10'),
(2, 'NPP30001', 'Intan Permata Sari', 'cs_twitter', 100, 0, '2016-02-24 10:54:43');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_account_merchandise`
--

CREATE TABLE IF NOT EXISTS `tbl_account_merchandise` (
  `merchandise_id` int(11) NOT NULL,
  `merchandise_name` varchar(20) NOT NULL DEFAULT 'Merchandise BNI',
  `merchandise_stock` int(11) NOT NULL DEFAULT '0',
  `merchandise_redeemed` int(11) NOT NULL DEFAULT '0',
  `merchandise_active` int(1) NOT NULL DEFAULT '1',
  `merchandise_priority` int(11) NOT NULL DEFAULT '1',
  `valid_until` date DEFAULT NULL,
  `deleted` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_account_merchandise`
--

INSERT INTO `tbl_account_merchandise` (`merchandise_id`, `merchandise_name`, `merchandise_stock`, `merchandise_redeemed`, `merchandise_active`, `merchandise_priority`, `valid_until`, `deleted`, `created_at`) VALUES
(1, 'Merchandise BNI', 100, 1, 1, 1, '2016-03-31', 0, '2016-02-24 10:54:16'),
(2, 'Tumbler BNI', 100, 1, 1, 2, '2016-12-31', 0, '2016-02-24 12:05:35');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_account_users`
--

CREATE TABLE IF NOT EXISTS `tbl_account_users` (
  `id` int(11) NOT NULL,
  `account_no` varchar(30) NOT NULL DEFAULT 'Account Number',
  `account_code` varchar(20) NOT NULL DEFAULT '0' COMMENT 'Promotional Code',
  `account_holder` varchar(50) NOT NULL DEFAULT 'Account Holder''s Name',
  `account_handler` varchar(30) NOT NULL DEFAULT 'Twitter Username',
  `account_phone` varchar(20) NOT NULL DEFAULT '0',
  `account_merchandise` int(11) NOT NULL DEFAULT '0',
  `account_merchandise_name` varchar(20) NOT NULL DEFAULT 'Merchandise BNI',
  `account_cs` int(11) NOT NULL DEFAULT '0',
  `deleted` int(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_account_users`
--

INSERT INTO `tbl_account_users` (`id`, `account_no`, `account_code`, `account_holder`, `account_handler`, `account_phone`, `account_merchandise`, `account_merchandise_name`, `account_cs`, `deleted`, `created_at`) VALUES
(1, '201602001', 'TM123456', 'Danny Delvian', 'DannyDelvian', '0812345678', 1, 'Merchandise BNI', 1, 0, '2016-02-24 10:58:54'),
(2, '201602002', 'TN123456', 'Endra Septianto', 'EndraSep', '0812345679', 2, 'Tumbler BNI', 2, 0, '2016-02-24 12:46:30');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_directmessages`
--

CREATE TABLE IF NOT EXISTS `tbl_directmessages` (
  `message_id` int(11) NOT NULL,
  `message_type` varchar(255) DEFAULT 'default',
  `message_section` varchar(10) NOT NULL,
  `message_content` varchar(1024) NOT NULL,
  `message_deleted` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_directmessages`
--

INSERT INTO `tbl_directmessages` (`message_id`, `message_type`, `message_section`, `message_content`, `message_deleted`) VALUES
(1, 'default', 'head', 'Pelanggan [@user] Yth, ', 0),
(2, 'default', 'body', '\r\n\r\nDiinformasikan bahwa [content]', 0),
(3, 'default', 'footer', '\r\n\r\n[salam] Terima Kasih.', 0),
(4, 'followed', 'head', 'Terima kasih telah mem-follow Akun Twitter @BNI46. Dapatkan informasi tentang produk dan layanan BNI (#AskBNI) dan  informasi promo BNI (#Promo) .', 0),
(5, 'followed', 'body', '========== \n\nDaftarkan Nama dan No.HP Anda melalui DM ke @BNI46. (Optional) \n\nKetik : #daftar #nama_lengkap #nomortelepon \n\nContoh : #daftar #Andi_Waluyo #62213456789 \n\nNote : \n\n1. Nama Awal dan Akhir dipisah dengan "_"\n2 Penulisan #nomortelepon : #62xxxxxxxxx BUKAN #+62xxxxxxxxx\n\nJika berhasil akan ada balasan via DM dari Twitter kami.\n ========== \n\nINFO HASHTAG \n\n#HelpPromo : Untuk melihat seluruh Keyword #Promo \n\n#HelpBNI : Untuk melihat seluruh Keyword #AskBNI \n\nCara Menggunakan :\n\n1. #Promo (spasi) #KEYWORD , Contoh : #Promo #Hotel \n\n2. #AskBNI (spasi) #KEYWORD , Contoh : #AskBNI #Taplus \n\n3. #AskBNI : Untuk melihat informasi secara lengkap', 0),
(6, 'followed', 'footer', '\r\nTerima kasih telah memanfaatkan layanan Twitter #Hashtag For Banking dari @BNI46. \r\n\r\nSelamat Beraktivitas!', 0),
(7, 'daftar', 'head', 'Untuk mengakses layanan kami via Twitter silakan ketik melalui DM ke @BNI46 :', 0),
(8, 'daftar', 'body', '1. #AskBNI : Untuk mendapatkan info tentang layanan informasi BNI via Twitter Hashtag.\n\n2. #Promo [spasi] #Keyword : Untuk mendapatkan informasi tentang promo-promo BNI saat ini. Contoh : #Promo #Hotel \n\n3. #AskBNI [spasi] #Keyword : Untuk mendapatkan info tentang layanan Customer Service BNI via Twitter. Contoh : #AskBNI #Taplus\n\n4. #HelpPromo : Untuk melihat Keyword #Promo \n\n5. #HelpBNI : Untuk melihat Keyword #AskBNI', 0),
(9, 'daftar', 'footer', ' ', 0),
(10, 'customer_open_account', 'head', 'Terima kasih kepada Bapak/Ibu @account_holder yang akan membuka rekening BNI Taplus Muda.\r\nInformasi promo pembukaan rekening:\r\n', 0),
(11, 'customer_open_account', 'body', 'No Ref     : @account_code\r\nNama       : @account_holder\r\nTwitter    : @account_handler\r\nHadiah     : @account_merchandise_name\r\nValid Date : @valid_date\r\n', 0),
(12, 'customer_open_account', 'footer', 'Mohon untuk menunjukkan reply DM ini kepada Customer Service.', 0),
(13, 'cs_open_account_self', 'head', 'Terima kasih telah membuka Rekening Taplus Muda atas nama:\r\n', 0),
(14, 'cs_open_account_self', 'body', 'No Ref     : @account_code\r\nNama       : @account_holder\r\nTwitter    : @account_handler\r\nHadiah     : @account_merchandise_name\r\nValid Date : @valid_date\r\n', 0),
(15, 'cs_open_account_self', 'footer', 'Poin Anda s.d. @until_date : @cs_point', 0),
(16, 'cs_open_account_customer', 'head', 'Terima kasih telah membuka Rekening Taplus Muda.\r\n', 0),
(17, 'cs_open_account_customer', 'body', '\r\nGunakan e-Banking BNI dalam setiap transaksi Anda.\r\n', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_account_cs`
--
ALTER TABLE `tbl_account_cs`
  ADD PRIMARY KEY (`cs_id`);

--
-- Indexes for table `tbl_account_merchandise`
--
ALTER TABLE `tbl_account_merchandise`
  ADD PRIMARY KEY (`merchandise_id`);

--
-- Indexes for table `tbl_account_users`
--
ALTER TABLE `tbl_account_users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_directmessages`
--
ALTER TABLE `tbl_directmessages`
  ADD PRIMARY KEY (`message_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_account_cs`
--
ALTER TABLE `tbl_account_cs`
  MODIFY `cs_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbl_account_merchandise`
--
ALTER TABLE `tbl_account_merchandise`
  MODIFY `merchandise_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbl_account_users`
--
ALTER TABLE `tbl_account_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbl_directmessages`
--
ALTER TABLE `tbl_directmessages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=18;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
