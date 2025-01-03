use wms;

DROP TABLE IF EXISTS WAREHOUSE;
CREATE TABLE WAREHOUSE
(

  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  WAREHOUSE    VARCHAR(20)  NOT NULL COMMENT '倉庫代碼',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '倉庫資料';

DROP TABLE IF EXISTS STORAGE_LOCATION_TYPE;
CREATE TABLE STORAGE_LOCATION_TYPE
(

  HANDLE                VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_LOCATION_TYPE VARCHAR(20)  NOT NULL COMMENT '存儲位置類型代碼',
  DESCRIPTION           VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME           TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME           TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR               VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER               VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲位置類型資料';

DROP TABLE IF EXISTS STORAGE_LOCATION;
CREATE TABLE STORAGE_LOCATION
(

  HANDLE                   VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_LOCATION         VARCHAR(20)  NOT NULL COMMENT '存儲位置代碼',
  DESCRIPTION              VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STORAGE_LOCATION_TYPE_BO VARCHAR(412) NOT NULL COMMENT '關聯存儲位置類型主鍵',
  WAREHOUSE_BO              VARCHAR(412) NOT NULL COMMENT '關聯倉庫主鍵',
  CREATE_TIME              TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME              TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR                  VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER                  VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲位置資料';

DROP TABLE IF EXISTS STORAGE_BIN_TYPE;
CREATE TABLE STORAGE_BIN_TYPE
(

  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN_TYPE VARCHAR(20)  NOT NULL COMMENT '存儲貨格類型代碼',
  DESCRIPTION      VARCHAR(200)   DEFAULT NULL COMMENT '描述',
  WIDTH            DECIMAL(38, 6) DEFAULT NULL COMMENT '寬度',
  LENGTH           DECIMAL(38, 6) DEFAULT NULL COMMENT '長度',
  HEIGHT           DECIMAL(38, 6) DEFAULT NULL COMMENT '高度',
  MAX_WEIGHT       DECIMAL(38, 6) DEFAULT NULL COMMENT '最大承重',
  MIX_ITEM         CHAR(1)        DEFAULT NULL COMMENT '可否混料',
  CREATE_TIME      TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP      DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格類型資料';

DROP TABLE IF EXISTS STORAGE_BIN;
CREATE TABLE STORAGE_BIN
(

  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN         VARCHAR(20)  NOT NULL COMMENT '存儲貨格代碼',
  DESCRIPTION         VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STORAGE_BIN_TYPE_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯存儲貨格類型主鍵',
  STORAGE_LOCATION_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯存儲位置主鍵(一個存儲位置可放多個貨格)',
  SHELF_ROW           INT          DEFAULT NULL COMMENT '行',
  SHELF_COLUMN        INT          DEFAULT NULL COMMENT '列',
  CREATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR             VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER             VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格資料';

DROP TABLE IF EXISTS STORAGE_BIN_STATUS;
CREATE TABLE STORAGE_BIN_STATUS
(

  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  STORAGE_BIN         VARCHAR(20)  NOT NULL COMMENT '存儲貨格代碼',
  STATUS              VARCHAR(20)  NOT NULL COMMENT '存儲貨格狀態',
  CREATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '存儲貨格狀態';

DROP TABLE IF EXISTS ITEM_GROUP;
CREATE TABLE ITEM_GROUP
(

  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_GROUP       VARCHAR(20)  NOT NULL COMMENT '物料組代碼',
  DESCRIPTION      VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CONSUMPTION_MODE VARCHAR(10)  DEFAULT NULL COMMENT '耗用模式',
  CREATE_TIME      TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料組資料';

DROP TABLE IF EXISTS ITEM_TYPE;
CREATE TABLE ITEM_TYPE
(

  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_TYPE   VARCHAR(20)  NOT NULL COMMENT '物料類型代碼',
  MIX_ITEM    CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述(原材料、輔材、包材、中間品、半成品、成品、子設備、工具、備用件)',
  CREATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料類型資料';

DROP TABLE IF EXISTS ITEM;
CREATE TABLE ITEM
(

  HANDLE              VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM                VARCHAR(50)  NOT NULL COMMENT '物料代碼',
  DESCRIPTION         VARCHAR(200) DEFAULT NULL COMMENT '描述',
  MIX_ITEM            CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  INSPECTION_REQUIRED CHAR(1)      DEFAULT NULL COMMENT '是否必須檢驗（需要檢驗的物料創建庫存必須為限制庫存）',
  ITEM_STATUS         VARCHAR(20)  NOT NULL COMMENT '狀態',
  ITEM_TYPE_BO        VARCHAR(412) NOT NULL COMMENT '關聯物料類型主鍵',
  CONSUMPTION_MODE    VARCHAR(50)  DEFAULT NULL COMMENT '耗用模式(【列舉】先進先出、近效先出、後進先出)',
  UNIT_OF_MEASURE     VARCHAR(20)  DEFAULT NULL COMMENT '單位',
  CREATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME         TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR             VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER             VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料資料';

DROP TABLE IF EXISTS ITEM_GROUP_MEMBER;
CREATE TABLE ITEM_GROUP_MEMBER
(

  HANDLE        VARCHAR(412) NOT NULL COMMENT '主鍵',
  ITEM_GROUP_BO VARCHAR(412) NOT NULL COMMENT '關聯物料組主鍵',
  ITEM_BO       VARCHAR(412) NOT NULL COMMENT '關聯物料主鍵',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料組&物料關聯關係';

DROP TABLE IF EXISTS CONSUMPTION_MODE;
CREATE TABLE CONSUMPTION_MODE
(

  CONSUMPTION_MODE VARCHAR(50) NOT NULL COMMENT '耗用模式',
  DESCRIPTION      VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME      TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME      TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER          VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (CONSUMPTION_MODE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '物料耗用模式資料';

DROP TABLE IF EXISTS MEASURE_UNIT;
CREATE TABLE MEASURE_UNIT
(

  MEASURE_UNIT VARCHAR(20) NOT NULL COMMENT '計量單位',
  DESCRIPTION  VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME  TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME  TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR      VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER      VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (MEASURE_UNIT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '計量單位資料';

DROP TABLE IF EXISTS MEASURE_UNIT_MATRIX;
CREATE TABLE MEASURE_UNIT_MATRIX
(

  HANDLE            VARCHAR(412) NOT NULL COMMENT '主鍵',
  MEASURE_UNIT      VARCHAR(20)    DEFAULT NULL COMMENT '原始單位',
  DEST_MEASURE_UNIT VARCHAR(20)    DEFAULT NULL COMMENT '目標單位',
  TRANSFER_RATE     DECIMAL(38, 6) DEFAULT NULL COMMENT '轉換比率',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '單位轉換矩陣';

DROP TABLE IF EXISTS INVENTORY;
CREATE TABLE INVENTORY
(

  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',

  STATUS          VARCHAR(20)    DEFAULT NULL COMMENT '狀態',
  ITEM_BO         VARCHAR(412)   DEFAULT NULL COMMENT '關聯物料主鍵',
  UNIT_OF_MEASURE VARCHAR(20)    DEFAULT NULL COMMENT '單位（物料單位）',
  BATCH_NO        VARCHAR(50)    DEFAULT NULL COMMENT '批次號',
  VENDOR_BATCH_NO VARCHAR(50)    DEFAULT NULL COMMENT '供應商批次號',
  ORIGINAL_QTY    DECIMAL(38, 6) DEFAULT NULL COMMENT '原始批次數量',
  QTY_ON_HAND     DECIMAL(38, 6) DEFAULT NULL COMMENT '現有數量',
  VALID           CHAR(1)        DEFAULT NULL  COMMENT '是否有效庫存(一旦入貨格之後即視為有效庫存，不可刪除)',
  PRODUCTION_DATE DATE           DEFAULT NULL COMMENT '生產日期',
  EXPIRE_DATE     DATE           DEFAULT NULL COMMENT '失效日期',
  CREATE_TIME     TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME     TIMESTAMP      DEFAULT NULL COMMENT '更新日期',
  CREATOR         VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER         VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '庫存（物料批次）';

DROP TABLE IF EXISTS CARRIER_TYPE;
CREATE TABLE CARRIER_TYPE
(

  HANDLE       VARCHAR(412) NOT NULL COMMENT '主鍵',
  CARRIER_TYPE VARCHAR(20)  DEFAULT NULL COMMENT '載具類型',
  DESCRIPTION  VARCHAR(200) DEFAULT NULL COMMENT '描述',
  MIX_ITEM     CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  CREATE_TIME  TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME  TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR      VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER      VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '載具類型資料';

DROP TABLE IF EXISTS CARRIER;
CREATE TABLE CARRIER
(

  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',
  CARRIER         VARCHAR(20)  DEFAULT NULL COMMENT '載具',
  STATUS         VARCHAR(20)  DEFAULT NULL COMMENT '狀態',
  DESCRIPTION     VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CARRIER_TYPE_BO VARCHAR(412) DEFAULT NULL COMMENT '關聯載具類型主鍵',
  CREATE_TIME     TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME     TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR         VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER         VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '載具資料';

DROP TABLE IF EXISTS RESOURCE_TYPE;
CREATE TABLE RESOURCE_TYPE
(

  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESOURCE_TYPE VARCHAR(20)  NOT NULL COMMENT '資源類型代碼',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  CREATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源組組資料';

DROP TABLE IF EXISTS RESRCE;
CREATE TABLE RESRCE
(

  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESRCE      VARCHAR(50)  NOT NULL COMMENT '資源',
  DESCRIPTION VARCHAR(200) DEFAULT NULL COMMENT '描述',
  STATUS      VARCHAR(20)  DEFAULT NULL COMMENT '狀態',
  CREATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME TIMESTAMP    DEFAULT NULL COMMENT '更新日期',
  CREATOR     VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  UPDATER     VARCHAR(64)  DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源資料';

DROP TABLE IF EXISTS RESOURCE_TYPE_RESOURCE;
CREATE TABLE RESOURCE_TYPE_RESOURCE
(

  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  RESOURCE_TYPE_BO VARCHAR(412) NOT NULL COMMENT '關聯資源類型主鍵',
  RESOURCE_BO      VARCHAR(412) NOT NULL COMMENT '關聯資源主鍵',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '資源關聯資源類型';

DROP TABLE IF EXISTS STATUS;
CREATE TABLE STATUS
(

  HANDLE       VARCHAR(412) NOT NULL COMMENT '主鍵',
  STATUS       VARCHAR(412) NOT NULL COMMENT '狀態',
  STATUS_GROUP VARCHAR(64) DEFAULT NULL COMMENT '狀態組(RESRCE/ITEM/INVENTORY)',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '狀態資料';

DROP TABLE IF EXISTS STATUS_T;
CREATE TABLE STATUS_T
(

  HANDLE      VARCHAR(412) NOT NULL COMMENT '主鍵',
  STATUS_BO   VARCHAR(412)  NOT NULL COMMENT '狀態',
  LOCALE      VARCHAR(10)  NOT NULL COMMENT '區域',
  DESCRIPTION VARCHAR(200) DEFAULT '' COMMENT '描述',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '狀態描述多國翻譯';

DROP TABLE IF EXISTS HANDLING_UNIT;
CREATE TABLE HANDLING_UNIT
(

  HANDLE                    VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID               VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）數量改變則重建（不包含盤點）',
  CARRIER_BO                VARCHAR(412) NOT NULL COMMENT '關聯載具主鍵',
  STATUS                    VARCHAR(20)    DEFAULT NULL COMMENT '狀態（可用/不可用）',
  MIXED                     CHAR(1)      DEFAULT NULL COMMENT '可否混料',
  HANDLING_UNIT_CONTEXT_GBO VARCHAR(412)   DEFAULT NULL COMMENT '載具綁定對象主鍵(庫存批次號)',
  INVENTORY_QTY             DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME               TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  UPDATE_TIME               TIMESTAMP      DEFAULT NULL COMMENT '更新日期',
  CREATOR                   VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  UPDATER                   VARCHAR(64)    DEFAULT NULL COMMENT '更新人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元';

DROP TABLE IF EXISTS HANDLING_UNIT_SPEC;
CREATE TABLE HANDLING_UNIT_SPEC
(

  HANDLE      VARCHAR(412)   NOT NULL COMMENT '主鍵（處理單元標識）',
  CARRIER_BO                VARCHAR(412) NOT NULL COMMENT '關聯載具主鍵',
  WIDTH       DECIMAL(38, 6) NOT NULL COMMENT '寬度',
  HEIGHT      DECIMAL(38, 6) DEFAULT NULL COMMENT '高度',
  LENGTH      DECIMAL(38, 6) DEFAULT NULL COMMENT '長度',
  WEIGHT      DECIMAL(38, 6) DEFAULT NULL COMMENT '重量',
  CREATE_TIME TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  CREATOR     VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元規格記錄（處理單元過測量設備時記錄）';

DROP TABLE IF EXISTS HANDLING_UNIT_LOCATION;
CREATE TABLE HANDLING_UNIT_LOCATION
(

  HANDLE           VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID      VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  CARRIER_BO       VARCHAR(412) NOT NULL COMMENT '關聯載具主鍵',
  BIND_CONTEXT_GBO VARCHAR(412) DEFAULT NULL COMMENT '載具綁定對象主鍵（庫位元/資源/接駁站）',
  BIND_TYPE        VARCHAR(20)  DEFAULT NULL COMMENT '綁定類型（庫位/資源/接駁站）(載具目前對應是在小車/貨格/接駁站上（應該只有貨格，小車&接駁站資訊在WCS中管控）)',
  CREATE_TIME      TIMESTAMP    DEFAULT NULL COMMENT '創建日期',
  CREATOR          VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元位置綁定（記錄當前處理單元所在的貨格）';

DROP TABLE IF EXISTS HANDLING_UNIT_LOG;
CREATE TABLE HANDLING_UNIT_LOG
(

  HANDLE         VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID    VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  CARRIER_BO     VARCHAR(412) NOT NULL COMMENT '關聯載具主鍵',
  STATUS         VARCHAR(20)    DEFAULT NULL COMMENT '狀態（可用/不可用）',
  INVENTORY_BO   VARCHAR(412)   DEFAULT NULL COMMENT '庫存',
  STORAGE_BIN_BO VARCHAR(412)   DEFAULT NULL COMMENT '當前所屬貨格',
  INVENTORY_QTY  DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME    TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  CREATOR        VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元創建日誌記錄';

DROP TABLE IF EXISTS HANDLING_UNIT_CHANGE_LOG;
CREATE TABLE HANDLING_UNIT_CHANGE_LOG
(

  HANDLE         VARCHAR(412) NOT NULL COMMENT '主鍵',
  HANDLING_ID    VARCHAR(32)  NOT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  ACTION_CODE    VARCHAR(20)  NOT NULL COMMENT '變更事件代碼（創建/拆並垛/出入庫/盤點）',
  STORAGE_BIN_BO VARCHAR(412)   DEFAULT NULL COMMENT '當前所屬貨格',
  INVENTORY_QTY  DECIMAL(38, 6) DEFAULT NULL COMMENT '庫存數量',
  CREATE_TIME    TIMESTAMP      DEFAULT NULL COMMENT '創建日期',
  CREATOR        VARCHAR(64)    DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '處理單元變更日誌記錄';

DROP TABLE IF EXISTS ERP_POSTING_LOG;
CREATE TABLE ERP_POSTING_LOG
(

  HANDLE        VARCHAR(412) NOT NULL COMMENT '主鍵',
  NAME          VARCHAR(50)  DEFAULT NULL COMMENT '互動名稱',
  TYPE          VARCHAR(10)  DEFAULT NULL COMMENT '互動類型',
  RESOURCE_BO   VARCHAR(200) DEFAULT NULL COMMENT '資源',
  KEY1          VARCHAR(200) DEFAULT NULL COMMENT '描述',
  KEY2          VARCHAR(200) DEFAULT NULL COMMENT '資源',
  KEY3          VARCHAR(200) DEFAULT NULL COMMENT '資源',
  MESSAGE_ID        VARCHAR(412) DEFAULT NULL COMMENT '請求ID',
  RESULT        VARCHAR(10)  DEFAULT NULL COMMENT '互動返回代碼',
  MSG           VARCHAR(200) DEFAULT NULL COMMENT '回饋資訊',
  REQUEST_BODY  TEXT         DEFAULT NULL COMMENT '請求內容',
  RESPONSE_BODY TEXT         DEFAULT NULL COMMENT '返回內容',
  PROCESS_TIME  LONG         DEFAULT NULL COMMENT '處理時間（毫秒）',
  DATE_CREATE   DATETIME COMMENT '創建日期',
  CREATOR       VARCHAR(64)  DEFAULT NULL COMMENT '創建人員',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'ERP互動日誌';


DROP TABLE IF EXISTS INVENTORY_LOG;
CREATE TABLE INVENTORY_LOG
(

  HANDLE          VARCHAR(412) NOT NULL COMMENT '主鍵',
  BATCH_NO        VARCHAR(50)  NOT NULL COMMENT '批次號',
  STATUS          VARCHAR(20)    DEFAULT NULL COMMENT '狀態',
  HANDLING_ID     VARCHAR(32)    DEFAULT NULL COMMENT '處理單元標識（同一處理單元多物料同一個ID）',
  ITEM_BO         VARCHAR(412)   DEFAULT NULL COMMENT '關聯物料主鍵',
  UNIT_OF_MEASURE VARCHAR(20)    DEFAULT NULL COMMENT '批次號',
  ACTION_CODE     VARCHAR(10)    DEFAULT NULL COMMENT '變更事件代碼',
  VENDOR_BATCH_NO VARCHAR(50)    DEFAULT NULL COMMENT '轉換比率',
  ORIGINAL_QTY    DECIMAL(38, 6) DEFAULT NULL COMMENT '原始批次數量',
  QTY_ON_HAND     DECIMAL(38, 6) DEFAULT NULL COMMENT '現有數量',
  PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '庫存變更歷史';

DROP TABLE IF EXISTS MQ_LOG;
CREATE TABLE MQ_LOG (
                      HANDLE	VARCHAR(412),
                      SERVER_ID	VARCHAR(50) COMMENT 'IP地址',
                      BROKER	VARCHAR(50) COMMENT 'MQ地址',
                      QUEUE	VARCHAR(50) COMMENT '佇列名稱',
                      MESSAGE_ID	 VARCHAR(50) COMMENT '訊息ID',
                      CORRELATION_ID VARCHAR(32) COMMENT '關聯ID',
                      MESSAGE_BODY	VARCHAR(2000) COMMENT '訊息內容',
                      RESPONSE_BODY VARCHAR(2000) COMMENT '返回訊息內容',
                      RESULT	VARCHAR(5) COMMENT '結果',
                      ERROR	VARCHAR(2000) COMMENT '錯誤資訊',
                      MESSAGE_TYPE	VARCHAR(50) COMMENT '訊息類型',
                      CREATED_DATE_TIME	DATETIME COMMENT '創建日期',
                      PRIMARY KEY (HANDLE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = 'MQ日誌';

DROP TABLE IF EXISTS SHELF_OFF_LOG;
CREATE TABLE SHELF_OFF_LOG (
                             HANDLE	VARCHAR(412),
                             MESSAGE_ID	VARCHAR(36) COMMENT 'request id',
                             CORRELATION_ID VARCHAR(36) COMMENT 'correlation id',
                             ACTION_CODE VARCHAR(10) COMMENT 'action code',
                             CARRIER	VARCHAR(50) COMMENT 'carrier',
                             STORAGE_LOCATION	VARCHAR(50) COMMENT 'storage location',
                             STORAGE_BIN	VARCHAR(50) COMMENT 'storage bin',
                             INVENTORY_BO	VARCHAR(412) COMMENT 'inventory handle',
                             QTY	DECIMAL(38, 6) COMMENT 'qty',
                             SPLIT	CHAR(1) COMMENT 'split',
                             EXECUTE_RESULT	CHAR(1) COMMENT 'execute result',
                             CREATE_TIME	 timestamp COMMENT 'create time',
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

create table if not exists ds_carrier_status
(
  HANDLE varchar(412) not null primary key,
  ITEM varchar(50) null comment 'material',
  CARRIER varchar(50) null comment 'carrier',
  OPERATION varchar(50) null comment 'operation',
  STATUS varchar(50) null comment 'status'
  )
  comment 'carrier stataus summary' charset=utf8mb4;
-- end dashboard----------------------------------------------------------------------------------------
INSERT INTO warehouse (HANDLE, WAREHOUSE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('WarehouseBO:1001', '1001', '默認倉庫', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');

INSERT INTO status (HANDLE, STATUS, STATUS_GROUP)
VALUES ('StatusBO:AVAILABLE,INVENTORY', 'AVAILABLE', 'INVENTORY');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP)
VALUES ('StatusBO:FROZEN,INVENTORY', 'FROZEN', 'INVENTORY');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP)
VALUES ('StatusBO:RESTRICT,INVENTORY', 'RESTRICT', 'INVENTORY');

INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:AVAILABLE,ITEM', 'AVAILABLE', 'ITEM');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:FROZEN,ITEM', 'FROZEN', 'ITEM');

INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:301,RESOURCE', '301', 'RESOURCE');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:302,RESOURCE', '302', 'RESOURCE');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:303,RESOURCE', '303', 'RESOURCE');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:304,RESOURCE', '304', 'RESOURCE');
INSERT INTO status (HANDLE, STATUS, STATUS_GROUP) VALUES('StatusBO:305,RESOURCE', '305', 'RESOURCE');


INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:AVAILABLE,INVENTORY,en-US', 'StatusBO:AVAILABLE,INVENTORY', 'en-US', 'Available');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:AVAILABLE,INVENTORY,zh-CN', 'StatusBO:AVAILABLE,INVENTORY', 'zh-CN', '可用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:AVAILABLE,INVENTORY,zh-TW', 'StatusBO:AVAILABLE,INVENTORY', 'zh-TW', '可用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:FROZEN,INVENTORY,en-US', 'StatusBO:FROZEN,INVENTORY', 'en-US', 'Frozen');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:FROZEN,INVENTORY,zh-CN', 'StatusBO:FROZEN,INVENTORY', 'zh-CN', '凍結');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:FROZEN,INVENTORY,zh-TW', 'StatusBO:FROZEN,INVENTORY', 'zh-TW', '凍結');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:RESTRICT,INVENTORY,en-US', 'StatusBO:RESTRICT,INVENTORY', 'en-US', 'Restrict');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:RESTRICT,INVENTORY,zh-CN', 'StatusBO:RESTRICT,INVENTORY', 'zh-CN', '限制');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION)
VALUES ('StatusBO:RESTRICT,INVENTORY,zh-TW', 'StatusBO:RESTRICT,INVENTORY', 'zh-TW', '限制');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:AVAILABLE,ITEM,en-US', 'StatusBO:AVAILABLE,ITEM', 'en-US', 'Available');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:AVAILABLE,ITEM,zh-CN', 'StatusBO:AVAILABLE,ITEM', 'zh-CN', '可用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:AVAILABLE,ITEM,zh-TW', 'StatusBO:AVAILABLE,ITEM', 'zh-TW', '可用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:FROZEN,ITEM,en-US', 'StatusBO:FROZEN,ITEM', 'en-US', 'Frozen');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:FROZEN,ITEM,zh-CN', 'StatusBO:FROZEN,ITEM', 'zh-CN', '凍結');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:FROZEN,ITEM,zh-TW', 'StatusBO:FROZEN,ITEM', 'zh-TW', '凍結');

INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:301,RESOURCE,en-US', 'StatusBO:301,RESOURCE', 'en-US', 'Enabled');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:301,RESOURCE,zh-CN', 'StatusBO:301,RESOURCE', 'zh-CN', '已啟用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:301,RESOURCE,zh-TW', 'StatusBO:301,RESOURCE', 'zh-TW', '已啟用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:302,RESOURCE,en-US', 'StatusBO:302,RESOURCE', 'en-US', 'Disabled');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:302,RESOURCE,zh-CN', 'StatusBO:302,RESOURCE', 'zh-CN', '已禁用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:302,RESOURCE,zh-TW', 'StatusBO:302,RESOURCE', 'zh-TW', '已禁用');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:303,RESOURCE,en-US', 'StatusBO:303,RESOURCE', 'en-US', 'Hold');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:303,RESOURCE,zh-CN', 'StatusBO:303,RESOURCE', 'zh-CN', '保留');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:303,RESOURCE,zh-TW', 'StatusBO:303,RESOURCE', 'zh-TW', '保留');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:304,RESOURCE,en-US', 'StatusBO:304,RESOURCE', 'en-US', 'Assembled');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:304,RESOURCE,zh-CN', 'StatusBO:304,RESOURCE', 'zh-CN', '已裝配');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:304,RESOURCE,zh-TW', 'StatusBO:304,RESOURCE', 'zh-TW', '已裝配');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:305,RESOURCE,en-US', 'StatusBO:305,RESOURCE', 'en-US', 'Scrapped');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:305,RESOURCE,zh-CN', 'StatusBO:305,RESOURCE', 'zh-CN', '已報廢');
INSERT INTO status_t (HANDLE, STATUS_BO, LOCALE, DESCRIPTION) VALUES('StatusBO:305,RESOURCE,zh-TW', 'StatusBO:305,RESOURCE', 'zh-TW', '已報廢');


INSERT INTO consumption_mode (CONSUMPTION_MODE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('FIFO', '先進先出', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO consumption_mode (CONSUMPTION_MODE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('NTFO', '近效期先出', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO consumption_mode (CONSUMPTION_MODE, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('LIFO', '後進先出', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');

INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1001', '1001', 'N', '原材料', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1002', '1002', 'N', '輔材', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1003', '1003', 'N', '包材', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1004', '1004', 'N', '中間品', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1005', '1005', 'N', '半成品', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');
INSERT INTO item_type (HANDLE, ITEM_TYPE, MIX_ITEM, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('ItemTypeBO:1006', '1006', 'N', '成品', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');

INSERT INTO measure_unit (MEASURE_UNIT, DESCRIPTION, CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER) VALUES('EA', 'EA', '2020-07-13 19:31:00.000', '2020-07-13 19:31:00.000', 'Administrator', 'Administrator');

INSERT INTO sys_role (role_id, role_name, role_code, role_desc, ds_type, ds_scope, create_time, update_time, del_flag, tenant_id) VALUES(1, '管理員', 'ROLE_ADMIN', '管理員', '3', '2', '2017-10-30 04:45:51.000', '2018-12-27 04:09:11.000', 'N', '1');

INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1000, '許可權管理', NULL, '', -1, 'sap-icon://permission', 0, 'N', '0', '2018-09-28 21:29:53.000', '2018-09-28 21:53:01.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1100, '用戶管理', NULL, 'sysUserMaintain', 1000, 'sap-icon://user-settings', 1, 'Y', '0', '2017-11-03 11:24:37.000', '2019-06-25 03:36:36.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1101, '用戶修改', 'sys_user_edit', NULL, 1100, NULL, NULL, 'N', '1', '2017-11-08 23:52:48.000', '2018-09-28 22:06:37.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1200, '功能表管理', NULL, 'sysMenuMaintain', 1000, 'sap-icon://menu', 2, 'N', '0', '2017-11-08 23:57:27.000', '2019-06-25 03:42:07.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1201, '選單新增', 'sys_menu_add', NULL, 1200, NULL, NULL, 'N', '1', '2017-11-09 00:15:53.000', '2018-09-28 22:07:16.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1202, '選單修改', 'sys_menu_edit', NULL, 1200, NULL, NULL, 'N', '1', '2017-11-09 00:16:23.000', '2018-09-28 22:07:18.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1203, '選單刪除', 'sys_menu_delete', NULL, 1200, NULL, NULL, 'N', '1', '2017-11-09 00:16:43.000', '2018-09-28 22:07:22.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1300, '角色管理', NULL, 'sysRoleMaintain', 1000, 'sap-icon://role', 3, 'N', '0', '2017-11-09 00:13:37.000', '2018-09-28 22:00:48.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, NULL, 'N', '1', '2017-11-09 00:14:18.000', '2018-09-28 22:07:46.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, NULL, 'N', '1', '2017-11-09 00:14:41.000', '2018-09-28 22:07:49.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1303, '角色刪除', 'sys_role_del', NULL, 1300, NULL, NULL, 'N', '1', '2017-11-09 00:14:59.000', '2018-09-28 22:07:53.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(1304, '分配許可權', 'sys_role_perm', NULL, 1300, NULL, NULL, 'N', '1', '2018-04-20 20:22:55.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10013, '倉庫管理', NULL, 'stayInReport', -1, 'sap-icon://inventory', 10, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10014, '庫存接收', NULL, 'inventoryReceive', 10013, NULL, 10, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10015, '載具綁定', NULL, 'carrierBind', 10013, NULL, 20, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10016, '創建', 'inventory_add', NULL, 10014, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10017, '刪除', 'inventory_delete', NULL, 10014, NULL, 999, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10018, '綁定', 'handling_unit_carrier_bind', NULL, 10015, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10022, '解綁', 'handling_unit_carrier_unbind', NULL, 10015, NULL, 999, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10026, '系統領料', NULL, 'materialRequisition', 10013, NULL, 30, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10027, '確認', 'material_requisition_confirm', NULL, 10026, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10028, '載具維護', NULL, 'carrierMaintain', 10071, NULL, 200, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10029, '新增', 'carrier_add', 'carrierMaintain', 10028, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10030, '修改', 'carrier_edit', 'carrierMaintain', 10028, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10031, '刪除', 'carrier_delete', 'carrierMaintain', 10028, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10032, '載具類型維護', NULL, 'carrierTypeMaintain', 10071, NULL, 210, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10033, '新增', 'carrier_type_add', NULL, 10032, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10034, '修改', 'carrier_type_edit', NULL, 10032, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10035, '刪除', 'carrier_type_delete', NULL, 10032, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10036, '物料組維護', NULL, 'itemGroupMaintain', 10071, NULL, 220, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10037, '新增', 'item_group_add', NULL, 10036, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10038, '修改', 'item_group_edit', NULL, 10036, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10039, '刪除', 'item_group_delete', NULL, 10036, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10040, '物料維護', NULL, 'itemMaintain', 10071, NULL, 230, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10041, '新增', 'item_add', NULL, 10040, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10042, '修改', 'item_edit', NULL, 10040, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10043, '刪除', 'item_delete', NULL, 10040, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10044, '資源維護', NULL, 'resrceMaintain', 10071, NULL, 250, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10045, '新增', 'resrce_add', NULL, 10044, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10046, '修改', 'resrce_edit', NULL, 10044, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10047, '刪除', 'resrce_delete', NULL, 10044, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10048, '資源類型維護', NULL, 'resourceTypeMaintain', 10071, NULL, 240, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10049, '新增', 'resource_type_add', NULL, 10048, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10050, '修改', 'resource_type_edit', NULL, 10048, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10051, '刪除', 'resource_type_delete', NULL, 10048, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10052, '貨格維護', NULL, 'storageBinMaintain', 10071, NULL, 280, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10053, '新增', 'storage_bin_add', NULL, 10052, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10054, '修改', 'storage_bin_edit', NULL, 10052, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10055, '刪除', 'storage_bin_delete', NULL, 10052, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10056, '存儲地點維護', NULL, 'storageLocationMaintain', 10071, NULL, 260, 'N', '0', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10057, '新增', 'storage_location_add', NULL, 10056, NULL, 10, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10058, '修改', 'storage_location_edit', NULL, 10056, NULL, 20, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10059, '刪除', 'storage_location_delete', NULL, 10056, NULL, 30, 'N', '1', '2018-09-28 22:13:23.000', '2018-09-28 22:13:23.000', 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10060, '貨格類型維護', NULL, 'storageBinTypeMaintain', 10071, NULL, 270, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10061, '新增', 'storage_bin_type_add', NULL, 10060, NULL, 10, 'N', '1', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10062, '修改', 'storage_bin_type_edit', NULL, 10060, NULL, 20, 'N', '1', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10063, '刪除', 'storage_bin_type_delete', NULL, 10060, NULL, 30, 'N', '1', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10065, '報表管理', NULL, NULL, -1, 'sap-icon://trip-report', 999, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10066, '倉庫查詢報表', NULL, 'warehouseReport', 10065, '', 10, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10067, '待入庫查詢報表', NULL, 'stayInReport', 10065, NULL, 20, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10068, '看板', NULL, NULL, -1, 'sap-icon://bbyd-dashboard', 1000, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10069, '待入/出庫物料', NULL, 'biz/dashboard/pages/workshopInfra.html', 10068, NULL, 10, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10070, '產線狀態', NULL, 'biz/dashboard/pages/dayInfra.html', 10068, NULL, 20, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10071, '資料管理', NULL, NULL, -1, 'sap-icon://database', 5, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10072, '庫位轉移', NULL, 'storageChange', 10013, NULL, 40, 'N', '0', NULL, NULL, 'N');
INSERT INTO sys_menu (menu_id, name, permission, `path`, parent_id, icon, sort, keep_alive, `type`, create_time, update_time, del_flag) VALUES(10073, '確認', 'storage_change_confirm', NULL, 10072, NULL, 10, 'N', '1', NULL, NULL, 'N');
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

INSERT INTO sys_user (user_id, username, first_name, last_name, password, salt, phone, avatar, dept_id, create_time, update_time, lock_flag, del_flag, tenant_id) VALUES(1, 'Administrator', NULL, 'Administrator', NULL, NULL, NULL, NULL, NULL, '2020-06-22 22:01:56.000', '2020-07-16 13:29:12.223', 'N', 'N', NULL);

INSERT INTO sys_user_role (user_id, role_id) VALUES(1, 1);
