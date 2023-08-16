package com.wimoor.admin.service;


import java.math.BigInteger;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysRole;

public interface ISysRoleService extends IService<SysRole> {

    boolean delete(List<BigInteger> ids);
}
