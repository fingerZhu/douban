package spider4j.douban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class ExcelUtils {

	static HSSFSheet sheet = null;
	
	public static void exportExcel(List<Book> data) {
		
		//加载sheet
		HSSFWorkbook wb = initSheet();
		
		//插入数据
		for (int i = 1, n = data.size()+1; i < n; i++) {
			sheet.createRow(i);
			Book book = data.get(i-1);
			setValue(i, 0, i);
			setValue(i, 1, book.getTag());
			setValue(i, 2, book.getTitle());
			setValue(i, 3, book.getScore());
			setValue(i, 4, book.getPeople());
			setValue(i, 5, book.getAuthor());
			setValue(i, 6, book.getPubCompany());
			setValue(i, 7, book.getPubDate());
			setValue(i, 8, book.getPrice());
		}
		
		//导出
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(FileSystemView.getFileSystemView().getHomeDirectory()+File.separator+"豆瓣图书top100.xls");
			wb.write(fos);
			System.out.println("导出到桌面成功");
		} catch (FileNotFoundException e) {
			System.err.println("该文件正在使用,请关闭后重试^^");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fos!=null){
					fos.close();
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static HSSFWorkbook initSheet(){
		HSSFWorkbook wb = new HSSFWorkbook();
		sheet = wb.createSheet("豆瓣图书top100");// 创建sheet
		sheet.setColumnWidth(0, 6*256);
		sheet.setColumnWidth(1, 7*256);
		sheet.setColumnWidth(2, 30*256);
		sheet.setColumnWidth(3, 8*256);
		sheet.setColumnWidth(4, 10*256);
		sheet.setColumnWidth(5, 35*256);
		sheet.setColumnWidth(6, 25*256);
		sheet.setColumnWidth(7, 10*256);
		sheet.setColumnWidth(8, 8*256);
		// 表头
		HSSFRow headRow = sheet.createRow(0);
        HSSFCellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont font = wb.createFont();
        font.setBold(true);
        headStyle.setFont(font);
        headRow.createCell(0).setCellValue("序号");
        headRow.createCell(1).setCellValue("标签");
        headRow.createCell(2).setCellValue("书名");
        headRow.createCell(3).setCellValue("评分");
        headRow.createCell(4).setCellValue("评价人数");
        headRow.createCell(5).setCellValue("作者");
        headRow.createCell(6).setCellValue("出版社");
        headRow.createCell(7).setCellValue("出版日期");
        headRow.createCell(8).setCellValue("价格");
        for(int i=0;i<9;i++){
        	headRow.getCell(i).setCellStyle(headStyle);
        }
		return wb;
	}
	
	private static void setValue(int rowIndex,int colIndex,Object value){
		if(value instanceof Double)
			sheet.getRow(rowIndex).createCell(colIndex).setCellValue(Double.parseDouble(value+""));
		else if(value instanceof Integer)
			sheet.getRow(rowIndex).createCell(colIndex).setCellValue(Integer.parseInt(value+""));
		else
			sheet.getRow(rowIndex).createCell(colIndex).setCellValue(value+"");
	}
}
