package com.sap.ewm.sys.constants;

/**
 * @author syngna
 * @date 2019-04-28
 * <p>
 * 快取的key 常量
 */
public interface CacheConstants {

	/**
	 * 全域性快取，在快取名稱上加上該字首表示該快取不區分租戶，比如:
	 * <p/>
	 * {@code @Cacheable(value = CacheConstants.GLOBALLY+CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")}
	 */
	String GLOBALLY = "gl:";

	/**
	 * 驗證碼字首
	 */
	String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";


	/**
	 * 菜單資訊快取
	 */
	String MENU_DETAILS = "menu_details";

	/**
	 * 使用者資訊快取
	 */
	String USER_DETAILS = "user_details";

	/**
	 * 字典資訊快取
	 */
	String DICT_DETAILS = "dict_details";

	/**
	 * spring boot admin 事件key
	 */
	String EVENT_KEY = "event_key";

	/**
	 * 路由存放
	 */
	String ROUTE_KEY = "gateway_route_key";

	/**
	 * redis reload 事件
	 */
	String ROUTE_REDIS_RELOAD_TOPIC = "gateway_redis_route_reload_topic";

	/**
	 * 記憶體reload 時間
	 */
	String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

	/**
	 * 參數快取
	 */
	String PARAMS_DETAILS = "params_details";

	/**
	 * 租戶快取 (不區分租戶)
	 */
	String TENANT_DETAILS = GLOBALLY + "tenant_details";
}
