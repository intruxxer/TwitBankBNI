-- phpMyAdmin SQL Dump
-- version 4.4.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 16, 2015 at 10:31 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bnizona`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_customerservices`
--

CREATE TABLE IF NOT EXISTS `tbl_customerservices` (
  `cs_id` int(11) NOT NULL,
  `cs_title` varchar(140) NOT NULL,
  `cs_content` text NOT NULL,
  `cs_hashtag` int(11) NOT NULL,
  `cs_deleted` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_customerservices`
--

INSERT INTO `tbl_customerservices` (`cs_id`, `cs_title`, `cs_content`, `cs_hashtag`, `cs_deleted`) VALUES
(1, 'Kartu ATM Hilang', 'Nasabah Yth, Silakan telpon ke BNI CALL 1500046 untuk memblokir Kartu ATMnya. Kemudian mengganti kartu yang hilang dengan mendatangi  BNI terdekat dengan membawa Buku Taplus dan KTP Asli. Bisa diganti di hari yang sama jika memesan Kartu Debit Instan.', 17, 0),
(2, 'Kartu ATM Tertelan', 'Nasabah Yth.,\r\nSilakan telpon ke BNI CALL 1500046 untuk melaporkan bahwa kartu tertelan dan untuk memblokir Kartu ATM.', 18, 0),
(3, 'Buku Tabungan Hilang', 'Nasabah Yth.,\r\n\r\nSilakan datang ke Kantor Cabang BNI dimana rekening dibuka dengan membawa Kartu ATM dan KTP Asli.', 19, 0),
(4, 'Informasi Token PLN', 'Nasabah Yth.,\r\n\r\nSilakan gunakan perintah\r\n\r\nTOP<spasi>PLN<spasi>No Meter/ID Pelanggan<spasi>Nominal | Contoh : TOP PLN 04176855916 50000\r\n\r\n- Jumlah nominal untuk 1 (satu) kali transaksi minimal Rp. 20.000,- & maksimal Rp. 1.000.000,-                \r\n\r\nCek Status pembelian Token PLN \r\n\r\nPrepaid : \r\n\r\nCEK<spasi>PLN<spasi> No Meter/ID Pelanggan<spasi>No Reff\r\n\r\nContoh : \r\n\r\nCEK PLN 04176855916 100917053449417628', 20, 0),
(5, 'Debit Online', 'Nasabah Yth.,\r\n\r\nSilakan Request VCN via SMS Banking : REQ[spasi]VCN[spasi]NominalBelanja \r\n\r\nlalu kirim ke 3346.', 21, 0),
(6, 'Pembukaan BNI Taplus', 'Nasabah Yth.,\r\n\r\nUntuk membuka BNI Taplus, setoran awal Rp 500.000,- (Jabodetabek) , Rp 250.000,- (Luar Jabodetabek). Silakan membawa KTP yang sama dengan lokasi BNI dimana rekening akan dibuka, serta NPWP jika sudah bekerja.', 22, 0),
(7, 'Tarif ATM Kerjasama', 'Tarif ATM Kerjasama (ATM LINK, ATM BERSAMA, ATM PRIMA) : \r\n\r\nTransfer Rp 6.500,- ;\r\n\r\nCek Saldo Rp 4.000,-; \r\n\r\nTarik Tunai Rp 6.500,-;\r\n\r\nTransaksi Ditolak Rp 2.500,- \r\n\r\nKhusus ATM Bersama Rp. 3.000', 23, 0),
(8, 'Pembayaran Biaya Paspor', 'Nasabah Yth.,\r\n\r\nCara Pembayaran Passport di ATM BNI berturut-turut:\r\n\r\nMasukkan Kartu dan PIN\r\n\r\nPilih Bahasa\r\n\r\nPilih Menu Lain\r\n\r\nPilih Menu Pembayaran\r\n\r\nPilih Menu Berikutnya\r\n\r\nPilih Menu Berikutnya\r\n\r\nPilih Imigrasi\r\n\r\nInput nomor Permohonan Passport (17 digit)\r\n\r\nTampilan layar konfirmasi : Pilih Setuju', 36, 0),
(9, 'Memblokir ATM BNI Anda', 'Nasbah Yth.,\r\n\r\nCara Memblokir Kartu Debit Anda dari Internet Banking dengna cara:\r\n\r\nLogin Internet Banking BNI\r\n\r\nPilih MENU LAYANAN LAINNYA\r\n\r\nPilih PERMOHONAN LAYANAN\r\n\r\nPilih PERMOHONAN BARU\r\n\r\nPilih REKENING TABUNGAN & GORI – BLOKIR KARTU DEBIT\r\n\r\nPilih Nomor Rekening yang Kartunya akan diblokir\r\n\r\nKlik PROSES\r\n\r\nKlik BLOKIR', 37, 0),
(10, 'Pembukaan Rekening Taplus Muda', 'Nasabah Yth.,\r\n\r\nSyarat pembukaan rekening Taplus Muda:\r\n\r\n(1) Untuk Usia nasabah 15 s/d < 17 tahun, belum menikah & belum memiliki bukti identitas diri :\r\n\r\nMengisi formulir pembukaan rekening yang turut ditandatangani orang tua/wali.\r\n\r\nMelampirkan copy Akte Kelahiran / Kartu Pelajar / paspor\r\n\r\nMelampirkan Copy bukti identitas diri salah satu orang tua / wali\r\n\r\nMengisi Formulir e-Statement (jika diperlukan sebagai sarana mutasi)\r\n\r\n(2) Nasabah yang sudah memiliki bukti identitas diri (KTP/SIM/Paspor)  di usia 17 s/d 25 tahun atau 15 s/d 17 tahun telah menikah :\r\n\r\nMengisi Formulir pembukaan rekening.\r\n\r\nMembawa asli bukti identitas diri dan melampirkan copy-nya.\r\n\r\nMengisi Formulir Data Pemberi Dana jika pekerjaan nasabah adalah \r\n\r\nMahasiswa/Pelajar atau Ibu Rumah Tangga.\r\n\r\nMengisi Formulir e-Statement (jika diperlukan sebagai sarana mutasi).\r\n\r\nMengisi Formulir layanan Notifikasi (jika diperlukan).\r\n\r\n[Informasi]\r\n\r\nSetoran awal minimal : Rp. 100.000,-\r\n\r\nSetoran minimal selanjutnya ( di counter Teller) : Rp.   10.000,-\r\n\r\nSaldo mengendap Rp.   50.000,-.', 38, 0),
(11, 'Cara Bayar Kartu Kredit Bank Lain', 'Nasabah Yth.,\r\n\r\nDaftar Kartu kredit bank lain yang bisa dilayani pembayarannya via SMS Banking, ATM dan Internet Banking adalah:\r\n\r\nCitibank & Ready Cash\r\n\r\nBank Mega\r\n\r\nANZ\r\n\r\nCIMB Niaga\r\n\r\nHSBC\r\n\r\nPermata Bank\r\n\r\nBukopin\r\n\r\nDanamon & Amex\r\n\r\nStandard Chartered\r\n\r\nBRI\r\n\r\nPanin\r\n\r\n*Biaya per transaksi Rp 7.500', 39, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_direct_messages`
--

CREATE TABLE IF NOT EXISTS `tbl_direct_messages` (
  `message_id` int(11) NOT NULL,
  `message_type` varchar(255) DEFAULT 'default',
  `message_section` varchar(10) NOT NULL,
  `message_content` varchar(510) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_direct_messages`
--

INSERT INTO `tbl_direct_messages` (`message_id`, `message_type`, `message_section`, `message_content`) VALUES
(1, 'default', 'head', 'Pelanggan [@user] Yth, '),
(2, 'default', 'body', '\r\n\r\nDiinformasikan bahwa [content]'),
(3, 'default', 'footer', '\r\n\r\n[salam] Terima Kasih.'),
(4, 'followed', 'head', 'Terima kasih telah mem-follow Akun Twitter @BNI46.\r\n \r\nTemukan informasi-informasi mengenai promo BNI dan informasi lain yang terkait dengan layanan kami disini. '),
(5, 'followed', 'body', '\r\nCaranya: \r\nDaftarkan dulu Nama dan No.HP Anda melalui DM ke @BNi46. \r\nKetik :\r\n#daftar #nama_lengkap #nomortelepon & Tunggu balasan via DM dari Twitter kami. \r\n\r\nContoh :\r\n\r\n#daftar #Andi_Waluyo #62213456789\r\n\r\nNote : Nama Awal dan Akhir dipisah dengan "_"\r\n\r\nSetelah itu, Anda akan mendapatkan panduan melalui balasan DM dari akun Twitter kami @bni46. '),
(6, 'followed', 'footer', '\r\nTerima kasih telah memanfaatkan layanan Twitter For Banking di @BNI46. \r\n\r\nSelamat Beraktivitas!'),
(7, 'daftar', 'head', '\r\nUntuk mengakses layanan kami via Twitter silakan ketik melalui DM atau Mention ke @BNI46 (Reply kami akan melalui DM):\r\n\r\n\r\n'),
(8, 'daftar', 'body', '\r\n1. #menu : Untuk mendapatkan info tentang layanan informasi BNI via Twitter\r\n\r\n2. #promo : Untuk mendapatkan informasi tentang promo-promo BNI saat ini.\r\n\r\n3. #cs : Untuk mendapatkan info tentang layanan Customer Service  BNI via Twitter\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hashtags`
--

CREATE TABLE IF NOT EXISTS `tbl_hashtags` (
  `hashtag_id` int(11) NOT NULL,
  `hashtag_category` varchar(10) NOT NULL,
  `hashtag_term` varchar(20) NOT NULL,
  `hashtag_deleted` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_hashtags`
--

INSERT INTO `tbl_hashtags` (`hashtag_id`, `hashtag_category`, `hashtag_term`, `hashtag_deleted`) VALUES
(1, 'menu', 'menu', 0),
(2, 'menu', 'daftar', 0),
(3, 'menux', 'promo', 0),
(4, 'promo', 'shopping ', 0),
(5, 'promo', 'property', 0),
(6, 'promo', 'entertainment', 0),
(7, 'promo', 'fashion', 0),
(8, 'promo', 'fb', 0),
(9, 'promo', 'food', 0),
(10, 'promo', 'beverage', 0),
(11, 'promo', 'foodandbeverage', 0),
(12, 'promo', 'health', 0),
(13, 'promo', 'healthservice', 0),
(14, 'promo', 'hotel', 0),
(15, 'promo', 'lifestyle', 0),
(16, 'promo', 'travel', 0),
(17, 'cs', 'kartuhilang', 0),
(18, 'cs', 'kartutertelan ', 0),
(19, 'cs', 'bukuhilang', 0),
(20, 'cs', 'tokenpln', 0),
(21, 'cs', 'debitonline', 0),
(22, 'cs', 'taplus', 0),
(23, 'cs', 'tarifatm', 0),
(24, 'promo', 'ecommerce', 0),
(25, 'promo', 'gadget', 0),
(26, 'promo', 'groceries', 0),
(27, 'promo', 'rbt', 0),
(28, 'promo', 'sports', 0),
(30, 'promo', 'debitonline', 0),
(31, 'promo', 'aeon', 0),
(32, 'promo', 'watch', 0),
(33, 'menu', 'helppromo', 0),
(34, 'menux', 'cs', 0),
(35, 'menu', 'helpcs', 0),
(36, 'cs', 'passport', 0),
(37, 'cs', 'blokiratm', 0),
(38, 'cs', 'taplusmuda', 0),
(39, 'cs', 'byrccbanklain', 0);

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
  `promotion_enddate` date NOT NULL,
  `promotion_deleted` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_promotions`
--

INSERT INTO `tbl_promotions` (`promotion_id`, `promotion_title`, `promotion_slug`, `promotion_content`, `promotion_hashtag`, `promotion_enddate`, `promotion_deleted`) VALUES
(1, 'Usulan Program 3x BRP di MCC Entertainment', 'Hanya untuk Anda! 3X BNI Reward Point untuk akumulasi trx di merchant kategori ENTERTAINMENT min Rp 1,5Jt dgn Kartu Kredit BNI Reguler sd 31', 'Hanya untuk Anda! 3X BNI Reward Point untuk akumulasi trx di merchant kategori ENTERTAINMENT min Rp 1,5Jt dgn Kartu Kredit BNI Reguler sd 31 Okt 15.S&K. 1500046.', 6, '2015-10-31', 0),
(2, 'Promo diskon 15% La Mer Collections', 'LA MER COLLECTIONS Special Offer! Disc 15% at La Mer Collections KotaKasablanka, TSM Bandung, Beachwalk Bali w/ BNI CreditCard until 31Dec15', 'LA MER COLLECTIONS Special Offer! Disc 15% at La Mer Collections KotaKasablanka, TSM Bandung, Beachwalk Bali w/ BNI CreditCard until 31Dec15. T&C Apply. 1500046', 7, '2015-12-31', 0),
(3, 'Usulan Joint Promo BNI - PUYO', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K ', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K berlaku.1500046. ', 8, '2015-02-26', 0),
(4, 'BNI Dining Experience @ Ta Yang Suki BANDUNG', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K ', 'DISKON 25% utk Food Only (min trx Rp 300rb) dg Kartu Kredit BNI di Ta Yang Suki, Paskal Hypersquare Jl Pasir Kaliki Bandung s.d 15Des15.S&K berlaku.1500046. ', 8, '2015-12-15', 0),
(5, 'Promo Dining Exp - Diskon 10% @WARUNG ONGAN BALI', 'DISKON 10% (F&B) setiap hari Senin-Jumat dg Kartu Kredit BNI di WARUNG ONGAN, Jl Noja Sarawaswati 12A Denpasar, sd 14 Jan 16. S&K berlaku. I', 'DISKON 10% (F&B) setiap hari Senin-Jumat dg Kartu Kredit BNI di WARUNG ONGAN, Jl Noja Sarawaswati 12A Denpasar, sd 14 Jan 16. S&K berlaku. Info BNI Call 1500046', 8, '2016-01-04', 0),
(6, 'Promo BNI - Pizza Hut Diskon 15% Pizza Ukuran Apa Saja & Big Box', 'Diskon 15% untuk Pizza apa saja & Paket Big Box di PIZZA HUT setiap Hari Sabtu & Minggu dengan Kartu Kredit BNI s.d 27 Des 2015. S&K Berlaku', 'Diskon 15% untuk Pizza apa saja & Paket Big Box di PIZZA HUT setiap Hari Sabtu & Minggu dengan Kartu Kredit BNI s.d 27 Des 2015. S&K Berlaku. Info BNI: 1500046 ', 8, '2015-12-27', 0),
(7, 'Joint Promo BNI - Excelso', 'DISKON 20% di EXCELSO dengan Kartu Kredit BNI Titanium, Platinum & Infinite s/d 10 Maret 2016. S&K berlaku. Info BNI Call: 1500046', 'DISKON 20% di EXCELSO dengan Kartu Kredit BNI Titanium, Platinum & Infinite s/d 10 Maret 2016. S&K berlaku. Info BNI Call: 1500046', 8, '2016-03-10', 0),
(8, 'Promo Dining Exp - Diskon hingga 25% @XO SUKI BALI', 'DISKON hingga 25% (Suki only) min trx Rp 200rb dg Kartu Kredit BNI di XO SUKI, Jl Cok Agung Tresna 29 & Jl Sunset Road Barat 7 Kuta Bali, sd', 'DISKON hingga 25% (Suki only) min trx Rp 200rb dg Kartu Kredit BNI di XO SUKI, Jl Cok Agung Tresna 29 & Jl Sunset Road Barat 7 Kuta Bali, sd 31Jan16.S&K.1500046', 8, '2016-01-31', 0),
(9, 'Promo Dining Exp - Diskon hingga 30% @PRIMARASA SURABAYA', 'DISKON 30% (F&B) min trx Rp 150rb dg Kartu Kredit BNI di AYAM BAKAR PRIMARASA, Jl Kusumabangsa, Manyar, Raya Kupang, A Yani Surabaya, sd 31 ', 'DISKON 30% (F&B) min trx Rp 150rb dg Kartu Kredit BNI di AYAM BAKAR PRIMARASA, Jl Kusumabangsa, Manyar, Raya Kupang, A Yani Surabaya, sd 31 Des 15.S&K.1500046', 8, '2015-12-31', 0),
(10, 'Review dan perpanjangan program promo BNI-Beranda Cafe (Crowne Plaza Hotel', 'GET 20% DISCOUNT at Beranda Cafe @Crowne Plaza Jakarta with BNI CreditCard. Valid Until 10 Juni 2016. T&C Apply. Info 1500046', 'GET 20% DISCOUNT at Beranda Cafe @Crowne Plaza Jakarta with BNI CreditCard. Valid Until 10 Juni 2016. T&C Apply. Info 1500046', 8, '2016-06-10', 0),
(11, 'Program Join Promo BNI- RS Premier Jatinegara (Ramsay Healthcare Indonesia)', 'Disc 20% utk diagnostic MRI &MSCT ,Disc 10% utk pemeriksaan paket Med Check-Up di RS Premier Jatinegara dgn Kartu Kredit BNI(hingga 31Des15)', 'Disc 20% utk diagnostic MRI &MSCT ,Disc 10% utk pemeriksaan paket Med Check-Up di RS Premier Jatinegara dgn Kartu Kredit BNI(hingga 31Des15).S&K berlaku.1500046', 13, '2015-12-31', 0),
(12, 'Join Promo Reguler BNI - Optik Seis', 'Diskon 10% utk pembelian Frame & Sunglasses di seluruh outlet Optik Seis dgn Kartu Kredit BNI Anda.(Hingga 31 Jan16).S&K berlaku.Info 150004', 'Diskon 10% utk pembelian Frame & Sunglasses di seluruh outlet Optik Seis dgn Kartu Kredit BNI Anda.(Hingga 31 Jan16).S&K berlaku.Info 1500046', 13, '2016-01-31', 0),
(13, 'Leverage Program MasterCard - Park Hotel Group', 'Stay 3 Pay 2 and Free Upgrade Room at Park Hotel Group in Singapore,China,Hongkong & Japan with your BNI Mastercard Credit Card (til 31Dec15', 'Stay 3 Pay 2 and Free Upgrade Room at Park Hotel Group in Singapore,China,Hongkong & Japan with your BNI Mastercard Credit Card (til 31Dec15).T&C apply.1500046', 14, '2015-12-31', 0),
(14, 'Perpanjangan Program Promo Ticktab.com', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI. S&K berlaku.info 1', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI. S&K berlaku.info 1500046', 14, '2015-12-31', 0),
(15, 'Joint Promo BNI - Graha Lifestyle tahun 2015-2016', 'Come & enjoy BNI Installment 0% up to 12 months at Diane von Furstenberg, ETRO and & Halston Heritage stores with BNI Credit Card until 30 A', 'Come & enjoy BNI Installment 0% up to 12 months at Diane von Furstenberg, ETRO and & Halston Heritage stores with BNI Credit Card until 30 Apr16. T&C. 021500046', 15, '2016-04-30', 0),
(16, 'Program Join Promo BNI-Emirates Airlines', 'Enjoy Disc. Up to 15% Emirates Airline for departure from JKT/DPS with BNI Credit Cards, book via www.emirates.com/id/bni until 30Mar16.T&C ', 'Enjoy Disc. Up to 15% Emirates Airline for departure from JKT/DPS with BNI Credit Cards, book via www.emirates.com/id/bni until 30Mar16.T&C Apply.1500046', 16, '2016-03-30', 0),
(17, 'Perpanjangan Program Promo Ticktab.com', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI.', 'Nikmati menginap di hotel dengan potongan harga Rp100rb di www.ticktab.com/promo/bni (s.d 31 Des15) dgn Kartu Kredit BNI. S&K berlaku. Info 1500046.\r\n', 24, '2015-12-31', 0),
(18, 'Reebonz Weekend Love! Disc 6% di www.reebonz.com + 20.000 BNI RwrdPoint', 'Reebonz Weekend Love! Disc 6% di www.reebonz.com + 20.000 BNI RwrdPoint sd 31Des15 khusus Kartu Kredit BNI', 'Reebonz Weekend Love! Disc 6% di www.reebonz.com + 20.000 BNI RwrdPoint sd 31Des15 khusus Kartu Kredit BNI MasterCard Anda. Kode voucher MASTERID6. S&K. 68888', 24, '2015-12-31', 0),
(19, 'BNI THURSDAY @www.lazada.co.id! Get DISC up to 50%+Add.DISC 10%+ 0%(6&12 Mos)', 'BNI THURSDAY @www.lazada.co.id! Get DISC up to 50%+Add.DISC 10%+ 0%(6&12 Mos) with BNI Credit Card', 'BNI THURSDAY @www.lazada.co.id! Get DISC up to 50%+Add.DISC 10%+ 0%(6&12 Mos) with BNI Credit Card every Thursday until Dec 31, 2015. T&C Apply.Info 1500046', 24, '2015-12-31', 0),
(20, 'Minyak Goreng 2 Liter hanya Rp10.900,- di Super Indo dengan Kartu Debit BNI', 'Minyak Goreng di Super Indo dengan Kartu Debit BNI. Setiap belanja minimal Rp 400.000,-, akan mendapatkan Minyak Goreng 365 kemasan 2L.', 'Minyak Goreng 2 Liter hanya Rp10.900,- di Super Indo dengan Kartu Debit BNI\r\n\r\nSetiap belanja minimal Rp 400.000,-, akan mendapatkan Minyak Goreng 365 kemasan 2L seharga :\r\n\r\nRp 10.900,- dengan Kartu Debit Silver dan Gold\r\n\r\nRp 7.900,- dengan Kartu Platinum & Emerald\r\n\r\nPromo berlaku setiap Senin - Kamis. Periode Promo hingga 31 Desember 2015. Tidak berlaku kelipatan dan split transaksi.', 26, '2015-12-31', 0),
(21, 'Diskon hingga 50% @semua Hotel Best Western Jakarta, Jawa, Bali,& Makasar', 'Diskon hingga 50% @semua Hotel Best Western Jakarta,Jawa,Bali & Makasar (15Jan-30Des15) dgn Kartu Kredit BNI.', 'Diskon hingga 50% @semua Hotel Best Western Jakarta,Jawa,Bali & Makasar (15Jan-30Des15) dgn Kartu Kredit BNI. S&K berlaku. Call Center Best Western 021-57932030', 14, '2015-12-31', 0),
(22, 'Get discount 20% for room and 15% for resto at Le Meridien Bali Jimbaran', 'Get discount 20% for room and 15% for resto at Le Meridien Bali Jimbaran with BNI Credit Card', 'Get discount 20% for room and 15% for resto at Le Meridien Bali Jimbaran with BNI Credit Card.Valid until 31 Dec15.T&C Apply.Info 021500046', 14, '2015-12-31', 0),
(23, 'DISCOUNT 50% ROOM + Special Privileges at PADMA Hotel Bandung', 'DISCOUNT 50% ROOM + Special Privileges at PADMA Hotel Bandung with BNI Credit Card', 'DISCOUNT 50% ROOM + Special Privileges at PADMA Hotel Bandung with BNI Credit Card.Valid until 20 Dec15.T&C Apply.info 021500046', 14, '2015-12-20', 0),
(24, 'Get discount 25% for room and 15% for resto @Four Points by Sheraton Bali', 'Get discount 25% for room and 15% for resto @Four Points by Sheraton Bali, Kuta with BNI Credit Card.Valid until 31 Dec', 'Get discount 25% for room and 15% for resto @Four Points by Sheraton Bali, Kuta with BNI Credit Card.Valid until 31 Dec15.T&C Apply.Info 1500046', 14, '2015-12-31', 0),
(25, 'Diskon hingga 60% (publish rate) di Prime Plaza Hotels & Resorts', 'Diskon hingga 60% (publish rate) di Prime Plaza Hotels & Resorts', 'Diskon hingga 60% (publish rate) di Prime Plaza Hotels & Resorts Purwakarta, Cikarang, Karawang, Yogya & Bali dgn Kartu Kredit BNI s/d 31Des15.S&K Berlaku. 1500046', 14, '2015-12-31', 0),
(26, 'DISC 60% utk Deluxe Room+DISC 20% utk F&B dg Kartu Kredit BNI di Kagum Group Hotel', 'DISC 60% utk Deluxe Room+DISC 20% utk F&B dg Kartu Kredit BNI di Kagum Group Hotel', 'DISC 60% utk Deluxe Room+DISC 20% utk F&B dg Kartu Kredit BNI di Kagum Group Hotel Bandung,Bali,Jakarta,Pekanbaru,Semarang & Yogyakarta s.d 30Apr16.S&K.1500046', 14, '2016-04-30', 0),
(27, 'Bersantap hemat hingga 15% di Robaa Yakitori Grand Indonesia & Mal Taman Anggrek ', 'Bersantap hemat hingga 15% di Robaa Yakitori Grand Indonesia & Mal Taman Anggrek ', 'Nikmati bersantap hemat hingga 15% di Robaa Yakitori Grand Indonesia & Mal Taman Anggrek dengan menukarkan BNI Reward Points Anda Sd 30 Jun 16. S&K. BNICall 1500046', 8, '2016-06-30', 0),
(28, 'Rejeki BNI Taplus', 'Transaksi e-Banking dan tambah saldo terus di program Rejeki BNI Taplus, Ada Poinnya. ', 'Ubah kebiasaan nggak ada poinnya menjadi poin. Menangkan dan bawa pulang Mercedes-Benz atau Honda HR-V atau Vespa Primavera yang keren banget dengan melakukan transaksi e-Banking dan tambah saldo terus di program Rejeki BNI Taplus, Ada Poinnya. Selain itu bisa bawa pulang hadiah langsungnya hanya menambah tabungan minimal Rp45 juta.\r\n\r\nProgram ini berlaku sejak tanggal 1 Oktober 2015 s.d. Maret 2016 dan berlaku bagi nasabah BNI Taplus, BNI Taplus Bisnis, BNI Taplus Muda, BNI Taplus Anak dan BNI Emerald Saving. \r\n\r\nInfo lengkap : http://bnimicrosite.com/rejekibnitaplus/', 27, '2016-03-25', 0),
(29, 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di Lazada', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di Lazada.', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di\r\n\r\nmerchant-merchant online berikut ini:\r\n\r\nhttp://www.lazada.co.id/mastercardmonday/\r\n\r\n- Diskon tambahan 10% (selected item) tiap hari Senin\r\n\r\n- Periode: s.d. 31 Desember 2015', 30, '2015-12-31', 0),
(30, 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di Hotels.com', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di Hotels.com', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di merchant-merchant online berikut ini:\r\n\r\nhttp://www.hotels.com/deals/mc_apac_terms/\r\n\r\nDiskon 10% untuk pemesanan Hotel\r\n\r\n- Booking period: s.d. 31 Desember 2015\r\n\r\n- Stay period: s.d. 31 Maret 2016\r\n', 30, '2015-12-31', 0),
(31, 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di merchant-merchant online', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di merchant-merchant online', 'Nikmati penawaran menarik dari MasterCard dan BNI Debit Online di merchant-merchant online berikut ini:\r\n\r\nhttp://www.spg-\r\n\r\nasiapacific.com/save20/en/?EM=VTY_SPG_MASTERCARDSAVE20_PROMOTIO\r\n\r\nN\r\n\r\n- Diskon 20% untuk pemesanan Hotel\r\n\r\n- Booking period: s.d. 31 Desember 2015\r\n\r\n- Stay period: s.d. 14 Juli 2016', 30, '2015-12-31', 0),
(32, 'CASHBACK Rp.50rb utk akumulasi trx Rp.1jt/bln di toko BAJU/TAS/SEPATU/JAM', 'CASHBACK Rp.50rb utk akumulasi trx Rp.1jt/bln di toko BAJU/TAS/SEPATU/JAM', 'CASHBACK Rp.50rb utk akumulasi trx Rp.1jt/bln di toko BAJU/TAS/SEPATU/JAM (non-DepStore) dgn Kartu Kredit BNI di AEON Mall, BSD City s.d 31Des15. T&C Apply. 1500046', 31, '2015-12-31', 0),
(33, 'BONIA special offer!', 'BONIA Special Offer! Enjoy 0% Installment 3 & 6 Months at BONIA Boutiques', 'BONIA Special Offer! Enjoy 0% Installment 3 & 6 Months at BONIA Boutiques\r\n\r\nwith BNI Credit Card until 30June''16. T&C Apply. 1500046', 32, '2016-06-30', 0),
(34, 'Exclusive offer at Rado Watches Senayan City', 'Exclusive offer at Rado Watches Senayan City', 'Exclusive offer at Rado Watches Senayan City with BNI Credit Card. Enjoy Special Privilege + Installment 0% up to 12 Months. T&C Apply. BNI Call 021500046', 32, '2016-06-30', 0),
(35, 'Dptkan diskon hingga 10% utk  medcheck @RS Mitra Kemayoran', 'Dptkan diskon hingga 10% utk  medcheck,lab,radiolog & pemeriksaan kesehatan lainnya @RS Mitra Kemayoran', 'Dptkan diskon hingga 10% utk  medcheck,lab,radiolog & pemeriksaan kesehatan lainnya @RS Mitra Kemayoran dgn Kartu Kredit BNI Anda (hingga 21Agt16).S&K.1500046', 13, '2016-08-21', 0),
(36, 'Banana Republic, GAP, GAP Kids & Baby GAP Stores w/BNI Credit Card.', 'Banana Republic, GAP, GAP Kids & Baby GAP Stores w/BNI Credit Card.', 'Exclusive offer at Banana Republic, GAP, GAP Kids & Baby GAP Stores w/BNI Credit Card. Instl 0% up to 12-mths + save up to 100% w/BNI RewardPoints. T&C. 021500046', 7, '2016-12-31', 0),
(37, 'Exclusive offer at Guess', 'Exclusive offer at Guess', 'Exclusive offer at Guess, Guess Accessories & Guess Kids stores w/BNI Credit Card. Instl 0% up to 12-mths + save up to 100% w/BNI Reward Points. T&C. Telp: 021500046', 7, '2016-06-30', 0),
(38, 'Langganan MNC Play Pay 8 Get Free 4 months atau Pay 4 Get Free 2 months', 'Langganan MNC Play Pay 8 Get Free 4 months atau Pay 4 Get Free 2 months', 'Langganan MNC Play Pay 8 Get Free 4 months atau Pay 4 Get Free 2 months\r\n\r\nPromo berlaku untuk pelanggan baru dan yang sudah berlangganan MNC Play \r\n\r\nserta memiliki Kartu Kredit BNI\r\n\r\nPenawaran istimewa Pay 8 Get Free 4 months atau Pay 4 Get Free 2 months \r\n\r\ndapat digabungkan dengan Cicilan 0% selama 6 dan 12 bulan dengan minimum \r\n\r\ntransaksi Rp 500.000,-\r\n\r\nBerlaku untuk semua Kartu Kredit BNI kecuali Corporate Card dan iB Hasanah \r\n\r\nCard\r\n\r\nPromo berakhir pada 14 Juni 2016', 6, '2016-06-14', 0),
(39, 'BNI Credit Card at www.agoda.com/bni', 'Get Add. Disc 7% with Gold, Titanium, Platinum & Infinite BNI Credit Card at www.agoda.com/bni', 'Book now and get Add. Disc 7% with Gold, Titanium, Platinum & Infinite BNI Credit Card at www.agoda.com/bni (until 31Dec15).T&C apply.1500046', 16, '2015-12-31', 0),
(40, 'Save 8% for HOTEL Booking + Cashback', 'Save 8% for HOTEL Booking + Cashback up to IDR100k for Airlines Ticket w/BNI Credit Card.', 'Save 8% for HOTEL Booking + Cashback up to IDR100k for Airlines Ticket w/BNI Credit Card. Book via nusatrip.com/bni (until 30Dec15).T&C Apply.1500046', 16, '2015-12-30', 0),
(41, 'Nikmati 0%3,6&12 bln utk tiket Sriwijaya Air & NAM Air', 'Nikmati 0%3,6&12 bln utk tiket Sriwijaya Air & NAM Air', 'Nikmati 0%3,6&12 bln utk tiket Sriwijaya Air & NAM Air @semua Ticketing Office & www.sriwijayaair.co.id (hingga 15 Juni 16) dgn Kartu Kredit BNI.S&K berlaku. 1500046', 16, '2016-06-15', 0),
(42, 'Enjoy Disc. Up to 15% Emirates Airline with BNI', 'Enjoy Disc. Up to 15% Emirates Airline with BNI', 'Enjoy Disc. Up to 15% Emirates Airline for departure from JKT/DPS with BNI Credit Cards, book via www.emirates.com/id/bni until 30Mar16. T&C Apply. 1500046', 16, '2016-03-30', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

CREATE TABLE IF NOT EXISTS `tbl_users` (
  `user_id` int(11) NOT NULL,
  `user_fullname` varchar(50) NOT NULL,
  `user_twitname` varchar(20) NOT NULL,
  `user_phonenum` varchar(16) NOT NULL,
  `user_accnum` varchar(20) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_customerservices`
--
ALTER TABLE `tbl_customerservices`
  ADD PRIMARY KEY (`cs_id`);

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
-- Indexes for table `tbl_users`
--
ALTER TABLE `tbl_users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_customerservices`
--
ALTER TABLE `tbl_customerservices`
  MODIFY `cs_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `tbl_direct_messages`
--
ALTER TABLE `tbl_direct_messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `tbl_hashtags`
--
ALTER TABLE `tbl_hashtags`
  MODIFY `hashtag_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT for table `tbl_mentions`
--
ALTER TABLE `tbl_mentions`
  MODIFY `mention_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_promotions`
--
ALTER TABLE `tbl_promotions`
  MODIFY `promotion_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=43;
--
-- AUTO_INCREMENT for table `tbl_users`
--
ALTER TABLE `tbl_users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
