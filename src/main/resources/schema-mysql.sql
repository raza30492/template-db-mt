
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `db_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `account_expired` bit(1) DEFAULT NULL,
  `account_locked` bit(1) DEFAULT NULL,
  `credential_expired` bit(1) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_sent_at` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `retry_count` int(11) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `IDX7516795akd6qg7e0i8e5rv58s` (`name`,`email`,`username`),
  KEY `FKbwv4uspmyi7xqjwcrgxow361t` (`company_id`),
  CONSTRAINT `FKbwv4uspmyi7xqjwcrgxow361t` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjuqcxi07jmwxksjjokolg6jc7` (`company_id`),
  CONSTRAINT `FKjuqcxi07jmwxksjjokolg6jc7` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `url_interceptor`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `url_interceptor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access` varchar(255) DEFAULT NULL,
  `http_method` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


------------------------------------------------

CREATE TABLE IF NOT EXISTS oauth_client_details (
   client_id VARCHAR(256) PRIMARY KEY,
   resource_ids VARCHAR(256),
   client_secret VARCHAR(256),
   scope VARCHAR(256),
   authorized_grant_types VARCHAR(256),
   web_server_redirect_uri VARCHAR(256),
   authorities VARCHAR(256),
   access_token_validity INTEGER,
   refresh_token_validity INTEGER,
   additional_information VARCHAR(4096)
 );
 
 CREATE TABLE IF NOT EXISTS oauth_client_token (
   token_id VARCHAR(256),
   token BLOB,
   authentication_id VARCHAR(256),
   user_name VARCHAR(256),
   client_id VARCHAR(256)
 );
 
 CREATE TABLE IF NOT EXISTS oauth_access_token (
   token_id VARCHAR(256),
   token BLOB,
   authentication_id VARCHAR(256),
   user_name VARCHAR(256),
   client_id VARCHAR(256),
   authentication BLOB,
   refresh_token VARCHAR(256)
 );
 
 CREATE TABLE IF NOT EXISTS oauth_refresh_token (
   token_id VARCHAR(256),
   token BLOB,
   authentication BLOB
 );
 
 CREATE TABLE IF NOT EXISTS oauth_code (
   code VARCHAR(256), authentication BLOB
 );
 
 -- customized oauth_client_details table
 CREATE TABLE IF NOT EXISTS client_details (
   appId VARCHAR(256) PRIMARY KEY,
   resourceIds VARCHAR(256),
   appSecret VARCHAR(256),
   scope VARCHAR(256),
   grantTypes VARCHAR(256),
   redirectUrl VARCHAR(256),
   authorities VARCHAR(256),
   access_token_validity INTEGER,
   refresh_token_validity INTEGER,
   additionalInformation VARCHAR(4096)
 );