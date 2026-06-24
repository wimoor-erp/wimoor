export function tranStatus(row){
				let val = row.status;
				if(val==0){
					return "已取消";
				}else if(val==1){
					 return "待确认";
				}else if(val>=5){
					let status=  row.shipmentstatus;
					if(status=="已删除"||status=="已取消"){
						return "异常";
					}else if(val==6){
						return "已完成"
				    }else{
						return status;
					}
				}else if(val==2){
					return "配货";
				}else if(val==4){
					return "发货出库";
				}else if(val==3){
					return "装箱";
				}else if(val==-1){
					return "已驳回";
				}
 }
 
 export  function tranStatusType(row){
				let val = row.status
				if(val==0){
					return "danger"
				}else if(val==1){
					 return "warning"
				}else if(val>=5){
					let status=  row.shipmentstatus;
					if(status=="已删除"||status=="已取消"||status=="状态异常"){
						return "danger";
					}else if(val==6){
						return "success"
				    }else{
						return "success"
					}
				}else if(val==2){
					return "info"
				}else if(val==4){
					return "warning"
				}else if(val==3){
					return "info"
				}
			}
			
export function tranStatusNew(row){
				let val = row.status;
				let mystatus="";
				if(row.shipmentstatus=="WORKING"&&val>=5){
					mystatus= "状态异常";
				}
				if(row.shipmentstatus=="CANCELLED"&&val!=0){
					mystatus= "状态异常";
				}
				if(row.shipmentstatus=="CLOSED"&&val>=5){
					mystatus="已完成";
				}
				if(row.shipmentstatus=="RECEIVING"&&val>=5){
					mystatus= "正在接受";
				}
				if(row.shipmentstatus=="IN_TRANSIT"&&val>=5){
					mystatus= "在途";
				}
				if(row.shipmentstatus=="SHIPPED"&&val>=5){
					mystatus= "已发货";
				}
				
				if(row.shipmentstatus=="WORKING"){
					mystatus= "工作中";
				}
				if(val==0||row.shipmentstatus=="CANCELLED"){
					mystatus= "已取消";
				}else if(val==1){
					mystatus=(mystatus!="待确认"?mystatus+"(待确认)":mystatus);
				}else if(val==3){
					mystatus=(mystatus!="仓库装箱"?mystatus+"(仓库装箱)":mystatus);
				}else if(val==4){
					mystatus=(mystatus!="仓库打标"?mystatus+"(仓库打标)":mystatus);
				}else if(val==5){
					mystatus=(mystatus!="物流跟踪"?mystatus+"(物流跟踪)":mystatus);
				}else if(val==6){
					mystatus=(mystatus!="海关"?mystatus+"(海关)":mystatus);
				}else if(val==7){
					mystatus=(mystatus!="待接收"?mystatus+"(待接收)":mystatus);
				}else if(val==-1){
					mystatus=(mystatus!="已驳回"?mystatus+"(已驳回)":mystatus);
				}else{
					let status=  row.shipmentstatus;
					if(status=="已删除"||status=="已取消"){
						mystatus= "异常";
					}else if(val==8){
						mystatus= "已完成"
					}else{
						if(status=="SHIPPED")mystatus= "已发货";
						if(status=="WORKING")mystatus= "待发货";
						if(status=="CLOSE")mystatus= "已完成";
						mystatus= status;
					}
				}
				return mystatus;
 }
 
 export  function tranStatusTypeNew(row){
				let val = parseInt(row.status); 
				if(row.shipmentstatus=="WORKING"&&val>=5){
					return "danger";
				}
				else if(val==0){
					return "danger"
				}else if(val==1){
					 return "warning"
				}else if(val>=5){
					let status=tranStatusNew(row);
					if(status=="已删除"||status=="已取消"||status=="状态异常"){
						return "danger";
					}else if(val==6){
						return "success"
				    }else{
						return "success"
					}
				}else if(val==3){
					return "warning";
				}else if(val==4){
					return "primary";
				}else if(val==5){
					return "primary";
				}else if(val==6){
					return "primary";
				}else if(val==7){
					return "success";
				}else if(val==-1){
					return "已驳回";
				}
			}