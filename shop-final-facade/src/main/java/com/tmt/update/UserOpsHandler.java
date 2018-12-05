package com.tmt.update;

import java.util.List;

import com.tmt.Constants;
import com.tmt.system.entity.UpdateData;

/**
 * 用户操作
 * 
 * @author lifeng
 */
public class UserOpsHandler implements UpdateHandler {

	@Override
	public boolean handleFor(Byte moudle) {
		return moudle != null && (moudle == Constants.USER_IN
				|| moudle == Constants.USER_UP);
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		return null;
	}

	@Override
	public Boolean doHandler(UpdateData data) {
		return null;
	}
}