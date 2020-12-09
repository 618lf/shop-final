package com.tmt.api.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.api.entity.Mock;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * Mock 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Repository("apiMockDao")
public class MockDao extends BaseDaoImpl<Mock,Long> {}
