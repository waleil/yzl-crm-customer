package cn.net.yzl.crm.customer.interceptor.auth;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.UUIDGenerator;
import cn.net.yzl.crm.customer.config.AppIdsConfig;
import cn.net.yzl.crm.customer.config.QueryIds;
import cn.net.yzl.logger.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@Slf4j
@RefreshScope
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AppIdsConfig appIdsConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 验证请求方的 AppId
        String appId = request.getHeader("appId");
        Set<String> appIdsSet = appIdsConfig.getIdsSet();
        if (StringUtils.isBlank(appId) || CollectionUtils.isEmpty(appIdsSet) || !appIdsSet.contains(appId)) {
            returnJson(response, ComResponse.fail(ResponseCodeEnums.AUTHOR_ERROR_CODE));
            return false;
        }

        String spanId = UUIDGenerator.getUUID();
        String traceId = request.getHeader("traceId");

        QueryIds.tranceId.set(traceId);
        QueryIds.spanId.set(spanId);

        log.info("{requestServer:{},traceId:{},spanId:{}}", appId, traceId, spanId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        QueryIds.tranceId.remove();
        QueryIds.spanId.remove();
    }


    private void returnJson(HttpServletResponse response, ComResponse<Object> result) {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JacksonUtil.toJsonString(result));
        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}