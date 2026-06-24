package com.wimoor.amazon.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

public class ExcelRowCopyUtil {

    /**
     * 复制指定行范围的所有内容（包括格式、合并单元格）到目标起始行
     * <p>
     * 注意：此方法会覆盖目标行范围内的原有内容，不会自动向下移动其他行。
     * 如需插入式复制（保留原目标行及以下内容），请在调用本方法前自行使用 shiftRows 腾出空间。
     *
     * @param sheet        工作表
     * @param srcStartRow  源起始行索引（0-based）
     * @param srcEndRow    源结束行索引（0-based，包含）
     * @param destStartRow 目标起始行索引（0-based）
     */
    public static void copyRowRange(Sheet sheet, int srcStartRow, int srcEndRow, int destStartRow) {
        // 1. 复制行高和单元格（数据、样式、公式）
        for (int i = srcStartRow; i <= srcEndRow; i++) {
            Row srcRow = sheet.getRow(i);
            int destRowIndex = destStartRow + (i - srcStartRow);
            Row destRow = sheet.getRow(destRowIndex);
            if (destRow == null) {
                destRow = sheet.createRow(destRowIndex);
            } else {
                // 清空目标行原有单元格（避免残留）
                for (int col = 0; col < destRow.getLastCellNum(); col++) {
                    Cell cell = destRow.getCell(col);
                    if (cell != null) {
                        destRow.removeCell(cell);
                    }
                }
            }

            if (srcRow != null) {
                // 复制行高
                destRow.setHeight(srcRow.getHeight());

                // 遍历每一列
                for (int col = 0; col < srcRow.getLastCellNum(); col++) {
                    Cell srcCell = srcRow.getCell(col);
                    if (srcCell != null) {
                        Cell destCell = destRow.createCell(col);
                        copyCellValue(srcCell, destCell);
                        // 复制样式（直接共享，注意旧版 POI 样式数量限制）
                        destCell.setCellStyle(srcCell.getCellStyle());
                    }
                }
            }
        }

        // 2. 处理合并单元格
        List<CellRangeAddress> newMergedRegions = new ArrayList<>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            // 判断合并区域是否与源行范围有交集
            if (merged.getFirstRow() <= srcEndRow && merged.getLastRow() >= srcStartRow) {
                // 计算新的合并区域行偏移
                int newFirstRow = merged.getFirstRow() - srcStartRow + destStartRow;
                int newLastRow = merged.getLastRow() - srcStartRow + destStartRow;
                CellRangeAddress newMerged = new CellRangeAddress(
                        newFirstRow, newLastRow,
                        merged.getFirstColumn(), merged.getLastColumn()
                );
                newMergedRegions.add(newMerged);
            }
        }

        // 移除目标区域内已有的合并单元格（避免冲突）
        for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() <= destStartRow + (srcEndRow - srcStartRow)
                    && region.getLastRow() >= destStartRow) {
                sheet.removeMergedRegion(i);
            }
        }

        // 添加新的合并区域
        for (CellRangeAddress region : newMergedRegions) {
            sheet.addMergedRegion(region);
        }
    }

    /**
     * 复制单元格的值（支持字符串、数字、布尔、公式、空白）
     */
    private static void copyCellValue(Cell src, Cell dest) {
        switch (src.getCellType()) {
            case STRING:
                dest.setCellValue(src.getStringCellValue());
                break;
            case NUMERIC:
                // 注意：日期也是 NUMERIC，但复制普通数值即可，样式会保留日期格式
                dest.setCellValue(src.getNumericCellValue());
                break;
            case BOOLEAN:
                dest.setCellValue(src.getBooleanCellValue());
                break;
            case FORMULA:
                dest.setCellFormula(src.getCellFormula());
                break;
            case BLANK:
                dest.setCellValue("");
                break;
            default:
                // ERROR 或其他类型按字符串尝试
                dest.setCellValue(src.toString());
        }
    }
}