package com.tmt.core.staticize;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 快照设置， 其实和模板是一样的
 * @author root
 */
@XmlRootElement(name="snapshot")
public class Snapshot extends Template implements Serializable{
	private static final long serialVersionUID = 1L;
}