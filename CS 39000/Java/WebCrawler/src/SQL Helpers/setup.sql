CREATE TABLE CRAWLER.URL
(
  URLID       INT PRIMARY KEY AUTO_INCREMENT,
  URL         VARCHAR(500) NOT NULL,
  Description VARCHAR(500)
);

CREATE TABLE CRAWLER.Word (
  Word  VARCHAR(500) PRIMARY KEY NOT NULL,
  URLID INT                      NOT NULL,
  CONSTRAINT `Word__URL.URLID_fk` FOREIGN KEY (URLID) REFERENCES URL (URLID)
);
