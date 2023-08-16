package com.wimoor.amazon.report.mapper;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.report.pojo.entity.AmzBrowseNode;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Mapper
public interface AmzBrowseNodeMapper extends BaseMapper<AmzBrowseNode> {
	void insertBatch(List<AmzBrowseNode> list);

	List<AmzBrowseNode> selcetByParentId(BigInteger id);
}
