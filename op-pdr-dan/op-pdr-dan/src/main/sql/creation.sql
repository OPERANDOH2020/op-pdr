--CREATE DATABASE  IF NOT EXISTS `dan` /*!40100 DEFAULT CHARACTER SET latin1 */;
--USE `dan`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: snf-706921.vm.okeanos.grnet.gr    Database: dan
-- ------------------------------------------------------
-- Server version	5.5.52-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `METADATAFIELDREGISTRY`
--

DROP TABLE IF EXISTS `METADATAFIELDREGISTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `METADATAFIELDREGISTRY` (
  `id` int(11) NOT NULL,
  `element` varchar(255) NOT NULL,
  `label` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `METADATAFIELDREGISTRY`
--

LOCK TABLES `METADATAFIELDREGISTRY` WRITE;
/*!40000 ALTER TABLE `METADATAFIELDREGISTRY` DISABLE KEYS */;
INSERT INTO `METADATAFIELDREGISTRY` VALUES (0,'personalInfo.patient.dateOfBirth','Patient / Date of Birth\n','This field may contain only the year if\r day and month are not available'),(1,'personalInfo.patient.name','Patient / Name\n','The Name of the patient '),(2,'personalInfo.patient.surname','Patient / Surname\n',''),(4,'personalInfo.patient.placeOfBirth','Patient / Place of Birth\n',''),(5,'personalInfo.anthropometric.weight','Anthropometric Data / Weight (Kg)\n',''),(6,'personalInfo.anthropometric.age','Anthropometric Data / Age (calculated automat',''),(7,'personalInfo.anthropometric.height','Anthropometric Data / Height (cm)\n',''),(8,'personalInfo.anthropometric.bodyCircumferences','Anthropometric Data / Body circumferences (cm',''),(9,'contactInfo.patient.cityOfResidence','Patient / City of Residence',''),(10,'contactInfo.patient.cellPhoneNumber','Patient / Cell Phone Number',''),(11,'contactInfo.patient.email','Patient / email',''),(12,'contactInfo.patientLP.typeOfLegal','Patient LP / Type of Legal','Legal guardian or Contact person'),(13,'contactInfo.patientLP.legalName','Patient LP / Legal Name','The Name of the Contact Person/guardian'),(14,'contactInfo.patientLP.legalSurname','Patient LP / Legal Surname','This field can contain more than one element'),(15,'contactInfo.patientLP.legaldateOfBirth','Patient LP / Legal date of birth',''),(16,'contactInfo.patientLP.legalplaceOfBirth','Patient LP / Legal place of birth',''),(17,'contactInfo.patientLP.legalprovinceOfBirth','Patient LP / Legal province of birth',''),(18,'contactInfo.patientLP.legalresidenceCity','Patient LP / Legal residence - city',''),(19,'contactInfo.patientLP.legalresidenceStreet','Patient LP / Legal residence - street',''),(20,'contactInfo.patientLP.legalresidenceStreetNumb','Patient LP / Legal residence - street numb',''),(21,'contactInfo.patientLP.legalresidencePostCode','Patient LP / Legal residence - post code',''),(22,'contactInfo.patientLP.minorName','Patient LP / Minor Name',''),(23,'contactInfo.patientLP.minorSurname','Patient LP / Minor Surname',''),(24,'contactInfo.patientLP.minorDateOfBirth','Patient LP / Minor Date of Birth',''),(25,'contactInfo.patientLP.minorPlaceOfBirth','Patient LP / Minor Place of Birth',''),(26,'contactInfo.patientLP.minorProvinceBirth','Patient LP / Minor Province Birth',''),(27,'contactInfo.doctor.name','Doctor / Name',''),(28,'contactInfo.doctor.surname','Doctor / Surname',''),(29,'contactInfo.patientLP.legalCellPhoneNumber','Patient LP / Legal cell phone number',''),(30,'contactInfo.patientLP.legalEmailAddress','Patient LP / Legal e-mail address',''),(31,'alerts.clinicalData.Allergies','Clinical Data / Allergies','Description of the clinical manifestation of '),(32,'alerts.clinicalData.Intolerance','Clinical Data / Intolerance',''),(33,'hitstory.clinicalData.PastOrCurrentAssumptionOfDrugs','Clinical Data / Past or current assumption of','Describes the reason by which the problem cha'),(34,'hitstory.clinicalData.Pathologies','Clinical Data / Pathologies',''),(35,'medicalProblems.clinicalData.Pathologies','Clinical Data / Pathologies','Problems/diagnosis that fit under these condi'),(36,'medicalProblems.treatmentRecommendations','Medical Problems / Treatment Recommendations','Therapeutic recommendations that do not inclu'),(37,'medicationSummary.PastOrCurrentAssumptionOfDrugs','Clinical Data / Past or current assumption of','Substance that alone or in combination with o'),(38,'socialHistory.alcoholUse','Patient / Alcohol Use',''),(39,'socialHistory.tobaccoUse','Patient / Tobacco use',''),(40,'socialHistory.drugUse','Patient / Drug Use',''),(41,'physicalFindings.bloodExams','Clinical Data / Blood Exams','One value of blood pressure which includes: s');
/*!40000 ALTER TABLE `METADATAFIELDREGISTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `METADATAVALUES`
--

DROP TABLE IF EXISTS `METADATAVALUES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `METADATAVALUES` (
  `id` int(11) NOT NULL,
  `metadatafield_id` int(11) DEFAULT NULL,
  `text_value` varchar(255) DEFAULT NULL,
  `lang` varchar(45) DEFAULT '*',
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_fkey_idx` (`user_id`),
  KEY `metadata_field_fkey_idx` (`metadatafield_id`),
  CONSTRAINT `metadata_field_fkey` FOREIGN KEY (`metadatafield_id`) REFERENCES `METADATAFIELDREGISTRY` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_fkey` FOREIGN KEY (`user_id`) REFERENCES `USER` (`iduser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `METADATAVALUES`
--

LOCK TABLES `METADATAVALUES` WRITE;
/*!40000 ALTER TABLE `METADATAVALUES` DISABLE KEYS */;
/*!40000 ALTER TABLE `METADATAVALUES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QUERY`
--

DROP TABLE IF EXISTS `QUERY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QUERY` (
  `idquery` int(11) NOT NULL,
  `query` varchar(255) NOT NULL,
  PRIMARY KEY (`idquery`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QUERY`
--

LOCK TABLES `QUERY` WRITE;
/*!40000 ALTER TABLE `QUERY` DISABLE KEYS */;
/*!40000 ALTER TABLE `QUERY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (1,'Test','Tost'),(2,'Stamatis','Glykas');
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-14 12:55:32
