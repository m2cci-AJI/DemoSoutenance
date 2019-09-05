-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  lun. 06 mai 2019 à 08:13
-- Version du serveur :  5.7.24
-- Version de PHP :  7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `chargeplanapplication`
--

-- --------------------------------------------------------

--
-- Structure de la table `affectations`
--

DROP TABLE IF EXISTS `affectations`;
CREATE TABLE IF NOT EXISTS `affectations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_debut` date NOT NULL,
  `date_fin` date DEFAULT NULL,
  `charge` int(11) DEFAULT NULL,
  `commentaire` varchar(255) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `collaborator_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_affectations_collaborator_id` (`collaborator_id`),
  KEY `fk_affectations_project_id` (`project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `collaborators`
--

DROP TABLE IF EXISTS `collaborators`;
CREATE TABLE IF NOT EXISTS `collaborators` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom_collaborator` varchar(255) NOT NULL,
  `prenom_collaborator` varchar(255) DEFAULT NULL,
  `trigramme` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `competencies` varchar(255) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `collaborators`
--

INSERT INTO `collaborators` (`id`, `nom_collaborator`, `prenom_collaborator`, `trigramme`, `email`, `competencies`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'YAHYA', 'FZ', 'YAF', '@localhost', NULL, 'admin', '2019-04-17 11:06:22', 'admin', '2019-04-17 11:07:12');

-- --------------------------------------------------------

--
-- Structure de la table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `databasechangelog`
--

INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2019-04-17 15:02:27', 1, 'EXECUTED', '7:5379de4cea21b8709237ca094b44210b', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414205027-1', 'jhipster', 'config/liquibase/changelog/20190414205027_added_entity_Projects.xml', '2019-04-17 15:02:27', 2, 'EXECUTED', '7:d5b06e86c54c12d7ddea490d70b1e338', 'createTable tableName=projects', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414213517-audit-1', 'jhipster-entity-audit', 'config/liquibase/changelog/20190414205027_added_entity_Projects.xml', '2019-04-17 15:02:28', 3, 'EXECUTED', '7:69cdc1ab417b1a4996b0ff82c8a6ff47', 'addColumn tableName=projects', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414205028-1', 'jhipster', 'config/liquibase/changelog/20190414205028_added_entity_Collaborators.xml', '2019-04-17 15:02:28', 4, 'EXECUTED', '7:5e171f4a2cb4385b0f81c999d255629b', 'createTable tableName=collaborators', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414213517-audit-1', 'jhipster-entity-audit', 'config/liquibase/changelog/20190414205028_added_entity_Collaborators.xml', '2019-04-17 15:02:28', 5, 'EXECUTED', '7:d8d8846ab902da35c6e0d26f8d36a8eb', 'addColumn tableName=collaborators', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414205029-1', 'jhipster', 'config/liquibase/changelog/20190414205029_added_entity_Affectations.xml', '2019-04-17 15:02:28', 6, 'EXECUTED', '7:4cafff92a935f82bbf69aca492aaea92', 'createTable tableName=affectations', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414213517-audit-1', 'jhipster-entity-audit', 'config/liquibase/changelog/20190414205029_added_entity_Affectations.xml', '2019-04-17 15:02:28', 7, 'EXECUTED', '7:b0aeeddf814064e9005f9c2777088876', 'addColumn tableName=affectations', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414213517', 'jhipster', 'config/liquibase/changelog/20190414213517_added_entity_EntityAuditEvent.xml', '2019-04-17 15:02:29', 8, 'EXECUTED', '7:5a696671965a1033d4021cc98168b228', 'createTable tableName=jhi_entity_audit_event; createIndex indexName=idx_entity_audit_event_entity_id, tableName=jhi_entity_audit_event; createIndex indexName=idx_entity_audit_event_entity_type, tableName=jhi_entity_audit_event; dropDefaultValue co...', '', NULL, '3.5.4', NULL, NULL, '5506139767'),
('20190414205029-2', 'jhipster', 'config/liquibase/changelog/20190414205029_added_entity_constraints_Affectations.xml', '2019-04-17 15:02:31', 9, 'EXECUTED', '7:2823f1c3ada7f8abc1f2dd829a3e6d39', 'addForeignKeyConstraint baseTableName=affectations, constraintName=fk_affectations_collaborator_id, referencedTableName=collaborators; addForeignKeyConstraint baseTableName=affectations, constraintName=fk_affectations_project_id, referencedTableNa...', '', NULL, '3.5.4', NULL, NULL, '5506139767');

-- --------------------------------------------------------

--
-- Structure de la table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `databasechangeloglock`
--

INSERT INTO `databasechangeloglock` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
(1, b'1', '2019-05-02 09:37:57', 'MBT1DSK0138 (192.168.56.1)');

-- --------------------------------------------------------

--
-- Structure de la table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
CREATE TABLE IF NOT EXISTS `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_authority`
--

INSERT INTO `jhi_authority` (`name`) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- --------------------------------------------------------

--
-- Structure de la table `jhi_entity_audit_event`
--

DROP TABLE IF EXISTS `jhi_entity_audit_event`;
CREATE TABLE IF NOT EXISTS `jhi_entity_audit_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entity_id` bigint(20) NOT NULL,
  `entity_type` varchar(255) NOT NULL,
  `action` varchar(20) NOT NULL,
  `entity_value` longtext,
  `commit_version` int(11) DEFAULT NULL,
  `modified_by` varchar(100) DEFAULT NULL,
  `modified_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_entity_audit_event_entity_id` (`entity_id`),
  KEY `idx_entity_audit_event_entity_type` (`entity_type`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_entity_audit_event`
--

INSERT INTO `jhi_entity_audit_event` (`id`, `entity_id`, `entity_type`, `action`, `entity_value`, `commit_version`, `modified_by`, `modified_date`) VALUES
(1, 1, 'io.github.chargeplan.application.domain.Collaborators', 'CREATE', '{\"createdBy\":\"admin\",\"createdDate\":\"2019-04-17T13:06:22.033Z\",\"lastModifiedBy\":\"admin\",\"lastModifiedDate\":\"2019-04-17T13:06:22.033Z\",\"id\":1,\"nomCollaborator\":\"YAHYA\",\"prenomCollaborator\":\"FZ\",\"trigramme\":\"YAF\",\"email\":null,\"competencies\":null,\"affectations\":[]}', 1, 'admin', '2019-04-17 11:06:22'),
(2, 1, 'io.github.chargeplan.application.domain.Collaborators', 'UPDATE', '{\"createdBy\":\"admin\",\"createdDate\":\"2019-04-17T13:06:22Z\",\"lastModifiedBy\":\"admin\",\"lastModifiedDate\":\"2019-04-17T13:07:11.872Z\",\"id\":1,\"nomCollaborator\":\"YAHYA\",\"prenomCollaborator\":\"FZ\",\"trigramme\":\"YAF\",\"email\":\"@localhost\",\"competencies\":null,\"affectations\":null}', 2, 'admin', '2019-04-17 11:07:12'),
(3, 1, 'io.github.chargeplan.application.domain.Projects', 'CREATE', '{\"createdBy\":\"admin\",\"createdDate\":\"2019-04-18T10:03:01.532Z\",\"lastModifiedBy\":\"admin\",\"lastModifiedDate\":\"2019-04-18T10:03:01.532Z\",\"id\":1,\"nameProject\":\"Projet 1\",\"projectCode\":\"P1\",\"client\":\"ZZ\",\"dP\":\"ZZ\",\"description\":null,\"affectations\":[]}', 1, 'admin', '2019-04-18 08:03:02'),
(4, 1, 'io.github.chargeplan.application.domain.Projects', 'UPDATE', '{\"createdBy\":\"admin\",\"createdDate\":\"2019-04-18T10:03:02Z\",\"lastModifiedBy\":\"admin\",\"lastModifiedDate\":\"2019-04-18T10:03:15.567Z\",\"id\":1,\"nameProject\":\"Projet 1\",\"projectCode\":\"Pro1\",\"client\":\"ZZ\",\"dP\":\"ZZ\",\"description\":null,\"affectations\":null}', 2, 'admin', '2019-04-18 08:03:16');

-- --------------------------------------------------------

--
-- Structure de la table `jhi_persistent_audit_event`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(50) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=MyISAM AUTO_INCREMENT=137 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_persistent_audit_event`
--

INSERT INTO `jhi_persistent_audit_event` (`event_id`, `principal`, `event_date`, `event_type`) VALUES
(1, 'admin', '2019-04-17 11:05:36', 'AUTHENTICATION_SUCCESS'),
(2, 'admin', '2019-04-18 07:09:34', 'AUTHENTICATION_SUCCESS'),
(3, 'admin', '2019-04-18 07:40:04', 'AUTHENTICATION_SUCCESS'),
(4, 'admin', '2019-04-18 07:57:49', 'AUTHENTICATION_SUCCESS'),
(5, 'admin', '2019-04-18 07:58:41', 'AUTHENTICATION_SUCCESS'),
(6, 'admin', '2019-04-18 08:01:00', 'AUTHENTICATION_SUCCESS'),
(7, 'admin', '2019-04-18 08:02:23', 'AUTHENTICATION_SUCCESS'),
(8, 'admin', '2019-04-18 11:06:21', 'AUTHENTICATION_SUCCESS'),
(9, 'admin', '2019-04-18 11:20:07', 'AUTHENTICATION_SUCCESS'),
(10, 'admin', '2019-04-18 11:21:11', 'AUTHENTICATION_SUCCESS'),
(11, 'admin', '2019-04-18 11:22:30', 'AUTHENTICATION_SUCCESS'),
(12, 'admin', '2019-04-18 11:35:22', 'AUTHENTICATION_SUCCESS'),
(13, 'admin', '2019-04-18 11:41:18', 'AUTHENTICATION_SUCCESS'),
(14, 'admin', '2019-04-18 11:43:56', 'AUTHENTICATION_SUCCESS'),
(15, 'admin', '2019-04-18 11:46:38', 'AUTHENTICATION_SUCCESS'),
(16, 'admin', '2019-04-18 11:48:52', 'AUTHENTICATION_SUCCESS'),
(17, 'admin', '2019-04-24 04:57:35', 'AUTHENTICATION_SUCCESS'),
(18, 'admin', '2019-04-24 04:58:45', 'AUTHENTICATION_SUCCESS'),
(19, 'admin', '2019-04-24 05:12:58', 'AUTHENTICATION_SUCCESS'),
(20, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(21, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(22, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(23, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(24, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(25, 'admin', '2019-04-24 06:53:16', 'AUTHENTICATION_SUCCESS'),
(26, 'admin', '2019-04-24 06:53:22', 'AUTHENTICATION_SUCCESS'),
(27, 'admin', '2019-04-24 06:53:22', 'AUTHENTICATION_SUCCESS'),
(28, 'admin', '2019-04-24 06:53:22', 'AUTHENTICATION_SUCCESS'),
(29, 'admin', '2019-04-24 06:53:22', 'AUTHENTICATION_SUCCESS'),
(30, 'admin', '2019-04-24 06:53:22', 'AUTHENTICATION_SUCCESS'),
(31, 'admin', '2019-04-24 07:02:24', 'AUTHENTICATION_SUCCESS'),
(32, 'admin', '2019-04-24 07:02:25', 'AUTHENTICATION_SUCCESS'),
(33, 'admin', '2019-04-24 07:35:49', 'AUTHENTICATION_SUCCESS'),
(34, 'admin', '2019-04-24 07:35:49', 'AUTHENTICATION_SUCCESS'),
(35, 'admin', '2019-04-24 07:35:50', 'AUTHENTICATION_SUCCESS'),
(36, 'admin', '2019-04-24 07:35:50', 'AUTHENTICATION_SUCCESS'),
(37, 'admin', '2019-04-24 07:35:51', 'AUTHENTICATION_SUCCESS'),
(38, 'admin', '2019-04-24 07:35:53', 'AUTHENTICATION_SUCCESS'),
(39, 'admin', '2019-04-24 07:35:53', 'AUTHENTICATION_SUCCESS'),
(40, 'admin', '2019-04-24 08:17:40', 'AUTHENTICATION_SUCCESS'),
(41, 'admin', '2019-04-24 08:17:40', 'AUTHENTICATION_SUCCESS'),
(42, 'admin', '2019-04-24 08:19:49', 'AUTHENTICATION_SUCCESS'),
(43, 'admin', '2019-04-24 11:10:58', 'AUTHENTICATION_SUCCESS'),
(44, 'admin', '2019-04-24 11:10:56', 'AUTHENTICATION_SUCCESS'),
(45, 'admin', '2019-04-24 11:10:56', 'AUTHENTICATION_SUCCESS'),
(46, 'admin', '2019-04-25 05:13:05', 'AUTHENTICATION_SUCCESS'),
(47, 'admin', '2019-04-25 06:57:15', 'AUTHENTICATION_SUCCESS'),
(48, 'admin', '2019-04-25 07:30:19', 'AUTHENTICATION_SUCCESS'),
(49, 'admin', '2019-04-25 07:30:19', 'AUTHENTICATION_SUCCESS'),
(50, 'admin', '2019-04-25 10:08:31', 'AUTHENTICATION_SUCCESS'),
(51, 'admin', '2019-04-25 10:08:31', 'AUTHENTICATION_SUCCESS'),
(52, 'admin', '2019-04-25 10:27:51', 'AUTHENTICATION_SUCCESS'),
(53, 'admin', '2019-04-25 10:27:51', 'AUTHENTICATION_SUCCESS'),
(54, 'admin', '2019-04-25 10:27:51', 'AUTHENTICATION_SUCCESS'),
(55, 'admin', '2019-04-25 11:38:01', 'AUTHENTICATION_SUCCESS'),
(56, 'admin', '2019-04-25 11:38:01', 'AUTHENTICATION_SUCCESS'),
(57, 'admin', '2019-04-25 11:38:01', 'AUTHENTICATION_SUCCESS'),
(58, 'admin', '2019-04-25 11:38:01', 'AUTHENTICATION_SUCCESS'),
(59, 'admin', '2019-04-25 11:38:01', 'AUTHENTICATION_SUCCESS'),
(60, 'admin', '2019-04-25 11:38:02', 'AUTHENTICATION_SUCCESS'),
(61, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(62, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(63, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(64, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(65, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(66, 'admina', '2019-04-26 06:43:03', 'AUTHENTICATION_FAILURE'),
(67, 'admina', '2019-04-26 06:43:19', 'AUTHENTICATION_FAILURE'),
(68, 'admina', '2019-04-26 06:43:19', 'AUTHENTICATION_FAILURE'),
(69, 'admina', '2019-04-26 06:43:20', 'AUTHENTICATION_FAILURE'),
(70, 'admin', '2019-04-26 06:43:23', 'AUTHENTICATION_SUCCESS'),
(71, 'admin', '2019-04-26 06:43:23', 'AUTHENTICATION_SUCCESS'),
(72, 'admin', '2019-04-26 06:43:23', 'AUTHENTICATION_SUCCESS'),
(73, 'admin', '2019-04-29 09:21:00', 'AUTHENTICATION_SUCCESS'),
(74, 'admin', '2019-04-29 09:21:00', 'AUTHENTICATION_SUCCESS'),
(75, 'admin', '2019-04-29 09:21:00', 'AUTHENTICATION_SUCCESS'),
(76, 'admin', '2019-04-29 09:21:00', 'AUTHENTICATION_SUCCESS'),
(77, 'admin', '2019-04-29 09:21:01', 'AUTHENTICATION_SUCCESS'),
(78, 'admin', '2019-04-29 10:24:52', 'AUTHENTICATION_SUCCESS'),
(79, 'admin', '2019-04-29 10:27:35', 'AUTHENTICATION_SUCCESS'),
(80, 'admin', '2019-04-29 10:40:30', 'AUTHENTICATION_SUCCESS'),
(81, 'admin', '2019-04-29 10:41:25', 'AUTHENTICATION_SUCCESS'),
(82, 'admin', '2019-04-30 06:08:03', 'AUTHENTICATION_SUCCESS'),
(83, 'admin', '2019-04-30 07:15:16', 'AUTHENTICATION_SUCCESS'),
(84, 'admin', '2019-04-30 07:15:16', 'AUTHENTICATION_SUCCESS'),
(85, 'admin', '2019-04-30 07:15:23', 'AUTHENTICATION_SUCCESS'),
(86, 'admin', '2019-04-30 07:15:23', 'AUTHENTICATION_SUCCESS'),
(87, 'admin', '2019-04-30 07:15:23', 'AUTHENTICATION_SUCCESS'),
(88, 'admin', '2019-04-30 07:15:23', 'AUTHENTICATION_SUCCESS'),
(89, 'admin', '2019-04-30 07:15:25', 'AUTHENTICATION_SUCCESS'),
(90, 'admin', '2019-04-30 07:15:25', 'AUTHENTICATION_SUCCESS'),
(91, 'admin', '2019-04-30 07:25:11', 'AUTHENTICATION_SUCCESS'),
(92, 'admin', '2019-04-30 07:25:11', 'AUTHENTICATION_SUCCESS'),
(93, 'admin', '2019-04-30 07:25:11', 'AUTHENTICATION_SUCCESS'),
(94, 'admin', '2019-04-30 07:25:12', 'AUTHENTICATION_SUCCESS'),
(95, 'admin', '2019-04-30 07:25:12', 'AUTHENTICATION_SUCCESS'),
(96, 'admin', '2019-04-30 07:25:12', 'AUTHENTICATION_SUCCESS'),
(97, 'admin', '2019-04-30 07:26:13', 'AUTHENTICATION_SUCCESS'),
(98, 'admin', '2019-04-30 07:26:13', 'AUTHENTICATION_SUCCESS'),
(99, 'admin', '2019-04-30 07:26:13', 'AUTHENTICATION_SUCCESS'),
(100, 'admin', '2019-04-30 09:08:08', 'AUTHENTICATION_SUCCESS'),
(101, 'admin', '2019-04-30 09:08:08', 'AUTHENTICATION_SUCCESS'),
(102, 'admin', '2019-04-30 09:08:08', 'AUTHENTICATION_SUCCESS'),
(103, 'admin', '2019-04-30 09:08:08', 'AUTHENTICATION_SUCCESS'),
(104, 'admin', '2019-04-30 09:08:08', 'AUTHENTICATION_SUCCESS'),
(105, 'admin', '2019-04-30 09:09:12', 'AUTHENTICATION_SUCCESS'),
(106, 'admin', '2019-04-30 09:09:12', 'AUTHENTICATION_SUCCESS'),
(107, 'admin', '2019-04-30 09:09:12', 'AUTHENTICATION_SUCCESS'),
(108, 'admin', '2019-04-30 09:09:12', 'AUTHENTICATION_SUCCESS'),
(109, 'admin', '2019-04-30 09:09:18', 'AUTHENTICATION_SUCCESS'),
(110, 'admin', '2019-04-30 09:09:18', 'AUTHENTICATION_SUCCESS'),
(111, 'admin', '2019-04-30 09:09:18', 'AUTHENTICATION_SUCCESS'),
(112, 'admin', '2019-04-30 09:09:18', 'AUTHENTICATION_SUCCESS'),
(113, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(114, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(115, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(116, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(117, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(118, 'admin', '2019-04-30 12:23:43', 'AUTHENTICATION_SUCCESS'),
(119, 'admin', '2019-04-30 12:23:45', 'AUTHENTICATION_SUCCESS'),
(120, 'admin', '2019-04-30 12:23:45', 'AUTHENTICATION_SUCCESS'),
(121, 'admin', '2019-04-30 12:23:45', 'AUTHENTICATION_SUCCESS'),
(122, 'admin', '2019-04-30 12:23:45', 'AUTHENTICATION_SUCCESS'),
(123, 'admin', '2019-04-30 12:23:45', 'AUTHENTICATION_SUCCESS'),
(124, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(125, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(126, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(127, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(128, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(129, 'admin', '2019-04-30 12:23:46', 'AUTHENTICATION_SUCCESS'),
(130, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(131, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(132, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(133, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(134, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(135, 'admin', '2019-05-06 05:36:37', 'AUTHENTICATION_SUCCESS'),
(136, 'admin', '2019-05-06 05:36:38', 'AUTHENTICATION_SUCCESS');

-- --------------------------------------------------------

--
-- Structure de la table `jhi_persistent_audit_evt_data`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(150) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_persistent_audit_evt_data`
--

INSERT INTO `jhi_persistent_audit_evt_data` (`event_id`, `name`, `value`) VALUES
(61, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(66, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(64, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(63, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(65, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(62, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(64, 'message', 'Bad credentials'),
(61, 'message', 'Bad credentials'),
(66, 'message', 'Bad credentials'),
(62, 'message', 'Bad credentials'),
(65, 'message', 'Bad credentials'),
(63, 'message', 'Bad credentials'),
(68, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(67, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(68, 'message', 'Bad credentials'),
(67, 'message', 'Bad credentials'),
(69, 'type', 'org.springframework.security.authentication.BadCredentialsException'),
(69, 'message', 'Bad credentials');

-- --------------------------------------------------------

--
-- Structure de la table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
CREATE TABLE IF NOT EXISTS `jhi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(6) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_user`
--

INSERT INTO `jhi_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `image_url`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '', b'1', 'fr', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(2, 'anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'User', 'anonymous@localhost', '', b'1', 'fr', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', b'1', 'fr', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(4, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', b'1', 'fr', NULL, NULL, 'system', NULL, NULL, 'system', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
CREATE TABLE IF NOT EXISTS `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jhi_user_authority`
--

INSERT INTO `jhi_user_authority` (`user_id`, `authority_name`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(3, 'ROLE_ADMIN'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Structure de la table `projects`
--

DROP TABLE IF EXISTS `projects`;
CREATE TABLE IF NOT EXISTS `projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name_project` varchar(255) NOT NULL,
  `project_code` varchar(255) NOT NULL,
  `client` varchar(255) DEFAULT NULL,
  `d_p` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `projects`
--

INSERT INTO `projects` (`id`, `name_project`, `project_code`, `client`, `d_p`, `description`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'Projet 1', 'Pro1', 'ZZ', 'ZZ', NULL, 'admin', '2019-04-18 08:03:02', 'admin', '2019-04-18 08:03:16');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
