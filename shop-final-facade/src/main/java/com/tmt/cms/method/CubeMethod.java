package com.tmt.cms.method;

import java.util.List;
import java.util.Map;

import com.tmt.core.utils.StringUtils;

import freemarker.ext.beans.MapModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 魔方
 * 
 * @author root
 */
public class CubeMethod implements TemplateMethodModelEx {

	/**
	 * 构建魔方数据
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object exec(List arguments) throws TemplateModelException {
		// 获得数据
		MapModel _config = (MapModel) arguments.get(0);
		Map<String, Object> config = (Map<String, Object>) _config.getWrappedObject();
		StringBuilder template = new StringBuilder();

		// denerateLayoutMap
		int layout_width = (int) config.get("layout_width");
		int layout_height = (int) config.get("layout_height");
		Sub[][] subs = new Sub[layout_height][layout_width];
		List<Map<String, Object>> sub_entry = (List<Map<String, Object>>) config.get("sub_entry");
		if (sub_entry != null && sub_entry.size() != 0) {
			for (Map<String, Object> _sub : sub_entry) {
				int x = (int) _sub.get("x");
				int y = (int) _sub.get("y");
				int w = (int) _sub.get("width");
				int h = (int) _sub.get("height");
				for (int n = y; n < y + h; n++) {
					for (int i = x; i < w + x; i++) {
						subs[n][i] = new Sub(_sub);
					}
				}

			}
		}

		// denerateTableContent
		for (int s = 0; s < layout_height; s++) {
			template.append("<tr>");
			for (int a = 0; a < layout_width; a++) {
				Sub sub = subs[s][a];
				if (sub != null) {
					if (s == sub.getY() && a == sub.getX()) {
						template.append(this.tdTemplate(sub));
					}
				} else {
					template.append("<td class=\"empty\" data-x=\"").append(a).append("\" data-y=\"").append(s)
							.append("\"></td>");
				}
			}
			template.append("</tr>");
		}

		return template;
	}

	// td
	private String tdTemplate(Sub sub) {
		StringBuilder template = new StringBuilder();
		template.append("<td class=\"not-empty cols-").append(sub.getWidth()).append(" rows-").append(sub.getHeight())
				.append("\"").append(" colspan=\"").append(sub.getWidth()).append("\"").append(" rowspan=\"")
				.append(sub.getHeight()).append("\">");
		String link_url = sub.getLink_url();
		String image_url = sub.getImage_url();
		if (StringUtils.isNotBlank(link_url)) {
			template.append("<a href=\"").append(link_url).append("\">").append("<img src=\"").append(image_url)
					.append("\"").append(" class=\"");
			if (StringUtils.isNotBlank(image_url)) {
				template.append("has-img\"></a>");
			} else {
				template.append("no-img\"></a>");
			}
		} else {
			template.append("<img src=\"").append(image_url).append("\"").append(" class=\"");
			if (StringUtils.isNotBlank(image_url)) {
				template.append("has-img\">");
			} else {
				template.append("no-img\">");
			}
		}
		template.append("</td>");
		return template.toString();
	}

	// 模型数据
	static class Sub {

		private Map<String, Object> value;

		Sub(Map<String, Object> value) {
			this.value = value;
		}

		public int getX() {
			return (int) value.get("x");
		}

		public int getY() {
			return (int) value.get("y");
		}

		public int getWidth() {
			return (int) value.get("width");
		}

		public int getHeight() {
			return (int) value.get("height");
		}

		public int getIndex() {
			return (int) value.get("index");
		}

		public String getImage_url() {
			return (String) value.get("image_url");
		}

		public String getLink_url() {
			return (String) value.get("link_url");
		}
	}
}
