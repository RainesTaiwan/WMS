

package com.sap.ewm.sys.constants;

/**
 * @author syngna
 * @date 2017/10/29
 */
public interface CommonConstants {
	/**
	 * header 中租戶ID
	 */
	String TENANT_ID = "TENANT-ID";

	/**
	 * header 中版本資訊
	 */
	String VERSION = "VERSION";

	/**
	 * 租戶ID
	 */
	Integer TENANT_ID_1 = 1;

	/**
	 * 刪除
	 */
	String STATUS_DEL = "Y";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "N";

	/**
	 * 鎖定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 選單
	 */
	String MENU = "0";

	/**
	 * 選單樹根節點
	 */
	Integer MENU_TREE_ROOT_ID = -1;

	/**
	 * 編碼
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "ewm-ui";

	/**
	 * 公共參數
	 */
	String PIG_PUBLIC_PARAM_KEY = "PIG_PUBLIC_PARAM_KEY";

	/**
	 * 成功標記
	 */
	Integer SUCCESS = 0;
	/**
	 * 失敗標記
	 */
	Integer FAIL = 1;

	/**
	 * 預設儲存bucket
	 */
	String BUCKET_NAME = "syngna";

	String Y = "Y";

	String N = "N";

	String NWA_USER_ID_PREFIX = "USER.PRIVATE_DATASOURCE.un:";

	String NWA_USER_ACCOUNT_ID_PREFIX = "UACC.PRIVATE_DATASOURCE.un:";

	String USER_PERMISSION_CACHE_KEY = "USER_PERMISSION_CACHE";
}
