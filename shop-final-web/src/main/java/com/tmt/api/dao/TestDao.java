package com.tmt.api.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.api.entity.Test;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 测试 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Repository("apiTestDao")
public class TestDao extends BaseDaoImpl<Test,Long> {}
