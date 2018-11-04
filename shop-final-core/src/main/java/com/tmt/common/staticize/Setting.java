package com.tmt.common.staticize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tmt.common.utils.Maps;

@XmlRootElement(name="setting")
public class Setting implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<Conf> confs;
	private List<Template> templates;
	private List<Snapshot> snapshots;
	private String storagePath;
	private String urlPath;
	
	// 转换后的方式
	private Map<String, Object> _confs;
	private Map<String, Template> _templates;
	private Map<String, Snapshot> _snapshots;
	
	@XmlElement(name = "template")
	public List<Template> getTemplates() {
		return templates;
	}
	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}
	@XmlElement(name = "conf")
	public List<Conf> getConfs() {
		return confs;
	}
	public void setConfs(List<Conf> confs) {
		this.confs = confs;
	}
	@XmlElement(name = "storagePath")
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	@XmlElement(name = "urlPath")
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	@XmlElement(name = "snapshot")
	public List<Snapshot> getSnapshots() {
		return snapshots;
	}
	public void setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
	}
	public Map<String, Object> get_confs() {
		return _confs;
	}
	public Map<String, Template> get_templates() {
		return _templates;
	}
	public Map<String, Snapshot> get_snapshots() {
		return _snapshots;
	}
	/**
	 * 转换一种方式存储
	 */
	public void transform() {
		
		_confs = Maps.newHashMap();
		if (this.confs != null) {
			for(Conf conf: this.confs) {
				_confs.put(conf.getName(), ExpressionUtils.getValue(conf.getValue()));
			}
		}
		
		_templates = Maps.newHashMap();
		if (this.templates != null) {
			for(Template template: this.templates) {
				_templates.put(template.getName(), template);
			}
		}
		
		_snapshots = Maps.newHashMap();
		if (this.snapshots != null) {
			for(Snapshot snapshot: this.snapshots) {
				_snapshots.put(snapshot.getName(), snapshot);
			}
		}
		
		// for gc
		this.templates.clear();
		this.templates = null;
		this.snapshots.clear();
		this.snapshots = null;
	}
}