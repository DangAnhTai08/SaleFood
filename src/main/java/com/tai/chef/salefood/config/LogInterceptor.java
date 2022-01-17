//package com.tai.chef.salefood.config;
//
//import com.tai.chef.salefood.util.SecurityUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.UUID;
//
//import static org.springframework.boot.web.servlet.server.Encoding.Type.REQUEST;
//
//@Slf4j
//public class LogInterceptor extends HandlerInterceptorAdapter {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // For all dispatchers that are not "REQUEST" like "ERROR", do
//        // an early return which prevents the preHandle function from
//        // running multiple times
//        if (!request.getDispatcherType().name().equals(REQUEST.name()))
//            return false;
//
//        // get start time
//        long startTimeRequest = System.currentTimeMillis();
//        request.setAttribute("time-request", String.valueOf(startTimeRequest));
//
//        // generate a random request-id
//        UUID uuid = UUID.randomUUID();
//        request.setAttribute("request-id", uuid);
//
//        log.info("Start request-id: {}, method: {}, path: {}, client-ip: {}",
//                uuid,
//                request.getMethod(),
//                request.getRequestURI(),
//                SecurityUtil.getClientIP(request)
//        );
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        long timeRequest = System.currentTimeMillis() - Long.parseLong(request.getAttribute("time-request").toString());
//        log.info("Finish request-id: {}, method: {}, path: {}, client-ip: {}, time-request: {}ms",
//                request.getAttribute("request-id"),
//                request.getMethod(),
//                request.getRequestURI(),
//                SecurityUtil.getClientIP(request),
//                timeRequest
//        );
//    }
//}
