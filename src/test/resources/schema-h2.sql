-- 客户表
CREATE TABLE IF NOT EXISTS customer (
  customer_id VARCHAR2(20) PRIMARY KEY ,
  company_name VARCHAR2(30),
  contact_name VARCHAR2(50),
  address VARCHAR2(100),
  city VARCHAR2(20),
  postal_code INTEGER,
  country VARCHAR2(10)
);

DELETE FROM customer;

INSERT INTO customer VALUES ('APPLE','Apple Computer, Inc.','Steven Jobs','1 Infinite Loop Cupertino, CA 95014','Cupertino',	95014,'USA');
INSERT INTO customer VALUES ('BAIDU','	BAIDU, Inc','李彦宏','北京市北四环西路58号理想国际大厦','北京',	100080,'China');
INSERT INTO customer VALUES ('Canon','Canon USA, Inc.','Tsuneji Uchida','One Canon Plaza Lake Success, NY 11042','New York',	11042,'USA');
INSERT INTO customer VALUES ('Nokia','	Nokia Corporation','Olli-Pekka Kallasvuo','P.O. Box 226, FIN-00045 Nokia Group, Finland','Helsinki',	NULL ,'Finland');

CREATE TABLE IF NOT EXISTS cars(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR2(20),
  descrption VARCHAR2(30)
);

DELETE FROM cars;

INSERT INTO cars(name, descrption) VALUES ('兰博基尼egoista','兰博基尼概念车系列');
INSERT INTO cars(name, descrption) VALUES ('自由光','首款9速自动变速箱');
INSERT INTO cars(name, descrption) VALUES ('北京现代ix25','小型SUV汽车');