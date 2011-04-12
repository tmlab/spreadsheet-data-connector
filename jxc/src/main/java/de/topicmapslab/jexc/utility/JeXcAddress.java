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

import java.util.Calendar;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.exception.JeXcException;

/**
 * The address appeared to the XLSX cell
 * 
 * @author Sven Krosse
 */
public class JeXcAddress {

	private final String sheetName;
	private final int rowNumber;
	private final int columnNumber;

	/**
	 * constructors
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param rowNumber
	 *            the row number
	 * @param columnNumber
	 *            the column number
	 */
	public JeXcAddress(String sheetName, int rowNumber, int columnNumber) {
		this.sheetName = sheetName;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
	}

	/**
	 * Accessing the cell of the given workbook by the internal represented
	 * address.
	 * 
	 * @param workbook
	 *            the workbook
	 * @return the cell and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if address is invalid for the given workbook
	 */
	public Cell accessCell(Workbook workbook) throws JeXcException {
		try {
			return workbook.getSheet(sheetName).getRow(rowNumber).getCell(columnNumber);
		} catch (Exception e) {
			throw new JeXcException("Invalid cell address.", e);
		}
	}

	/**
	 * Returns the cell value as string addressed by the internal address.
	 * 
	 * @param workbook
	 *            the workbook
	 * @return the value and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if address is invalid for the given workbook
	 */
	public String accessStringValue(Workbook workbook) throws JeXcException {
		return accessCell(workbook).getStringCellValue();
	}

	/**
	 * Returns the cell value as date addressed by the internal address.
	 * 
	 * @param workbook
	 *            the workbook
	 * @return the value and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if address is invalid for the given workbook
	 */
	public Calendar accessCalendarValue(Workbook workbook) throws JeXcException {
		Calendar c = Calendar.getInstance();
		c.setTime(accessCell(workbook).getDateCellValue());
		return c;
	}

	/**
	 * Returns the cell value as numerical value addressed by the internal
	 * address.
	 * 
	 * @param workbook
	 *            the workbook
	 * @return the value and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if address is invalid for the given workbook
	 */
	public Double accessNumericalValue(Workbook workbook) throws JeXcException {
		return accessCell(workbook).getNumericCellValue();
	}
	
	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}
	
	/**
	 * @return the columnNumber
	 */
	public int getColumnNumber() {
		return columnNumber;
	}
	
	/**
	 * @return the rowNumber
	 */
	public int getRowNumber() {
		return rowNumber;
	}

}
