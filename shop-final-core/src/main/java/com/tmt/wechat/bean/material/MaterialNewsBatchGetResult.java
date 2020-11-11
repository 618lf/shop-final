package com.tmt.wechat.bean.material;

import java.util.Date;
import java.util.List;

public class MaterialNewsBatchGetResult {

	private int total_count;
	private int item_count;
	private List<MaterialNewsBatchItem> items;

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

	public List<MaterialNewsBatchItem> getItems() {
		return items;
	}

	public void setItems(List<MaterialNewsBatchItem> items) {
		this.items = items;
	}

	public static class MaterialNewsBatchItem {
		private String mediaId;
		private Date updateTime;
		private MaterialNewsResult content;

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

		public MaterialNewsResult getContent() {
			return this.content;
		}

		public void setContent(MaterialNewsResult content) {
			this.content = content;
		}
	}
}
