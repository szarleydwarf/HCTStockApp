-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 24, 2018 at 03:57 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hct_stock`
--

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

CREATE TABLE `brands` (
  `id` int(11) NOT NULL,
  `brand` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `brands`
--

INSERT INTO `brands` (`id`, `brand`) VALUES
(1, 'Volkswagen'),
(2, 'Toyota'),
(3, 'Ford'),
(4, 'Mazda'),
(5, 'Skoda'),
(6, 'Renault'),
(7, 'Citroen'),
(8, 'Peugeot'),
(9, 'Audi'),
(10, 'BMW'),
(11, 'Mercedes'),
(12, 'Opel'),
(13, 'Porsche'),
(14, 'Suzuki'),
(15, 'Mitshubishi'),
(16, 'Lancia'),
(17, 'Fiat'),
(18, 'Alfa Romeo'),
(19, 'Honda'),
(20, 'Isuzu'),
(21, 'Nissan'),
(22, 'Subaru'),
(23, 'Lexus'),
(24, 'Kia'),
(25, 'Hyundai'),
(26, 'SSangyong'),
(27, 'Daewoo'),
(28, 'Chevrolet'),
(29, 'Vauxhal'),
(30, 'Seat'),
(31, 'Saab'),
(32, 'Volvo'),
(33, 'Bentley'),
(34, 'Aston Martin'),
(35, 'Jaguar'),
(36, 'Land Rover'),
(37, 'Range Rover'),
(38, 'Rover'),
(39, 'Mini'),
(40, 'Lotus'),
(41, 'Rols Roys'),
(42, 'Chrysler'),
(43, 'Dodge'),
(44, 'Jeep'),
(45, 'Other'),
(46, 'Tractor');

-- --------------------------------------------------------

--
-- Table structure for table `business`
--

CREATE TABLE `business` (
  `id` int(11) NOT NULL,
  `vat_tax` varchar(64) NOT NULL,
  `name` varchar(128) NOT NULL,
  `address` varchar(256) NOT NULL,
  `cars` varchar(64) NOT NULL,
  `no_of_services` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `business`
--

INSERT INTO `business` (`id`, `vat_tax`, `name`, `address`, `cars`, `no_of_services`) VALUES
(1, '123456789', 'TEST', 'TEST', '', 0),
(2, 'ySJsUxMUfF', 'Test 2', 'no road', '', 1),
(3, 'rVenhiCmdU', 'Test 2', 'no road', '', 2),
(4, 'uLdmRqyoKT', 'Test 2', 'no road', '', 1),
(6, 'YWcNyskyLX', 'Test 2', 'no road', '', 10),
(7, 'VGrKuYuXuG', 'Test 2', 'no road', '', 1),
(8, 'DNPOzKaZNF', 'Test 2', 'no road', '', 1),
(9, 'uZuoCkLHkS', 'Test 2', 'no road', '', 3),
(10, 'nHQkhALGME', 'Test 2', 'no road', '', 1),
(11, 'MZKBlaENTH', 'Test 2', 'no road', '', 1),
(12, 'HfnKetoOae', 'Test 2', 'no road', '', 1),
(13, 'BLvvIhPzHV', 'Test 2', 'no road', '', 1),
(14, 'bXbUtcWGFJ', 'Test 2', 'no road', '', 2),
(15, 'yqCASOdkYb', 'Test 2', 'no road', '', 1),
(16, 'VeYBmCkJyJ', 'Test 2', 'no road', '', 1),
(17, 'JtqhVpfSbP', 'Test 2', 'no road', '', 1),
(18, 'yDKfyCAOAF', 'Test 2', 'no road', '', 1),
(19, 'ZXfAaClmHK', 'Test 2', 'no road', '', 1),
(20, 'jWuznMYBvJ', 'Test 2', 'no road', '', 1),
(21, 'iagHLRyTyk', 'Test 2', 'no road', '', 1),
(22, 'hDpcsrEJHR', 'Test 2', 'no road', '', 1),
(23, 'NbRfzUHsxo', 'Test 2', 'no road', '', 1),
(24, 'nGGECrwFvY', 'Test 2', 'no road', '', 1),
(25, 'tJOTNObHrk', 'Test 2', 'no road', '', 1),
(26, 'OoHTruGHiL', 'Test 2', 'no road', '', 1),
(27, 'fvQjqczNFJ', 'Test 2', 'no road', '', 1),
(28, 'uXCsdigqcW', 'Test 2', 'no road', '', 1),
(29, 'JwtItCnZEq', 'Test 2', 'no road', '', 1),
(30, 'FyaWObEMfx', 'Test 2', 'no road', '', 1),
(31, 'UcXvVrKyDk', 'Test 2', 'no road', '', 1),
(32, '168435', 'Leo', 'htdhtdjyrd', '', 1),
(33, '3546384', 'kyjfjft', 'kyjfjyfhtd', '', 1),
(34, '16841378', 'dgsfg', 'argare', '', 1),
(35, '124563251', 'LOpex', '32khgvgt', '', 1),
(36, '3874384', 'kologo', ' jyfjugjkygjy', '', 5),
(37, '38434734', 'kjytfvju', '34354', '45', 2);

-- --------------------------------------------------------

--
-- Table structure for table `car`
--

CREATE TABLE `car` (
  `id` int(11) NOT NULL,
  `registration` varchar(10) NOT NULL DEFAULT '00AA00000',
  `brand_id` int(11) NOT NULL DEFAULT '45'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `car`
--

INSERT INTO `car` (`id`, `registration`, `brand_id`) VALUES
(1, '00AA0000', 45),
(2, 'YSJSUXMUFF', 6),
(3, 'RVENHICMDU', 4),
(4, 'ULDMRQYOKT', 8),
(5, 'PVIEIHSMUE', 26),
(6, 'YWCNYSKYLX', 22),
(8, 'DNPOZKAZNF', 42),
(9, 'UZUOCKLHKS', 30),
(10, 'NHQKHALGME', 29),
(11, 'MZKBLAENTH', 29),
(12, 'HFNKETOOAE', 13),
(13, 'BLVVIHPZHV', 25),
(14, 'BXBUTCWGFJ', 31),
(15, 'YQCASODKYB', 39),
(16, 'VEYBMCKJYJ', 30),
(17, 'UDNEVTXGEB', 36),
(18, 'DIXFHHMZGR', 25),
(19, 'NMYEJQPICX', 42),
(20, 'TJFVTBTQLI', 9),
(21, 'TCNDBYFREW', 1),
(22, 'HDPCSREJHR', 11),
(23, 'NBRFZUHSXO', 42),
(24, 'NGGECRWFVY', 13),
(25, 'TJOTNOBHRK', 9),
(26, 'OOHTRUGHIL', 40),
(27, 'FVQJQCZNFJ', 10),
(28, 'UXCSDIGQCW', 12),
(29, 'JWTITCNZEQ', 39),
(30, 'FYAWOBEMFX', 18),
(31, 'UCXVVRKYDK', 12),
(32, '03KO1532', 39),
(33, '01O15220', 9),
(34, '151J21012', 17),
(35, '00LM1526', 34),
(36, 'XGH351', 18),
(37, 'COMPANY', 45),
(38, '15O1200', 10),
(39, '02KO1203', 3),
(40, '161K12035', 45),
(41, '00LM1566', 45),
(42, 'YWCNYSKYLX', 45),
(43, '015821325', 45),
(44, '151LM1523', 45),
(45, '121LM2356', 45),
(46, 'BLVVIHPZHV', 45),
(47, '151LM5642', 45),
(48, '02LO7895', 45),
(49, '02LO85446', 45),
(50, '02LO85446', 45),
(51, '98O9898', 45),
(52, '98O9898', 45),
(53, '98O9898', 45),
(54, 'ULDMRQYOKT', 45),
(55, 'YWCNYSKYLX', 45),
(56, 'UZUOCKLHKS', 45),
(57, '00AA0000', 45),
(58, '03KO1532', 45),
(59, 'PVIEIHSMUE', 45),
(60, 'PVIEIHSMUE', 45),
(61, 'OOHTRUGHIL', 45);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `no_of_services` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`id`, `car_id`, `no_of_services`) VALUES
(1, 1, 5),
(2, 2, 9),
(3, 3, 7),
(4, 4, 9),
(5, 5, 6),
(6, 6, 7),
(8, 8, 6),
(9, 9, 2),
(10, 10, 2),
(11, 11, 0),
(12, 12, 3),
(13, 13, 5),
(14, 14, 4),
(15, 15, 4),
(16, 16, 4),
(17, 17, 1),
(18, 18, 10),
(19, 19, 4),
(20, 20, 7),
(21, 21, 5),
(22, 22, 4),
(23, 23, 4),
(24, 24, 4),
(25, 25, 4),
(26, 26, 5),
(27, 27, 6),
(28, 28, 4),
(29, 29, 5),
(30, 30, 5),
(31, 31, 4),
(32, 32, 2),
(33, 33, 2),
(34, 34, 3),
(35, 35, 4),
(36, 36, 2),
(37, 37, 1),
(38, 38, 1),
(39, 39, 1),
(40, 40, 1),
(41, 41, 2),
(42, 42, 1),
(43, 43, 1),
(44, 44, 1),
(45, 45, 1),
(46, 46, 1),
(47, 47, 1),
(48, 48, 1),
(49, 49, 1),
(50, 50, 1),
(51, 51, 1),
(52, 52, 1),
(53, 53, 1),
(54, 54, 1),
(55, 55, 1),
(56, 56, 1),
(57, 57, 1),
(58, 58, 1),
(59, 59, 1),
(60, 60, 1),
(61, 61, 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `id` int(11) NOT NULL,
  `customer` varchar(128) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,
  `stock_codes` varchar(1024) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,
  `discount` decimal(6,2) NOT NULL,
  `ispercent` tinyint(1) NOT NULL,
  `total` decimal(6,2) NOT NULL,
  `date` varchar(16) NOT NULL,
  `file_name` varchar(256) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invoices`
--

INSERT INTO `invoices` (`id`, `customer`, `stock_codes`, `discount`, `ispercent`, `total`, `date`, `file_name`) VALUES
(1, 'C_1', '1*CW_CARWASH#0.25@6.00;', '1.10', 0, '30.05', '2018-01-29', 'test.pdf'),
(2, 'B_2', '2*TRC_TEST#12.50@27.75;1*SR_TEST#3.0@7.55;', '0.00', 0, '63.05', '29-01-2018', 'TEST.pdf'),
(3, 'C_2', '4*TRC_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(4, 'B_3', '4*TB_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(5, 'C_4', '4*TB_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(6, 'C_4', '4*TB_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(7, 'B_2', '4*TB_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(8, 'B_2', '4*TB_TEST#12.50@27.75;3*CW_TEST#3.0@7.55;', '0.00', 0, '63.05', '02-02-2018', 'TEST.pdf'),
(9, 'B_7', '5*TB_TEST#12.50@27.75;1*CW_TEST#3.0@7.55;', '10.00', 1, '124.33', '05-02-2018', 'TEST.pdf'),
(10, 'B_7', '5*TB_TEST#12.50@27.75;1*CW_TEST#3.0@7.55;', '10.00', 1, '124.33', '05-02-2018', 'TEST.pdf'),
(11, 'B_7', '5*TB_TEST#12.50@27.75;1*CW_TEST#3.0@7.55;', '10.00', 1, '124.33', '05-02-2018', 'TEST.pdf'),
(12, 'C_12', '1*TRA_225/45/18 GOALSTAR V78#99.81@120.0;', '0.00', 0, '120.00', '13-03-2018', '13-03-2018.pdf'),
(13, 'C_12', '1*TRC_205/55 R15 DUNLOP #38.37@58.37;1*TRA_205/55 R15 DUNLOP #38.37@58.37;', '6.74', 1, '110.00', '13-03-2018', '13-03-2018.pdf'),
(14, 'B_35', '1*TRC_195/65 R16 TEST TYRE#25.68@55.5;1*TRC_195/65 R16 TEST TYRE#25.68@55.5;', '6.74', 1, '111.00', '13-03-2018', '13-03-2018.pdf'),
(15, 'B_35', '1*TRA_205/55 R15 DUNLOP #38.37@58.37;1*TRA_205/55 R15 DUNLOP #38.37@58.37;', '0.00', 0, '116.74', '13-03-2018', '13-03-2018.pdf'),
(16, 'B_36', '1*SR_VULCANIZATION S#4.22@12.0;1*TR_175/50 R14 XL KOTY#37.87@057.87;1*TR_175/50 R14 XL KOTY#37.87@057.87;1*SH_LOLEK#77.39@115.37;1*SH_LOLEK#77.39@115.37;10*SH_WIPER BLADE BOSH SP13#4.81@010.00;', '25.00', 1, '343.86', '13-03-2018', '13-03-2018.pdf'),
(17, 'B_37', '1*OT_LOLEK#77.39@115.37;', '0.00', 0, '115.37', '13-03-2018', '13-03-2018.pdf'),
(18, 'C_12', '1*CW_CAR WASH#0.25@6.0;', '0.00', 0, '6.00', '20-03-2018', '20-03-2018.pdf'),
(19, 'C_8', '1*TR_175/50 R14 XL KOTY#37.87@57.87;1*CW_VAN WASH#2.3@8.0;1*OT_LOLEK#77.39@115.37;1*SR_VULCANIZATION S#4.22@12.0;', '0.00', 0, '193.24', '20-03-2018', '20-03-2018.pdf'),
(20, 'C_8', '1*CW_VAN WASH#2.3@8.0;1*OT_LOLEK#77.39@115.37;1*SR_VULCANIZATION S#4.22@12.0;', '0.00', 0, '135.37', '20-03-2018', '20-03-2018.pdf'),
(21, 'C_37', '1*CW_CAR WASH + WAX#0.54@6.0;1*SR_VULCANIZATION S#4.22@12.0;1*OT_LOLEK#77.39@115.37;1*SH_WHEEL TRIM 15\"#15.15@35.15;1*TRA_175/50 R14 XL KOTY#37.87@57.87;', '0.00', 0, '226.39', '20-03-2018', '20-03-2018.pdf'),
(22, 'C_10', '1*CW_CAR WASH + WAX#0.54@6.0;1*SR_VULCANIZATION S#4.22@12.0;1*OT_LOLEK#77.39@115.37;1*SH_WHEEL TRIM 15\"#15.15@35.15;1*TRC_175/50 R14 XL KOTY#37.87@57.87;', '0.00', 0, '226.39', '20-03-2018', '20-03-2018.pdf'),
(23, 'C_10', '1*TRA_205/55 R15 DUNLOP #38.37@58.37;1*TRC_205/55 R15 DUNLOP #38.37@58.37;', '0.00', 0, '116.74', '20-03-2018', '20-03-2018.pdf'),
(24, 'C_10', '1*TRA_205/55 R15 DUNLOP #38.37@58.37;1*TRC_205/55 R15 DUNLOP #38.37@58.37;1*CW_CAR WASH#0.25@6.0;1*OT_LOLEK#77.39@115.37;1*SH_WHEEL TRIM 15\"#15.15@35.15;1*SR_VULCANIZATION S#4.22@12.0;', '0.00', 0, '285.26', '20-03-2018', '20-03-2018.pdf'),
(25, 'C_11', '1*TRA_205/55 R15 DUNLOP #38.37@58.37;1*TRC_205/55 R15 DUNLOP #38.37@58.37;1*CW_CAR WASH + WAX#0.54@6.0;4*SH_WHEEL TRIM 15\"#15.15@35.15;', '15.00', 1, '248.34', '20-03-2018', '20-03-2018.pdf'),
(26, 'C_47', '1*TRA_13.6 R38 DUNLOP #47.21@067.21;', '0.00', 0, '67.21', '2018-04-23', '2018-04-23_26.pdf'),
(27, 'C_48', '1*SH_LOLEK#77.39@115.37;1*SH_LOLEK#77.39@115.37;1*TRC_195/65 R16 TEST TYRE#25.68@055.50;1*TRC_195/65 R16 TEST TYRE#25.68@055.50;', '0.00', 0, '341.74', '2018-04-24', '2018-04-24_27.pdf'),
(28, 'C_49', '1*OT_205/55 R16 Kenda TR32#47.06@063.53;1*OT_205/55 R16 Kenda TR32#47.06@063.53;', '0.00', 0, '127.06', '2018-04-24', '2018-04-24_28.pdf'),
(29, 'C_50', '1*TRC_205/55 R16 KENDA TR32#47.06@063.53;1*TRC_205/55 R16 KENDA TR32#47.06@063.53;', '0.00', 0, '127.06', '2018-04-24', '2018-04-24_29.pdf'),
(30, 'C_51', '1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;', '10.00', 1, '241.96', '2018-04-24', '2018-04-24_30.pdf'),
(31, 'C_52', '1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;1*TRA_13.6 R38 DUNLOP #47.21@067.21;', '10.00', 1, '241.96', '2018-04-24', '2018-04-24_31.pdf'),
(32, 'C_53', '1*TRA_16.9 r 28 sdfghjkl#160.27@216.36;1*TRA_16.9 r 28 sdfghjkl#160.27@216.36;', '0.00', 0, '432.72', '2018-04-24', '2018-04-24_32.pdf'),
(33, 'C_54', '1*TRC_165/45 R15 TAYPAN#24.59@051.59;1*TRC_165/45 R15 TAYPAN#24.59@051.59;', '0.00', 0, '103.18', '2018-04-24', '2018-04-24_33.pdf'),
(34, 'C_55', '1*TRC_175/50 R14 XL KOTY#37.87@057.87;1*TRC_175/50 R14 XL KOTY#37.87@057.87;1*TRC_175/50 R14 XL KOTY#37.87@057.87;1*TRC_175/50 R14 XL KOTY#37.87@057.87;', '0.00', 0, '231.48', '2018-04-24', '2018-04-24_34.pdf'),
(35, 'C_56', '1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;', '0.00', 0, '538.96', '2018-04-24', '2018-04-24_35.pdf'),
(36, 'C_57', '4*TRA_16.9r34 Mitas#226.37@305.60;', '0.00', 0, '1222.40', '2018-04-24', '2018-04-24_36.pdf'),
(37, 'C_58', '2*TRA_20.8 R42 INFINITY ECOSYS#499.7@674.60;', '0.00', 0, '1349.20', '2018-04-24', '2018-04-24_37.pdf'),
(38, 'C_59', '1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;2*TRC_205/55 R15 DUNLOP #38.37@058.37;', '0.00', 0, '386.22', '2018-04-24', '2018-04-24_38.pdf'),
(39, 'C_60', '1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;1*TRC_225/45/18 GOALSTAR V78#99.81@134.74;', '0.00', 0, '269.48', '2018-04-24', '2018-04-24_39.pdf'),
(40, 'C_61', '4*TRA_24.5r32 bkt cedr#213.48@288.20;', '10.00', 1, '1037.52', '2018-04-24', '2018-04-24_40.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `repak_report`
--

CREATE TABLE `repak_report` (
  `date` varchar(7) NOT NULL DEFAULT '2018-01',
  `sold_car` int(11) NOT NULL,
  `fitted_car` int(11) NOT NULL,
  `bought_car` int(11) NOT NULL,
  `sold_agri` int(11) NOT NULL,
  `fitted_agri` int(11) NOT NULL,
  `bought_agri` int(11) NOT NULL,
  `returned_car` int(11) NOT NULL,
  `returned_agri` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `repak_report`
--

INSERT INTO `repak_report` (`date`, `sold_car`, `fitted_car`, `bought_car`, `sold_agri`, `fitted_agri`, `bought_agri`, `returned_car`, `returned_agri`) VALUES
('2018-02', 2, 8, 5, 3, 7, 5, 10, 15),
('2018-03', 10, 30, 20, 10, 30, 20, 50, 50),
('2018-04', 18, 0, 66, 10, 45, 161, 20, 48);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `id` int(11) NOT NULL,
  `path` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE `stock` (
  `id` int(11) NOT NULL,
  `code` varchar(4) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `addVat` tinyint(4) DEFAULT '0',
  `addTransit` tinyint(4) DEFAULT '0',
  `addVEMC` tinyint(4) NOT NULL DEFAULT '0',
  `price` decimal(6,2) DEFAULT NULL,
  `qnt` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`id`, `code`, `name`, `cost`, `addVat`, `addTransit`, `addVEMC`, `price`, `qnt`) VALUES
(1, 'TRC', '195/65 R16 TEST TYRE', '25.68', 1, 1, 0, '55.50', 92),
(2, 'TRC', '205/55 R15 DUNLOP ', '38.37', 1, 1, 1, '58.37', 92),
(3, 'TRA', '13.6 R38 DUNLOP ', '47.21', 1, 1, 1, '67.21', 78),
(4, 'TRC', '225/45/18 GOALSTAR V78', '99.81', 1, 0, 0, '134.74', 82),
(5, 'TRC', '175/50 R14 XL KOTY', '37.87', 1, 0, 0, '57.87', 85),
(7, 'TRC', '165/45 R15 TAYPAN', '24.59', 1, 1, 0, '51.59', 88),
(8, 'CW', 'Car Wash', '0.25', 0, 0, 0, '6.00', 0),
(9, 'CW', 'Van Wash', '2.30', 0, 0, 0, '8.00', 0),
(10, 'CW', 'Car Wash + Wax', '0.54', 0, 0, 0, '6.00', 0),
(11, 'SH', 'LOLEK', '77.39', 1, 1, 0, '115.37', 1492),
(13, 'TB', '145/155 R15 TUBE', '5.26', 0, 1, 0, '12.00', 1496),
(14, 'SR', 'VULCANIZATION S', '4.22', 0, 0, 0, '12.00', 0),
(15, 'SH', 'WHEEL TRIM 15\"', '15.15', 0, 1, 0, '35.15', 1496),
(16, 'CW', 'CAR WASH + WAX + DRY', '0.75', 0, 0, 0, '7.00', 0),
(17, 'TRC', '205/55 R16 DUNLOP', '33.33', 1, 1, 1, '66.66', 99),
(18, 'TRC', '205/55 R16 GOODYEAR EFFIGRIP', '62.48', 1, 0, 1, '84.35', 95),
(19, 'CW', 'VAN WASH + WAX', '1.54', 1, 0, 0, '10.00', 0),
(20, 'SH', 'WIPER BLADE BOSH SP13', '4.81', 1, 0, 0, '10.00', 1489),
(21, 'SH', 'LIGHT BULB H4', '0.01', 1, 0, 0, '5.00', 1500),
(22, 'OT', 'TEST OTHER', '1.33', 1, 1, 0, '5.20', 0),
(23, 'TRC', '205/55 R16 KENDA TR32', '47.06', 1, 1, 1, '63.53', 0),
(24, 'TRA', '20.8 R42 INFINITY ECOSYS', '499.70', 1, 1, 1, '674.60', 98),
(25, 'TRC', '195/65 R14 INC.COM', '33.52', 1, 1, 1, '53.52', 10),
(26, 'TRA', '20.8 R38 BLASSS', '257.75', 1, 1, 1, '347.96', 24),
(27, 'TRA', '16.9 r 28 sdfghjkl', '160.27', 1, 1, 1, '216.36', 10),
(28, 'TRC', '175/65 r14', '36.36', 1, 1, 1, '56.36', 44),
(29, 'TRC', '225/55R17 DUBLO BUBLO', '98.82', 1, 1, 1, '133.41', 12),
(30, 'TRA', '16.9r34 Mitas', '226.37', 1, 1, 1, '305.60', 12),
(31, 'TRC', '255/40r19 falken', '163.34', 1, 1, 1, '220.51', 44),
(32, 'TRC', '245/45R17 COISRE', '111.19', 1, 1, 1, '150.11', 0),
(33, 'TRC', '235/50R17 DUNLOP', '102.43', 1, 1, 1, '138.28', 0),
(34, 'TRA', '13.6R38 BKT', '155.47', 1, 1, 1, '209.88', 0),
(35, 'TRC', '245/45R18 MISUNA', '87.83', 1, 1, 1, '118.57', 44),
(36, 'TRA', '24.5r32 bkt cedr', '213.48', 1, 1, 1, '288.20', 96);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `business`
--
ALTER TABLE `business`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `vat_tax` (`vat_tax`);

--
-- Indexes for table `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`),
  ADD KEY `brand_id` (`brand_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `car_id` (`car_id`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `repak_report`
--
ALTER TABLE `repak_report`
  ADD PRIMARY KEY (`date`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `brands`
--
ALTER TABLE `brands`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
--
-- AUTO_INCREMENT for table `stock`
--
ALTER TABLE `stock`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `car_ibfk_1` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);

--
-- Constraints for table `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
