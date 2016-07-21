package test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import org.junit.Test;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.serotonin.mango.rt.dataImage.IDataPoint;

public class test {

	@Test
	public void test() {
//		int istart = script.indexOf("PDSEARCH_START,");
//		int iend = script.indexOf(",PDSEARCH_END");
//		if (istart > 0 && iend > 0) {
//
//			String pdStr = script.substring(istart + 15, iend);
//			String[] pralist = pdStr.split(",");

			try {
				File f = new File("d:\\thl\\副本节能量计算表.xls");
				FileInputStream fis;
				fis = new FileInputStream(f);
				jxl.Workbook rwb = Workbook.getWorkbook(fis);
				Sheet[] sheet = rwb.getSheets();
				if (sheet.length == 2) {
					int ibar = 0;
					int ikw = 0;
//					if (pralist.length == 3) {
						Sheet rs;
//						if (pralist[0].equals("1")) {
							rs = rwb.getSheet(0);
//						} else {
//							rs = rwb.getSheet(1);
//						}
//
//						IDataPoint pointbar = context.get(pralist[1]);
//						IDataPoint pointkw = context.get(pralist[2]);

						double bar = new BigDecimal(8.0)
								.setScale(1, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						int kw = new BigDecimal(110).setScale(0,
								BigDecimal.ROUND_HALF_UP).intValue();

						if (rs.getRows() > 1) {
							Cell[] row1 = rs.getRow(0);
							for (int j = 1; j < row1.length; j++) {
								String barstr = row1[j].getContents().trim();
								if (!barstr.equals("") && bar==Double.valueOf(barstr)){
									ibar = j;
									break;
								}
							}
						}

						if (rs.getColumns() > 1) {
							Cell[] col1 = rs.getColumn(0);
							for (int k = 1; k < col1.length; k++) {
								String kwstr = col1[k].getContents().trim();
								if (!kwstr.equals("") && Integer.valueOf(kwstr) == kw) {
									ikw = k;
									break;
								}
							}
						}
						String searchResult = "0";
						if (ibar == 0 || ikw == 0) {
						} else {
							searchResult = rs.getCell(ibar, ikw)
									.getContents();
						}
						System.out.print(searchResult);
					}
//				}
				fis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
