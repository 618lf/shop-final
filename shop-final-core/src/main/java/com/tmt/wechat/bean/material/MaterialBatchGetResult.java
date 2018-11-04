package com.tmt.wechat.bean.material;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MaterialBatchGetResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total_count;
	private int item_count;
	private List<MaterialBatchGetNewsItem> items;

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	public List<MaterialBatchGetNewsItem> getItems() {
		return this.items;
	}

	public void setItems(List<MaterialBatchGetNewsItem> items) {
		this.items = items;
	}

	public static class MaterialBatchGetNewsItem {
		private String mediaId;
		private Date updateTime;
		private String name;
		private String url;

		public String getMediaId() {
			return this.mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}

		public Date getUpdateTime() {
			return this.updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return this.url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
