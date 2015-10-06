-- phpMyAdmin SQL Dump
-- version 4.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 06, 2015 at 10:06 AM
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
  `hashtag_term` varchar(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_hashtags`
--

INSERT INTO `tbl_hashtags` (`hashtag_id`, `hashtag_category`, `hashtag_term`) VALUES
(1, 'menu', 'category'),
(2, 'menu', 'daftar'),
(3, 'menu', 'promo'),
(4, 'promo', 'shopping '),
(5, 'promo', 'property'),
(6, 'promo', 'entertainment'),
(7, 'promo', 'fashion'),
(8, 'promo', 'foodandbeverage'),
(9, 'promo', 'food'),
(10, 'promo', 'beverage'),
(11, 'promo', 'fnb'),
(12, 'promo', 'health'),
(13, 'promo', 'healthservice'),
(14, 'promo', 'hotel'),
(15, 'promo', 'lifestyle'),
(16, 'promo', 'travel');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_mentions`
--

CREATE TABLE IF NOT EXISTS `tbl_mentions` (
  `mention_id` int(11) NOT NULL,
  `mention_username` varchar(50) NOT NULL,
  `mention_tweet` varchar(140) NOT NULL,
  `mention_hashtag` varchar(140) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_promotions`
--

CREATE TABLE IF NOT EXISTS `tbl_promotions` (
  `promotion_id` int(11) NOT NULL,
  `promotion_title` varchar(140) NOT NULL,
  `promotion_slug` varchar(140) NOT NULL,
  `promotion_content` text,
  `promotion_hashtag` int(11) NOT NULL,
  `promotion_enddate` date NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_promotions`
--

INSERT INTO `tbl_promotions` (`promotion_id`, `promotion_title`, `promotion_slug`, `promotion_content`, `promotion_hashtag`, `promotion_enddate`) VALUES
(1, 'Usulan Program 3x BRP di MCC Entertainment', 'Hanya untuk Anda! 3X BNI Reward Point untuk akumulasi trx di merchant kategori ENTERTAINMENT min Rp 1,5Jt dgn Kartu Kredit BNI Reguler sd 31', 'Hanya untuk Anda! 3X BNI Reward Point untuk akumulasi trx di merchant kategori ENTERTAINMENT min Rp 1,5Jt dgn Kartu Kredit BNI Reguler sd 31 Okt 15.S&K. 1500046.', 6, '2015-10-31'),
(2, 'Promo diskon 15% la mer collections', 'LA MER COLLECTIONS Special Offer! Disc 15% at La Mer Collections KotaKasablanka, TSM Bandung, Beachwalk Bali w/ BNI CreditCard until 31Dec15', 'LA MER COLLECTIONS Special Offer! Disc 15% at La Mer Collections KotaKasablanka, TSM Bandung, Beachwalk Bali w/ BNI CreditCard until 31Dec15. T&C Apply. 1500046', 7, '2015-12-31'),
(3, 'Usulan Joint Promo BNI - PUYO', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K ', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K berlaku.1500046. ', 8, '2015-02-26'),
(4, 'BNI Dining Experience @ Ta Yang Suki BANDUNG', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K ', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K berlaku.1500046. ', 8, '2015-12-15'),
(5, 'Promo Dining Exp - Diskon 10% @WARUNG ONGAN BALI', 'DISKON 10% (F&B) setiap hari Senin-Jumat dg Kartu Kredit BNI di WARUNG ONGAN, Jl Noja Sarawaswati 12A Denpasar, sd 14 Jan 16. S&K berlaku. I', 'DISKON 10% (F&B) setiap hari Senin-Jumat dg Kartu Kredit BNI di WARUNG ONGAN, Jl Noja Sarawaswati 12A Denpasar, sd 14 Jan 16. S&K berlaku. Info BNI Call 1500046', 8, '2016-01-04'),
(6, 'Promo BNI - Pizza Hut Diskon 15% Pizza Ukuran Apa Saja & Big Box', 'Diskon 15% untuk Pizza apa saja & Paket Big Box di PIZZA HUT setiap Hari Sabtu & Minggu dengan Kartu Kredit BNI s.d 27 Des 2015. S&K Berlaku', 'Diskon 15% untuk Pizza apa saja & Paket Big Box di PIZZA HUT setiap Hari Sabtu & Minggu dengan Kartu Kredit BNI s.d 27 Des 2015. S&K Berlaku. Info BNI: 1500046 ', 8, '2015-12-27'),
(7, 'Usulan Joint Promo BNI - Excelso', 'DISKON 20% di EXCELSO dengan Kartu Kredit BNI Titanium, Platinum & Infinite s/d 10 Maret 2016. S&K berlaku. Info BNI Call: 1500046', 'DISKON 20% di EXCELSO dengan Kartu Kredit BNI Titanium, Platinum & Infinite s/d 10 Maret 2016. S&K berlaku. Info BNI Call: 1500046', 8, '2016-03-10'),
(8, 'Promo Dining Exp - Diskon hingga 25% @XO SUKI BALI', 'DISKON hingga 25% (Suki only) min trx Rp 200rb dg Kartu Kredit BNI di XO SUKI, Jl Cok Agung Tresna 29 & Jl Sunset Road Barat 7 Kuta Bali, sd', 'DISKON hingga 25% (Suki only) min trx Rp 200rb dg Kartu Kredit BNI di XO SUKI, Jl Cok Agung Tresna 29 & Jl Sunset Road Barat 7 Kuta Bali, sd 31Jan16.S&K.1500046', 8, '2016-01-31'),
(9, 'Promo Dining Exp - Diskon hingga 30% @PRIMARASA SURABAYA', 'DISKON 30% (F&B) min trx Rp 150rb dg Kartu Kredit BNI di AYAM BAKAR PRIMARASA, Jl Kusumabangsa, Manyar, Raya Kupang, A Yani Surabaya, sd 31 ', 'DISKON 30% (F&B) min trx Rp 150rb dg Kartu Kredit BNI di AYAM BAKAR PRIMARASA, Jl Kusumabangsa, Manyar, Raya Kupang, A Yani Surabaya, sd 31 Des 15.S&K.1500046', 8, '2015-12-31'),
(10, 'Review dan perpanjangan program promo BNI-Beranda Cafe (Crowne Plaza Hotel', 'GET 20% DISCOUNT at Beranda Cafe @Crowne Plaza Jakarta with BNI CreditCard. Valid Until 10 Juni 2016. T&C Apply. Info 1500046', 'GET 20% DISCOUNT at Beranda Cafe @Crowne Plaza Jakarta with BNI CreditCard. Valid Until 10 Juni 2016. T&C Apply. Info 1500046', 8, '2016-06-10'),
(11, 'Program Join Promo BNI- RS Premier Jatinegara (Ramsay Healthcare Indonesia)', 'Disc 20% utk diagnostic MRI &MSCT ,Disc 10% utk pemeriksaan paket Med Check-Up di RS Premier Jatinegara dgn Kartu Kredit BNI(hingga 31Des15)', 'Disc 20% utk diagnostic MRI &MSCT ,Disc 10% utk pemeriksaan paket Med Check-Up di RS Premier Jatinegara dgn Kartu Kredit BNI(hingga 31Des15).S&K berlaku.1500046', 13, '2015-12-31'),
(12, 'Join Promo Reguler BNI - Optik Seis', 'Diskon 10% utk pembelian Frame & Sunglasses di seluruh outlet Optik Seis dgn Kartu Kredit BNI Anda.(Hingga 31 Jan16).S&K berlaku.Info 150004', 'Diskon 10% utk pembelian Frame & Sunglasses di seluruh outlet Optik Seis dgn Kartu Kredit BNI Anda.(Hingga 31 Jan16).S&K berlaku.Info 1500046', 13, '2016-01-31'),
(13, 'Leverage Program MasterCard - Park Hotel Group', 'Stay 3 Pay 2 and Free Upgrade Room at Park Hotel Group in Singapore,China,Hongkong & Japan with your BNI Mastercard Credit Card (til 31Dec15', 'Stay 3 Pay 2 and Free Upgrade Room at Park Hotel Group in Singapore,China,Hongkong & Japan with your BNI Mastercard Credit Card (til 31Dec15).T&C apply.1500046', 14, '2015-12-31'),
(14, 'Perpanjangan Program Promo Ticktab.com', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI. S&K berlaku.info 1', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI. S&K berlaku.info 1500046', 14, '2015-12-31'),
(15, 'Joint Promo BNI - Graha Lifestyle tahun 2015-2016', 'Come & enjoy BNI Installment 0% up to 12 months at Diane von Furstenberg, ETRO and & Halston Heritage stores with BNI Credit Card until 30 A', 'Come & enjoy BNI Installment 0% up to 12 months at Diane von Furstenberg, ETRO and & Halston Heritage stores with BNI Credit Card until 30 Apr16. T&C. 021500046', 15, '2016-04-30'),
(16, 'Program Join Promo BNI-Emirates Airlines', 'Enjoy Disc. Up to 15% Emirates Airline for departure from JKT/DPS with BNI Credit Cards, book via www.emirates.com/id/bni until 30Mar16.T&C ', 'Enjoy Disc. Up to 15% Emirates Airline for departure from JKT/DPS with BNI Credit Cards, book via www.emirates.com/id/bni until 30Mar16.T&C Apply.1500046', 16, '2016-03-30');

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
-- Indexes for table `tbl_mentions`
--
ALTER TABLE `tbl_mentions`
  ADD PRIMARY KEY (`mention_id`);

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
  MODIFY `hashtag_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `tbl_mentions`
--
ALTER TABLE `tbl_mentions`
  MODIFY `mention_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_promotions`
--
ALTER TABLE `tbl_promotions`
  MODIFY `promotion_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
