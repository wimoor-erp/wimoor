package com.wimoor.admin.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysMenu;
import com.wimoor.admin.pojo.vo.MenuVO;
import com.wimoor.admin.pojo.vo.NextRouteVO;
import com.wimoor.admin.pojo.vo.RouteVO;
import com.wimoor.common.SelectVO;
import com.wimoor.common.TreeSelectVO;
import com.wimoor.common.user.UserInfo;
 

/**
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2020-11-06
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 菜单表格(Table)层级列表
     *
     * @param name 菜单名称
     * @return
     */
    List<MenuVO> listTable(String name);


    /**
     * 菜单下拉(Select)层级列表
     *
     * @return
     */
    List<SelectVO> listSelect();

 
    /**
     * 菜单下拉(TreeSelect)层级列表
     *
     * @return
     */
    List<TreeSelectVO> listTreeSelect();

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    boolean saveMenu(SysMenu menu);


    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 清理路由缓存
     */
    void cleanCache();
    public void cleanCacheByUser(UserInfo user) ;

	/**
	 * 获取路由列表(适配Vue3的vue-next-router)
	 *
	 * @return
	 */
	List<NextRouteVO> listNextRoutes(UserInfo user);


	/**
	 * 菜单路由（Route）层级列表
	 * <p>
	 * 读多写少，缓存至Redis
	 *
	 * @return
	 * @Cacheable cacheNames:缓存名称，不同缓存的数据是彼此隔离； key: 缓存Key。
	 */
	List<RouteVO> listRoute(UserInfo user);


	public List<MenuVO> listCompanyTreeSelect(UserInfo user);

}
