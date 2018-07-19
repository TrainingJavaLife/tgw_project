package com.tgw360.repository;

import com.tgw360.common.Constants;
import com.tgw360.common.RedisUtils;
import com.tgw360.common.UUIDUtil;
import com.tgw360.entity.MessageState;
import com.tgw360.exception.StompDataException;
import com.tgw360.exception.StompWrongRequestException;
import javapns.communication.ConnectionToAppleServer;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.*;
import org.apache.axis.client.Call;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by 危宇 on 2018/1/10 17:23
 */
@Repository
public class MessageRepository {

    private final Logger logger = LoggerFactory.getLogger(MessageRepository.class);

    /**Webservice相关*/
    @Value("${webservices.username}")
    private String userName;

    @Value("${webservices.password}")
    private String passWord;

    @Value("${webservices.url}")
    private String url;

    @Value("${webservices.namespace}")
    private String nameSpace;

    @Value("${webservices.actionUri}")
    private String actionUri;

    @Value("${webservices.option}")
    private String option;

    @Value("${messages.registerCode}")
    private String registerCode;

    @Value("${messages.loginCode}")
    private String loginCode;

    @Value("${messages.payCode}")
    private String payCode;

    @Value("${messages.generalCode}")
    private String generalCode;

    @Value("${messages.generalTime}")
    private String generalTime;

    /**苹果push相关token*/
    @Value("${iosToken.host}")
    private String hostURL;

    @Value("${iosToken.port}")
    private Integer port;

    @Value("${iosToken.passWd}")
    private String passWd;

    @Value("${iosToken.sound}")
    private String sound;

    @Value("${iosToken.badge}")
    private Integer badge;

    private String keystore = "C:/Users/ck.p12";

    @Autowired
    private StringRedisTemplate template;

    /**
     * 获取手机的验证码
     * @param phone 手机号码
     * @param type 验证码类型
     * @return
     */
    public MessageState acquireIdentifyCode(String phone, Integer type){

        try {
            MessageState identifyCode = null;
            // 生成验证码
            String randomCode = UUIDUtil.generateInteger(6);
            String content;
            Integer codeType = 1;   // 约定1代表登录和注册的验证码，2代表重置密码 3.支付
            String templateContent = "%s：%s，%s";
            if (type == 1){
                content = String.format(templateContent,generalCode,randomCode,generalTime);
                codeType = 1;
            } else if (type == 2){
                content = String.format(templateContent,generalCode,randomCode,generalTime);
                codeType = 1;
            } else if (type == 3){
                content = String.format(templateContent,generalCode,randomCode,generalTime);
                codeType = 3;
            } else {
                throw new StompWrongRequestException("验证码类型参数错误");
            }
            long result = (long)sendWebservice(phone,content);
            System.out.println(result);
            ValueOperations<String,String> opsForValue = template.opsForValue();
            if (result == 1){
                identifyCode = new MessageState(0,"发送成功");
                String key = Constants.REDIS_SMS_MOBILE + "-" + phone + "-" + Integer.toString(codeType);
                String value = randomCode;
                opsForValue.set(key,value);
                template.expire(key,10, TimeUnit.MINUTES);
            } else if (result == -1001){
                identifyCode = new MessageState(4001,"用户名密码验证失败");
            } else if (result == -1002){
                identifyCode = new MessageState(4002,"黑名单");
            } else if (result == -1003){
                identifyCode = new MessageState(4003,"其他异常");
            } else if (result == -1004){
                identifyCode = new MessageState(4004,"短信服务商调用失败");
            } else if (result == -1005){
                identifyCode = new MessageState(4005,"手机号码错误");
            } else if (result == -1006){
                identifyCode = new MessageState(4006,"短信发送频繁");
            }
            return identifyCode;
        } catch (MalformedURLException e) {
            throw new StompDataException(e);
        } catch (ServiceException e) {
            throw new StompDataException(e);
        } catch (RemoteException e) {
            throw new StompDataException(e);
        } catch (StompWrongRequestException e){
            throw new StompWrongRequestException("验证码类型参数错误");
        }
    }

    /**
     * 验证手机验证码
     * @param phone
     * @param code
     * @param codeType
     * @return
     */
    public boolean verifyIdentifyCode(String phone,String code,Integer codeType){
        String key = null;
        String value = null;
        if (codeType == 1 || codeType == 2){
            key = Constants.REDIS_SMS_MOBILE + "-" + phone + "-" + Integer.toString(1);
            value = RedisUtils.getValue(template,key);
            if (value !=null && code.equals(value)){
                return true;
            }
        }
        key = Constants.REDIS_SMS_MOBILE + "-" + phone + "-" + Integer.toString(codeType);
        value = RedisUtils.getValue(template,key);
        if (value !=null && code.equals(value)){
            return true;
        }
        return false;
    }

    /**
     * 发送短信到指定的手机
     * @param phone
     * @param content
     * @return
     * @throws RemoteException
     * @throws ServiceException
     * @throws MalformedURLException
     */
    public MessageState sendTextMessage(String phone,String content){
        try {
            MessageState messageState = null;
            long result = (long)sendWebservice(phone,content);
            if (result == 1){
                messageState = new MessageState();
                messageState.setCode(0);
                messageState.setContent("发送成功");
            } else if (result == -1001){
                messageState = new MessageState();
                messageState.setCode(4001);
                messageState.setContent("用户名密码验证失败");

            } else if (result == -1002){
                messageState = new MessageState();
                messageState.setCode(4002);
                messageState.setContent("黑名单");

            } else if (result == -1003){
                messageState = new MessageState();
                messageState.setCode(4003);
                messageState.setContent("其他异常");

            } else if (result == -1004){
                messageState = new MessageState();
                messageState.setCode(4004);
                messageState.setContent("短信服务商调用失败");
            } else if (result == -1005){
                messageState = new MessageState();
                messageState.setCode(4005);
                messageState.setContent("手机号码错误");
            } else if (result == -1006){
                messageState = new MessageState();
                messageState.setCode(4006);
                messageState.setContent("短信发送频繁");
            }
            return messageState;
        } catch (Exception e) {
            throw new StompDataException(e);
        }
    }


    /**
     * 推送IOS消息
     * @param tokenList
     * @param message
     */
    public void sendIOSPush(List<String> tokenList,String message){
        try {
            PushNotificationPayload payLoad = PushNotificationPayload.complex();
            payLoad.addAlert(message);
            payLoad.addSound(sound);
            PushNotificationManager pushManager = new PushNotificationManager();
            /* 指定服务器信息 */
            AppleNotificationServer customServer = new AppleNotificationServerBasicImpl(keystore, passWd,
                    ConnectionToAppleServer.KEYSTORE_TYPE_PKCS12, hostURL, port);
            pushManager.initializeConnection(customServer);
            List<PushedNotification> notifications = new ArrayList<>();
            if (tokenList.size() == 1) {
                Device device = new BasicDevice();
                device.setToken(tokenList.get(0));
                PushedNotification notification = pushManager.sendNotification(
                        device, payLoad, true);
                notifications.add(notification);
            } else {
                List<Device> device = new ArrayList<>();
                for (String token : tokenList) {
                    device.add(new BasicDevice(token));
                }
                notifications = pushManager.sendNotifications(payLoad, device);
            }
            List<PushedNotification> failedNotifications = PushedNotification
                    .findFailedNotifications(notifications);
            if (failedNotifications != null) {
                int failed = failedNotifications.size();
                String errorLog = "失败条数=" + failed + "; ";
                for (PushedNotification failedNotification : failedNotifications) {
                    Device d = failedNotification.getDevice();
                    errorLog += "deviceId=" + d.getDeviceId() + "; token="
                            + d.getToken();
                }

                logger.info("消息推送失败记录：" + errorLog);
            }
            pushManager.stopConnection(); //关闭连接
            logger.info("推送结束");
        } catch (Exception e) {
            throw new StompDataException(e);
        }
    }


    /**
     * 调用webService服务发送手机号和验证码
     * @param phone 手机号码
     * @param content 短信内容
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public Object sendWebservice(String phone,String content) throws MalformedURLException, ServiceException, RemoteException {
        org.apache.axis.client.Service service = new org.apache.axis.client.Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(new java.net.URL(url));
        call.setUseSOAPAction(true);
        call.setSOAPActionURI(nameSpace + actionUri);  // action uri
        call.setOperationName(new QName(nameSpace, option)); // 设置要调用哪个方法
        // 设置参数名称，具体参照从浏览器中看到的
        call.addParameter(new QName(nameSpace, "username"), XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(new QName(nameSpace, "userpwd"), XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(new QName(nameSpace, "sender"), XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(new QName(nameSpace, "SmsType"), XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(new QName(nameSpace, "Smscontent"), XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(new QName(nameSpace, "receiveType"), XMLType.XSD_STRING, ParameterMode.IN);
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING); // 要返回的数据类型
        Object[] params = new Object[] {userName,passWord,"0", "vcode2", content, phone };
        long result = Long.parseLong((String) call.invoke(params));
        return result;
    }
}
