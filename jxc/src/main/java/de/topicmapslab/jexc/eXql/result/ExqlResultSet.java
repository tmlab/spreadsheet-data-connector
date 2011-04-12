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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A XQL result
 * 
 * @author Sven Krosse
 * 
 */
public abstract class ExqlResultSet<T extends Object> {

	private final List<Integer> rows;
	private final List<T> results;
	int index = -1;

	/**
	 * constructor
	 * 
	 * @param rows
	 *            the rows
	 * @param results
	 *            the results
	 */
	public ExqlResultSet(List<Integer> rows) {
		this.rows = rows;
		this.results = new LinkedList<T>();
	}

	/**
	 * 
	 * @param result
	 */
	public void addResult(T result) {
		this.results.add(result);
	}

	/**
	 * constructor
	 * 
	 * @param rows
	 *            the rows
	 * @param results
	 *            the results
	 */
	public ExqlResultSet(List<Integer> rows, List<T> results) {
		this.rows = rows;
		this.results = results;
	}

	/**
	 * Move the result set to next row
	 * 
	 * @return <code>true</code> if the result set could move to next row,
	 *         <code>false</code> if reference located after last row
	 */
	public boolean next() {
		index++;
		return getIndex() < results.size();
	}

	/**
	 * Returns the current index of the result set
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the row index of the current result tuple
	 * 
	 * @return the row index
	 */
	public int getRow() {
		return rows.get(getIndex());
	}

	/**
	 * Returns the row index of the given index
	 * 
	 * @param index
	 *            the index of selected rows
	 * @return the row index
	 */
	public int getRow(long index) {
		return rows.get((int)index);
	}

	/**
	 * Returns the current result content
	 * 
	 * @return the content at the current index
	 */
	public T getResult() {
		return results.get(getIndex());
	}

	/**
	 * Returns the total number of rows contained by this result
	 * 
	 * @return the number of rows
	 */
	public int size() {
		return results.size();
	}

	/**
	 * Returns the list of selected rows
	 * 
	 * @return the rows as unmodifiable list
	 */
	public List<Integer> getRows() {
		return Collections.unmodifiableList(rows);
	}

	/**
	 * Returning the results
	 * 
	 * @return the results
	 */
	List<T> getResults() {
		return results;
	}
}
