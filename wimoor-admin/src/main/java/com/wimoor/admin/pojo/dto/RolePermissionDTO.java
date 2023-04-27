package com.wimoor.admin.pojo.dto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class RolePermissionDTO {
    private BigInteger roleId;
    private List<BigInteger> permissionIds;
    private BigInteger menuId;
}
