<strong><p>此项目由深圳市万墨信息科技有限公司开发，如果有任何疑问请联系开发团队。</p></strong>

<strong><p>1、安装node (注意将Bin添加环境变量Path中)</p></strong>
  1>下载node[](https://nodejs.org/en/download/)<br>
  2、安装vue<br>
npm create vue@latest<br>

<strong><p>3、进入项目目录(安装依赖)</p></strong>
npm install



授权逻辑：根据在菜单管理页面配置的权限如权限erp:wh:shelf:add 则可以在组件中加入如下类容,v-hasPerm为权限关键子
在文件utils/permission.js中配置
<textarea class="myclass" v-hasPerm="'erp:wh:shelf:add'">test</textarea>
