package com.yitu.cpcFounding.api.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 防止xss和sql注入，重写HttpServletRequestWrapper
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest orgRequest;
    private static Logger log = LoggerFactory.getLogger(XssAndSqlHttpServletRequestWrapper.class);


    public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }



    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = xssEncode(parameters[i]);
            }
        }
        return parameters;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String dataString =  getBodyString(super.getInputStream());
        dataString = stripXSSAndSql(dataString);
        if (!StringUtils.isEmpty(dataString)) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataString.getBytes("UTF-8"));
            try {
                return newServletInputStream(byteArrayInputStream);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 创建ServletInputStream
     * @param byteArrayInputStream
     * @return javax.servlet.ServletInputStream
     * @author yaoyanhua
     * @date 2020/6/23 16:03
     */
    public ServletInputStream newServletInputStream (ByteArrayInputStream byteArrayInputStream) {
        ServletInputStream servletInputStream = new ServletInputStream(){
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // setReadListener
            }
        };
        return servletInputStream;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (Map.Entry<String,String[]> entry : parameters.entrySet()) {
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(entry.getKey(), values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {

        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     * @param s
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 11:57
     */
    private static String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            s = stripXSSAndSql(s);
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    // 转义大于号
                    sb.append("＞");
                    break;
                case '<':
                    // 转义小于号
                    sb.append("＜");
                    break;
                case '\'':
                    // 转义单引号
                    sb.append("＇");
                    break;
//                case '\"':
//                    // 转义双引号
//                    sb.append("＂");
//                    break;
                case '&':
                    // 转义&
                    sb.append("＆");
                    break;
                case '#':
                    // 转义#
                    sb.append("＃");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssAndSqlHttpServletRequestWrapper) {
            return ((XssAndSqlHttpServletRequestWrapper) req).getOrgRequest();
        }

        return req;
    }

    private static Pattern TAGS_PATTERN = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);

    private static Pattern TAGS_TWO_PATTERN = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static Pattern TAGS_THREE_PATTERN = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);

    private static Pattern TAGS_FOUR_PATTERN = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static Pattern TAGS_FIVE_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static Pattern TAGS_SIX_PATTERN = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static Pattern TAGS_SEVEN_PATTERN = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);

    private static Pattern TAGS_EIGHT_PATTERN = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);

    private static Pattern TAGS_NINE_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    //private static Pattern TAGS_SQL_PATTERN = Pattern.compile("(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)|(\\*|;|\\+|'|%)",Pattern.CASE_INSENSITIVE);
    private static Pattern TAGS_SQL_PATTERN = Pattern.compile("(drop|delete|truncate)|(\\\\\\\\\\*|;|')",Pattern.CASE_INSENSITIVE);


    /**
     * 防止xss跨脚本和sql注入攻击（替换，根据实际情况调整）
     */

    public static String stripXSSAndSql(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and
            // uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
            // Avoid null characters

            // value = value.replaceAll("", "");

            // Avoid anything between script tags
            value = TAGS_PATTERN.matcher(value).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            value = TAGS_TWO_PATTERN.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            value = TAGS_THREE_PATTERN.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            value = TAGS_FOUR_PATTERN.matcher(value).replaceAll("");
            // Avoid eval(...) expressions
            value = TAGS_FIVE_PATTERN.matcher(value).replaceAll("");
            // Avoid e-xpression(...) expressions
            value = TAGS_SIX_PATTERN.matcher(value).replaceAll("");
            // Avoid javascript:... expressions
            value = TAGS_SEVEN_PATTERN.matcher(value).replaceAll("");
            // Avoid vbscript:... expressions
            value = TAGS_EIGHT_PATTERN.matcher(value).replaceAll("");
            // Avoid onload= expressions
            value = TAGS_NINE_PATTERN.matcher(value).replaceAll("");
            value = TAGS_SQL_PATTERN.matcher(value).replaceAll("");
        }
        return value;
    }


    /** 
     * 获取工作流数据
     * @param servletInputStream
     * @return java.lang.String
     * @author J.K
     * @date 2020/6/19 9:34
     */
    public String getBodyString(final ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        try (
                InputStream inputStream = cloneInputStream(servletInputStream);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        ){
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 复制输入流
     *
     * @param inputStream
     * @return</br>
     */
    public InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }
//    @Override
//    public BufferedReader getReader() throws IOException {
//        return new BufferedReader(new InputStreamReader(getInputStream()));
//    }
//
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//
//        final ByteArrayInputStream bais = new ByteArrayInputStream(dateBody);
//
//        return new ServletInputStream() {
//
//            @Override
//            public int read() throws IOException {
//                return bais.read();
//            }
//
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//
//            @Override
//            public void setReadListener(ReadListener readListener) {
//            }
//        };
//    }


}