package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Message;

@Repository
public class MessageDao extends BaseDaoImpl<Message, Long>{}