# **Introduction:**
Amazon published a new API used to replace MWS at 2020-10,Called Selling Partner API(SP-API). SP-API must use the AWS IAM to integration.
    this is a java version client SDK can make integration easily.
    <br>
(亚马逊(amazon)在2020年10月推出了新的替代mws的api方案，称为Selling Partner API(SP-API)。
sp-api在修改原mws的接口方式的基础上引入了aws的IAM权限管理，增加了开发者的对接门坎和学习成本。
这里整理了一份java版的对接信息，供大家借鉴)

# **MWS:**<br>
document:http://docs.developer.amazonservices.com/en_US/dev_guide/index.html <br/>
sdk library:https://developer.amazonservices.com/javaclients <br/>

# **SP-API:** <br/>
document: https://developer.amazonservices.com/  <br/>
GitHub: https://github.com/amzn/selling-partner-api-docs <br/>
models: https://github.com/amzn/selling-partner-api-models/ <br/>
Hands-on training camp：https://www.spapi.org.cn/cn/intro.html  <br/>
（This training camp station is from AWS china team, the guide of the process and initialization of SP-API） <br/>
java version client sdk：https://github.com/penghaiping/amazon-sp-api <br/>

# **Different between MWS and SP-API:** <br/>
1.SP-API是rest方式，mws是特殊的结构有xml也有xsd <br/>
2.SP-API授权利用了LWA，即Amazon对OAuth 2.0的实现。该模型消除了Amazon MWS所要求的手工交换认证令牌(mwsAuthToken)的需要 <br/>
3.SP-API提供比Amazon MWS更好的数据访问控制。开发人员只能请求访问他们需要的数据，而销售商可以在API部分、操作或数据资源级别授予权限 <br/>
4.SP-API允许您使用AWS身份和访问管理(IAM)直接获取和管理自己的身份验证凭据。对于Amazon MWS，您可以使用特殊的注册工作流接收Amazon提供的身份验证凭证，并且通过打开与Amazon MWS支持的联系方式获得新的凭证 <br/>
5.SP-API使用AWS签名版本4进行身份验证。Amazon MWS使用签名版本2 <br/>

# **Directions for use:** <br/>
1.All the api class at path:src/main/java/com/amazon/spapi/api <br/>
(所有references相关的api的调用类都在src/main/java/com/amazon/spapi/api) <br/>
2.All the api test case class at path:src/test/java/com/amazon/spapi/api <br/>
(所有references相关的api的调用测试类都在src/test/java/com/amazon/spapi/api)<br/>
3.sellersApiTest is a call API test case class<br/>
(sellersApiTest写了个测试调用类，可参考) <br/>
4.docs directory include all the api and models documents  <br/>
(所有md文档，包括api文档及models文档都在docs文件目录下)<br/>
5.documents directory include the Utils for upload and download files.<br/>
(在FeedApi及reportApi中上传和下载文件的工具类在documents目录下，里面是amazon提供的java case，包含了加解密)