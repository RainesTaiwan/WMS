package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 *
 * </p>
 *
 * @author Glanz
 *
 */
public class ReceiveStation extends Model<ReceiveStation> {

   private static final long serialVersionUID = 1L;

   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
   @TableField("RECEIVE_STATION")
   private String receiveStation;
   @TableField("DESCRIPTION")
   private String description;
   @TableField("STATUS")
   private String status;

   // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
   // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
   // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
   // 任務4 (機械手臂): ROBOTIC_ARM
   // 任務5 (無人運輸車): AGV_TRANS
   // 任務目標
   @TableField("TASK_GOAL")
   private String taskGoal;
   // 目前任務
   @TableField("NOW_TASK")
   private String nowTask;

   //State: 0=設備開機啟動中；1=設備開機完成/待命中；2=設備故障；3=設備正在移動棧板
   @TableField("STATE")
   private String state;

   //CV1運行方向: 1:CV1-->CV2 2:CV1-->退出
   @TableField("CV1_DIRECTION")
   private String cv1Direction;
   //設備工作遏制: 當HOLD值=0時，設備照常運作；當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新；
   @TableField("CV1_HOLD")
   private String cv1Hold;
   //驅動棧板移動:
   // MOVE值=1時為Ready；MOVE完成後值會從4更新為1
   // MOVE值=2時為MOVE Query；
   // MOVE值=3時為傳輸異常
   // MOVE值=4時CV運轉中
   @TableField("CV1_MOVE")
   private String cv1Move;
   @TableField("CV1_PALLET_SENSOR")
   private String cv1PalletSensor;

   //CV2運行方向: 1:CV2-->CV3(入庫) 2:CV2-->CV1(入庫_退料) 3.CV2->CV1(出庫)
   @TableField("CV2_DIRECTION")
   private String cv2Direction;
   //設備工作遏制: 當HOLD值=0時，設備照常運作；當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新；
   @TableField("CV2_HOLD")
   private String cv2Hold;
   //驅動棧板移動:
   // MOVE值=1時為Ready；MOVE完成後值會從4更新為1
   // MOVE值=2時為MOVE Query；
   // MOVE值=3時為傳輸異常
   // MOVE值=4時CV運轉中
   @TableField("CV2_MOVE")
   private String cv2Move;
   @TableField("CV2_PALLET_SENSOR")
   private String cv2PalletSensor;

   //CV3運行方向: 1:CV3-->CV2(出庫) 2:CV3-->AGV(入庫) 3:AGV-->CV3(出庫)
   @TableField("CV3_DIRECTION")
   private String cv3Direction;
   //設備工作遏制: 當HOLD值=0時，設備照常運作；當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新；
   @TableField("CV3_HOLD")
   private String cv3Hold;
   //驅動棧板移動:
   // MOVE值=1時為Ready；MOVE完成後值會從4更新為1
   // MOVE值=2時為MOVE Query；
   // MOVE值=3時為傳輸異常
   // MOVE值=4時CV運轉中
   @TableField("CV3_MOVE")
   private String cv3Move;
   @TableField("CV3_PALLET_SENSOR")
   private String cv3PalletSensor;

   @TableField("BUTTON")
   private String button;

   @TableField("HEIGHT")
   private String height;
   @TableField("WIDTH")
   private String width;
   @TableField("LENGTH")
   private String length;
   @TableField("WEIGHT")
   private String weight;

   @TableField("CREATE_USER")
   private String createUser;
   @TableField("CREATED_TIME")
   private Date createdTime;
   @TableField("UPDATE_USER")
   private String updateUser;
   @TableField("UPDATED_TIME")
   private Date updatedTime;

   public String getHandle() {
      return handle;
   }
   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getReceiveStation() {
      return receiveStation;
   }
   public void setReceiveStation(String receiveStation) {
      this.receiveStation = receiveStation;
   }

   public String getDescription() {
      return description;
   }
   public void setDescription(String description) {
      this.description = description;
   }

   public String getStatus() {
      return status;
   }
   public void setStatus(String status) {
      this.status = status;
   }

   public String getTaskGoal() {
      return taskGoal;
   }
   public void setTaskGoal(String taskGoal) {
      this.taskGoal = taskGoal;
   }

   public String getNowTask() {
      return nowTask;
   }
   public void setNowTask(String nowTask) {
      this.nowTask = nowTask;
   }

   public String getState() {
      return state;
   }
   public void setState(String state) {
      this.state = state;
   }

   public String getCv1Direction() {
      return cv1Direction;
   }
   public void setCv1Direction(String cv1Direction) {
      this.cv1Direction = cv1Direction;
   }

   public String getCv1Hold() {
      return cv1Hold;
   }
   public void setCv1Hold(String cv1Hold) {
      this.cv1Hold = cv1Hold;
   }

   public String getCv1Move() {
      return cv1Move;
   }
   public void setCv1Move(String cv1Move) {
      this.cv1Move = cv1Move;
   }

   public String getCv1PalletSensor() {
      return cv1PalletSensor;
   }
   public void setCv1PalletSensor(String cv1PalletSensor) {
      this.cv1PalletSensor = cv1PalletSensor;
   }

   public String getCv2Direction() {
      return cv2Direction;
   }
   public void setCv2Direction(String cv2Direction) {
      this.cv2Direction = cv2Direction;
   }

   public String getCv2Hold() {
      return cv2Hold;
   }
   public void setCv2Hold(String cv2Hold) {
      this.cv2Hold = cv2Hold;
   }

   public String getCv2Move() {
      return cv2Move;
   }
   public void setCv2Move(String cv2Move) {
      this.cv2Move = cv2Move;
   }

   public String getCv2PalletSensor() {
      return cv2PalletSensor;
   }
   public void setCv2PalletSensor(String cv2PalletSensor) {
      this.cv2PalletSensor = cv2PalletSensor;
   }

   public String getCv3Direction() {
      return cv3Direction;
   }
   public void setCv3Direction(String cv3Direction) {
      this.cv3Direction = cv3Direction;
   }

   public String getCv3Hold() {
      return cv3Hold;
   }
   public void setCv3Hold(String cv3Hold) {
      this.cv3Hold = cv3Hold;
   }

   public String getCv3Move() {
      return cv3Move;
   }
   public void setCv3Move(String cv3Move) {
      this.cv3Move = cv3Move;
   }

   public String getCv3PalletSensor() {
      return cv3PalletSensor;
   }
   public void setCv3PalletSensor(String cv3PalletSensor) {
      this.cv3PalletSensor = cv3PalletSensor;
   }

   public String getHeight() {
      return height;
   }
   public void setHeight(String height) {
      this.height = height;
   }

   public String getWidth() {
      return width;
   }
   public void setWidth(String width) {
      this.width = width;
   }

   public String getLength() {
      return length;
   }
   public void setLength(String length) {
      this.length = length;
   }

   public String getWeight() {
      return weight;
   }
   public void setWeight(String weight) {
      this.weight = weight;
   }

   public String getButton() {
      return button;
   }
   public void setButton(String button) {
      this.button = button;
   }

   public String getCreateUser() {
      return createUser;
   }
   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getCreatedTime() {
      return createdTime;
   }
   public void setCreatedTime(Date createdTime) {
      this.createdTime = createdTime;
   }

   public String getUpdateUser() {
      return updateUser;
   }
   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public Date getUpdatedTime() {
      return updatedTime;
   }
   public void setUpdatedTime(Date updatedTime) {
      this.updatedTime = updatedTime;
   }

   public static final String HANDLE = "HANDLE";

   public static final String RECEIVE_STATION = "RECEIVE_STATION";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String STATUS = "STATUS";

   public static final String TASK_GOAL = "TASK_GOAL";

   public static final String NOW_TASK = "NOW_TASK";

   public static final String STATE = "STATE";

   public static final String CV1_DIRECTION = "CV1_DIRECTION";
   public static final String CV1_HOLD = "CV1_HOLD";
   public static final String CV1_MOVE = "CV1_MOVE";
   public static final String CV1_PALLET_SENSOR = "CV1_PALLET_SENSOR";

   public static final String CV2_DIRECTION = "CV2_DIRECTION";
   public static final String CV2_HOLD = "CV2_HOLD";
   public static final String CV2_MOVE = "CV2_MOVE";
   public static final String CV2_PALLET_SENSOR = "CV2_PALLET_SENSOR";

   public static final String CV3_DIRECTION = "CV3_DIRECTION";
   public static final String CV3_HOLD = "CV3_HOLD";
   public static final String CV3_MOVE = "CV3_MOVE";
   public static final String CV3_PALLET_SENSOR = "CV3_PALLET_SENSOR";

   public static final String BUTTON = "BUTTON";

   public static final String HEIGHT = "HEIGHT";
   public static final String WIDTH = "WIDTH";
   public static final String LENGTH = "LENGTH";
   public static final String WEIGHT = "WEIGHT";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATED_TIME = "CREATED_TIME";

   public static final String UPDATE_USER = "UPDATE_USER";

   public static final String UPDATED_TIME = "UPDATED_TIME";

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "ReceiveStation{" +
              "handle = " + handle +
              ", receiveStation = " + receiveStation +
              ", description = " + description +
              ", status = " + status +
              ", taskGoal = " + taskGoal +
              ", nowTask = " + nowTask +
              ", state = " + state +
              ", cv1Direction = " + cv1Direction +
              ", cv1Hold = " + cv1Hold +
              ", cv1Move = " + cv1Move +
              ", cv1PalletSensor = " + cv1PalletSensor +
              ", cv2Direction = " + cv2Direction +
              ", cv2Hold = " + cv2Hold +
              ", cv2Move = " + cv2Move +
              ", cv2PalletSensor = " + cv2PalletSensor +
              ", cv3Direction = " + cv3Direction +
              ", cv3Hold = " + cv3Hold +
              ", cv3Move = " + cv3Move +
              ", cv3PalletSensor = " + cv3PalletSensor +
              ", button = " + button +
              ", height = " + height +
              ", width = " + width +
              ", length = " + length +
              ", weight = " + weight +
              ", createUser = " + createUser +
              ", createdTime = " + createdTime +
              ", updateUser = " + updateUser +
              ", updatedTime = " + updatedTime +
              "}";
   }
}