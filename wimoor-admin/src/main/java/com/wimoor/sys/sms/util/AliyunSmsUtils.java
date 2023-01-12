package com.wimoor.sys.sms.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;


/**
 * 阿里云短信服务：
 * 注意：需要 签名名称、模版CODE 以及 RAM访问控制中的 AccessKeyID 和 AccessKeySecret  
 */
@Slf4j
@Component
public class AliyunSmsUtils {
	 //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
 
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	@Value("${aliyun.sms.access_key_id}")
    String accessKeyId ;  // TODO 修改成自己的
	
	@Value("${aliyun.sms.access_key_secret}")
    String accessKeySecret ;   // TODO 修改成自己的
 
    public SendSmsResponse sendSms(String telephone, String value,String ftype) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("wimoor");    // TODO 修改成自己的
        //必填:短信模板-可在短信控制台中找到
		if ("register".equals(ftype)) {//系统注册
			request.setTemplateCode("SMS_165380197"); // TODO 修改成自己的
			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"code\":\"" + value + "\"}");
		} else if("losepassword".equals(ftype)){//密码找回
			request.setTemplateCode("SMS_165385637");
			request.setTemplateParam("{\"code\":\"" + value + "\"}");
		} else if("invalid".equals(ftype)){//授权异常
			request.setTemplateCode("SMS_176911470");
			request.setTemplateParam("{\"name\":\"" + value + "\"}");
		}else if("changephone".equals(ftype)) {
			request.setTemplateCode("SMS_212275351"); 
			request.setTemplateParam("{\"code\":\"" + value + "\"}");
		}
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
            System.out.println(new Date()+","+ftype+"类型短信发送成功！");
            log.info(ftype+"类型短信发送成功！");
            setNewphone(telephone);
        }else {
            System.out.println(new Date()+","+ftype+"类型短信发送失败！");
            log.error(ftype+"类型短信发送失败！");
        }
        return sendSmsResponse;
    }
 
 
  /*  不删 留着 以后可能有用
  public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        return querySendDetailsResponse;
    }
*/
 
    //以下为测试代码，随机生成验证码
    private static int newcode;
    private static String newphone;
	public static int getNewcode() {
        return newcode;
    }
    public static void setNewcode(){
        newcode = (int)((Math.random()*9+1)*1000);  //每次调用生成一位四位数的随机数
    }
    public static String getNewphone() {
  		return newphone;
  	}
  	public static void setNewphone(String newphone) {
  		AliyunSmsUtils.newphone = newphone;
  	}
  	
    public static void test() throws ClientException, InterruptedException {
        setNewcode();
        String code = Integer.toString(getNewcode());
        System.out.println("发送的验证码为："+code);
        //发短信
        //SendSmsResponse response =sendSms("15270466193",code,"register"); // TODO 填写你需要测试的手机号码
//        System.out.println("短信接口返回的数据----------------");
//        System.out.println("Code=" + response.getCode());
//        System.out.println("Message=" + response.getMessage());
//        System.out.println("RequestId=" + response.getRequestId());
//        System.out.println("BizId=" + response.getBizId());
 
       /* 不删 留着 以后可能有用
        System.out.println("  ==============================================  ");
        Thread.sleep(3000L);
        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }*/
 
    }
	
}
