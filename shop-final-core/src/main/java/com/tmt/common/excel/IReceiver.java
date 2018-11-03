package com.tmt.common.excel;

import java.util.Map;
/**
 * 
 * @ClassName: IReceiver 
 * @author 李锋
 * @date 2013-4-26 下午09:48:04 
 *
 */
public interface IReceiver<T> {
	
    /**
     * 接收收据
     * @param Excel转换过来的值对象：valueMap
     * @param 映射对象：excelMapper
     * @return 实体对象
     */
	 T receive(Map<String,Object> valueMap, Class<T> clazz);
}
