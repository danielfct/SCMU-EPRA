-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 11-Jun-2018 às 17:35
-- Versão do servidor: 10.1.31-MariaDB
-- PHP Version: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id5662112_epra_db`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `alarme`
--

CREATE TABLE `alarme` (
  `estadoAtual` tinyint(1) NOT NULL DEFAULT '0',
  `tempoAtividade` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `intensidade` int(10) NOT NULL DEFAULT '1000',
  `intervalo` int(10) NOT NULL DEFAULT '1',
  `tempoDeToque` int(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `alarme`
--

INSERT INTO `alarme` (`estadoAtual`, `tempoAtividade`, `intensidade`, `intervalo`, `tempoDeToque`) VALUES
(1, '2018-06-04 13:06:36', 1000, 1, 0);

-- --------------------------------------------------------

--
-- Estrutura da tabela `areas`
--

CREATE TABLE `areas` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `alarmeLigado` tinyint(1) NOT NULL DEFAULT '0',
  `sensor` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `areas`
--

INSERT INTO `areas` (`id`, `nome`, `alarmeLigado`, `sensor`) VALUES
(1, 'WC', 0, 'Ledzinho'),
(2, 'Quarto', 1, 'Ledzinho');

-- --------------------------------------------------------

--
-- Estrutura da tabela `devices`
--

CREATE TABLE `devices` (
  `nome` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `tipo` enum('Sensor','Actuator','Simulator') COLLATE utf8_unicode_ci DEFAULT NULL,
  `ligado` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `devices`
--

INSERT INTO `devices` (`nome`, `tipo`, `ligado`) VALUES
('Ledzinho', 'Actuator', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `historico`
--

CREATE TABLE `historico` (
  `id` int(11) NOT NULL,
  `evento` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `datahora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `historico`
--

INSERT INTO `historico` (`id`, `evento`, `datahora`) VALUES
(5, 'José Legatheaux ligou o alarme!', '2018-05-26 19:03:37'),
(6, 'José Legatheaux desligou o alarme!', '2018-05-26 19:04:34'),
(7, 'José Legatheaux enviou 100 pacotes!', '2018-05-26 19:05:13');

-- --------------------------------------------------------

--
-- Estrutura da tabela `historicoTracking`
--

CREATE TABLE `historicoTracking` (
  `area` int(11) NOT NULL,
  `duracao` int(11) NOT NULL,
  `trackingId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `historicoTracking`
--

INSERT INTO `historicoTracking` (`area`, `duracao`, `trackingId`) VALUES
(2, 2, 1),
(1, 5, 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `simuladores`
--

CREATE TABLE `simuladores` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `estadoAtual` tinyint(1) NOT NULL DEFAULT '0',
  `atuador` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `areaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `simuladores`
--

INSERT INTO `simuladores` (`id`, `nome`, `estadoAtual`, `atuador`, `areaId`) VALUES
(1, 'Candeeiro1', 0, 'Ledzinho', 1),
(2, 'Estores1', 1, 'Ledzinho', 1),
(3, 'Candeeiro2', 1, 'Ledzinho', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `tracking`
--

CREATE TABLE `tracking` (
  `id` int(11) NOT NULL,
  `areaAtual` int(11) NOT NULL,
  `areaEntrada` int(11) NOT NULL,
  `pessoasNotificadas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `tracking`
--

INSERT INTO `tracking` (`id`, `areaAtual`, `areaEntrada`, `pessoasNotificadas`) VALUES
(1, 2, 2, 1),
(2, 1, 2, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `utilizador`
--

CREATE TABLE `utilizador` (
  `nome` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `telemovel` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `privilegios` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pin` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `utilizador`
--

INSERT INTO `utilizador` (`nome`, `telemovel`, `email`, `password`, `admin`, `privilegios`, `pin`) VALUES
('José', '912345679', 'joselegateu@gmail.com', 'jose', 1, '1,2', 1234),
('Manuel', '912345678', 'manuel@gmail.com', 'manuel', 0, '1,2', 4321);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `areas`
--
ALTER TABLE `areas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`),
  ADD KEY `sensor` (`sensor`);

--
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`nome`);

--
-- Indexes for table `historico`
--
ALTER TABLE `historico`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `historicoTracking`
--
ALTER TABLE `historicoTracking`
  ADD KEY `area` (`area`),
  ADD KEY `trackingId` (`trackingId`);

--
-- Indexes for table `simuladores`
--
ALTER TABLE `simuladores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`),
  ADD KEY `atuador` (`atuador`),
  ADD KEY `areaId` (`areaId`);

--
-- Indexes for table `tracking`
--
ALTER TABLE `tracking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `areaAtual` (`areaAtual`),
  ADD KEY `areaEntrada` (`areaEntrada`);

--
-- Indexes for table `utilizador`
--
ALTER TABLE `utilizador`
  ADD PRIMARY KEY (`email`),
  ADD UNIQUE KEY `telemovel` (`telemovel`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `areas`
--
ALTER TABLE `areas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `historico`
--
ALTER TABLE `historico`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `simuladores`
--
ALTER TABLE `simuladores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tracking`
--
ALTER TABLE `tracking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `areas`
--
ALTER TABLE `areas`
  ADD CONSTRAINT `areas_ibfk_1` FOREIGN KEY (`sensor`) REFERENCES `devices` (`nome`);

--
-- Limitadores para a tabela `historicoTracking`
--
ALTER TABLE `historicoTracking`
  ADD CONSTRAINT `historicoTracking_ibfk_1` FOREIGN KEY (`area`) REFERENCES `areas` (`id`),
  ADD CONSTRAINT `historicoTracking_ibfk_2` FOREIGN KEY (`trackingId`) REFERENCES `tracking` (`id`);

--
-- Limitadores para a tabela `simuladores`
--
ALTER TABLE `simuladores`
  ADD CONSTRAINT `simuladores_ibfk_1` FOREIGN KEY (`atuador`) REFERENCES `devices` (`nome`),
  ADD CONSTRAINT `simuladores_ibfk_2` FOREIGN KEY (`areaId`) REFERENCES `areas` (`id`);

--
-- Limitadores para a tabela `tracking`
--
ALTER TABLE `tracking`
  ADD CONSTRAINT `tracking_ibfk_1` FOREIGN KEY (`areaAtual`) REFERENCES `areas` (`id`),
  ADD CONSTRAINT `tracking_ibfk_2` FOREIGN KEY (`areaEntrada`) REFERENCES `areas` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
