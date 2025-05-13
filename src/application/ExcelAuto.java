package application;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelAuto {
	
	DBMSConnection dbms = new DBMSConnection(DBMSConnection.getURL(),DBMSConnection.getUsername() , DBMSConnection.getPassword());
	
	Project project = dbms.loadProject();
	
	ExcelAuto(){
		
	}
	
    public void excelSheet() {
        
        Workbook workbook = new XSSFWorkbook();

        // Creating a sheet in the workbook
        Sheet sheet = workbook.createSheet("Machines");

        // Column Headers
        String[] columns = {"Machine Type", "Number of Machines", "Size of Machine", "Machine Cost", "Installation Cost", "Total Cost"};

        // The data goes here
        Object[][] data = {
            {project.getMTypeA(), project.getNumMachineA(), project.getSizeA(), 50000, 10000, 60000},
            {project.getMTypeB(), project.getNumMachineB(), project.getSizeB(), 20000, 5000, 25000},
            {"Lathe Machine", 3, "Small", 30000, 7000, 37000}
        };

        Font headerFont = workbook.createFont();
        
        CellStyle headerCellStyle = workbook.createCellStyle();

        CellStyle dataCellStyle = workbook.createCellStyle();
        
        CellStyle totalCostCellStyle = workbook.createCellStyle();
        
        styling(headerFont,headerCellStyle,dataCellStyle,totalCostCellStyle);
       

      headerRow(sheet,columns,headerCellStyle);

      dataToSheet(data,sheet,dataCellStyle,totalCostCellStyle);

        // Auto-size columns to fit the content IMPORTANT
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        saveFile(workbook);
        
    }
    
    private void headerRow(Sheet sheet, String[] columns, CellStyle headerCellStyle) {
    	  // Create the header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

       private void dataToSheet(Object[][] data,Sheet sheet,CellStyle dataCellStyle, CellStyle totalCostCellStyle) {
    	   //data to the sheet
           int rowNum = 1;
           for (Object[] rowData : data) {
               Row row = sheet.createRow(rowNum++);
               int colNum = 0;
               for (Object field : rowData) {
                   Cell cell = row.createCell(colNum++);
                   if (field instanceof String) {
                       cell.setCellValue((String) field);
                   } else if (field instanceof Integer) {
                       cell.setCellValue((Integer) field);
                   } else if (field instanceof Double) {
                       cell.setCellValue((Double) field);
                   }

                   // Apply data cell style
                   if (colNum - 1 != 5) { // Not the "Total Cost" column
                       cell.setCellStyle(dataCellStyle);
                   } else {
                       cell.setCellStyle(totalCostCellStyle); // Apply special style for "Total Cost"
                   }
               }
           }		
	}

	public void saveFile(Workbook workbook) {
    	   // Save the workbook to a file
           try (FileOutputStream fileOut = new FileOutputStream("Machines.xlsx")) {
               workbook.write(fileOut);
               System.out.println("Excel file created successfully!");
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               try {
                   workbook.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
	
	private void styling(Font headerFont,CellStyle headerCellStyle, CellStyle dataCellStyle ,CellStyle totalCostCellStyle) {
	       
		//font for headers
		headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
       
        // Create a cell style for headers
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        
        // Create a cell style for data cells
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        
        // Create a cell style for total cost cells
        totalCostCellStyle.setBorderBottom(BorderStyle.THIN);
        totalCostCellStyle.setBorderTop(BorderStyle.THIN);
        totalCostCellStyle.setBorderLeft(BorderStyle.THIN);
        totalCostCellStyle.setBorderRight(BorderStyle.THIN);
        totalCostCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        totalCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}
    
}