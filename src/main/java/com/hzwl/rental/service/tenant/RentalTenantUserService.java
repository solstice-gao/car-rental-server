package com.hzwl.rental.service.tenant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzwl.rental.constants.BizException;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.tenant.RentalTenantUser;
import com.hzwl.rental.entity.user.RentalUser;
import com.hzwl.rental.mapper.tenant.RentalTenantUserMapper;
import com.hzwl.rental.utils.PwdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Author GA666666
 * @Date 2023/9/3 12:43
 */
@Slf4j
@Service
public class RentalTenantUserService {


    @Autowired
    private RentalTenantUserMapper rentalTenantUserMapper;

    /**
     * 注册用户
     *
     * @param rentalTenantUser
     * @return
     */
    public String register(RentalTenantUser rentalTenantUser) {
        String pass = PwdUtils.encodePassword(rentalTenantUser.getTenantPassword());
        rentalTenantUser.setTenantPassword(pass);
        rentalTenantUser.setLastLoginTime(new Date());
        rentalTenantUser.setUpdateTime(new Date());
        rentalTenantUser.setCreateTime(new Date());
        int insert = this.rentalTenantUserMapper.insert(rentalTenantUser);
        return rentalTenantUser.getTenantId();
    }


    /**
     * 通过ID查询单条数据
     *
     * @param tenantId 主键
     * @return 实例对象
     */
    public RentalTenantUser queryById(String tenantId) {
        return rentalTenantUserMapper.selectById(tenantId);
    }

    /**
     * 分页查询
     *
     * @param rentalTenantUser 筛选条件
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<RentalTenantUser> queryEquities(RentalTenantUser rentalTenantUser, Integer pageNum, Integer pageSize, String startTime, String endTime) {

        Page<RentalTenantUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RentalTenantUser> queryWrapper = new QueryWrapper<>();
        Field[] fields = rentalTenantUser.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(rentalTenantUser);
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    queryWrapper.like(StrUtil.toUnderlineCase(field.getName()), value);
                }
            } catch (IllegalAccessException e) {
                log.error("Error accessing field", e);
            }
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        Page<RentalTenantUser> resultPage = rentalTenantUserMapper.selectPage(page, queryWrapper);
        return resultPage;
    }

    /**
     * 新增数据
     *
     * @param rentalTenantUser 实例对象
     * @return 实例对象
     */
    public RentalTenantUser insert(RentalTenantUser rentalTenantUser) {
        rentalTenantUserMapper.insert(rentalTenantUser);
        return rentalTenantUser;
    }

    /**
     * 更新数据
     *
     * @param rentalTenantUser 实例对象
     * @return 实例对象
     */
    public RentalTenantUser update(RentalTenantUser rentalTenantUser) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<RentalTenantUser> chainWrapper = new LambdaUpdateChainWrapper<RentalTenantUser>(rentalTenantUserMapper);
        if (StrUtil.isNotBlank(rentalTenantUser.getTenantName())) {
            chainWrapper.eq(RentalTenantUser::getTenantName, rentalTenantUser.getTenantName());
        }
        if (StrUtil.isNotBlank(rentalTenantUser.getCompanyId())) {
            chainWrapper.eq(RentalTenantUser::getCompanyId, rentalTenantUser.getCompanyId());
        }
        if (StrUtil.isNotBlank(rentalTenantUser.getTenantPassword())) {
            chainWrapper.eq(RentalTenantUser::getTenantPassword, rentalTenantUser.getTenantPassword());
        }
        if (StrUtil.isNotBlank(rentalTenantUser.getPassSalt())) {
            chainWrapper.eq(RentalTenantUser::getPassSalt, rentalTenantUser.getPassSalt());
        }
        if (StrUtil.isNotBlank(rentalTenantUser.getTenantStatus())) {
            chainWrapper.eq(RentalTenantUser::getTenantStatus, rentalTenantUser.getTenantStatus());
        }
        //2. 设置主键，并更新
        chainWrapper.set(RentalTenantUser::getTenantId, rentalTenantUser.getTenantId());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(rentalTenantUser.getTenantId());
        } else {
            return rentalTenantUser;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param tenantId 主键
     * @return 是否成功
     */
    public boolean deleteById(String tenantId) {
        int total = rentalTenantUserMapper.deleteById(tenantId);
        return total > 0;
    }


    public RentalTenantUser login(RentalTenantUser rentalTenantUser) {
        QueryWrapper<RentalTenantUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_name", rentalTenantUser.getTenantName());
        RentalTenantUser tenantUser = this.rentalTenantUserMapper.selectOne(queryWrapper);
        Assert.notNull(tenantUser, ErrorCode.TENNAT_IS_BLANK.getErrorMsg());
        String encodePassword = PwdUtils.encodePassword(rentalTenantUser.getTenantPassword());


        if (!encodePassword.equals(tenantUser.getTenantPassword())) {
            throw new BizException(ErrorCode.VERIFY_PASSWORD_FAILED);
        }

        StpUtil.login(tenantUser.getTenantId());
        this.updateLastLoginTime(tenantUser);
        tenantUser.setTenantPassword(null);
        return tenantUser;
    }


    @Async
    private void updateLastLoginTime(RentalTenantUser rentalUser) {
        rentalUser.setLastLoginTime(new Date());
        rentalTenantUserMapper.updateById(rentalUser);
    }
}