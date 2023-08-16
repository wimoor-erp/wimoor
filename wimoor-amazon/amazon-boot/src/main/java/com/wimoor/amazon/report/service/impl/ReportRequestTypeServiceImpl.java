package com.wimoor.amazon.report.service.impl;

import com.wimoor.amazon.report.pojo.entity.ReportRequestType;
import com.wimoor.amazon.report.mapper.ReportRequestTypeMapper;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 亚马逊报表类型 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Service
public class ReportRequestTypeServiceImpl extends ServiceImpl<ReportRequestTypeMapper, ReportRequestType> implements IReportRequestTypeService {

	@Override
	public ReportRequestType findByTypeCode(String requestType) {
		// TODO Auto-generated method stub
		QueryWrapper<ReportRequestType> query=new QueryWrapper<ReportRequestType>();
		query.eq("code", requestType);
		return this.getOne(query);
	}

}
