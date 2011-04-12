/*******************************************************************************
 * Copyright 2010, Topic Map Lab ( http://www.topicmapslab.de )
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.topicmapslab.jexc.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author Sven Krosse
 * 
 */
public class XlsxCellUtils {

	/**
	 * Returns the cell range of the given cell
	 * 
	 * @param cell
	 *            the cell
	 * @return the cell range of merged region the cell is part of or
	 *         <code>null</code>
	 */
	public static CellRangeAddress getCellRange(Cell cell) {
		Sheet s = cell.getSheet();
		for (int i = 0; i < s.getNumMergedRegions(); i++) {
			CellRangeAddress a = s.getMergedRegion(i);
			if (a.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Returns the string value of the given cell range
	 * 
	 * @param sheet
	 *            the sheet
	 * @param address
	 *            the cell range address
	 * @return the value
	 */
	public static String getCellRangeStringValue(Sheet sheet, CellRangeAddress address) {
		Cell c = sheet.getRow(address.getFirstRow()).getCell(address.getFirstColumn());
		return c.getStringCellValue();
	}

}
