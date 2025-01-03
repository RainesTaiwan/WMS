package com.sap.ewm.core.security.interceptor;


import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.security.PermissionSubject;
import com.sap.ewm.core.security.SecurityInstance;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.security.api.IUser;
import com.sap.security.api.UMFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created by Syngna on 2020/06/16 22:25
 * 許可權攔截器
 */
public class SecurityInterceptor implements HandlerInterceptor {

    public static TransmittableThreadLocal<SecurityInstance> threadLocal = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = null;
        SecurityInstance securityInstance = new SecurityInstance();
        if(CommonMethods.isLocal()) {
            userId = "Administrator";
        } else {
            userId = getUserId(request, response);
        }
        securityInstance.setUser(userId);
        threadLocal.set( securityInstance );
        //---------------------------------------------------------------------------------------------

        // 驗證許可權
        if (this.hasPermission(request, response, handler, userId)) {
            return true;
        }
        // response.getWriter().write("{header:{code: 1, message: 'ssss'}}");
        response.sendError(HttpStatus.FORBIDDEN.value(), "使用者" + userId + "無許可權操作此功能");
        return false;
    }

    private String getUserId(HttpServletRequest request, HttpServletResponse response) {
        String userId = null;
        Principal principal = request.getUserPrincipal();
        if(principal == null || StrUtil.isBlank(principal.getName())) {
            IUser iUser = UMFactory.getAuthenticator().getLoggedInUser(request, response);
            if(iUser != null) {
                userId = iUser.getName();
            }
        } else if(principal != null) {
            userId = principal.getName();
        }
        return userId;
    }

    /**
     * 是否有許可權
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response, Object handler, String userId) throws IOException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 獲取方法上的註解
            Secured secured = handlerMethod.getMethod().getAnnotation(Secured.class);
            // 如果方法上的註解為空 則獲取類的註解
            if (secured == null) {
                secured = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Secured.class);
            }
            // 如果標記了註解，則判斷許可權
            if (secured == null) {
                return true;
            }

            if (secured.loginCheck()) {
                if (StrUtil.isBlank(userId)) {
                    return false;
                }
            }
            return validatePermissions(secured, userId);
        }

        return true;
    }

    private boolean validatePermissions(Secured secured, String validateUser) throws BusinessException {
        if ("".equals(secured.value())) {
            return true;
        }

        //String authorizedUserId = ctx.getHeaderString(APP_USER);
        //boolean authorized = authorizedUserId == null ? false : true;
        String authType = secured.authType();
        if (authType == null) {
            return true;
        }
        if ("MENU".equals(secured.authType())) {
            List<String> permissionList = StringUtils.parseList(secured.value(), ",");
            return PermissionSubject.getInstance().hasPermission(validateUser, permissionList);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        threadLocal.remove();
    }
}
