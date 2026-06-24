/**
 * http://usejsdoc.org/
 */
  let menuDataModel=[
               {name:'产品',iconName:'Commodity',id:"123"},
               {name:'销售',iconName:'SalesReport',id:"456"},
               {name:'采购',iconName:'ShoppingBag'},
               {name:'仓库',iconName:'Home'},
               {name:'物流',iconName:'Ship'},
               {name:'广告',iconName:'Ad'},
               {name:'财务',iconName:'Finance'},
               {name:'客服',iconName:'HeadsetOne'},
               {name:'授权',iconName:'Config'},
               {name:'系统',iconName:'Workbench'}, 
               ];
  
  let submenulistModel=[
	   {
           id:'123',
           menugroup:[
              {namegroup:[{name:'本地产品',path:'/product/localproduct'},{name:'产品标签',path:'/product/Mark'}], isShow:false},
              {namegroup:[{name:'品牌管理',path:'/product/Brand'},{name:'品类管理',path:'/product/Sort'}], isShow:false},
              {namegroup:[{name:'辅料管理',path:'/product/Material'}], isShow:false},
              {namegroup:[{name:'SKU配对',path:'/product/Pair'}], isShow:false}
           ]
	   }
      ];
  
  let allMenuModel =[
	   {
			title:'产品',
			subMenu:[
				{subGroup:[{id:"xxx",name:'本地产品',path:"xxx"},{name:'产品标签'}]},
				{subGroup:[{name:'品牌管理'},{name:'品类管理'}]},
				{subGroup:[{name:'SKU配对'}]},
			]
		   },
		   {
			title:'销售',
			subMenu:[
				{subGroup:[{name:'订单列表'},{name:'退货订单'},{name:'移除订单'}]},
				{subGroup:[{name:'在线商品'},{name:'销量详情'},{name:'调价队列'},{name:'商品刊登'}]},
				{subGroup:[{name:'店铺业绩'},{name:'销售排名'},{name:'跟卖监控'}]},
			]  
		   },
		    {
			title:'采购',
			subMenu:[
				{subGroup:[{name:'采购预测'},{name:'采购计划'},{name:'采购单'},{name:'加工计划'}]},
				{subGroup:[{name:'采购变更单'},{name:'采购记录'},]},
				{subGroup:[{name:'供应商'},{name:'采购合同'}]},
			]  
		   },
		   {
			title:'仓库',
			subMenu:[
				{subGroup:[{name:'仓库列表'},{name:'库存管理'}]},
				{subGroup:[{name:'收货单'},{name:'质检单'},{name:'扫码收货'}]},
				{subGroup:[{name:'入库单'},{name:'出库单'},{name:'加工单'},{name:'调拨单'},{name:'调整单'},{name:'盘点单'}]},
				{subGroup:[{name:'库存流水'},{name:'滞销品'},{name:'仓储费'},{name:'库存周转率'}]},
				{subGroup:[{name:'成本补录'},]},
			]  
		   },
		    {
			title:'物流',
			subMenu:[
				{subGroup:[{name:'发货预测'},{name:'FBA发货计划'},{name:'拣货单'},{name:'发货单'},{name:'货件处理'}]},
				{subGroup:[{name:'发货地址'},{name:'头程分摊'},{name:'发货统计'},{name:'人力预测'}]},
				{subGroup:[{name:'物流商资料'},{name:'匹配规则'},{name:'渠道报价'}]},
			]  
		   },
		   {
			title:'广告',
			subMenu:[
				{subGroup:[{name:'SP广告'},{name:'SB广告'},{name:'SD广告'},{name:'操作历史'}]},
				{subGroup:[{name:'定时任务'},{name:'广告提醒'}]},
				{subGroup:[{name:'SP报表'},{name:'SB报表'},{name:'SD报表'}]},
			]  
		   },
		   {
			title:'财务',
			subMenu:[
				{subGroup:[{name:'资金流水'},{name:'付款单'},{name:'回款记录'}]},
				{subGroup:[{name:'收款单'}]},
				{subGroup:[{name:'利润报告'},{name:'业绩一览'},{name:'利润月报'},{name:'利润月报'}]},
				{subGroup:[{name:'费用管理'},{name:'成本货值'}]},
			]  
		   },
		    {
			title:'客服',
			subMenu:[
				{subGroup:[{name:'Review'},{name:'Feedback'}]},
				{subGroup:[{name:'邮件回复'},{name:'邮件模板'}]},
				{subGroup:[{name:'VAT管理'},{name:'发票管理'},{name:'发票上传'}]},
				{subGroup:[{name:'请求规则'},{name:'请求日志'}]}
			]  
		   },
		   {
			title:'授权',
			subMenu:[
				{subGroup:[{name:'店铺授权'},{name:'1688授权'}]},
				{subGroup:[{name:'组织架构'}]},
				{subGroup:[{name:'用户授权'},]},
			]  
		   },
		   {
			title:'系统',
			subMenu:[
				{subGroup:[{name:'计算工具'}]},
				{subGroup:[{name:'个人中心'},{name:'消息中心'}]},
				{subGroup:[{name:'汇率管理'},{name:'参数配置'},{name:'操作日志'}]},
			]  
		   },
	   ];
  export {menuDataModel,submenulistModel,allMenuModel};