package com.wimoor.admin.service.impl;

 
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.common.constants.GlobalConstants;
import com.wimoor.admin.common.constants.SystemConstants;
import com.wimoor.admin.mapper.SysMenuMapper;
import com.wimoor.admin.pojo.entity.SysMenu;
import com.wimoor.admin.pojo.entity.SysPermission;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.pojo.vo.MenuVO;
import com.wimoor.admin.pojo.vo.NextRouteVO;
import com.wimoor.admin.pojo.vo.RouteVO;
import com.wimoor.admin.pojo.vo.RouteVO.Meta;
import com.wimoor.admin.service.ISysMenuService;
import com.wimoor.admin.service.ISysPermissionService;
import com.wimoor.admin.service.ISysRoleMenuService;
import com.wimoor.admin.service.ISysRolePermissionService;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.common.SelectVO;
import com.wimoor.common.TreeSelectVO;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserType;
import com.wimoor.manager.service.IManagerLimitService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;


/**
 * 菜单业务类
 *
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2020-11-06
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysPermissionService permissionService;
    private final ISysRolePermissionService iSysRolePermissionService;
    private final ISysUserRoleService iSysUserRoleService;
    private final IManagerLimitService iManagerLimitService;
    private final ISysRoleMenuService iSysRoleMenuService;
    /**
     * 菜单表格（Table）层级列表
     *
     * @param name 菜单名称
     * @return
     */
    @Override
    public List<MenuVO> listTable(String name) {
        List<SysMenu> menuList = this.list(
                new LambdaQueryWrapper<SysMenu>()
                        .like(StrUtil.isNotBlank(name), SysMenu::getName, name)
                        .orderByAsc(SysMenu::getSort)
        );
        return recursion(menuList);
    }

    /**
     * 递归生成菜单表格层级列表
     *
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private static List<MenuVO> recursion(List<SysMenu> menuList) {
        List<MenuVO> menuTableList = new ArrayList<>();
        // 保存所有节点的 id
        Set<String> nodeIdSet = menuList.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());
        for (SysMenu sysMenu : menuList) {
            // 不在节点 id 集合中存在的 id 即为顶级节点 id, 递归生成列表
            String parentId = sysMenu.getParentId();
            if (!nodeIdSet.contains(parentId)) {
                menuTableList.addAll(recursionTableList(parentId, menuList));
                nodeIdSet.add(parentId);
            }
        }
        // 如果结果列表为空说明所有的节点都是独立分散的, 直接转换后返回
        if (menuTableList.isEmpty()) {
            return menuList.stream()
                    .map(item -> {
                        MenuVO menuVO = new MenuVO();
                        BeanUtil.copyProperties(item, menuVO);
                        return menuVO;
                    })
                    .collect(Collectors.toList());
        }
        return menuTableList;
    }

    /**
     * 递归生成菜单表格层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private static List<MenuVO> recursionTableList(String parentId, List<SysMenu> menuList) {
        List<MenuVO> menuTableList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    MenuVO menuVO = new MenuVO();
                    BeanUtil.copyProperties(menu, menuVO);
                    List<MenuVO> children = recursionTableList(menu.getId(), menuList);

                    if (CollectionUtil.isNotEmpty(children)) {
                        menuVO.setChildren(children);
                    }
                    menuTableList.add(menuVO);
                });
        return menuTableList;
    }


    /**
     * 菜单下拉（Select）层级列表
     *
     * @return
     */
    @Override
    public List<SelectVO> listSelect() {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        List<SelectVO> menuSelectList = recursionSelectList(SystemConstants.ROOT_MENU_ID, menuList);
        return menuSelectList;
    }


    /**
     * 递归生成菜单下拉层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private static List<SelectVO> recursionSelectList(String parentId, List<SysMenu> menuList) {
        List<SelectVO> menuSelectList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    SelectVO selectVO = new SelectVO(menu.getId(), menu.getName());
                    List<SelectVO> children = recursionSelectList(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        selectVO.setChildren(children);
                    }
                    menuSelectList.add(selectVO);
                });
        return menuSelectList;
    }


    /**
     * 菜单路由（Route）层级列表
     * <p>
     * 读多写少，缓存至Redis
     *
     * @return
     * @Cacheable cacheNames:缓存名称，不同缓存的数据是彼此隔离； key: 缓存Key。
     */

    @Override
    @Cacheable(cacheNames = "system", key = "#user.id")
    public List<RouteVO> listRoute(UserInfo user) {
        List<SysUserRole> roleList = iSysUserRoleService.findByUserId(user.getId());
        List<SysMenu> menuList = null;
        boolean isadmin=user.getUsertype().equals(UserType.admin.getCode());
        boolean ismanager=user.getUsertype().equals(UserType.manager.getCode());
        if(isadmin) {
        	menuList = this.baseMapper.listRouteAll();
        }else if(ismanager){
        	menuList = this.baseMapper.listRoute(user.getId());
        }else {
        	menuList = this.baseMapper.listRoute(user.getId());
        }
        List<RouteVO> list = recursionRoute(SystemConstants.ROOT_MENU_ID, menuList);
        if(list!=null&&list.size()>0) {
        	Set<String> permissionSet=new HashSet<String>();
        	if(isadmin||ismanager) {
        		roleList.stream().forEach(role->{
              		 List<BigInteger> rp = iSysRolePermissionService.listMenuPermissionId(null);
               		 if(rp.size()>0) {
               			permissionSet.addAll(rp.stream().map(item ->{ return permissionService.getById(item).getBtnPerm().toString();}).collect(Collectors.toSet()));
               		 }
               	   });
            }else {
            	roleList.stream().forEach(role->{
              		 List<BigInteger> rp = iSysRolePermissionService.listPermissionId(null,role.getRoleId());
               		 if(rp.size()>0) {
               			permissionSet.addAll(rp.stream().map(item ->{ return permissionService.getById(item).getBtnPerm().toString();}).collect(Collectors.toSet()));
               		 }
               	   });
            }
       	   Meta meta = list.get(0).getMeta();
       	   if(meta==null) {
       		   meta=new Meta();
       	   }
         	meta.setPermissions(new ArrayList<String>(permissionSet));
         	list.get(0).setMeta(meta);
        }
        return list;
    }

    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<RouteVO> recursionRoute(String parentId, List<SysMenu> menuList) {
        List<RouteVO> list = new LinkedList<>();
        Optional.ofNullable(menuList)
                .ifPresent(menus -> menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouteVO routeVO = new RouteVO();
                    routeVO.setName(menu.getId() + ""); // 根据name路由跳转 this.$router.push({path:xxx})
                    routeVO.setPath(menu.getPath());    // 根据path路由跳转 this.$router.push({name:xxx})
                    routeVO.setRedirect(menu.getRedirect());
                    routeVO.setComponent(menu.getComponent());
                    routeVO.setRedirect(menu.getRedirect());
                    routeVO.setSort(menu.getSort());
                    RouteVO.Meta meta = new RouteVO.Meta(menu.getName(), menu.getIcon(), menu.getPermissions());
                    routeVO.setMeta(meta);
                    // 菜单显示隐藏
                    routeVO.setHidden(!GlobalConstants.STATUS_YES.equals(menu.getVisible()));
                    List<RouteVO> children = recursionRoute(menu.getId(), menuList);
                    routeVO.setChildren(children);
                    if (CollectionUtil.isNotEmpty(children)) {
                        routeVO.setAlwaysShow(Boolean.TRUE);
                    }
                    list.add(routeVO);
                }));
        
        list.sort(new Comparator<RouteVO>() {
			@Override
			public int compare(RouteVO o1, RouteVO o2) {
				// TODO Auto-generated method stub
				return o1.getSort().compareTo(o2.getSort());
			}
        });
        return list;

    }

    /**
     * 菜单下拉（TreeSelect）层级列表
     *
     * @return
     */
    @Override
    public List<TreeSelectVO> listTreeSelect() {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        List<TreeSelectVO> menuSelectList = recursionTreeSelectList(SystemConstants.ROOT_MENU_ID, menuList);
        return menuSelectList;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean saveMenu(SysMenu menu) {
        String component = menu.getComponent();
        if ("Layout".equals(component)) {
            menu.setPath("/" + IdUtil.simpleUUID());
        } else {
            menu.setPath(component.replaceAll("/", "_"));
        }

        boolean result = this.save(menu);
        if (result == true) {
            permissionService.refreshPermRolesRules();
        }
        return result;
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean updateMenu(SysMenu menu) {
        String component = menu.getComponent();

        // 检测页面路径是否变化
        SysMenu oldMenu = this.getById(menu.getId());
        if (oldMenu.getComponent() != null && !oldMenu.getComponent().equals(component)) {
            if ("Layout".equals(component)) {
                menu.setPath("/" + IdUtil.simpleUUID());
            } else {
                menu.setPath(component.replaceAll("/", "_"));
            }
        }
        boolean result = this.updateById(menu);
        if (result == true) {
            permissionService.refreshPermRolesRules();
        }
        return result;
    }

    /**
     * 递归生成菜单下拉(TreeSelect)层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private static List<TreeSelectVO> recursionTreeSelectList(String parentId, List<SysMenu> menuList) {
        List<TreeSelectVO> menuSelectList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    TreeSelectVO treeSelectVO = new TreeSelectVO(menu.getId(), menu.getName());
                    List<TreeSelectVO> children = recursionTreeSelectList(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        treeSelectVO.setChildren(children);
                    }
                    menuSelectList.add(treeSelectVO);
                });
        return menuSelectList;
    }

    /**
     * 清理路由缓存
     */
    @Override
    @CacheEvict(cacheNames = "system", key = "#user.id")
    public void cleanCacheByUser(UserInfo user) {
    }
    /**
     * 清理路由缓存
     */
    @Override
    @CacheEvict(cacheNames = "system", allEntries = true)
    public void cleanCache() {
    }

    /**
     * 获取路由列表(适配Vue3的vue-next-router)
     *
     * @return
     */
    @Override
    @Cacheable(cacheNames = "system", key = "'nextRouteList'")
    public List<NextRouteVO> listNextRoutes(UserInfo user) {
        List<SysMenu> menuList = this.baseMapper.listRoute(user.getId());
        List<NextRouteVO> list = recursionNextRoute(SystemConstants.ROOT_MENU_ID, menuList);
        return list;
    }


    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<NextRouteVO> recursionNextRoute(String parentId, List<SysMenu> menuList) {
        List<NextRouteVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    NextRouteVO nextRouteVO = new NextRouteVO();
                    nextRouteVO.setName(menu.getId() + ""); // 根据name路由跳转 this.$router.push({path:xxx})
                    nextRouteVO.setPath(menu.getPath()); // 根据path路由跳转 this.$router.push({name:xxx})
                    nextRouteVO.setRedirect(menu.getRedirect());
                    nextRouteVO.setComponent(menu.getComponent());
                    nextRouteVO.setRedirect(menu.getRedirect());

                    NextRouteVO.Meta meta = new NextRouteVO.Meta();
                    meta.setTitle(menu.getName());
                    meta.setIcon(menu.getIcon());
                    meta.setRoles(menu.getRoles());
                    meta.setHidden(!GlobalConstants.STATUS_YES.equals(menu.getVisible()));

                    nextRouteVO.setMeta(meta);
                    List<NextRouteVO> children = recursionNextRoute(menu.getId(), menuList);
                    nextRouteVO.setChildren(children);
                    if (CollectionUtil.isNotEmpty(children)) {
                        meta.setAlwaysShow(Boolean.TRUE);
                    }
                    list.add(nextRouteVO);
                }));
        return list;
    }

    public void setPermsession(List<SysMenu> menuList) {
    	List<SysPermission> perms = permissionService.list();
    	Map<String,List<SysPermission>> hasPermMenu=new HashMap<String,List<SysPermission>>();
        for(SysPermission item:perms) {
        	List<SysPermission> list = hasPermMenu.get(item.getMenuId().toString());
        	if(list==null) {
        		list=new LinkedList<SysPermission>();
        	}
        	list.add((item));
        	hasPermMenu.put(item.getMenuId().toString(), list);
        }
    	
    	for(SysMenu item:menuList) {
   		 List<SysPermission> list = hasPermMenu.get(item.getId());
   		 if(list!=null) {
   			 LinkedList<String> permissions = new LinkedList<String>();
   			 for(SysPermission itemp:list) {
   				 permissions.add(itemp.getId());
   			 }
   			 item.setPermissions(permissions);
   		 }
   	  }
    }
	@Override
	public List<MenuVO> listCompanyTreeSelect(UserInfo user) {
		// TODO Auto-generated method stub
        boolean isadmin=user.getUsertype().equals(UserType.admin.getCode());
        if(isadmin) {
        	  List<SysMenu> menuList = this.list();
        	  setPermsession(menuList);
              return recursion(menuList);
        }else {
        	String roleid = iManagerLimitService.getCompanyRole(user.getCompanyid());
        	List<SysUserRole> uroles = this.iSysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, user.getId()).list();
    		List<BigInteger> result = this.iSysRoleMenuService.listMenuIds(new BigInteger(roleid));
    		for(SysUserRole item:uroles) {
    			List<BigInteger> itemresult = this.iSysRoleMenuService.listMenuIds(item.getRoleId());
    			if(itemresult!=null&&itemresult.size()>0) {
    				result.addAll(itemresult);
    			}
    		}
    		List<SysMenu> allList = this.list();
    		List<SysMenu> menuList=new ArrayList<SysMenu>();
    		Set<String> roleMenuIdSet=new HashSet<String>();
    		for(BigInteger id:result) {
    			roleMenuIdSet.add(id.toString());
    		}
    		for(SysMenu menu:allList) {
    			if(roleMenuIdSet.contains(menu.getId())) {
    				menuList.add(menu);
    			}
    		}
    		setPermsession(menuList);
    		List<MenuVO> menuSelectList = recursion(menuList);
    		return menuSelectList;
        }
		
	}

 
}
