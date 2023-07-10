package com.wimoor.admin.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.common.constants.GlobalConstants;
import com.wimoor.admin.mapper.SysPermissionMapper;
import com.wimoor.admin.pojo.entity.SysPermission;
import com.wimoor.admin.pojo.vo.PermissionVO;
import com.wimoor.admin.service.ISysPermissionService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;


/**
 * 权限业务类
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    

    @Override
    public IPage<PermissionVO> list(Page<PermissionVO> page, String name, Long menuId) {
        List<PermissionVO> list= this.baseMapper.list(page, name,menuId);
        page.setRecords(list);
        return page;
    }

    @Override
    public List<SysPermission> listPermRoles() {
        return this.baseMapper.listPermRoles();
    }

	@Override
    public boolean refreshPermRolesRules() {
		stringRedisTemplate.delete(Arrays.asList(GlobalConstants.URL_PERM_ROLES_KEY, GlobalConstants.BTN_PERM_ROLES_KEY));
        List<SysPermission> permissions = this.listPermRoles();
        if (CollectionUtil.isNotEmpty(permissions)) {
            // 初始化URL【权限->角色(集合)】规则
            List<SysPermission> urlPermList = permissions.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getUrlPerm()))
                    .collect(Collectors.toList());
            
            if (CollectionUtil.isNotEmpty(urlPermList)) {
                Map<String,Set<String>> urlPermRoles = new HashMap<String,Set<String>>();
                urlPermList.stream().forEach(item -> {
                    String perm = item.getUrlPerm();
                    Set<String> rolesSet=urlPermRoles.get(perm);
                    if(rolesSet==null) {
                    	rolesSet=new HashSet<String>();
                    }
                    rolesSet.add(item.getRoleids());
                    urlPermRoles.put(perm, rolesSet);
                });
                Map<String,String> urlPermRolesMap =  CollectionUtil.newHashMap();
                for(String  key:urlPermRoles.keySet()) {
                	Set<String> value = urlPermRoles.get(key);
                	if(value!=null&&value.size()>0) {
                		StringBuffer roles=new StringBuffer();
                		for(String roleid:value) {
                			roles.append(roleid+",");
                		}
                		String rolesid=roles.toString();
                		if(rolesid.contains(",")) {
                			rolesid=rolesid.substring(0,rolesid.length()-1);
                		}
                		urlPermRolesMap.put(key,rolesid);
                	}
                }
                stringRedisTemplate.opsForHash().putAll(GlobalConstants.URL_PERM_ROLES_KEY, urlPermRolesMap);
                stringRedisTemplate.convertAndSend("cleanRoleLocalCache", "true");
            }
            // 初始化URL【按钮->角色(集合)】规则
            List<SysPermission> btnPermList = permissions.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getBtnPerm()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(btnPermList)) {
                Map<String, String> btnPermRoles = CollectionUtil.newHashMap();
                btnPermList.stream().forEach(item -> {
                    String perm = item.getBtnPerm();
                    btnPermRoles.put(perm, item.getRoleids());
                });
                stringRedisTemplate.opsForHash().putAll(GlobalConstants.BTN_PERM_ROLES_KEY, btnPermRoles);
            }
        }
        return true;
    }

    @Override
    public List<String> listBtnPermByRoles(List<String> roles) {
        List<String> perms = this.baseMapper.listBtnPermByRoles(roles);
        return perms;
    }
}
