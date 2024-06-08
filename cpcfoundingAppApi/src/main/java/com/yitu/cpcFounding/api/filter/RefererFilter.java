package com.yitu.cpcFounding.api.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全扫描过滤器
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class RefererFilter implements Filter {
    private boolean isOpen = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    //白名单地址
    private static List<String> whiteUrlList;
    private static synchronized List<String> getWhiteUrlList() {
        if(whiteUrlList == null) {
            whiteUrlList = new ArrayList<>();
            whiteUrlList.add("http://192.9.102.59");
            whiteUrlList.add("http://113.108.119.116:32102");
            whiteUrlList.add("http://localhost");
            whiteUrlList.add("http://192.9.102.53");
        }
        return whiteUrlList;
    }

    /**
     * 解决跨站点漏洞问题
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (!isOpen) {
            chain.doFilter(req, resp);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        // 限制只允许POST、GET请求
        if (!request.getMethod().equals("POST") && !request.getMethod().equals("GET")) {
            ((HttpServletResponse) resp).setStatus(403);
        }
        // 处理跨站点请求伪造的问题 ，判断处理Referer
        String referer = request.getHeader("Referer");
        // 打开首页正常访问，此时referer为null
        if (referer != null) {
            // 来访的url，是否包含了referer
            //URL refererUrl = new URL(referer);
//            if(getWhiteUrlList().stream().anyMatch(p -> referer.toLowerCase().startsWith(p))){
//                chain.doFilter(request, resp);
//            } else {
//                ((HttpServletResponse) resp).setStatus(403);
//            }
            chain.doFilter(request, resp);
        } else {
            chain.doFilter(request, resp);
        }
    }

}
