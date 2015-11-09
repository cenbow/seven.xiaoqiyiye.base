package seven.xiaoqiyiye.base.common.excel.parse;

import seven.xiaoqiyiye.base.common.excel.ExcelException;

public interface ExcelRowProcessor<T> {

	T createRow();
	
	void process(RowWrapper<T> rowWrapper) throws ExcelException;
	
}
