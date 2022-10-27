package com.wimoor.erp.warehouse.service.impl;

import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfTreeVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.mapper.WarehouseShelfMapper;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 仓库货柜 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Service
@RequiredArgsConstructor
public class WarehouseShelfServiceImpl extends ServiceImpl<WarehouseShelfMapper, WarehouseShelf> implements IWarehouseShelfService {

	final IWarehouseService warehouseService;
	@Lazy
	@Autowired
	IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	
	@Override
	public List<WarehouseShelfTreeVo> getAllTree(UserInfo user) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<Warehouse> wrapper=new LambdaQueryWrapper<Warehouse>();
		wrapper.eq(Warehouse::getShopid, user.getCompanyid());
		wrapper.eq(Warehouse::getDisabled, false);
		wrapper.isNull(Warehouse::getFbawareid);
		List<Warehouse>  list = warehouseService.list(wrapper);
		List<WarehouseShelfTreeVo> result= recursionTreeSelectList(user,null,list);
		return result;
	}

    /**
     * 递归生成仓库表格层级列表
     *
     * @param parentId
     * @param deptList
     * @return
     */
 
        public   List<WarehouseShelfTreeVo> recursionTreeSelectList(UserInfo user,String parentId, List<Warehouse> wareList) {
        	List<WarehouseShelfTreeVo> warehouseTreeSelectList=new ArrayList<WarehouseShelfTreeVo>();
        	if(wareList==null||wareList.size()==0)return warehouseTreeSelectList;
            Optional.ofNullable(wareList).orElse(new ArrayList<>())
                .stream()
                .filter(warehouse -> (parentId==null&&warehouse.getParentid()==null)||(parentId!=null&&warehouse.getParentid()!=null&&warehouse.getParentid().equals(parentId)))
                .forEach(warehouse -> {
                	WarehouseShelfTreeVo treeSelectVO = new WarehouseShelfTreeVo();
                	treeSelectVO.setId(warehouse.getId());
                    treeSelectVO.setIsclick(true);
                	treeSelectVO.setName(warehouse.getName());
                	treeSelectVO.setNumber(warehouse.getNumber());
                	treeSelectVO.setParentid(warehouse.getParentid()==null?null:new BigInteger(warehouse.getParentid()));
                    List<WarehouseShelfTreeVo> children = recursionTreeSelectList(user,warehouse.getId(), wareList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        treeSelectVO.setChildren(children);
                    }else {
                		LambdaQueryWrapper<WarehouseShelf> wrapperShelf=new LambdaQueryWrapper<WarehouseShelf>();
                		wrapperShelf.eq(WarehouseShelf::getShopid, user.getCompanyid());
                		wrapperShelf.eq(WarehouseShelf::getWarehouseid, treeSelectVO.getId());
                		wrapperShelf.eq(WarehouseShelf::getIsdelete, false);
                		List<WarehouseShelf> list = this.list(wrapperShelf);
                		List<WarehouseShelfTreeVo> childrenShelf = recursionShelfTreeSelectList(user,treeSelectVO.getId(),list);
                		if(CollectionUtil.isNotEmpty(childrenShelf)) {
                			  treeSelectVO.setChildren(childrenShelf);
                		}
                    }
                    warehouseTreeSelectList.add(treeSelectVO);
                });
           return warehouseTreeSelectList;
        }
        
        public   List<WarehouseShelfTreeVo> recursionShelfTreeSelectList(UserInfo user,String parentId, List<WarehouseShelf> shelfList) {
        	List<WarehouseShelfTreeVo> shelfTreeSelectList=new ArrayList<WarehouseShelfTreeVo>();
            Optional.ofNullable(shelfList).orElse(new ArrayList<>())
                .stream()
                .filter(shelf -> (parentId==null&&shelf.getParentid()==null)||(parentId!=null&&shelf.getParentid().toString().equals(parentId)))
                .forEach(shelf -> {
                	WarehouseShelfTreeVo treeSelectVO = new WarehouseShelfTreeVo();
                	treeSelectVO.setId(shelf.getId());
                	treeSelectVO.setIsclick(true);
                	treeSelectVO.setName(shelf.getName());
                	treeSelectVO.setNumber(shelf.getNumber());
                	treeSelectVO.setIsfrozen(shelf.getIsfrozen());
                	treeSelectVO.setIswarn(shelf.getIswarn());
                	treeSelectVO.setTreepath(shelf.getTreepath());
                	treeSelectVO.setLength(shelf.getLength());
                	treeSelectVO.setHeight(shelf.getHeight());
                	treeSelectVO.setWidth(shelf.getWidth());
                	treeSelectVO.setCapacity(shelf.getCapacity());
                	treeSelectVO.setParentid(shelf.getParentid());
                	treeSelectVO.setWarehouseid(shelf.getWarehouseid());
                    List<WarehouseShelfTreeVo> children = recursionShelfTreeSelectList(user,shelf.getId(), shelfList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        treeSelectVO.setChildren(children);
                    }else {
                    	
                    }
                    shelfTreeSelectList.add(treeSelectVO);
                });
           return shelfTreeSelectList;
        }

		@Override
		public boolean save(List<WarehouseShelf> shelflist) {
			// TODO Auto-generated method stub
			UserInfo user=UserInfoContext.get();
				shelflist.forEach(shelf->{
						shelf.setOperator(new BigInteger(user.getId()));
						shelf.setCreator(new BigInteger(user.getId()));
						shelf.setShopid(new BigInteger(user.getCompanyid()));
						LambdaQueryWrapper<WarehouseShelf> wrapperShelf=new LambdaQueryWrapper<WarehouseShelf>();
	            		wrapperShelf.eq(WarehouseShelf::getShopid, shelf.getShopid());
	            		wrapperShelf.eq(WarehouseShelf::getParentid, shelf.getParentid());
	            		wrapperShelf.eq(WarehouseShelf::getIsdelete, false);
	            		wrapperShelf.eq(WarehouseShelf::getNumber, shelf.getNumber());
						long count = this.baseMapper.selectCount(wrapperShelf);
						if(count>0) {
							throw new BizException("编码["+shelf.getNumber()+"]在父节点中已经存在，请修改后重试");
						}
						this.save(shelf);
					});
 
			return true;
		}

		@Override
		public boolean logicDeleteById(String ids) {
			// TODO Auto-generated method stub
			List<String>  idList=  Arrays.asList(ids.split(","));
			List<WarehouseShelf> list = this.baseMapper.selectBatchIds(idList);	
			Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(item->{ 
			WarehouseShelfInventorySummaryVo result = iWarehouseShelfInventoryService.sumByShelf(item);
				if(result!=null&&result.getSkunum()>0) {
					throw new BizException("编码["+item.getNumber()+"] "+item.getName()+"中存在产品无法删除");
				}
				item.setIsdelete(true);
				baseMapper.updateById(item);
			});
			return true;
		}

		@Override
		public List<WarehouseShelfVo> detail(UserInfo user,String id) {
			// TODO Auto-generated method stub
		  return	deltailByType(user,id,"shelf");
		}
		 
	    public WarehouseShelfVo getShelfInfo(UserInfo user,String shelfid) {
	 		WarehouseShelf  shelf = this.getById(shelfid)  ;
	 		if(shelf==null) {
	 			throw new BizException("当前库位ID在系统中不存在！");
	 		}else {
	 			return getShelfVo(shelf);
	 		}
	    }
	    
	    public WarehouseShelfVo getShelfVo(WarehouseShelf item) {
	    	WarehouseShelfVo shelf=new WarehouseShelfVo();
			BeanUtil.copyProperties(item, shelf);
			WarehouseShelfInventorySummaryVo sum = iWarehouseShelfInventoryService.sumByShelf(item);
			if(sum!=null) {
				Float size = sum.getSize();
				Float capacity = shelf.getCapacity();
				if(capacity!=null&&capacity>0) {
					if(size!=null&&size>0) {
						Float usesize = size*100/capacity;
    					BigDecimal b = new BigDecimal(usesize); 
    					usesize = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
    					shelf.setUsesize(usesize);
					}else {
						shelf.setUsesize(0.0F);
					}
				}else {
					shelf.setUsesize(0.0F);
				}
				shelf.setSummary(sum);
			}else {
				shelf.setUsesize(0.0F);
			}
			if(shelf.getExpnumber()==null) {
				shelf.setExpnumber(0);
			}
			shelf.setId(item.getId());
			shelf.setSort(shelf.getSort());
			return shelf;
	    }
	    
		private List<WarehouseShelfVo> deltailByType(UserInfo user ,String id,String type){
			LambdaQueryWrapper<WarehouseShelf> wrapperShelf=new LambdaQueryWrapper<WarehouseShelf>();
    		wrapperShelf.eq(WarehouseShelf::getShopid, user.getCompanyid());
    		if(type.equals("shelf")) {
    			wrapperShelf.eq(WarehouseShelf::getParentid, id);
    		}
    		if(type.equals("warehouse")) {
    			wrapperShelf.eq(WarehouseShelf::getWarehouseid, id);
    		}
    		wrapperShelf.eq(WarehouseShelf::getIsdelete, false);
    		List<WarehouseShelf> list = this.baseMapper.selectList(wrapperShelf);
    		List<WarehouseShelfVo> result=list.stream().map(item->{
    			return getShelfVo(item);
    		}).collect(Collectors.toList());
			return result;
		}
		
		@Override
		public List<WarehouseShelfVo> detailWarehouse(UserInfo user,String warehouseid) {
			// TODO Auto-generated method stub
			 return	deltailByType(user,warehouseid,"warehouse");
		}

		@Override
		public String getAllParentName(String shelfid) {
			// TODO Auto-generated method stub
			 WarehouseShelf shelf = this.getById(shelfid);
			 if(shelf!=null) {
				return  searchShelfParentName(shelf.getNumber()+"-"+shelf.getName(),shelf);
			 }else {
				 return null;
			 }
			 
		}
		
		private String  searchShelfParentName(String name,WarehouseShelf shelf) {
			 WarehouseShelf parent = this.getById(shelf.getParentid());
			 if(parent!=null) {
				 name=parent.getNumber()+"-"+parent.getName()+"/"+name;
				 return searchShelfParentName(name,parent);
			 }else {
				 return name;
			 }
		}
		
		@Override
		public boolean updateById(WarehouseShelf shelf) {
			// TODO Auto-generated method stub
			LambdaQueryWrapper<WarehouseShelf> wrapperShelf=new LambdaQueryWrapper<WarehouseShelf>();
    		wrapperShelf.eq(WarehouseShelf::getShopid, shelf.getShopid());
    		wrapperShelf.eq(WarehouseShelf::getParentid, shelf.getParentid());
    		wrapperShelf.eq(WarehouseShelf::getIsdelete, false);
    		wrapperShelf.eq(WarehouseShelf::getNumber, shelf.getNumber());
			List<WarehouseShelf> result = this.baseMapper.selectList(wrapperShelf);
			if(result.size()>0) {
				for(WarehouseShelf item:result) {
					if(!item.getId().equals(shelf.getId())) {
						throw new BizException("编码["+item.getNumber()+"] "+"已经存在，请修改后提交");
					}
				}
			}
			return super.updateById(shelf);
		}
 
}
