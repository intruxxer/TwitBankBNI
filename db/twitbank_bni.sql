-- phpMyAdmin SQL Dump
-- version 4.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 06, 2015 at 06:34 AM
-- Server version: 5.6.23
-- PHP Version: 5.5.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `twitbank_bni`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_direct_messages`
--

CREATE TABLE IF NOT EXISTS `tbl_direct_messages` (
  `message_id` int(11) NOT NULL,
  `message_section` varchar(10) NOT NULL,
  `message_content` varchar(38) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_direct_messages`
--

INSERT INTO `tbl_direct_messages` (`message_id`, `message_section`, `message_content`) VALUES
(1, 'head', 'Pelanggan [@user] Yth, '),
(2, 'body', 'Diinformasikan bahwa [content]'),
(3, 'footer', '[salam] Terima Kasih.');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hashtags`
--

CREATE TABLE IF NOT EXISTS `tbl_hashtags` (
  `hashtag_id` int(11) NOT NULL,
  `hashtag_category` varchar(10) NOT NULL,
  `hashtag_term` varchar(10) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_hashtags`
--

INSERT INTO `tbl_hashtags` (`hashtag_id`, `hashtag_category`, `hashtag_term`) VALUES
(1, 'menu', 'category'),
(2, 'menu', 'daftar'),
(3, 'promo', 'travel'),
(4, 'promo', 'shopping '),
(5, 'promo', 'property');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_promotions`
--

CREATE TABLE IF NOT EXISTS `tbl_promotions` (
  `promotion_id` int(11) NOT NULL,
  `promotion_slug` varchar(100) DEFAULT NULL,
  `promotion_content` text NOT NULL,
  `promotion_hashtag` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_promotions`
--

INSERT INTO `tbl_promotions` (`promotion_id`, `promotion_slug`, `promotion_content`, `promotion_hashtag`) VALUES
(1, 'Travel with style with BNI in Garuda Travel Fair 2015. Apply at BNI Booth in JCC Travel Fair (JKT)', 'Travel with style with BNI in Garuda Travel Fair 2015. Apply at BNI Booth in JCC Travel Fair (Jakarta) or PVJ (Bandung) 25-28 September 2015', 3),
(2, 'Own your dream Homewith BNI @Jakarta Property 2015, @PVJ (Bandung) 20-22 Oktober 2015', 'Shop with style with BNI in Jakarta Property 2015. Apply at BNI Booth in PVJ (Bandung) 20-22 Oktober 2015', 5);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_direct_messages`
--
ALTER TABLE `tbl_direct_messages`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `tbl_hashtags`
--
ALTER TABLE `tbl_hashtags`
  ADD PRIMARY KEY (`hashtag_id`);

--
-- Indexes for table `tbl_promotions`
--
ALTER TABLE `tbl_promotions`
  ADD PRIMARY KEY (`promotion_id`),
  ADD KEY `promotion_hashtag` (`promotion_hashtag`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_direct_messages`
--
ALTER TABLE `tbl_direct_messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `tbl_hashtags`
--
ALTER TABLE `tbl_hashtags`
  MODIFY `hashtag_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `tbl_promotions`
--
ALTER TABLE `tbl_promotions`
  MODIFY `promotion_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
