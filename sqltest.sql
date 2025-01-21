USE wcs;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ---------------------------
-- Table structure for agv
-- ---------------------------
DROP TABLE IF EXISTS `agv`;
CREATE TABLE `agv` (
	HANDLE VARCHAR(412) NOT NULL,
	AGV_NO VARCHAR(100),
	DESCRIPTION VARCHAR(100),
	MODE VARCHAR(50),	
	POSITION VARCHAR(100),
	TASK_ID VARCHAR(412),
	TASK_TYPE VARCHAR(50),
	TASK_STARTTIME VARCHAR(100),
	CAPACITY VARCHAR(50),
	STATUS VARCHAR(100),
	WORKING_TIME VARCHAR(100),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ---------------------------------
-- Table structure for agv_alarm
-- ---------------------------------
DROP TABLE IF EXISTS `agv_alarm`;
CREATE TABLE `agv_alarm`(
	HANDLE VARCHAR(412) NOT NULL,
	AGV_NO VARCHAR(100),
	SEVERITY VARCHAR(10),
	ALARM_CODE VARCHAR(50),
	DESCRIPTION VARCHAR(200),
	STATUS VARCHAR(100),
	OCCUR_TIME TIMESTAMP(3),
	RELEASE_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- --------------------------------
-- Table structure for agv_task
-- --------------------------------
DROP TABLE IF EXISTS `agv_task`;
CREATE TABLE `agv_task`(
	HANDLE VARCHAR(412) NOT NULL,
	CATEGORY VARCHAR(50),
	AGV_NO VARCHAR(100),
	CARRIER VARCHAR(100),
	START_POSITION VARCHAR(100),
	TARGET_POSITION VARCHAR(100),
	STATUS VARCHAR(100),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for carrier_rfid
-- ------------------------------------
DROP TABLE IF EXISTS `carrier_rfid`;
CREATE TABLE `carrier_rfid`(
	HANDLE VARCHAR(412) NOT NULL,
	RFID VARCHAR(100),
	CARRIER VARCHAR(100),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for carrier_task
-- ------------------------------------
DROP TABLE IF EXISTS `carrier_task`;
CREATE TABLE `carrier_task`(
	HANDLE VARCHAR(412) NOT NULL,
	CATEGORY VARCHAR(10),
	CARRIER VARCHAR(100),
	START_POSITION VARCHAR(100),
	TARGET_POSITION VARCHAR(100),
	STATUS VARCHAR(50),
	AGV_NO VARCHAR(100),
	VMS_ID VARCHAR(412),
	WMS_ID VARCHAR(412),
	COMMENTS VARCHAR(300),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for monitoring_task
-- ------------------------------------
DROP TABLE IF EXISTS `monitoring_task`;
CREATE TABLE `monitoring_task`(
	HANDLE VARCHAR(412) NOT NULL,
	RESOURCE VARCHAR(100),
	STORAGE_BIN VARCHAR(100),
	TYPE VARCHAR(50),
	STATUS VARCHAR(50),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ---------------------------------------
-- Table structure for receive_station
-- ---------------------------------------
DROP TABLE IF EXISTS `receive_station`;
CREATE TABLE `receive_station`(
	HANDLE VARCHAR(412) NOT NULL,
	RECEIVE_STATION VARCHAR(100),
	DESCRIPTION VARCHAR(200),
	STATUS VARCHAR(50),
	TASK_GOAL VARCHAR(100),
	NOW_TASK VARCHAR(100),
	STATE VARCHAR(10),
	CV1_DIRECTION VARCHAR(10),
	CV1_HOLD VARCHAR(10),
	CV1_MOVE VARCHAR(10),
	CV1_PALLET_SENSOR VARCHAR(10),
	CV2_DIRECTION VARCHAR(10),
	CV2_HOLD VARCHAR(10),
	CV2_MOVE VARCHAR(10),
	CV2_PALLET_SENSOR VARCHAR(10),
	CV3_DIRECTION VARCHAR(10),
	CV3_HOLD VARCHAR(10),
	CV3_MOVE VARCHAR(10),
	CV3_PALLET_SENSOR VARCHAR(10),
	BUTTON VARCHAR(10),
	HEIGHT VARCHAR(50),
	WIDTH VARCHAR(50),
	LENGTH VARCHAR(50),
	WEIGHT VARCHAR(100),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;
-- ---------------------------------------
-- Table structure for Receive_Station_Task
-- ---------------------------------------
DROP TABLE IF EXISTS `Receive_Station_Task`;
CREATE TABLE `Receive_Station_Task`(
	HANDLE VARCHAR(412) NOT NULL,
	MESSAGE_ID VARCHAR(412),
	RECEIVE_STATION VARCHAR(100),
	START_STATION VARCHAR(50),
	END_STATION VARCHAR(100),
	PALLET VARCHAR(100),
	TYPE VARCHAR(100),
	PALLET_INFO_CHECK VARCHAR(5),
	STATUS VARCHAR(100),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- --------------------------------------------
-- Table structure for receive_station_bind
-- --------------------------------------------
DROP TABLE IF EXISTS `receive_station_bind`;
CREATE TABLE `receive_station_bind`(
	HANDLE VARCHAR(412) NOT NULL,
	RECEIVE_STATION VARCHAR(100),
	STATION VARCHAR(10),
	CARRIER VARCHAR(100),
	STATUS VARCHAR(50),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------
-- Table structure for resrce
-- ------------------------------
DROP TABLE IF EXISTS `resrce`;
CREATE TABLE `resrce`(
	HANDLE VARCHAR(412) NOT NULL,
	RESRCE VARCHAR(100),
	DESCRIPTION VARCHAR(200),
	STATUS VARCHAR(50),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- -----------------------------------
-- Table structure for storage_bin
-- -----------------------------------
DROP TABLE IF EXISTS `storage_bin`;
CREATE TABLE `storage_bin`(
	HANDLE VARCHAR(412) NOT NULL,
	STORAGE_BIN VARCHAR(100),
	DESCRIPTION VARCHAR(200),
	STORAGE_BIN_TYPE_BO VARCHAR(412),
	STORAGE_LOCATION_BO VARCHAR(412),
	SHELF_ROW INT,
	SHELF_COLUMN INT,
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ----------------------------------------
-- Table structure for storage_bin_type
-- ----------------------------------------
DROP TABLE IF EXISTS `storage_bin_type`;
CREATE TABLE `storage_bin_type`(
	HANDLE VARCHAR(412) NOT NULL,
	STORAGE_BIN_TYPE VARCHAR(100),
	DESCRIPTION VARCHAR(200),
	WIDTH DECIMAL(38,6),
	LENGHT DECIMAL(38,6),
	HEIGHT DECIMAL(38,6),
	MAX_WEIGHT DECIMAL(38,6),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ----------------------------------------
-- Table structure for storage_location
-- ----------------------------------------
DROP TABLE IF EXISTS `storage_location`;
CREATE TABLE `storage_location`(
	HANDLE VARCHAR(412) NOT NULL,
	STORAGE_LOCATION VARCHAR(100),
	DESCRIPTION VARCHAR(200),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- -------------------------------
-- Table structure for RFID_Reader
-- -------------------------------
DROP TABLE IF EXISTS `RFID_Reader`;
CREATE TABLE `RFID_Reader`(
  HANDLE VARCHAR(412) NOT NULL,
  READER_ID VARCHAR(100),
  RECEIVE_STATION VARCHAR(100),
  STATION VARCHAR(10),
  STATUS VARCHAR(100),
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  UPDATE_USER VARCHAR(30),
  UPDATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for RFID_Reader_Mask
-- ------------------------------------
DROP TABLE IF EXISTS `RFID_Reader_Mask`;
CREATE TABLE `RFID_Reader_Mask`(
  HANDLE VARCHAR(412) NOT NULL,  
  VOUCHER_NO VARCHAR(100),
  RFID VARCHAR(100),
  STATUS VARCHAR(100),
  PALLET VARCHAR(100),
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  UPDATE_USER VARCHAR(30),
  UPDATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for RFID_Reader_Task
-- ------------------------------------
DROP TABLE IF EXISTS `RFID_Reader_Task`;
CREATE TABLE `RFID_Reader_Task`(
  HANDLE varchar(412) NOT NULL,
  VOUCHER_NO VARCHAR(100),
  MESSAGE_ID VARCHAR(412),
  READER_ID VARCHAR(100),
  STATUS VARCHAR(100),
  TYPE VARCHAR(100),
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  UPDATE_USER VARCHAR(30),
  UPDATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- -------------------------------
-- Table structure for Robotic_Arm
-- -------------------------------
DROP TABLE IF EXISTS `Robotic_Arm`;
CREATE TABLE `Robotic_Arm`(
  HANDLE VARCHAR(412) NOT NULL,
  ROBOTIC_ARM VARCHAR(100),
  SERVICE_RESOURCE VARCHAR(100),
  STATUS VARCHAR(100),
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  UPDATE_USER VARCHAR(30),
  UPDATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- -------------------------------------
-- Table structure for Robotic_Arm_Alarm
-- -------------------------------------
DROP TABLE IF EXISTS `Robotic_Arm_Alarm`;
CREATE TABLE `Robotic_Arm_Alarm`(
	HANDLE VARCHAR(412) NOT NULL,
	ROBOTIC_ARM VARCHAR(100),
	SEVERITY VARCHAR(10),
	ALARM_CODE VARCHAR(50),
	DESCRIPTION VARCHAR(200),
	OCCUR_TIME TIMESTAMP(3),
	RELEASE_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for Robotic_Arm_Task
-- ------------------------------------
DROP TABLE IF EXISTS `Robotic_Arm_Task`;
CREATE TABLE `Robotic_Arm_Task`(
  HANDLE VARCHAR(412) NOT NULL,
  VOUCHER_NO VARCHAR(100)   DEFAULT NULL COMMENT '憑單號',
  WO_SERIAL VARCHAR(412)  DEFAULT NULL COMMENT '訂單號',
  MESSAGE_ID VARCHAR(412)  DEFAULT NULL COMMENT '任務的MQID',
  WO_QTY VARCHAR(100)  DEFAULT NULL COMMENT '貨物要求數量',
  DO_QTY VARCHAR(100)  DEFAULT NULL COMMENT '此次貨物要求數量',
  FROM_PALLET_QTY VARCHAR(100)  DEFAULT NULL COMMENT '來料棧板數量(出庫用)',
  TO_PALLET_QTY VARCHAR(100)  DEFAULT NULL COMMENT '機械手臂放置物料的棧板，目前的數量',
  ROBOTIC_ARM VARCHAR(100)  DEFAULT NULL COMMENT '使用機械手臂',
  RESOURCE VARCHAR(100)  DEFAULT NULL COMMENT '使用輸送帶',
  TYPE VARCHAR(100)  DEFAULT NULL COMMENT '出入庫類型',
  CARRIER VARCHAR(100)  DEFAULT NULL COMMENT '出庫使用-棧板ID',
  STORAGE_BIN VARCHAR(100)  DEFAULT NULL COMMENT '出庫使用-庫位ID',
  STATUS VARCHAR(50)  DEFAULT NULL COMMENT '任務狀態',
  RESULT VARCHAR(100)  DEFAULT NULL COMMENT '任務結果',
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  UPDATE_USER VARCHAR(30),
  UPDATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- ------------------------------------
-- Table structure for PLC_Data
-- ------------------------------------
DROP TABLE IF EXISTS `PLC_Data`;
CREATE TABLE `PLC_Data`(
  HANDLE VARCHAR(412) NOT NULL,
  PLC_ID VARCHAR(100),
  DATA VARCHAR(2000),
  CREATE_USER VARCHAR(30),
  CREATED_TIME TIMESTAMP(3),
  CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;

-- --------------------------------
-- Table structure for Button_Task
-- --------------------------------
DROP TABLE IF EXISTS `Button_Task`;
CREATE TABLE `Button_Task`(
	HANDLE VARCHAR(412) NOT NULL,
	RECEIVE_STATION VARCHAR(100),
	TYPE VARCHAR(50),
	STATUS VARCHAR(50),
	CREATE_USER VARCHAR(30),
	CREATED_TIME TIMESTAMP(3),
	UPDATE_USER VARCHAR(30),
	UPDATED_TIME TIMESTAMP(3),
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ;
-- Data-------------------------------------------------------------------------------------------------

INSERT INTO agv (HANDLE, AGV_NO, DESCRIPTION, MODE, POSITION, TASK_ID, TASK_TYPE, TASK_STARTTIME, CAPACITY, STATUS, WORKING_TIME, CREATE_USER, CREATED_TIME, UPDATE_USER, UPDATED_TIME) VALUES
 ('AGVBO:01','AGV01','AGV01',NULL,NULL,NULL,NULL,NULL,0,'IDLE',NULL,'Administrator','2021-05-25 20:20:20.0','Administrator','2021-05-25 20:20:20.0')
,('AGVBO:02','AGV02','AGV02',NULL,NULL,NULL,NULL,NULL,0,'IDLE',NULL,'Administrator','2021-05-25 20:20:20.0','Administrator','2021-05-25 20:20:20.0')
,('AGVBO:03','AGV03','AGV03',NULL,NULL,NULL,NULL,NULL,0,'IDLE',NULL,'Administrator','2021-05-25 20:20:20.0','Administrator','2021-05-25 20:20:20.0');

INSERT INTO carrier_rfid (HANDLE,RFID,CARRIER,CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES 
 ('CarrierBO:ASRS_PALLET_00001','0000 0001 0002 0003 0001','ASRS_PALLET_00001','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('CarrierBO:ASRS_PALLET_00002','0000 0001 0002 0003 0002','ASRS_PALLET_00002','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('CarrierBO:ASRS_PALLET_00003','0000 0001 0002 0003 0003','ASRS_PALLET_00003','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('CarrierBO:ASRS_PALLET_00004','0000 0001 0002 0003 0004','ASRS_PALLET_00004','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('CarrierBO:ASRS_PALLET_00005','0000 0001 0002 0003 0005','ASRS_PALLET_00005','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
;

INSERT INTO receive_station (HANDLE,RECEIVE_STATION,DESCRIPTION,STATUS,TASK_GOAL, NOW_TASK, STATE, CV1_DIRECTION, CV1_HOLD, CV1_MOVE, CV1_PALLET_SENSOR, CV2_DIRECTION, CV2_HOLD, CV2_MOVE, CV2_PALLET_SENSOR, CV3_DIRECTION, CV3_HOLD, CV3_MOVE, CV3_PALLET_SENSOR, BUTTON, HEIGHT, WIDTH, LENGTH, WEIGHT, CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES 
 ('ReceiveStationBO:01','Conveyor1','Conveyor1','IDLE','', "","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('ReceiveStationBO:02','Conveyor2','Conveyor2','IDLE','', "","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('ReceiveStationBO:03','Conveyor3','Conveyor3','IDLE','', "","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('ReceiveStationBO:04','Conveyor4','Conveyor4','IDLE','', "","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('ReceiveStationBO:05','Conveyor5','Conveyor5','IDLE','', "","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
;

INSERT INTO storage_bin_type (HANDLE,STORAGE_BIN_TYPE,DESCRIPTION,WIDTH,LENGHT,HEIGHT,MAX_WEIGHT,CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES
 ('StorageBinTypeBO:L1','L1','Layer 1', 1200, 1200, 1290, 1500,'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageBinTypeBO:L2','L2','Layer 2', 1200, 1200, 1290, 1500,'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageBinTypeBO:L3','L3','Layer 3', 1200, 1200, 1290, 1500,'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageBinTypeBO:L4','L4','Layer 4', 1200, 1200, 790, 1500,'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageBinTypeBO:L5','L5','Layer 5', 1200, 1200, 790, 1500,'Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageBinTypeBO:EmptyL1', 'Empty L1', 'Empty Layer 1', 1200, 1200, 200, 100, 'Administrator', '2021-09-10 10:10:10.0', 'Administrator' ,'2021-09-10 10:10:10.0')
,('StorageBinTypeBO:EmptyL2', 'Empty L2', 'Empty Layer 2', 1200, 1200, 200, 100, 'Administrator', '2021-09-10 10:10:10.0', 'Administrator' ,'2021-09-10 10:10:10.0')
,('StorageBinTypeBO:EmptyL3', 'Empty L3', 'Empty Layer 3', 1200, 1200, 200, 100, 'Administrator', '2021-09-10 10:10:10.0', 'Administrator' ,'2021-09-10 10:10:10.0')
,('StorageBinTypeBO:EmptyL4', 'Empty L4', 'Empty Layer 4', 1200, 1200, 200, 100, 'Administrator', '2021-09-10 10:10:10.0', 'Administrator' ,'2021-09-10 10:10:10.0')
,('StorageBinTypeBO:EmptyL5', 'Empty L5', 'Empty Layer 5', 1200, 1200, 200, 100, 'Administrator', '2021-09-10 10:10:10.0', 'Administrator' ,'2021-09-10 10:10:10.0')
;

INSERT INTO storage_location (HANDLE,STORAGE_LOCATION,DESCRIPTION,CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES 
 ('StorageLocationBO:C01','C01','C01','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C02','C02','C02','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C03','C03','C03','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C04','C04','C04','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C05','C05','C05','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C06','C06','C06','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C07','C07','C07','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C08','C08','C08','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C09','C09','C09','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C10','C10','C10','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C11','C11','C11','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C12','C12','C12','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
,('StorageLocationBO:C13','C13','C13','Administrator','2021-09-10 10:10:10.0','Administrator','2021-09-10 10:10:10.0')
;

INSERT INTO RFID_Reader (HANDLE,READER_ID,RECEIVE_STATION,STATION,STATUS,CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES 
 ('RFIDReaderBO:11','ReaderL1_1','Conveyor1','CV1','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:12','ReaderL1_2','Conveyor1','CV2','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:13','ReaderL1_3','Conveyor1','CV3','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:21','ReaderL2_1','Conveyor2','CV1','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:22','ReaderL2_2','Conveyor2','CV2','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:23','ReaderL2_3','Conveyor2','CV3','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:31','ReaderL3_1','Conveyor3','CV1','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:32','ReaderL3_2','Conveyor3','CV2','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:33','ReaderL3_3','Conveyor3','CV3','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:41','ReaderL4_1','Conveyor4','CV1','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:42','ReaderL4_2','Conveyor4','CV2','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:43','ReaderL4_3','Conveyor4','CV3','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:51','ReaderL5_1','Conveyor5','CV1','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:52','ReaderL5_2','Conveyor5','CV2','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RFIDReaderBO:53','ReaderL5_3','Conveyor5','CV3','Online','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
;

INSERT INTO Robotic_Arm (HANDLE,ROBOTIC_ARM,SERVICE_RESOURCE,STATUS,CREATE_USER,CREATED_TIME,UPDATE_USER,UPDATED_TIME) VALUES 
 ('RoboticArmBO:1','RArm1','','IDLE','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RoboticArmBO:2','RArm2','','IDLE','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
,('RoboticArmBO:3','RArm3','','IDLE','Administrator','2021-01-19 10:10:10.0','Administrator','2021-01-19 10:10:10.0')
;



-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------

USE wms;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
  `role_id`     int(11) NOT NULL AUTO_INCREMENT,
  `role_name`   VARCHAR(64)  DEFAULT NULL,
  `role_code`   VARCHAR(64)  DEFAULT NULL,
  `role_desc`   VARCHAR(255) DEFAULT NULL,
  `ds_type`     char(1)      DEFAULT '2',
  `ds_scope`    VARCHAR(255) DEFAULT NULL,
  `create_time` timestamp(3) DEFAULT NULL,
  `update_time` timestamp(3) DEFAULT NULL,
  `del_flag`    char(1)      DEFAULT 'N',
  `tenant_id`   VARCHAR(10)  DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY           `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='系統角色表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
  `menu_id`     int(11) NOT NULL AUTO_INCREMENT COMMENT '選單ID',
  `name`        VARCHAR(32)  DEFAULT NULL,
  `permission`  VARCHAR(32)  DEFAULT NULL,
  `path`        VARCHAR(128) DEFAULT NULL,
  `parent_id`   int(11) DEFAULT NULL COMMENT '父選單ID',
  `icon`        VARCHAR(32)  DEFAULT NULL,
  `sort`        int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive`  char(1)      DEFAULT 'N',
  `type`        char(1)      DEFAULT '0',
  `create_time` timestamp(3) DEFAULT NULL COMMENT '創建時間',
  `update_time` timestamp(3) DEFAULT NULL COMMENT '更新時間',
  `del_flag`    char(1)      DEFAULT 'N',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10013 DEFAULT CHARSET=utf8mb4 COMMENT='選單許可權表';

-- ---------------------------------
-- Table structure for sys_role_menu
-- ---------------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '選單ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色選單表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
  `user_id`     int(11) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `username`    VARCHAR(64)  DEFAULT NULL,
  `first_name`  VARCHAR(64)  DEFAULT NULL,
  `last_name`   VARCHAR(64)  DEFAULT NULL,
  `password`    VARCHAR(255) DEFAULT NULL,
  `salt`        VARCHAR(255) DEFAULT NULL,
  `phone`       VARCHAR(20)  DEFAULT NULL,
  `avatar`      VARCHAR(255) DEFAULT NULL,
  `dept_id`     int(11) DEFAULT NULL COMMENT '部門ID',
  `create_time` timestamp(3) DEFAULT NULL COMMENT '創建時間',
  `update_time` timestamp(3) DEFAULT NULL COMMENT '修改時間',
  `lock_flag`   char(1)      DEFAULT 'N',
  `del_flag`    char(1)      DEFAULT 'N',
  `tenant_id`   VARCHAR(10)  DEFAULT NULL COMMENT '所屬租戶',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY           `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用戶表';


-- ---------------------------------
-- Table structure for sys_user_role
-- ---------------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
  `user_id` int(11) NOT NULL COMMENT '用戶ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用戶角色表';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
  `dept_id`     int(20) NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(50) DEFAULT NULL,
  `sort`        int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改時間',
  `del_flag`    char(1)     DEFAULT '0',
  `parent_id`   int(11) DEFAULT NULL,
  `tenant_id`   int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='部門管理';

-- -----------------------------
-- Table structure for WAREHOUSE
-- -----------------------------
DROP TABLE IF EXISTS WAREHOUSE;
CREATE TABLE WAREHOUSE
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  WAREHOUSE    VARCHAR(20)  NOT NULL COMMENT '倉庫代號',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '倉庫資料';

-- -----------------------------------------
-- Table structure for STORAGE_LOCATION_TYPE
-- -----------------------------------------
DROP TABLE IF EXISTS STORAGE_LOCATION_TYPE;
CREATE TABLE STORAGE_LOCATION_TYPE
(
  HANDLE                VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_LOCATION_TYPE VARCHAR(50)  NOT NULL COMMENT '存儲位置類型代號',
  DESCRIPTION           VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME           TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME           TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR               VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER               VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲位置類型資料';

-- ------------------------------------
-- Table structure for STORAGE_LOCATION
-- ------------------------------------
DROP TABLE IF EXISTS STORAGE_LOCATION;
CREATE TABLE STORAGE_LOCATION
(
  HANDLE                   VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_LOCATION         VARCHAR(50)  NOT NULL COMMENT '存儲位置代號',
  DESCRIPTION              VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STORAGE_LOCATION_TYPE_BO VARCHAR(412) NOT NULL COMMENT '關聯存儲位置類型主鍵',
  WAREHOUSE_BO              VARCHAR(412) NOT NULL COMMENT '關聯倉庫主鍵',
  CREATE_TIME              TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME              TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR                  VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER                  VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲位置資料';

-- ------------------------------------
-- Table structure for STORAGE_BIN_TYPE
-- ------------------------------------
DROP TABLE IF EXISTS STORAGE_BIN_TYPE;
CREATE TABLE STORAGE_BIN_TYPE
(
  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN_TYPE VARCHAR(50)  NOT NULL COMMENT '存儲貨格類型代號',
  DESCRIPTION      VARCHAR(200)   DEFAULT NULL COMMENT '描述',
  WIDTH            DECIMAL(38, 6) DEFAULT NULL COMMENT '寬度',
  LENGTH           DECIMAL(38, 6) DEFAULT NULL COMMENT '長度',
  HEIGHT           DECIMAL(38, 6) DEFAULT NULL COMMENT '高度',
  MAX_WEIGHT       DECIMAL(38, 6) DEFAULT NULL COMMENT '最大承重',
  MIX_ITEM         CHAR(1)        DEFAULT NULL COMMENT '可否混料',
  CREATE_TIME      TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP(3)   DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格類型資料';

-- -------------------------------
-- Table structure for STORAGE_BIN
-- -------------------------------
DROP TABLE IF EXISTS STORAGE_BIN;
CREATE TABLE STORAGE_BIN
(
  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN         VARCHAR(50)  NOT NULL COMMENT '存儲貨格代號',
  DESCRIPTION         VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STORAGE_BIN_TYPE_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯存儲貨格類型主鍵',
  STORAGE_LOCATION_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯存儲位置主鍵(一個存儲位置可放多個貨格)',
  SHELF_ROW           INT          DEFAULT NULL COMMENT '行',
  SHELF_COLUMN        INT          DEFAULT NULL COMMENT '列',
  CREATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR             VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER             VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格資料';

-- --------------------------------------
-- Table structure for STORAGE_BIN_STATUS
-- --------------------------------------
DROP TABLE IF EXISTS STORAGE_BIN_STATUS;
CREATE TABLE STORAGE_BIN_STATUS
(
  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN         VARCHAR(50)  NOT NULL COMMENT '存儲貨格代號',
  STATUS              VARCHAR(50)  NOT NULL COMMENT '存儲貨格狀態',
  CREATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格狀態';

-- -----------------------------
-- Table structure for ITEM_TYPE
-- -----------------------------
DROP TABLE IF EXISTS ITEM_TYPE;
CREATE TABLE ITEM_TYPE
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_TYPE   VARCHAR(50)  NOT NULL COMMENT '物料類型代號',
  MIX_ITEM    CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述(原材料、輔材、包材、中間品、半成品、成品、子設備、工具、備用件)',
  CREATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料類型資料';

-- ------------------------
-- Table structure for ITEM
-- ------------------------
DROP TABLE IF EXISTS ITEM;
CREATE TABLE ITEM
(
  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM                VARCHAR(50)  NOT NULL COMMENT '物料代號',
  DESCRIPTION         VARCHAR(200) DEFAULT NULL COMMENT '描述',
  MIX_ITEM            CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  INSPECTION_REQUIRED CHAR(1)      DEFAULT NULL COMMENT '是否必須檢驗（需要檢驗的物料創建庫存必須為限制庫存）',
  ITEM_STATUS         VARCHAR(50)  NOT NULL COMMENT '狀態',
  ITEM_TYPE_BO        VARCHAR(412) NOT NULL COMMENT '關聯物料類型/使用包裝類型主鍵',
  CONSUMPTION_MODE    VARCHAR(50)  DEFAULT NULL COMMENT '耗用模式(【列舉】先進先出、近效先出、後進先出)',
  UNIT_OF_MEASURE     VARCHAR(50)  DEFAULT NULL COMMENT '單位',
  CREATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR             VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER             VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料資料';

-- ------------------------------
-- Table structure for ITEM_GROUP
-- ------------------------------
DROP TABLE IF EXISTS ITEM_GROUP;
CREATE TABLE ITEM_GROUP
(
  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_GROUP       VARCHAR(20)  NOT NULL COMMENT '物料組代號',
  DESCRIPTION      VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CONSUMPTION_MODE VARCHAR(50)  DEFAULT NULL COMMENT '耗用模式',
  CREATE_TIME      TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料組資料';

-- -------------------------------------
-- Table structure for ITEM_GROUP_MEMBER
-- -------------------------------------
DROP TABLE IF EXISTS ITEM_GROUP_MEMBER;
CREATE TABLE ITEM_GROUP_MEMBER
(
  HANDLE        VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_GROUP_BO VARCHAR(412) NOT NULL COMMENT '關聯物料組主鍵',
  ITEM_BO       VARCHAR(412) NOT NULL COMMENT '關聯物料主鍵',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料組&物料關聯關係';

-- ------------------------------------
-- Table structure for CONSUMPTION_MODE
-- ------------------------------------
DROP TABLE IF EXISTS CONSUMPTION_MODE;
CREATE TABLE CONSUMPTION_MODE
(
  CONSUMPTION_MODE VARCHAR(50) NOT NULL COMMENT '耗用模式',
  DESCRIPTION      VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME      TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (CONSUMPTION_MODE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料耗用模式資料';

-- --------------------------------
-- Table structure for MEASURE_UNIT
-- --------------------------------
DROP TABLE IF EXISTS MEASURE_UNIT;
CREATE TABLE MEASURE_UNIT
(
  MEASURE_UNIT VARCHAR(50) NOT NULL COMMENT '計量單位',
  DESCRIPTION  VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME  TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME  TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR      VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER      VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (MEASURE_UNIT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '計量單位資料';

-- ---------------------------------------
-- Table structure for MEASURE_UNIT_MATRIX
-- ---------------------------------------
DROP TABLE IF EXISTS MEASURE_UNIT_MATRIX;
CREATE TABLE MEASURE_UNIT_MATRIX
(
  HANDLE            VARCHAR(412) NOT NULL COMMENT '主鍵',
  MEASURE_UNIT      VARCHAR(50)    DEFAULT NULL COMMENT '原始單位',
  DEST_MEASURE_UNIT VARCHAR(50)    DEFAULT NULL COMMENT '目標單位',
  TRANSFER_RATE     DECIMAL(38, 6) DEFAULT NULL COMMENT '轉換比率',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '單位轉換矩陣';

-- -----------------------------
-- Table structure for INVENTORY
-- -----------------------------
DROP TABLE IF EXISTS INVENTORY;
CREATE TABLE INVENTORY
(
  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',
  STATUS          VARCHAR(50)    DEFAULT NULL COMMENT '狀態',
  ITEM_BO         VARCHAR(412)   DEFAULT NULL COMMENT '關聯物料主鍵',
  UNIT_OF_MEASURE VARCHAR(50)    DEFAULT NULL COMMENT '單位（物料單位）',
  BATCH_NO        VARCHAR(100)    DEFAULT NULL COMMENT '批次號',
  VENDOR_BATCH_NO VARCHAR(100)    DEFAULT NULL COMMENT '供應商批次號',
  ORIGINAL_QTY    DECIMAL(38, 6) DEFAULT NULL COMMENT '原始批次數量',
  QTY_ON_HAND     DECIMAL(38, 6) DEFAULT NULL COMMENT '現有數量',
  VALID           CHAR(1)        DEFAULT NULL  COMMENT '是否有效庫存(一旦入貨格之後即視為有效庫存，不可刪除)',
  PRODUCTION_DATE DATE           DEFAULT NULL COMMENT '生產日期',
  EXPIRE_DATE     DATE           DEFAULT NULL COMMENT '失效日期',
  CREATE_TIME     TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME     TIMESTAMP(3)   DEFAULT NULL COMMENT '更新日期',
  CREATOR         VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER         VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '庫存（物料批次）';

-- ---------------------------------
-- Table structure for INVENTORY_LOG
-- ---------------------------------
DROP TABLE IF EXISTS INVENTORY_LOG;
CREATE TABLE INVENTORY_LOG
(
  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',
  BATCH_NO        VARCHAR(100)  NOT NULL COMMENT '批次號',
  STATUS          VARCHAR(50)    DEFAULT NULL COMMENT '狀態',
  HANDLING_ID     VARCHAR(32)    DEFAULT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  ITEM_BO         VARCHAR(412)   DEFAULT NULL COMMENT '關聯物料主鍵',
  UNIT_OF_MEASURE VARCHAR(50)    DEFAULT NULL COMMENT '批次號',
  ACTION_CODE     VARCHAR(10)    DEFAULT NULL COMMENT '變更事件代號',
  VENDOR_BATCH_NO VARCHAR(50)    DEFAULT NULL COMMENT '轉換比率',
  ORIGINAL_QTY    DECIMAL(38, 6) DEFAULT NULL COMMENT '原始批次數量',
  QTY_ON_HAND     DECIMAL(38, 6) DEFAULT NULL COMMENT '現有數量',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '庫存變更歷史';

-- --------------------------------
-- Table structure for Carrier_Type
-- --------------------------------
DROP TABLE IF EXISTS Carrier_Type;
CREATE TABLE Carrier_Type
(
  HANDLE       VARCHAR(412) NOT NULL COMMENT '主鍵',
  CARRIER_TYPE VARCHAR(100)  DEFAULT NULL COMMENT '棧板類型',
  DESCRIPTION  VARCHAR(200) DEFAULT NULL COMMENT '描述',
  MIX_ITEM     CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  CREATE_TIME  TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME  TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR      VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER      VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '棧板類型資料';

-- ---------------------------
-- Table structure for CARRIER
-- ---------------------------
DROP TABLE IF EXISTS CARRIER;
CREATE TABLE CARRIER
(
  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',
  CARRIER         VARCHAR(100)  DEFAULT NULL COMMENT '棧板',
  STATUS         VARCHAR(50)  DEFAULT NULL COMMENT '狀態',
  DESCRIPTION     VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CARRIER_TYPE_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯棧板類型主鍵',
  CREATE_TIME     TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME     TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR         VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER         VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '棧板資料';

-- ---------------------------------
-- Table structure for RESOURCE_TYPE
-- ---------------------------------
DROP TABLE IF EXISTS RESOURCE_TYPE;
CREATE TABLE RESOURCE_TYPE
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESOURCE_TYPE VARCHAR(50)  NOT NULL COMMENT '資源類型代號',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源組組資料';

-- --------------------------
-- Table structure for RESRCE
-- --------------------------
DROP TABLE IF EXISTS RESRCE;
CREATE TABLE RESRCE
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESRCE      VARCHAR(50)  NOT NULL COMMENT '資源',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STATUS      VARCHAR(50)  DEFAULT NULL COMMENT '狀態',
  CREATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源資料';

-- ------------------------------------------
-- Table structure for RESOURCE_TYPE_RESOURCE
-- ------------------------------------------
DROP TABLE IF EXISTS RESOURCE_TYPE_RESOURCE;
CREATE TABLE RESOURCE_TYPE_RESOURCE
(
  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESOURCE_TYPE_BO VARCHAR(412) NOT NULL COMMENT '關聯資源類型主鍵',
  RESOURCE_BO      VARCHAR(412) NOT NULL COMMENT '關聯資源主鍵',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源關聯資源類型';

-- --------------------------
-- Table structure for STATUS
-- --------------------------
DROP TABLE IF EXISTS STATUS;
CREATE TABLE STATUS
(
  HANDLE       VARCHAR(412) NOT NULL COMMENT '主鍵',
  STATUS       VARCHAR(412) NOT NULL COMMENT '狀態',
  STATUS_GROUP VARCHAR(64) DEFAULT NULL COMMENT '狀態組(RESRCE/ITEM/INVENTORY)',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '狀態資料';

-- ----------------------------
-- Table structure for STATUS_T
-- ----------------------------
DROP TABLE IF EXISTS STATUS_T;
CREATE TABLE STATUS_T
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  STATUS_BO   VARCHAR(412)  NOT NULL COMMENT '狀態',
  LOCALE      VARCHAR(50)  NOT NULL COMMENT '區域',
  DESCRIPTION VARCHAR(200) DEFAULT '' COMMENT '描述',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '狀態描述多國翻譯';

-- ---------------------------------
-- Table structure for HANDLING_UNIT
-- ---------------------------------
DROP TABLE IF EXISTS HANDLING_UNIT;
CREATE TABLE HANDLING_UNIT
(
  HANDLE                    VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID               VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）數量改變則重建（不包含盤點）',
  CARRIER_BO                VARCHAR(412) NOT NULL COMMENT '關聯棧板主鍵',
  STATUS                    VARCHAR(50)    DEFAULT NULL COMMENT '狀態（可用/不可用）',
  MIXED                     CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  HANDLING_UNIT_CONTEXT_GBO VARCHAR(412)   DEFAULT NULL COMMENT '棧板綁定對象主鍵(庫存批次號)',
  INVENTORY_QTY             DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME               TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME               TIMESTAMP(3)   DEFAULT NULL COMMENT '更新日期',
  CREATOR                   VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER                   VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元';

-- --------------------------------------
-- Table structure for HANDLING_UNIT_SPEC
-- --------------------------------------
DROP TABLE IF EXISTS HANDLING_UNIT_SPEC;
CREATE TABLE HANDLING_UNIT_SPEC
(
  HANDLE      VARCHAR(412)   NOT NULL COMMENT '主鍵（處理單元標識）',
  CARRIER_BO                VARCHAR(412) NOT NULL COMMENT '關聯棧板主鍵',
  WIDTH       DECIMAL(38, 6) NOT NULL COMMENT '寬度',
  HEIGHT      DECIMAL(38, 6) DEFAULT NULL COMMENT '高度',
  LENGTH      DECIMAL(38, 6) DEFAULT NULL COMMENT '長度',
  WEIGHT      DECIMAL(38, 6) DEFAULT NULL COMMENT '重量',
  CREATE_TIME TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  CREATOR     VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元規格記錄（處理單元過測量設備時記錄）';

-- ------------------------------------------
-- Table structure for HANDLING_UNIT_LOCATION
-- ------------------------------------------
DROP TABLE IF EXISTS HANDLING_UNIT_LOCATION;
CREATE TABLE HANDLING_UNIT_LOCATION
(
  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID      VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  CARRIER_BO       VARCHAR(412) NOT NULL COMMENT '關聯棧板主鍵',
  BIND_CONTEXT_GBO VARCHAR(412) DEFAULT NULL COMMENT '棧板綁定對象主鍵（庫位/資源/輸送帶）',
  BIND_TYPE        VARCHAR(20)  DEFAULT NULL COMMENT '綁定類型（庫位/資源/輸送帶）(棧板目前對應是在AGV/貨格/輸送帶上（應該只有貨格，AGV&輸送帶資訊在WCS中管控）)',
  CREATE_TIME      TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元位置綁定（記錄當前處理單元所在的貨格）';

-- -------------------------------------
-- Table structure for HANDLING_UNIT_LOG
-- -------------------------------------
DROP TABLE IF EXISTS HANDLING_UNIT_LOG;
CREATE TABLE HANDLING_UNIT_LOG
(
  HANDLE         VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID    VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  CARRIER_BO     VARCHAR(412) NOT NULL COMMENT '關聯棧板主鍵',
  STATUS         VARCHAR(20)    DEFAULT NULL COMMENT '狀態（可用/不可用）',
  INVENTORY_BO   VARCHAR(412)   DEFAULT NULL COMMENT '庫存',
  STORAGE_BIN_BO VARCHAR(412)   DEFAULT NULL COMMENT '當前所屬貨格',
  INVENTORY_QTY  DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME    TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  CREATOR        VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元創建日誌記錄';

-- --------------------------------------------
-- Table structure for HANDLING_UNIT_CHANGE_LOG
-- --------------------------------------------
DROP TABLE IF EXISTS HANDLING_UNIT_CHANGE_LOG;
CREATE TABLE HANDLING_UNIT_CHANGE_LOG
(
  HANDLE         VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID    VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  ACTION_CODE    VARCHAR(20)  NOT NULL COMMENT '變更事件代號（創建/拆並垛/出入庫/盤點）',
  STORAGE_BIN_BO VARCHAR(412)   DEFAULT NULL COMMENT '當前所屬貨格',
  INVENTORY_QTY  DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME    TIMESTAMP(3)   DEFAULT NULL COMMENT '創建日期',
  CREATOR        VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元變更日誌記錄';

-- -----------------------------------
-- Table structure for ERP_POSTING_LOG
-- -----------------------------------
DROP TABLE IF EXISTS ERP_POSTING_LOG;
CREATE TABLE ERP_POSTING_LOG
(
  HANDLE        VARCHAR(412) NOT NULL COMMENT '主鍵',
  NAME          VARCHAR(50)  DEFAULT NULL COMMENT '互動名稱',
  TYPE          VARCHAR(50)  DEFAULT NULL COMMENT '互動類型',
  RESOURCE_BO   VARCHAR(200) DEFAULT NULL COMMENT '資源',
  KEY1          VARCHAR(200) DEFAULT NULL COMMENT '描述',
  KEY2          VARCHAR(200) DEFAULT NULL COMMENT '資源',
  KEY3          VARCHAR(200) DEFAULT NULL COMMENT '資源',
  MESSAGE_ID        VARCHAR(412) DEFAULT NULL COMMENT '請求ID',
  RESULT        VARCHAR(10)  DEFAULT NULL COMMENT '互動返回代號',
  MSG           VARCHAR(200) DEFAULT NULL COMMENT '回饋資訊',
  REQUEST_BODY  TEXT         DEFAULT NULL COMMENT '請求內容',
  RESPONSE_BODY TEXT         DEFAULT NULL COMMENT '返回內容',
  PROCESS_TIME  LONG         DEFAULT NULL COMMENT '處理時間（毫秒）',
  DATE_CREATE   DATETIME COMMENT '創建日期',
  CREATOR       VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'ERP互動日誌';

-- --------------------------
-- Table structure for MQ_LOG
-- --------------------------
DROP TABLE IF EXISTS MQ_LOG;
CREATE TABLE MQ_LOG (
                      HANDLE	VARCHAR(412),
                      SERVER_ID	VARCHAR(50) COMMENT 'IP地址',
                      BROKER	VARCHAR(50) COMMENT 'MQ地址',
                      QUEUE	VARCHAR(50) COMMENT '隊列名稱',
                      MESSAGE_ID	 VARCHAR(100) COMMENT '訊息ID',
                      CORRELATION_ID VARCHAR(100) COMMENT '關聯ID',
                      MESSAGE_BODY	VARCHAR(10000) COMMENT '訊息內容',
                      RESPONSE_BODY VARCHAR(2000) COMMENT '返回訊息內容',
                      RESULT	VARCHAR(5) COMMENT '結果',
                      ERROR	VARCHAR(2000) COMMENT '錯誤資訊',
                      MESSAGE_TYPE	VARCHAR(50) COMMENT '訊息類型',
                      CREATED_DATE_TIME	TIMESTAMP(3) COMMENT '創建日期',
                      PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'MQ日誌';

-- ---------------------------------
-- Table structure for SHELF_OFF_LOG
-- ---------------------------------
DROP TABLE IF EXISTS SHELF_OFF_LOG;
CREATE TABLE SHELF_OFF_LOG (
                             HANDLE	VARCHAR(412),
                             MESSAGE_ID	VARCHAR(100) COMMENT 'request id',
                             CORRELATION_ID VARCHAR(100) COMMENT 'correlation id',
                             ACTION_CODE VARCHAR(10) COMMENT 'action code',
                             CARRIER	VARCHAR(100) COMMENT 'carrier',
                             STORAGE_LOCATION	VARCHAR(50) COMMENT 'storage location',
                             STORAGE_BIN	VARCHAR(50) COMMENT 'storage bin',
                             INVENTORY_BO	VARCHAR(412) COMMENT 'inventory handle',
                             QTY	DECIMAL(38, 6) COMMENT 'qty',
                             SPLIT	CHAR(1) COMMENT 'split',
                             EXECUTE_RESULT	CHAR(1) COMMENT 'execute result',
                             CREATE_TIME	TIMESTAMP(3) COMMENT 'create time',
                             PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'shelf off log';
CREATE INDEX CORRELATION_ID_IDX ON SHELF_OFF_LOG (CORRELATION_ID);

CREATE INDEX inventory_qty_on_hand_idx USING BTREE ON inventory (QTY_ON_HAND) ;
CREATE INDEX inventory_batch_no_idx USING BTREE ON inventory (BATCH_NO) ;
CREATE INDEX inventory_item_bo_idx USING BTREE ON inventory (ITEM_BO) ;
CREATE INDEX inventory_production_date_idx USING BTREE ON inventory (PRODUCTION_DATE) ;

CREATE INDEX handling_unit_handling_unit_context_gbo_idx ON handling_unit (HANDLING_UNIT_CONTEXT_GBO) ;
CREATE INDEX handling_unit_carrier_bo_idx ON handling_unit (CARRIER_BO) ;

-- dashboard----------------------------------------------------------------------------------------
-- ----------------------------------------
-- Table structure for DS_INVENTORY_SUMMARY
-- ----------------------------------------
DROP TABLE IF EXISTS DS_INVENTORY_SUMMARY;
CREATE TABLE DS_INVENTORY_SUMMARY (
                                    HANDLE	VARCHAR(412),
                                    ITEM	VARCHAR(50) COMMENT 'material',
                                    TOTAL_QTY DECIMAL(38, 6) COMMENT 'total inventory qty',
                                    INBOUND_QTY	DECIMAL(38, 6) COMMENT 'inbound qty',
                                    OUTBOUND_QTY	DECIMAL(38, 6) COMMENT 'outbound qty',
                                    STATISTIC_DATE	 DATE COMMENT 'date',
                                    PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'inventory summary by day';
CREATE INDEX ds_inventory_summary_date_idx ON ds_inventory_summary (STATISTIC_DATE);

-- -------------------------------------
-- Table structure for DS_CARRIER_STATUS
-- -------------------------------------
DROP TABLE IF EXISTS DS_CARRIER_STATUS;
create table if not exists DS_CARRIER_STATUS
(
  HANDLE VARCHAR(412) not null primary key,
  ITEM VARCHAR(100) null comment 'material',
  CARRIER VARCHAR(100) null comment 'carrier',
  OPERATION VARCHAR(50) null comment 'operation',
  STATUS VARCHAR(50) null comment 'status'
  )
  comment 'carrier stataus summary' charset=utf8mb4;
-- end dashboard----------------------------------------------------------------------------------------
-- -------------------------------
-- Table structure for ASRS_ORDER
-- -------------------------------
DROP TABLE IF EXISTS Asrs_Order;
create table if not exists Asrs_Order
(
	HANDLE VARCHAR(412)         NOT NULL,
	MESSAGE_ID VARCHAR(412)     NOT NULL COMMENT 'MQ ID',
	WO_SERIAL VARCHAR(100)      NOT NULL COMMENT '工令(訂單)編號',
	WO_TYPE VARCHAR(50)         NOT NULL COMMENT '工令屬性',
	ORDER_QTY VARCHAR(100)      DEFAULT NULL COMMENT '此訂單處理量，校驗用（一訂單含多憑單號）',
	VOUCHER_NO VARCHAR(100)     DEFAULT NULL COMMENT '憑單號',
	ITEM_COUNT VARCHAR(100)     DEFAULT NULL COMMENT '憑單號處理量',
	ITEM VARCHAR(100)           DEFAULT NULL COMMENT '物料/貨品',
	CONTAINER VARCHAR(100)      DEFAULT NULL COMMENT '貨品包裝',
	STORAGE_BIN VARCHAR(50)     DEFAULT NULL COMMENT '存儲貨格代號',
	VALIDATION VARCHAR(50)      DEFAULT NULL COMMENT '驗證訂單合法性',
	STATUS VARCHAR(50)          DEFAULT NULL COMMENT '訂單狀態',
	RESOURCE VARCHAR(100)       DEFAULT NULL COMMENT '使用輸送帶',
	CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	CREATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '創建日期',
	UPDATER     VARCHAR(64)     DEFAULT NULL COMMENT '更新人員',
	UPDATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '更新日期',	
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'ASRS訂單';

-- -------------------------------
-- Table structure for ASRS_RFID
-- -------------------------------
DROP TABLE IF EXISTS ASRS_RFID;
create table if not exists ASRS_RFID
(
	HANDLE VARCHAR(100)         NOT NULL COMMENT 'RFID',
	ASRS_Order_BO VARCHAR(412)  DEFAULT NULL COMMENT '訂單主鍵',
	VOUCHER_NO VARCHAR(100)     DEFAULT NULL COMMENT '憑單號',
	HANDLING_ID  VARCHAR(412)   DEFAULT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
	CARRIER   VARCHAR(100)      DEFAULT NULL COMMENT '關聯棧板',
	STATUS   VARCHAR(100)       DEFAULT NULL COMMENT '狀態',
	MEASURE_UNIT   VARCHAR(50)  DEFAULT NULL COMMENT '單位',
	NET_WEIGHT  DECIMAL(38,6)   DEFAULT NULL COMMENT '量',
	CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	CREATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '創建日期',
	UPDATER     VARCHAR(64)     DEFAULT NULL COMMENT '更新人員',
	UPDATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '更新日期',
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'ASRS訂單物料資料';

-- ------------------------------------
-- Table structure for ROBOTIC_ARM_TASK
-- ------------------------------------
DROP TABLE IF EXISTS `ROBOTIC_ARM_TASK`;
CREATE TABLE `ROBOTIC_ARM_TASK`(
    HANDLE VARCHAR(412) NOT NULL COMMENT '任務的MQID',
    VOUCHER_NO VARCHAR(100)   DEFAULT NULL COMMENT '憑單號',
    WO_SERIAL VARCHAR(412)  DEFAULT NULL COMMENT '訂單號',
    WO_QTY VARCHAR(100)  DEFAULT NULL COMMENT '貨物要求數量',
    DO_QTY VARCHAR(100)  DEFAULT NULL COMMENT '此次貨物要求數量',
    FROM_PALLET_QTY VARCHAR(100)  DEFAULT NULL COMMENT '來料棧板數量(出庫用)',
    TO_PALLET_QTY VARCHAR(100)  DEFAULT NULL COMMENT '機械手臂放置物料的棧板，目前的數量',
	RESOURCE VARCHAR(100)  DEFAULT NULL COMMENT '使用輸送帶',
    TYPE VARCHAR(100)  DEFAULT NULL COMMENT '出入庫類型',
    CARRIER VARCHAR(100)  DEFAULT NULL COMMENT '出庫使用-棧板ID',
    STORAGE_BIN VARCHAR(100)  DEFAULT NULL COMMENT '出庫使用-庫位ID',
	TASK_ORDER VARCHAR(100)  DEFAULT NULL COMMENT '任務順序',
    STATUS VARCHAR(100)  DEFAULT NULL COMMENT '任務狀態',
    RESULT VARCHAR(100)  DEFAULT NULL COMMENT '任務結果',
    CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	CREATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '創建日期',
	UPDATER     VARCHAR(64)     DEFAULT NULL COMMENT '更新人員',
	UPDATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '更新日期',
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '機械手臂任務清單';

-- ------------------------------------
-- Table structure for CARRIER_TASK
-- ------------------------------------
DROP TABLE IF EXISTS `CARRIER_TASK`;
CREATE TABLE `CARRIER_TASK`(
    HANDLE VARCHAR(412)       NOT NULL COMMENT '任務的MQID',
	WO_TYPE VARCHAR(50)       DEFAULT NULL COMMENT '工令屬性',
    VOUCHER_NO VARCHAR(100)   DEFAULT NULL COMMENT '憑單號',
    WO_SERIAL VARCHAR(412)    DEFAULT NULL COMMENT '訂單號',
	RARM_TASK_ID VARCHAR(412) DEFAULT NULL COMMENT '機械手臂任務的MQID',
	CARRIER VARCHAR(100)      DEFAULT NULL COMMENT '棧板ID',
	RESOURCE VARCHAR(100)     DEFAULT NULL COMMENT '使用輸送帶',
	START_STATION VARCHAR(100) DEFAULT NULL COMMENT '起始輸送帶工位',
	END_STATION VARCHAR(100)   DEFAULT NULL COMMENT '終點輸送帶工位',
	STORAGE_BIN VARCHAR(100)   DEFAULT NULL COMMENT '儲位',
    TASK_TYPE VARCHAR(100)     DEFAULT NULL COMMENT '任務類型',
    STATUS VARCHAR(100)       DEFAULT NULL COMMENT '任務狀態',
    RESULT VARCHAR(100)       DEFAULT NULL COMMENT '任務結果',
	TASK_ORDER VARCHAR(5)     DEFAULT NULL COMMENT '任務順序',
    CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	CREATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '創建日期',
	UPDATER     VARCHAR(64)     DEFAULT NULL COMMENT '更新人員',
	UPDATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '更新日期',
	CONSTRAINT `PRIMARY` PRIMARY KEY (HANDLE)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '棧板任務清單';

-- ----------------------------
-- Table structure for CONVEYOR
-- ----------------------------
DROP TABLE IF EXISTS CONVEYOR;
CREATE TABLE CONVEYOR
(
  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  CONVEYOR    VARCHAR(50)  NOT NULL COMMENT '輸送帶',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  PRIORITY    DECIMAL(38, 6)  DEFAULT NULL COMMENT '優先選擇權(1~5)',
  WORKLOAD    DECIMAL(38, 6)  DEFAULT NULL COMMENT '工作量',
  CREATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP(3) DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '輸送帶資料';

-- -------------------------------
-- Table structure for REPORT_ASRS
-- -------------------------------
DROP TABLE IF EXISTS REPORT_ASRS;
create table if not exists REPORT_ASRS
(
	HANDLE VARCHAR(412) NOT NULL COMMENT '主鍵',
	CARRIER VARCHAR(100)         DEFAULT NULL COMMENT '棧板',	
	WO_SERIAL VARCHAR(100)      NOT NULL COMMENT '工令(訂單)編號',
	HANDLING_ID  VARCHAR(412)   DEFAULT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
	CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	CREATE_TIME TIMESTAMP(3)    DEFAULT NULL COMMENT '創建日期',
	PRIMARY KEY (HANDLE)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '用於需回傳紀錄';

-- ------------------------------------
-- Table structure for ASRS_ORDER_ALARM
-- ------------------------------------
DROP TABLE IF EXISTS Asrs_Order_Alarm;
create table if not exists Asrs_Order_Alarm
(
	HANDLE VARCHAR(412)         NOT NULL,
	MESSAGE_ID VARCHAR(412)     DEFAULT NULL COMMENT 'MQ ID',
	WO_SERIAL VARCHAR(100)      DEFAULT NULL COMMENT '工令(訂單)編號',
	VOUCHER_NO VARCHAR(100)     DEFAULT NULL COMMENT '憑單號',
	ALARM_TYPE VARCHAR(100)     DEFAULT NULL COMMENT '工單錯誤類型',
	ALARM_MSG VARCHAR(500)      DEFAULT NULL COMMENT '工單錯誤說明',
	OCCUR_TIME TIMESTAMP(3)     DEFAULT NULL COMMENT '發生日期',
	CREATOR     VARCHAR(64)     DEFAULT NULL COMMENT '創建人員',
	PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'ASRS訂單錯誤';

-- Data-------------------------------------------------------------------------------------------------
INSERT INTO warehouse (HANDLE, WAREHOUSE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES
('WarehouseBO:506', '506', '默認倉庫', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator');

INSERT INTO sys_user_role (user_id, role_id) VALUES(1, 1);

INSERT INTO sys_user (user_id, username, first_name, last_name, password, salt, phone, avatar, dept_id, create_time, update_time, lock_flag, del_flag, tenant_id) VALUES
(1, 'Administrator', NULL, 'Administrator', NULL, NULL, NULL, NULL, NULL, '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'N', 'N', NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1000);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1201);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1202);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1203);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1300);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1301);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1302);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1303);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 1304);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10013);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10014);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10015);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10016);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10017);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10018);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10022);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10026);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10027);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10028);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10029);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10030);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10031);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10032);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10033);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10034);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10035);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10036);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10037);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10038);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10039);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10040);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10041);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10042);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10043);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10044);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10045);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10046);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10047);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10048);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10049);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10050);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10051);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10052);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10053);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10054);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10055);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10056);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10057);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10058);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10059);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10060);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10061);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10062);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10063);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10065);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10066);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10067);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10068);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10069);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10070);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10071);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10072);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES(1, 10073);

INSERT INTO sys_role (role_id, role_name, role_code, role_desc, ds_type, ds_scope, create_time, update_time, del_flag, tenant_id) VALUES(1, '管理員', 'ROLE_Administrator', '管理員', '3', '2', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'N', '1');

INSERT INTO sys_menu (menu_id,name,permission,`path`,parent_id,icon,sort,keep_alive,`type`,create_time,update_time,del_flag) VALUES 
 (1000,'存取權管理',NULL,'',-1,'sap-icon://permission',0,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1100,'帳號管理',NULL,'sysUserMaintain',1000,'sap-icon://user-settings',1,'Y','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1101,'用戶修改','sys_user_edit',NULL,1100,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1200,'功能表管理',NULL,'sysMenuMaintain',1000,'sap-icon://menu',2,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1201,'選單新增','sys_menu_add',NULL,1200,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1202,'選單修改','sys_menu_edit',NULL,1200,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1203,'選單刪除','sys_menu_delete',NULL,1200,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1300,'角色管理',NULL,'sysRoleMaintain',1000,'sap-icon://role',3,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1301,'角色新增','sys_role_add',NULL,1300,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1302,'角色修改','sys_role_edit',NULL,1300,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1303,'角色刪除','sys_role_del',NULL,1300,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(1304,'分配許可權','sys_role_perm',NULL,1300,NULL,NULL,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10013,'倉庫管理',NULL,'stayInReport',-1,'sap-icon://inventory',10,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10014,'庫存接收',NULL,'inventoryReceive',10013,NULL,10,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10015,'棧板綁定',NULL,'carrierBind',10013,NULL,20,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10016,'創建','inventory_add',NULL,10014,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10017,'刪除','inventory_delete',NULL,10014,NULL,999,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10018,'綁定','handling_unit_carrier_bind',NULL,10015,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10022,'解綁','handling_unit_carrier_unbind',NULL,10015,NULL,999,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10026,'系統領料',NULL,'materialRequisition',10013,NULL,30,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10027,'確認','material_requisition_confirm',NULL,10026,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10028,'棧板維護',NULL,'carrierMaintain',10071,NULL,200,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10029,'新增','carrier_add','carrierMaintain',10028,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10030,'修改','carrier_edit','carrierMaintain',10028,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10031,'刪除','carrier_delete','carrierMaintain',10028,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10032,'棧板類型維護',NULL,'carrierTypeMaintain',10071,NULL,210,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10033,'新增','carrier_type_add',NULL,10032,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10034,'修改','carrier_type_edit',NULL,10032,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10035,'刪除','carrier_type_delete',NULL,10032,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10036,'物料組維護',NULL,'itemGroupMaintain',10071,NULL,220,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10037,'新增','item_group_add',NULL,10036,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10038,'修改','item_group_edit',NULL,10036,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10039,'刪除','item_group_delete',NULL,10036,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10040,'物料維護',NULL,'itemMaintain',10071,NULL,230,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10041,'新增','item_add',NULL,10040,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10042,'修改','item_edit',NULL,10040,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10043,'刪除','item_delete',NULL,10040,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10044,'設備維護',NULL,'resrceMaintain',10071,NULL,250,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10045,'新增','resrce_add',NULL,10044,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10046,'修改','resrce_edit',NULL,10044,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10047,'刪除','resrce_delete',NULL,10044,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10048,'設備類型維護',NULL,'resourceTypeMaintain',10071,NULL,240,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10049,'新增','resource_type_add',NULL,10048,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10050,'修改','resource_type_edit',NULL,10048,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10051,'刪除','resource_type_delete',NULL,10048,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10052,'儲位維護',NULL,'storageBinMaintain',10071,NULL,280,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10053,'新增','storage_bin_add',NULL,10052,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10054,'修改','storage_bin_edit',NULL,10052,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10055,'刪除','storage_bin_delete',NULL,10052,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10056,'儲存位置維護',NULL,'storageLocationMaintain',10071,NULL,260,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10057,'新增','storage_location_add',NULL,10056,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10058,'修改','storage_location_edit',NULL,10056,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10059,'刪除','storage_location_delete',NULL,10056,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10060,'儲位類型維護',NULL,'storageBinTypeMaintain',10071,NULL,270,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10061,'新增','storage_bin_type_add',NULL,10060,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10062,'修改','storage_bin_type_edit',NULL,10060,NULL,20,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10063,'刪除','storage_bin_type_delete',NULL,10060,NULL,30,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10065,'報表管理',NULL,NULL,-1,'sap-icon://trip-report',999,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10066,'倉庫查詢報表',NULL,'warehouseReport',10065,'',10,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10067,'待入庫查詢報表',NULL,'stayInReport',10065,NULL,20,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10068,'看板',NULL,NULL,-1,'sap-icon://bbyd-dashboard',1000,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10069,'待入/出庫物料',NULL,'biz/dashboard/pages/workshopInfra.html',10068,NULL,10,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10070,'產線狀態',NULL,'biz/dashboard/pages/dayInfra.html',10068,NULL,20,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10071,'資料管理',NULL,NULL,-1,'sap-icon://database',5,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10072,'棧板轉移',NULL,'storageChange',10013,NULL,40,'N','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
,(10073,'確認','storage_change_confirm',NULL,10072,NULL,10,'N','1','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','N')
;

INSERT INTO storage_location_type (HANDLE,STORAGE_LOCATION_TYPE,DESCRIPTION,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('StorageLocationTypeBO:A區', 'A區', 'A區', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationTypeBO:B區', 'B區', 'B區', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
;

INSERT INTO storage_location (HANDLE,STORAGE_LOCATION,DESCRIPTION,STORAGE_LOCATION_TYPE_BO,WAREHOUSE_BO,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('StorageLocationBO:C01', 'C01', 'C01', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C02', 'C02', 'C02', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C03', 'C03', 'C03', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C04', 'C04', 'C04', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C05', 'C05', 'C05', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C06', 'C06', 'C06', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C07', 'C07', 'C07', 'StorageLocationTypeBO:A區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C08', 'C08', 'C08', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C09', 'C09', 'C09', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C10', 'C10', 'C10', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C11', 'C11', 'C11', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C12', 'C12', 'C12', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageLocationBO:C13', 'C13', 'C13', 'StorageLocationTypeBO:B區', 'WarehouseBO:506', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
;

INSERT INTO storage_bin_type (HANDLE,STORAGE_BIN_TYPE,DESCRIPTION,WIDTH,LENGTH,HEIGHT,MAX_WEIGHT,MIX_ITEM,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('StorageBinTypeBO:L1', 'L1', 'Layer 1', 1200, 1200, 1290, 1500, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:L2', 'L2', 'Layer 2', 1200, 1200, 1290, 1500, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:L3', 'L3', 'Layer 3', 1200, 1200, 1290, 1500, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:L4', 'L4', 'Layer 4', 1200, 1200, 790, 1500, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:L5', 'L5', 'Layer 5', 1200, 1200, 790, 1500, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:EmptyL1', 'Empty L1', 'Empty Layer 1', 1200, 1200, 200, 100, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:EmptyL2', 'Empty L2', 'Empty Layer 2', 1200, 1200, 200, 100, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:EmptyL3', 'Empty L3', 'Empty Layer 3', 1200, 1200, 200, 100, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:EmptyL4', 'Empty L4', 'Empty Layer 4', 1200, 1200, 200, 100, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('StorageBinTypeBO:EmptyL5', 'Empty L5', 'Empty Layer 5', 1200, 1200, 200, 100, 'N', '2021-09-10 10:10:10.0', '2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
;

INSERT INTO status (HANDLE,STATUS,STATUS_GROUP) VALUES 
 ('StatusBO:301,RESOURCE','301','RESOURCE')
,('StatusBO:302,RESOURCE','302','RESOURCE')
,('StatusBO:303,RESOURCE','303','RESOURCE')
,('StatusBO:304,RESOURCE','304','RESOURCE')
,('StatusBO:305,RESOURCE','305','RESOURCE')
,('StatusBO:AVAILABLE,INVENTORY','AVAILABLE','INVENTORY')
,('StatusBO:AVAILABLE,ITEM','AVAILABLE','ITEM')
,('StatusBO:FROZEN,INVENTORY','FROZEN','INVENTORY')
,('StatusBO:FROZEN,ITEM','FROZEN','ITEM')
,('StatusBO:RESTRICT,INVENTORY','RESTRICT','INVENTORY')
;

INSERT INTO status_t (HANDLE,STATUS_BO,LOCALE,DESCRIPTION) VALUES 
('StatusBO:301,RESOURCE,en-US','StatusBO:301,RESOURCE','en-US','Enabled')
,('StatusBO:301,RESOURCE,zh-CN','StatusBO:301,RESOURCE','zh-CN','已啟用')
,('StatusBO:301,RESOURCE,zh-TW','StatusBO:301,RESOURCE','zh-TW','已啟用')
,('StatusBO:302,RESOURCE,en-US','StatusBO:302,RESOURCE','en-US','Disabled')
,('StatusBO:302,RESOURCE,zh-CN','StatusBO:302,RESOURCE','zh-CN','已禁用')
,('StatusBO:302,RESOURCE,zh-TW','StatusBO:302,RESOURCE','zh-TW','已禁用')
,('StatusBO:303,RESOURCE,en-US','StatusBO:303,RESOURCE','en-US','Hold')
,('StatusBO:303,RESOURCE,zh-CN','StatusBO:303,RESOURCE','zh-CN','保留')
,('StatusBO:303,RESOURCE,zh-TW','StatusBO:303,RESOURCE','zh-TW','保留')
,('StatusBO:304,RESOURCE,en-US','StatusBO:304,RESOURCE','en-US','Assembled')
,('StatusBO:304,RESOURCE,zh-CN','StatusBO:304,RESOURCE','zh-CN','已裝配')
,('StatusBO:304,RESOURCE,zh-TW','StatusBO:304,RESOURCE','zh-TW','已裝配')
,('StatusBO:305,RESOURCE,en-US','StatusBO:305,RESOURCE','en-US','Scrapped')
,('StatusBO:305,RESOURCE,zh-CN','StatusBO:305,RESOURCE','zh-CN','已報廢')
,('StatusBO:305,RESOURCE,zh-TW','StatusBO:305,RESOURCE','zh-TW','已報廢')
,('StatusBO:AVAILABLE,INVENTORY,en-US','StatusBO:AVAILABLE,INVENTORY','en-US','Available')
,('StatusBO:AVAILABLE,INVENTORY,zh-CN','StatusBO:AVAILABLE,INVENTORY','zh-CN','可用')
,('StatusBO:AVAILABLE,INVENTORY,zh-TW','StatusBO:AVAILABLE,INVENTORY','zh-TW','可用')
,('StatusBO:AVAILABLE,ITEM,en-US','StatusBO:AVAILABLE,ITEM','en-US','Available')
,('StatusBO:AVAILABLE,ITEM,zh-CN','StatusBO:AVAILABLE,ITEM','zh-CN','可用')
,('StatusBO:AVAILABLE,ITEM,zh-TW','StatusBO:AVAILABLE,ITEM','zh-TW','可用')
,('StatusBO:FROZEN,INVENTORY,en-US','StatusBO:FROZEN,INVENTORY','en-US','Frozen')
,('StatusBO:FROZEN,INVENTORY,zh-CN','StatusBO:FROZEN,INVENTORY','zh-CN','凍結')
,('StatusBO:FROZEN,INVENTORY,zh-TW','StatusBO:FROZEN,INVENTORY','zh-TW','凍結')
,('StatusBO:FROZEN,ITEM,en-US','StatusBO:FROZEN,ITEM','en-US','Frozen')
,('StatusBO:FROZEN,ITEM,zh-CN','StatusBO:FROZEN,ITEM','zh-CN','凍結')
,('StatusBO:FROZEN,ITEM,zh-TW','StatusBO:FROZEN,ITEM','zh-TW','凍結')
,('StatusBO:RESTRICT,INVENTORY,en-US','StatusBO:RESTRICT,INVENTORY','en-US','Restrict')
,('StatusBO:RESTRICT,INVENTORY,zh-CN','StatusBO:RESTRICT,INVENTORY','zh-CN','限制')
,('StatusBO:RESTRICT,INVENTORY,zh-TW','StatusBO:RESTRICT,INVENTORY','zh-TW','限制')
;

INSERT INTO resource_type (HANDLE,RESOURCE_TYPE,DESCRIPTION,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('ResourceTypeBO:RS001','RS001','RS001','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator');

INSERT INTO resrce (HANDLE,RESRCE,DESCRIPTION,STATUS,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('ResourceBO:RS001','RS001','RS001','301','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('ResourceBO:RS002','RS002','RS002','301','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
;

INSERT INTO resource_type_resource (HANDLE,RESOURCE_TYPE_BO,RESOURCE_BO) VALUES 
 ('ResourceTypeResourceBO:ResourceTypeBO:RS001,ResourceBO:RS001','ResourceTypeBO:RS001','ResourceBO:RS001')
,('ResourceTypeResourceBO:ResourceTypeBO:RS001,ResourceBO:RS002','ResourceTypeBO:RS001','ResourceBO:RS002')
;

INSERT INTO measure_unit (MEASURE_UNIT, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES
('MeasureUnitBO:EA', 'EA', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator');

INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES
 ('ItemTypeBO:1001', '1001', 'Y', '原材料', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:1002', '1002', 'Y', '輔材', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:1003', '1003', 'Y', '包材', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:1004', '1004', 'Y', '中間品', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:1005', '1005', 'Y', '半成品', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:1006', '1006', 'Y', '成品', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:PB1', 'PB1', 'Y', '包裝類型PB1', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:PB2', 'PB2', 'Y', '包裝類型PB2', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:PB3', 'PB3', 'Y', '包裝類型PB3', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:PB4', 'PB4', 'Y', '包裝類型PB4', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
,('ItemTypeBO:XXX', 'XXX', 'Y', '散件包裝類型', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Administrator', 'Administrator')
;

INSERT INTO consumption_mode (CONSUMPTION_MODE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES
 ('ConsumptionModeBO:FIFO', '先進先出', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Admi', 'Administrator')
,('ConsumptionModeBO:NTFO', '近效先出', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Admi', 'Administrator')
,('ConsumptionModeBO:LIFO', '後進先出', '2021-09-10 10:10:10.0','2021-09-10 10:10:10.0', 'Admi', 'Administrator')
;

INSERT INTO carrier_type (HANDLE,CARRIER_TYPE,DESCRIPTION,MIX_ITEM,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
('CarrierTypeBO:CarrierType001','CarrierType001','CarrierType001','Y','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator');

INSERT INTO carrier (HANDLE,CARRIER,STATUS,DESCRIPTION,CARRIER_TYPE_BO,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('CarrierBO:ASRS_PALLET_00001','ASRS_PALLET_00001','','ASRS_PALLET_00001','CarrierTypeBO:CarrierType001','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('CarrierBO:ASRS_PALLET_00002','ASRS_PALLET_00002','','ASRS_PALLET_00002','CarrierTypeBO:CarrierType001','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('CarrierBO:ASRS_PALLET_00003','ASRS_PALLET_00003','','ASRS_PALLET_00003','CarrierTypeBO:CarrierType001','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
;

INSERT INTO conveyor (HANDLE,CONVEYOR,DESCRIPTION,PRIORITY,WORKLOAD,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES 
 ('ConveyorBO:Conveyor1','Conveyor1','Conveyor1','0','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('ConveyorBO:Conveyor2','Conveyor2','Conveyor2','0','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('ConveyorBO:Conveyor3','Conveyor3','Conveyor3','0','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('ConveyorBO:Conveyor4','Conveyor4','Conveyor4','1','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
,('ConveyorBO:Conveyor5','Conveyor5','Conveyor5','0','0','2021-09-10 10:10:10.0','2021-09-10 10:10:10.0','Administrator','Administrator')
;

INSERT INTO wms.handling_unit (HANDLE,HANDLING_ID,CARRIER_BO,STATUS,MIXED,HANDLING_UNIT_CONTEXT_GBO,INVENTORY_QTY,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES
	 ('HU001','HANDLING123','CARRIER001','AVAILABLE','Y','InventoryBO:506AAA30480004',4.000000,'2025-01-14 19:08:24','2025-01-14 19:08:24','admin','admin');
INSERT INTO wms.handling_unit_location (HANDLE,HANDLING_ID,CARRIER_BO,BIND_CONTEXT_GBO,BIND_TYPE,CREATE_TIME,CREATOR) VALUES
	 ('HUL001','HANDLING123','CARRIER001','StorageBinBO:C09R04L1','庫位','2025-01-14 19:09:05','admin');
INSERT INTO wms.inventory (HANDLE,STATUS,ITEM_BO,UNIT_OF_MEASURE,BATCH_NO,VENDOR_BATCH_NO,ORIGINAL_QTY,QTY_ON_HAND,VALID,PRODUCTION_DATE,EXPIRE_DATE,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES
	 --('InventoryBO:506AAA30480004','AVAILABLE','ItemBO:5340YETQ05376',NULL,'506AAA30480004','506AAA30480004',4.000000,4.000000,'Y','2025-01-09',NULL,'2025-01-09 15:15:28.609000000','2025-01-09 15:15:28.609000000','ADMIN','ADMIN'),
	 ('InventoryBO:ItemGroup01','AVAILABLE','ItemBO:Item001','','ItemGroup01','ItemGroup01',100.000000,100.000000,'Y','2021-08-08',NULL,'2021-09-10 10:10:10','2021-09-10 10:10:10','Admi','Administrator'),
	 ('InventoryBO:ItemGroup02','AVAILABLE','ItemBO:Item001','','ItemGroup02','ItemGroup02',100.000000,100.000000,'Y','2021-08-08',NULL,'2021-09-10 10:10:10','2021-09-10 10:10:10','Admi','Administrator'),
	 ('InventoryBO:ItemGroup03','AVAILABLE','ItemBO:Item002','','ItemGroup03','ItemGroup03',50.000000,50.000000,'Y','2020-09-13',NULL,'2021-09-10 10:10:10','2021-09-10 10:10:10','Admi','Administrator');

INSERT INTO wms.item (HANDLE,ITEM,DESCRIPTION,MIX_ITEM,INSPECTION_REQUIRED,ITEM_STATUS,ITEM_TYPE_BO,CONSUMPTION_MODE,UNIT_OF_MEASURE,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES
	 ('ItemBO:5340YETQ05376','5340YETQ05376','5340YETQ05376','Y','N','AVAILABLE','ItemTypeBO:PB1','ConsumptionModeBO:FIFO',NULL,'2025-01-09 15:15:28.604000000','2025-01-09 15:15:28.604000000','ADMIN','ADMIN'),
	 ('ItemBO:534QYETQ05376','534QYETQ05376','534QYETQ05376','Y','N','AVAILABLE','ItemTypeBO:PB1','ConsumptionModeBO:FIFO','','2021-09-10 10:10:10','2021-09-10 10:10:10','Administrator','Administrator'),
	 ('ItemBO:Item002','Item002','Item002','Y','N','AVAILABLE','ItemTypeBO:1001','ConsumptionModeBO:FIFO','','2021-09-10 10:10:10','2021-09-10 10:10:10','Admi','Administrator');
INSERT INTO wms.storage_bin (HANDLE,STORAGE_BIN,DESCRIPTION,STORAGE_BIN_TYPE_BO,STORAGE_LOCATION_BO,SHELF_ROW,SHELF_COLUMN,CREATE_TIME,UPDATE_TIME,CREATOR,UPDATER) VALUES
	 ('StorageBinBO:C09R04L1','C09R04L1','C09R04L1','StorageBinTypeBO:L1','StorageLocationBO:C01',4,9,'2021-10-10 10:10:10','2021-10-10 10:10:10','Administrator','Administrator');
INSERT INTO wms.report_asrs (HANDLE, CARRIER, WO_SERIAL, HANDLING_ID, CREATOR, CREATE_TIME) values
	('HU001', 'CARRIER001', '15905448', 'E2801170000002123E026093', 'admin', '2025-01-14 19:09:05'),
	('HU002', 'CARRIER001', '15905448', 'E2801170000002123E026094', 'admin', '2025-01-14 19:09:05'),
	('HU003', 'CARRIER001', '15905448', 'E2801170000002123E026095', 'admin', '2025-01-14 19:09:05'),
	('HU004', 'CARRIER001', '15905448', 'E2801170000002123E026096', 'admin', '2025-01-14 19:09:05');
