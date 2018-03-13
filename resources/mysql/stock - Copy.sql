
CREATE DATABASE IF NOT EXISTS `the_stock` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `the_stock`;


CREATE TABLE IF NOT EXISTS `brands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;


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
(45, 'Other');


CREATE TABLE IF NOT EXISTS `business` (
  `id` int(11) NOT NULL,
  `vat_tax` varchar(64) NOT NULL,
  `name` varchar(128) NOT NULL,
  `address` varchar(256) NOT NULL,
  `cars` varchar(64) NOT NULL,
  `no_of_services` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vat_tax` (`vat_tax`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `car` (
  `id` int(11) NOT NULL,
  `registration` varchar(10) NOT NULL DEFAULT '00AA00000',
  `brand_id` int(11) NOT NULL DEFAULT '45',
  PRIMARY KEY (`id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `car_id` int(11) NOT NULL,
  `no_of_services` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `car_id` (`car_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `invoices` (
  `id` int(11) NOT NULL,
  `customer` varchar(128) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,
  `stock_codes` varchar(1024) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,
  `discount` decimal(6,2) NOT NULL,
  `ispercent` tinyint(1) NOT NULL,
  `total` decimal(6,2) NOT NULL,
  `date` varchar(16) NOT NULL,
  `file_name` varchar(256) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `repak_report` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `sold_car` int(11) NOT NULL,
  `fitted_car` int(11) NOT NULL,
  `bought_car` int(11) NOT NULL,
  `sold_agri` int(11) NOT NULL,
  `fitted_agri` int(11) NOT NULL,
  `bought_agri` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `settings` (
  `id` int(11) NOT NULL,
  `path` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(4) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `addVat` tinyint(4) DEFAULT '0',
  `addTransit` tinyint(4) DEFAULT '0',
  `addVEMC` tinyint(4) NOT NULL DEFAULT '0',
  `price` decimal(6,2) DEFAULT NULL,
  `qnt` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

ALTER TABLE `car`
  ADD CONSTRAINT `car_ibfk_1` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);

ALTER TABLE `customers`
  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`);
COMMIT;