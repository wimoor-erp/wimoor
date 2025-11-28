package com.wimoor.amazon.inbound.pojo.vo;

 

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value="ShipInboundShipmenSummarytVo对象", description="货件Item内容汇总")
public class ShipInboundShipmenSummarytVo{

	private String id;

	@ApiModelProperty(value = "货件ID")
	@TableId(value="shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "货件名称")
	private String name;

	@ApiModelProperty(value = "货件计划ID即表单ID")
	private String planid;

	@ApiModelProperty(value = "店铺ID")
	private String groupid;

	@ApiModelProperty(value = "审核状态【系统填写】")
	private Integer auditstatus;

	@ApiModelProperty(value = "站点ID")
	private String marketplaceid;

	@ApiModelProperty(value = "国家编码")
	private String countryCode;

	@ApiModelProperty(value = "亚马逊收货中心")
	private String centerId;

	@ApiModelProperty(value = "亚马逊收货中心数量")
	private String centerNumber;

	@ApiModelProperty(value = "渠道名称")
	private String channame;

	@ApiModelProperty(value = "订单状态")
	private String shipmentstatus;

	@ApiModelProperty(value = "SKU数量")
	private Long skuamount;

	@ApiModelProperty(value = "SKU接受数量")
	private Long sumrec;

	@ApiModelProperty(value = "SKU实际发货数量")
	private Long sumshipped;

	@ApiModelProperty(value = "审核时间")
	private Date auditime;

	@ApiModelProperty(value = "总发货量")
	private Long sumQuantity;

	@ApiModelProperty(value = "创建日期")
	private Date createdate;

	@ApiModelProperty(value = "仓库名称")
	private String warehouse;

	@ApiModelProperty(value = "店铺名称")
	private String groupname;

	@ApiModelProperty(value = "仓库ID")
	private String warehouseid;

	@ApiModelProperty(value = "国家")
	private String country;

	@ApiModelProperty(value = "亚马逊收货中心")
	private String center;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "店铺ID")
	private String shopid;

    @TableField(exist = false)
	private List<ShipInboundItemVo> itemList;

	@ApiModelProperty(value = "发货类型")
	private String transtyle;

	@ApiModelProperty(value = "订单状态")
	private String status;

	private String referenceid;

	@ApiModelProperty(value = "发货日期")
	private Date shopdate;

	@ApiModelProperty(value = "预计到货日期")
	private Date arrivalTime;

	@ApiModelProperty(value = "超期天数")
	private Integer delayDays;

	@ApiModelProperty(value = "驳回日期")
	private Date status0date;

	@ApiModelProperty(value = "创建日期")
	private Date status1date;

	@ApiModelProperty(value = "审核日期")
	private Date status2date;

	@ApiModelProperty(value = "配货日期")
	private Date status3date;

	@ApiModelProperty(value = "装箱日期")
	private Date status4date;

	@ApiModelProperty(value = "发货日期")
	private Date status5date;

	@ApiModelProperty(value = "结束日期")
	private Date status6date;

	@ApiModelProperty(value = "开始接收日期")
	private Date receivedate;

	@ApiModelProperty(value = "开始接收日期")
	private Date shipedDate;

	@ApiModelProperty(value = "同步订单库存同步1代表没有扣库存，2代表已经扣库存")
	private Integer syncinv;

	@ApiModelProperty(value = "同步订单库存同步1代表没有扣库存，2代表已经扣库存")
	private String checkinv;

	@ApiModelProperty(value = "订单编号")
	private String number;

	@ApiModelProperty(value = "物流公司")
	private String company;

	@ApiModelProperty(value = "物流公司ID")
	private String companyid;

	private String channelid;

	@ApiModelProperty(value = "物流公司API")
	private String apiSystem;

	@ApiModelProperty(value = "物流渠道")
	private String transtypename;

	@ApiModelProperty(value = "运输方式id")
	private String transtype;

	@ApiModelProperty(value = "是否系统提交")
	private String intendedBoxContentsSource;

	@ApiModelProperty(value = "物流追踪编码")
	private String ordernum;

	@ApiModelProperty(value = "是否原装发货")
	private Boolean areCasesRequired;

	@ApiModelProperty(value = "是否报价")
	private Boolean isquote;


	private String traceupstatus;

	private String tracknumber;

	private String reallyShipmentid;

	Date boxtime;

	Date refreshtime;

	Date startReceiveDate;

	Integer transday;

	Integer days;

	Date deliveryStart;

	Date deliveryEnd;

	Date deliveryUntil;

	String transactionName;
  }

 