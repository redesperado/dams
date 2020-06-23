package com.jqf.dams.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;



public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";

    /**
     * 根据文件后缀获取对应Workbook对象
     * @param filePath
     * @param fileType
     * @return
     */
    public static Workbook getWorkbook(String filePath,String fileType){
        Workbook workbook = null;
        FileInputStream fileInputStream = null;
        try{
            File excelFile = new File(filePath);
            if(!excelFile.exists()){
                logger.info(filePath+"文件不存在");
                return null;
            }
            fileInputStream = new FileInputStream(excelFile);
            if(fileType.equalsIgnoreCase(XLS)){
                workbook = new HSSFWorkbook(fileInputStream);
            }else if(fileType.equalsIgnoreCase(XLSX)){
                workbook = new XSSFWorkbook(fileInputStream);
            }
        }catch (Exception e){
            logger.error("获取文件失败",e);
        }finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                logger.error("关闭数据流出错！错误信息：" , e);
                return null;
            }
        }
        return workbook;
    }

    public static List<Object> readFolder(String filePath){
        int fileNum = 0;
        File file = new File(filePath);
        List<Object> returnList = new ArrayList<>();
        List<Map<String,String>> resultList = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isFile()) {
                    resultList = readExcel(file2.getAbsolutePath());
                    returnList.add(resultList);
                    fileNum++;
                }
            } 
        } else {
            logger.info("文件夹不存在");
            return null;
        }
        logger.info("共有文件："+fileNum);
        return returnList;
    }

    /**
     * 批量读取Excel文件，返回数据对象
     * @param filePath
     * @return
     */
    public static List<Map<String,String>> readExcel(String filePath){
        Workbook workbook = null;
        List<Map<String,String>> resultList = new ArrayList<>();
        try{
            String fileType = filePath.substring(filePath.lastIndexOf("."));
            workbook = getWorkbook(filePath,fileType);
            if(workbook == null){
                logger.info("获取workbook对象失败");
                return null;
            }
            resultList = analysisExcel(workbook);
            return resultList;
        }catch (Exception e){
            logger.error("读取Excel文件失败"+filePath+"错误信息",e);
            return null;
        }finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                logger.error("关闭数据流出错！错误信息：" , e);
                return null;
            }

        }
    }

    /**
     * 解析Excel文件，返回数据对象
     * @param workbook
     * @return
     */
    public static List<Map<String,String>> analysisExcel(Workbook workbook){
        List<Map<String,String>> dataList = new ArrayList<>();
        int sheetCount = workbook.getNumberOfSheets();//或取一个Excel中sheet数量
        for(int i = 0 ; i < sheetCount ; i ++){
            Sheet sheet = workbook.getSheetAt(i);

            if(sheet == null){
                continue;
            }
            int firstRowCount = sheet.getFirstRowNum();//获取第一行的序号
            Row firstRow = sheet.getRow(firstRowCount);
            int cellCount = firstRow.getLastCellNum();//获取列数

            List<String> mapKey = new ArrayList<>();

            //获取表头信息，放在List中备用
            if(firstRow == null){
                logger.info("解析Excel失败，在第一行没有读取到任何数据！");
            }else {
                for (int i1 = 0; i1 < cellCount; i1++) {
                    mapKey.add(firstRow.getCell(i1).toString());
                }
            }

            //解析每一行数据，构成数据对象
            int rowStart = firstRowCount + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for(int j = rowStart ; j < rowEnd ; j ++){
                Row row = sheet.getRow(j);//获取对应的row对象

                if(row == null){
                    continue;
                }

                Map<String,String> dataMap = new HashMap<>();
                //将每一行数据转化为一个Map对象
                dataMap = convertRowToData(row,cellCount,mapKey);
                dataList.add(dataMap);
            }
        }
        return dataList;
    }

    /**
     * 将每一行数据转化为一个Map对象
     * @param row 行对象
     * @param cellCount 列数
     * @param mapKey  表头Map
     * @return
     */
    public static Map<String,String> convertRowToData(Row row,int cellCount,List<String> mapKey){
        if(mapKey == null){
            logger.info("没有表头信息");
            return null;
        }
        Map<String,String> resultMap = new HashMap<>();
        Cell cell = null;
        for(int i = 0 ; i < cellCount ; i ++){
            cell = row.getCell(i);
            if(cell == null){
                resultMap.put(mapKey.get(i),"");
            }else {
//                resultMap.put(mapKey.get(i),getCellVal(cell));
                if(mapKey.get(i).equals("stuNumber")||mapKey.get(i).equals("id")||mapKey.get(i).equals("className")||mapKey.get(i).equals("dormBuilding")||mapKey.get(i).equals("dormNumber")||mapKey.get(i).equals("dormCapacity")||mapKey.get(i).equals("isFull")||mapKey.get(i).equals("remainNumber")){
                    double d = cell.getNumericCellValue();
                    resultMap.put(mapKey.get(i),Double.valueOf(d).longValue()+"");
                }else {
                    resultMap.put(mapKey.get(i),getCellVal(cell));
                }
            }
        }
        return resultMap;
    }

    /**
     * 获取单元格的值
     * @param cel
     * @return
     */
    public static String getCellVal(Cell cel) {
        if(cel.getCellType() == Cell.CELL_TYPE_STRING) {
            return cel.getRichStringCellValue().getString();
        }
        if(cel.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cel.getNumericCellValue() + "";
        }
        if(cel.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cel.getBooleanCellValue() + "";
        }
        if(cel.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cel.getCellFormula() + "";
        }
        return cel.toString();
    }


    public static void main(String[] args) {
        //读取文件夹，批量解析Excel文件
        System.out.println("--------------------读取文件夹，批量解析Excel文件-----------------------");
        List<Object> returnList = readFolder("C:\\Users\\Administrator\\Desktop\\ExcelTest");
        for(int i = 0 ; i < returnList.size() ; i ++){
            List<Map<String,String>> maps = (List<Map<String,String>>)returnList.get(i);
            for(int j = 0 ; j < maps.size() ; j ++){
                System.out.println(maps.get(j).toString());
            }
            System.out.println("--------------------手打List切割线-----------------------");
        }

        //读取单个文件
        System.out.println("--------------------读取并解析单个文件-----------------------");
        List<Map<String,String>> maps = readExcel("C:\\Users\\Administrator\\Desktop\\ExcelTest\\学生表.xlsx");
        for(int j = 0 ; j < maps.size() ; j ++){
            System.out.println(maps.get(j).toString());
        }
    }

}
