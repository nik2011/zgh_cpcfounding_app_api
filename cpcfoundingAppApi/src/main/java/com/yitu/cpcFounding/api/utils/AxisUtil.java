package com.yitu.cpcFounding.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.utils.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * TODO
 *
 * @author jxc
 * @date 2021/1/25 16:43
 */
@Slf4j
public class AxisUtil {

    private static final String RESPONSE_PREFIX = "//soap11env:Envelope/soap11env:Body/";

    /**
     * @param endpoint        //WSDL的地址
     * @param targetNamespace //命名空间
     * @param method          //具体调用的方法名
     * @param soapActionURI   //具体方法的调用URI
     * @param paramNames      //调用接口的参数的名字
     * @param paramValues     //调用接口的参数的值
     * @return
     */
    public static Object callWebService(String endpoint, String targetNamespace,
                                        String method, String soapActionURI, String[] paramNames, String[] paramValues) {
        try {
            //字符集
            final String encodingStyle = "utf-8";
            Service service = new Service();
            Call call = (Call) service.createCall();
//            call.setEncodingStyle(encodingStyle);//设置传入服务端的字符集格式如utf-8等
            call.setTimeout(new Integer(20000));  //设置超时时间
            call.setTargetEndpointAddress(new java.net.URL(endpoint));  //设置目标接口的地址
            call.addHeader(getSoapHeader("","",""));
            if (!StringUtils.isEmpty(soapActionURI)) {
                call.setSOAPActionURI(soapActionURI);
                call.setUseSOAPAction(true);
            }
            call.setOperationName(new QName(targetNamespace, method));// 具体调用的方法名，可以由接口提供方告诉你，也可以自己从WSDL中找
//            call.setOperationName(method);
            // 接口的参数
            call.addParameter(new QName(targetNamespace, paramNames[0]),
                    XMLType.XSD_STRING,
                    ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);// 设置返回类型  ，如String
            call.setReturnClass(String.class); //返回字符串数组类型
            // 给方法传递参数，并且调用方法 ，如果无参，则new Obe
//            String[] result = (String[]) call.invoke(new Object[]{paramValues[0]});
//            String[] result = (String[]) call.invoke(new Object[]{});
            Object result = call.invoke(new Object[]{paramValues[0]});
            // 打印返回值
            System.out.println("result is " + result.toString());
            return result;
        } catch (ServiceException | MalformedURLException | RemoteException e) {
            log.error("webservice接口调用异常", e);
        } catch (Exception e) {
            log.error("webservice接口调用异常", e);
        }
        return null;
    }

    /**
     * 调用webservice接口
     * 原文章链接：https://blog.csdn.net/qq_27471405/article/details/105275657
     * 其他均为盗版，公众号：灵儿的笔记(zygxsq)
     */
    public String sendWsdl(Object obj) {
        log.info("--------调用webservice接口begin-------");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient("http://xx.xxx.xx.xx:9556/xxx/ws/getAlarmWs?wsdl");
        String json = null;
        try {

            QName qName = new QName("http://xx.zygxsq.cn/", "getAlarmWs");
            //*原文章链接：https://blog.csdn.net/qq_27471405/article/details/105275657     * 其他均为盗版，公众号：灵儿的笔记(zygxsq)
            Object[] objects1= client.invoke(qName, "aaa","bbb"); //参数1，参数2，参数3......按顺序放就看可以

            //json = JSONObject.toJSONString(objects1[0]);
            System.out.println("返回数据:" + json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            log.info("服务器断开连接，请稍后再试");
        }
        log.info("--------调用webservice接口end-------");
        return json;


    }

    public String sendXml2(String xml) throws Throwable {
        String url = System.getProperty("wsdl");
        Service serv = new Service();
        Call call = (Call) serv.createCall();
        call.setTargetEndpointAddress(url);
        call.setOperationName(new QName("http://webservice.rpc.other.web.demo.g4studio.org/","createsite"));
        call.addParameter(new QName("http://webservice.rpc.other.web.demo.g4studio.org/", "xmlContent"),
                XMLType.XSD_STRING, Class.forName("java.lang.String"), ParameterMode.IN );
        call.setReturnType(XMLType.XSD_STRING);
        System.out.println("推送的xml---"+xml);
        String str=(String) call.invoke(new Object[] {xml});
        System.out.println(str+"----------------推送成功");
        return str;
    }

    /**
     * 获取axis请求形式的加密头
     * @return SOAPHeaderElement
     */
    public static SOAPHeaderElement getSoapHeader(String role,String username,String password){

        //上面代码为从缓存中取到我们需求传递到认证头的数据 下面开始添加认证头
        SOAPHeaderElement head = new SOAPHeaderElement(new QName("www.csscis.com","security","csscis"));
        try {
            SOAPElement a1 = head.addChildElement("role");
            a1.addTextNode(role);
            a1 = head.addChildElement("username");
            a1.addTextNode(username);
            a1 = head.addChildElement("password");
            a1.addTextNode(password);

            head.setPrefix("csscis");
            head.setActor(null);
            //head.setMustUnderstand(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return head;
    }

    public static void main(String[] args) {

        try {
            String key = "ewbadswt";
            // 指出service所在URL
            String endpoint = "http://app.szzgh.org/services/memberValidateService?wsdl";
            String targetNamespace = "http://impl.criterion.bus.common.css.com";
            String method="isMember";

            // 创建一个服务(service)调用(call)
            Service service = new Service();
            // 通过service创建call对象
            Call call = (Call) service.createCall();

            // 设置service所在URL
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName(targetNamespace, method));
//            call.addHeader(getSoapHeader("criterion",  "union" ,"abcd1234"));


            call.addHeader(getSoapHeader("criterion",  "zgfw" ,"zgfe324sfwwe"));

            call.addParameter(new QName(null,"parm","impl"), Constants.XSD_INT,ParameterMode.IN);
            call.setUseSOAPAction(true);

            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            String s = "{\n" +
                    "\"identity\":\"xxxxx\",\n" +
                    "\"idcard\":\"43242434542424234\",\n" +
                    "\"memcard\":\"54322423\",\n" +
                    "\"tel\":\"13838385656\"\n" +
                    "}";

            Object ret = call.invoke(new Object[] {DessUtils.encypt(s,key)});
            System.out.println(ret);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
