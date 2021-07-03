CREATE TABLE user (
  user_id bigint NOT NULL AUTO_INCREMENT,
  user_name varchar(50) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  email_address varchar(50) NOT NULL,
  role varchar(50) NOT NULL,
  ssn varchar(50) NOT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY UK_USER_SSN (ssn),
  UNIQUE KEY UK_USER_USERNAME (user_name)
)