package com.hawk.admin.orm.service.impl;

import com.hawk.admin.orm.dao.SysDeptMapper;
import com.hawk.admin.orm.entity.SysDept;
import com.hawk.admin.orm.service.SysDeptService;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
}
