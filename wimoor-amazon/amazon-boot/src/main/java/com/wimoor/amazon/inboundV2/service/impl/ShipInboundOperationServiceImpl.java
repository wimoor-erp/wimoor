package com.wimoor.amazon.inboundV2.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fulfillmentinbound.OperationStatus;
import com.amazon.spapi.model.fulfillmentinboundV20240320.InboundOperationStatus;
import com.amazon.spapi.model.fulfillmentinboundV20240320.OperationProblem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundOperationMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.common.mvc.BizException;

import lombok.RequiredArgsConstructor;

@Service("shipInboundOperationService")
@RequiredArgsConstructor
public class ShipInboundOperationServiceImpl extends  ServiceImpl<ShipInboundOperationMapper,ShipInboundOperation> implements IShipInboundOperationService {
	 @Autowired
	 @Lazy
	 IInboundApiHandlerService iInboundApiHandlerService;
	 
	public String getMessage(List<OperationProblem> list) {
		String msg="";
		if(list==null)return msg;
		for(OperationProblem item:list) {
			msg=msg+item.getMessage();
		}
		return msg;
	}
	@Override
	public ShipInboundOperation setOperationID(AmazonAuthority auth,String planid, String operationId) {
		ShipInboundOperation old = this.baseMapper.selectById(operationId);
//		if(old==null){
//			ShipInboundOperation one=new ShipInboundOperation();
//			one.setFormid(planid);
//			one.setOperationid(operationId);
//			one.setOperationStatus("IN_PROGRESS");
//			one.setOpttime(new Date());
//			one.setOperationProblem(null);
//			if(!this.save(one)) {
//				throw new BizException("报错操作记录失败");
//			}
//			old=one;
//		}else{
		if(old!=null && old.getOperationStatus().equals(OperationStatus.SUCCESS.getValue())){
			return old;
		}
		//}
		InboundOperationStatus response = iInboundApiHandlerService.getInboundOperationStatus(auth, operationId);
		if(response!=null) {
			old = this.baseMapper.selectById(operationId);
			if(old==null) {
				ShipInboundOperation one=new ShipInboundOperation();
				one.setFormid(planid);
				one.setOperationid(operationId);
				one.setOperation(response.getOperation());
				one.setOpttime(new Date());
				if(response.getOperationStatus()!=null) {
					one.setOperationStatus(response.getOperationStatus().getValue());
					if(response.getOperationStatus().getValue().equals(OperationStatus.FAILED.getValue())&&response.getOperationProblems()!=null) {
						one.setOperationProblem(getMessage(response.getOperationProblems()));
					}
				}
				if(!this.save(one)) {
					throw new BizException("报错操作记录失败");
				}
				return one;
			}else {
				old.setOpttime(new Date());
				old.setOperation(response.getOperation());
				if(response.getOperationStatus()!=null&&!old.getOperationStatus().equals(response.getOperationStatus().getValue())) {
					old.setOperationStatus(response.getOperationStatus().getValue());
					if(response.getOperationStatus().getValue().equals(OperationStatus.FAILED.getValue())&&response.getOperationProblems()!=null) {
						old.setOperationProblem(getMessage(response.getOperationProblems()));
					}
					updateById(old);
				}
				return old;
			}
		}
		return null;
	    
	}
	 
	
	@Override
	public ShipInboundOperation getOperation(AmazonAuthority auth,String operationid) {
		// TODO Auto-generated method stub
          ShipInboundOperation one = this.baseMapper.selectById(operationid);
		  InboundOperationStatus response = iInboundApiHandlerService.getInboundOperationStatus(auth, operationid);
			one.setOpttime(new Date());
	    	if(response.getOperationStatus()!=null&&!one.getOperationStatus().equals(response.getOperationStatus().getValue())) {
	    		one.setOperationStatus(response.getOperationStatus().getValue());
	    		if(response.getOperationStatus().getValue().equals(OperationStatus.FAILED.getValue())&&response.getOperationProblems()!=null) {
	    			  one.setOperationProblem(getMessage(response.getOperationProblems()));
	    		}
	    		 updateById(one);
	    	}
	    	return one;
	}

	
	@Override
	public ShipInboundOperation getOperation(AmazonAuthority auth,String fromid, String operation) {
		LambdaQueryWrapper<ShipInboundOperation> query=new LambdaQueryWrapper<ShipInboundOperation>();
		// TODO Auto-generated method stub
		query.eq(ShipInboundOperation::getFormid, fromid);
		query.eq(ShipInboundOperation::getOperation, operation);
		List<String> statuslist=new LinkedList<String>();
		statuslist.add(OperationStatus.IN_PROGRESS.getValue());
		statuslist.add(OperationStatus.SUCCESS.getValue());
		query.in(ShipInboundOperation::getOperationStatus, statuslist);
		query.orderByDesc(ShipInboundOperation::getOpttime);
		IPage<ShipInboundOperation> page=new Page<ShipInboundOperation>(0,1);
		IPage<ShipInboundOperation> list = this.baseMapper.selectPage(page,query);
		if(list!=null&&list.getRecords().size()>0) {
			ShipInboundOperation   one=list.getRecords().get(0);
			if(one.getOperationStatus()!=null&&!one.getOperationStatus().equals(OperationStatus.IN_PROGRESS.getValue())) {
				return one;
			}
			InboundOperationStatus response = iInboundApiHandlerService.getInboundOperationStatus(auth, one.getOperationid());
			one.setOpttime(new Date());
	    	if(response.getOperationStatus()!=null&&!one.getOperationStatus().equals(response.getOperationStatus().getValue())) {
	    		one.setOperationStatus(response.getOperationStatus().getValue());
	    		if(response.getOperationStatus().getValue().equals(OperationStatus.FAILED.getValue())&&response.getOperationProblems()!=null) {
	    			  one.setOperationProblem(getMessage(response.getOperationProblems()));
	    		}
	    		 updateById(one);
	    	}
	    	return one;
		}
		return null;
	}
}
