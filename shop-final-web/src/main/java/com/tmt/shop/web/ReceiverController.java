package com.tmt.shop.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.ColumnMapper;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.ExcelExpUtils;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.system.entity.User;

/**
 * 会员地址 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopReceiverController")
@RequestMapping(value = "${adminPath}/shop/receiver")
public class ReceiverController {
	
	@Autowired
	private ReceiverServiceFacade receiverService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Receiver receiver, Model model){
		return "/shop/ReceiverList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param receiver
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Receiver receiver, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(receiver, c);
		qc.setOrderByClause("CONSIGNEE");
		return receiverService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param receiver
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Receiver receiver, Model model) {
	    if(receiver != null && !IdGen.isInvalidId(receiver.getId())) {
			receiver = this.receiverService.get(receiver.getId());
		} else {
		   if( receiver == null) {
			   receiver = new Receiver();
		   }
		   receiver.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("receiver", receiver);
		return "/shop/ReceiverForm";
	}
	
	/**
	 *  获得用户默认的地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get_user_default")
	public AjaxResult getDefault(Long userId) {
		User user = new User(); user.setId(userId);
		Receiver receiver = this.receiverService.queryUserDefaultReceiver(user);
		return receiver != null ? AjaxResult.success(receiver) : AjaxResult.error("没有地址信息");
	}

	//查询条件
	private void initQc(Receiver receiver, Criteria c) {
		if (StringUtils.isNotBlank(receiver.getConsignee())) {
			c.andEqualTo("CONSIGNEE", receiver.getConsignee());
		}
		if (StringUtils.isNotBlank(receiver.getCreateName())) {
			c.andEqualTo("CREATE_NAME", receiver.getCreateName());
		}
		if (StringUtils.isNotBlank(receiver.getPhone())) {
			c.andEqualTo("PHONE", receiver.getPhone());
		}
	}

	/**
	 * 返回导出的数据
	 */
	public Map<String, Object> doExport(Receiver param, HttpServletRequest request) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria(); this.initQc(param, c);
		String[] ids = request.getParameterValues("export.idList");
		if(null != ids && ids.length != 0){
			List<String> idLst = Lists.newArrayList();
			idLst = Arrays.asList(ids);
			c.andIn("ID", idLst);
		}
		qc.setOrderByClause("CONSIGNEE");
		List<Receiver> receivers = this.receiverService.queryByCondition(qc);
		List<Map<String, Object>> values = Lists.newArrayList();
		for(Receiver item: receivers) {
			Map<String, Object> value = Maps.newHashMap();
			value.put("consignee", item.getConsignee());
			value.put("sex", item.getSex() == 1?"男":"女");
			value.put("phone", item.getPhone());
			value.put("tag", item.getTag());
			value.put("address", item.getFullAddress());
			value.put("isDefault", item.getIsDefault() == 1?"是":"否");
			value.put("createName", item.getCreateName());
			values.add(value);
		}
		List<ColumnMapper> columns = Lists.newArrayList();
		columns.add(ColumnMapper.buildExpMapper("收货人","B", DataType.STRING, "consignee"));
		columns.add(ColumnMapper.buildExpMapper("性别","C", DataType.STRING, "sex"));
		columns.add(ColumnMapper.buildExpMapper("手机","D", DataType.STRING, "phone"));
		columns.add(ColumnMapper.buildExpMapper("标签","E", DataType.STRING, "tag"));
		columns.add(ColumnMapper.buildExpMapper("地址","F", DataType.STRING, "address"));
		columns.add(ColumnMapper.buildExpMapper("是否默认","G", DataType.STRING, "isDefault"));
		columns.add(ColumnMapper.buildExpMapper("会员","H", DataType.STRING, "createName"));
		return ExcelExpUtils.buildExpParams("地址导出", "地址导出", columns, values, "receiverTemplate.xls", 2);
	}
}
