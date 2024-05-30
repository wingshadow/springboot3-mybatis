package com.hawk.mybatis.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hawk.common.entity.BaseDataEntity;
import com.hawk.common.exception.ServiceException;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.common.core.domain.model.LoginUser;
import com.hawk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;


/**
 * @program: springboot3-mybatis
 * @description: 自动填充数据对象创建人、创建时间、更新人、更新时间
 * @author: zhb
 * @create: 2024-05-15 15:12
 */
@Slf4j
public class FillInsertAndUpdateMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDataEntity) {
                BaseDataEntity baseDataEntity = (BaseDataEntity) metaObject.getOriginalObject();
                Date current = ObjectUtil.isNotNull(baseDataEntity.getCreateTime())
                        ? baseDataEntity.getCreateTime() : new Date();
                baseDataEntity.setCreateTime(current);
                baseDataEntity.setUpdateTime(current);
                String username = StringUtils.isNotBlank(baseDataEntity.getCreateBy())
                        ? baseDataEntity.getCreateBy() : getLoginAccount();
                // 当前已登录 且 创建人为空 则填充
                baseDataEntity.setCreateBy(username);
                // 当前已登录 且 更新人为空 则填充
                baseDataEntity.setUpdateBy(username);
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDataEntity) {
                BaseDataEntity baseDataEntity = (BaseDataEntity) metaObject.getOriginalObject();
                Date current = new Date();
                // 更新时间填充(不管为不为空)
                baseDataEntity.setUpdateTime(current);
                String username = getLoginAccount();
                // 当前已登录 更新人填充(不管为不为空)
                if (StringUtils.isNotBlank(username)) {
                    baseDataEntity.setUpdateBy(username);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    private String getLoginAccount() {
        LoginUser loginUser;
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录");
            return null;
        }
        return ObjectUtil.isNotNull(loginUser) ? loginUser.getUserAccount() : null;
    }
}
