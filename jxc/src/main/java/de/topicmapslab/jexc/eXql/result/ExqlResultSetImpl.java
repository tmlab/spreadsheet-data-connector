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
package de.topicmapslab.jexc.eXql.result;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

/**
 * A XQL result
 * 
 * @author Sven Krosse
 * 
 */
public class ExqlResultSetImpl extends ExqlResultSet<List<Object>> {

	/**
	 * constructor
	 * 
	 * @param rows
	 *            the rows
	 * @param results
	 *            the results
	 */
	public ExqlResultSetImpl(List<Integer> rows, List<List<Object>> results) {
		super(rows, results);
	}

	/**
	 * constructor
	 * 
	 * @param rows
	 *            the rows
	 */
	public ExqlResultSetImpl(List<Integer> rows) {
		super(rows);
	}

	/**
	 * Returns the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Cell getCell(long index) {
		return getValue(index);
	}

	/**
	 * Returns the string from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return string from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public String getString(long index) {
		Object obj = getValue(index);
		return obj.toString();
	}

	/**
	 * Returns the date value from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return date value from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Date getDate(long index) {
		return getValue(index);
	}

	/**
	 * Returns the boolean value from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return boolean value from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Boolean getBoolean(int index) {
		return getValue(index);
	}

	/**
	 * Returns the numeric value from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return numeric value from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Double getDouble(long index) {
		return Double.parseDouble(getValue(index).toString());
	}

	/**
	 * Returns the numeric value from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return numeric value from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Long getLong(long index) {
		return Long.parseLong(getValue(index).toString());
	}

	/**
	 * Returns the error value from the cell at the given index
	 * 
	 * @param index
	 *            the index
	 * @return error value from the cell at the given index
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public Byte getError(long index) {
		return getValue(index);
	}

	/**
	 * Return the number of results
	 * 
	 * @return the number of results
	 */
	public int getNumberOfResults() {
		return getResult().size();
	}

	/**
	 * Checks if the result value at the given index is empty
	 * 
	 * @param index
	 *            the index
	 * @return <code>true</code> if the result value is empty,
	 *         <code>false</code> otherwise.
	 */
	public boolean isEmpty(long index) {
		return getString(index).isEmpty();
	}

	/**
	 * Returns the value at the given position and try to cast theme.
	 * 
	 * @param <T>
	 *            the type of result
	 * @param index
	 *            the index
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T getValue(long index) {
		return (T) getResult().get((int) index);
	}

	/**
	 * Returns the value at the given position and try to cast theme.
	 * 
	 * @param <T>
	 *            the type of result
	 * @param rowIndex
	 *            the row number
	 * @param columnIndex
	 *            the column number
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T getValue(long rowIndex, long columnIndex) {
		return (T) getResults().get((int) rowIndex).get((int) columnIndex);
	}
}
