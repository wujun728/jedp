package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 查询角色菜单
     *
     * @param parentId parentId
     * @param roleId   roleId
     * @return data
     */
    public List<Integer> getRoleMenu(@Param("parentId") Integer parentId, @Param("roleId") Integer roleId);

    /**
     * 查询数据权限
     *
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 权限
     */
    public Integer queryDataType(@Param("userId") Long userId, @Param("menuId") Integer menuId);

    void deleteWorkRole(@Param("roleId") Integer roleId, @Param("editRoleId") Integer editRoleId);

    List<AdminRole> queryRoleByRoleTypeAndUserId(@Param("type") Integer type,@Param("userId") Long userId);

}
