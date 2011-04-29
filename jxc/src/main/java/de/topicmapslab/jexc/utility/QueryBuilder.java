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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.jexc.eXql.grammar.tokens.By;
import de.topicmapslab.jexc.eXql.grammar.tokens.Comma;
import de.topicmapslab.jexc.eXql.grammar.tokens.Distinct;
import de.topicmapslab.jexc.eXql.grammar.tokens.From;
import de.topicmapslab.jexc.eXql.grammar.tokens.Group;
import de.topicmapslab.jexc.eXql.grammar.tokens.Last;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketClose;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketOpen;
import de.topicmapslab.jexc.eXql.grammar.tokens.Select;
import de.topicmapslab.jexc.eXql.grammar.tokens.To;
import de.topicmapslab.jexc.eXql.grammar.tokens.Where;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class QueryBuilder {

	/**
	 * whitespace
	 */
	private static final String WHITESPACE = " ";
	/**
	 * dollar
	 */
	private static final String DOLLAR = "$";
	/**
	 * value constant
	 */
	private static final String VALUE = ".value";

	private final String sheetName;
	private Long rowMinimum;
	private Long rowMaximum;
	private Collection<Integer> rows;
	private StringBuilder builder;
	private long selectionEntriesCount = 0;
	private String condition;
	private String groupsDeclaration;
	private boolean distinct;

	/**
	 * Constructor
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param index
	 *            the index of row selection
	 */
	public QueryBuilder(final String sheetName, final int index) {
		this(sheetName, (long) index, (long) index+1);
	}

	/**
	 * Constructor
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param rowMinimum
	 *            the minimum index of row selection
	 * @param rowMaximum
	 *            the maximum index of row selection
	 */
	public QueryBuilder(final String sheetName, final Long rowMinimum, final Long rowMaximum) {
		this.sheetName = sheetName;
		this.rowMinimum = rowMinimum;
		this.rowMaximum = rowMaximum;
	}

	/**
	 * Constructor
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param rows
	 *            the rows to select
	 */
	public QueryBuilder(final String sheetName, final Collection<Integer> rows) {
		this.sheetName = sheetName;
		this.rows = rows;
	}

	/**
	 * Remove or add the DISTINCT token
	 * 
	 * @param distinct
	 *            the new distinct state
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * Creates a new selection entry to access the given row value
	 * 
	 * @param row
	 *            the row index
	 * @return the index of generated entry
	 */
	public long addSelectionEntry(Long row) {
		if (builder == null) {
			builder = new StringBuilder();
		} else {
			builder.append(Comma.TOKEN);
			builder.append(WHITESPACE);
		}
		builder.append(DOLLAR);
		builder.append(row);
		builder.append(VALUE);
		builder.append(WHITESPACE);
		return selectionEntriesCount++;
	}

	/**
	 * Creates a new selection entry as a function
	 * 
	 * @param functionName
	 *            the function name
	 * @param values
	 *            an array of values
	 * @return the index of generated entry
	 */
	public long addSelectionEntry(final String functionName, final String... values) {
		if (builder == null) {
			builder = new StringBuilder();
		} else {
			builder.append(Comma.TOKEN);
			builder.append(WHITESPACE);
		}
		builder.append(functionName);
		builder.append(WHITESPACE);
		builder.append(RoundBracketOpen.TOKEN);
		builder.append(WHITESPACE);
		boolean first = true;
		for (String value : values) {
			if (!first) {
				builder.append(WHITESPACE);
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(value);
			first = false;

		}
		builder.append(WHITESPACE);
		builder.append(RoundBracketClose.TOKEN);
		builder.append(WHITESPACE);
		return selectionEntriesCount++;
	}

	/**
	 * Add the given selection entry to internal clause
	 * 
	 * @param selectionEntry
	 *            the selection entry
	 * @return the index of generated entry
	 */
	public long addSelectionEntry(String selectionEntry) {
		if (builder == null) {
			builder = new StringBuilder();
		} else {
			builder.append(Comma.TOKEN);
			builder.append(WHITESPACE);
		}
		builder.append(selectionEntry);
		builder.append(WHITESPACE);
		return selectionEntriesCount++;
	}

	/**
	 * Method replace the query contained cell addresses by moving their column index by the given difference value
	 * 
	 * @param difference
	 *            the negative or positive index value
	 */
	public final void moveColumnIndex(long difference) throws JeXcException {
		if (builder != null) {
			String query = builder.toString();
			query = QueryUtils.moveColumnIndex(query, difference);
			builder = new StringBuilder(query);
		}
		if (condition != null) {
			condition = QueryUtils.moveColumnIndex(condition, difference);
		}
		if (groupsDeclaration != null) {
			groupsDeclaration = QueryUtils.moveColumnIndex(groupsDeclaration, difference);
		}
	}

	/**
	 * Building a query from internal definition
	 * 
	 * @return the built query
	 * @throws JeXcException
	 *             thrown if query builder was executed before defining at least one select entry
	 */
	public String buildQuery() throws JeXcException {
		if (builder == null) {
			throw new JeXcException("Invalid query definition. Query has to contain at least one selection entry.");
		}
		/*
		 * is a set of rows
		 */
		if (rows != null) {
			return buildQuery(distinct, sheetName, rows, builder.toString(), condition, groupsDeclaration);
		}
		/*
		 * from defined by minimum and maximum
		 */
		else {
			return buildQuery(distinct, sheetName, rowMinimum, rowMaximum, builder.toString(), condition, groupsDeclaration);
		}
	}

	/**
	 * Setting a condition clause representing the where-clause content
	 * 
	 * @param condition
	 *            the condition
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * Setting the groups declaration used to group the results to clusters
	 * 
	 * @param groupsDeclaration
	 *            the groups declaration to set
	 */
	public void setGroupsDeclaration(String groupsDeclaration) {
		this.groupsDeclaration = groupsDeclaration;
	}

	/**
	 * Building the query for the specified attributes
	 * 
	 * @param distinct
	 *            flag if DISTINCT should be set
	 * @param sheetName
	 *            the sheet name
	 * @param rows
	 *            the rows to select
	 * @param selectionPart
	 *            the selection part
	 * @param condition
	 *            the condition or <code>null</code>
	 * @param groupsDeclaration
	 *            the groups declaration
	 * @return the built query
	 */
	public static final String buildQuery(boolean distinct, final String sheetName, final Collection<Integer> rows, final String selectionPart, final String condition, final String groupsDeclaration) {
		StringBuilder builder = new StringBuilder();
		/*
		 * add selection part
		 */
		if (!selectionPart.startsWith(Select.TOKEN)) {
			builder.append(Select.TOKEN);
			if (distinct) {
				builder.append(WHITESPACE);
				builder.append(Distinct.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(WHITESPACE);
		}
		builder.append(selectionPart);
		builder.append(WHITESPACE);
		/*
		 * add from clause
		 */
		builder.append(From.TOKEN);
		builder.append(WHITESPACE);
		builder.append("\"");
		builder.append(sheetName);
		builder.append("\"");
		builder.append(WHITESPACE);
		builder.append(RoundBracketOpen.TOKEN);
		boolean first = true;
		List<Integer> sorted = new ArrayList<Integer>(rows);
		Collections.sort(sorted);
		for (Integer row : sorted) {
			if (!first) {
				builder.append(Comma.TOKEN);
			}
			builder.append(WHITESPACE);
			builder.append(row.toString());
			builder.append(WHITESPACE);
			first = false;
		}
		builder.append(RoundBracketClose.TOKEN);
		/*
		 * add condition part
		 */
		if (condition != null) {
			if (!condition.startsWith(Where.TOKEN)) {
				builder.append(Where.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(condition);
		}
		/*
		 * add grouping part
		 */
		if (groupsDeclaration != null) {
			if (!groupsDeclaration.startsWith(Group.TOKEN)) {
				builder.append(Group.TOKEN);
				builder.append(WHITESPACE);
				builder.append(By.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(groupsDeclaration);
		}
		return builder.toString();
	}

	/**
	 * Building the query for the specified attributes
	 * 
	 * @param distinct
	 *            flag if DISTINCT should be set
	 * @param sheetName
	 *            the sheet name
	 * @param rowMinimum
	 *            the minimum row index of selection
	 * @param rowMaximum
	 *            the maximum row index of selection or <code>null</code>
	 * @param selectionPart
	 *            the selection part
	 * @param condition
	 *            the condition or <code>null</code>
	 * @param groupsDeclaration
	 *            the groups declaration
	 * @return the built query
	 */
	public static final String buildQuery(boolean distinct, final String sheetName, final Long rowMinimum, final Long rowMaximum, final String selectionPart, final String condition,
			final String groupsDeclaration) {
		StringBuilder builder = new StringBuilder();
		/*
		 * add selection part
		 */
		if (!selectionPart.startsWith(Select.TOKEN)) {
			builder.append(Select.TOKEN);
			if (distinct) {
				builder.append(WHITESPACE);
				builder.append(Distinct.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(WHITESPACE);
		}
		builder.append(selectionPart);
		builder.append(WHITESPACE);
		/*
		 * add from clause
		 */
		builder.append(From.TOKEN);
		builder.append(WHITESPACE);
		builder.append("\"");
		builder.append(sheetName);
		builder.append("\"");
		builder.append(WHITESPACE);
		builder.append(RoundBracketOpen.TOKEN);
		builder.append(rowMinimum);
		builder.append(WHITESPACE);
		builder.append(To.TOKEN);
		builder.append(WHITESPACE);
		builder.append(rowMaximum == null ? Last.TOKEN : rowMaximum);
		builder.append(RoundBracketClose.TOKEN);
		/*
		 * add condition part
		 */
		if (condition != null) {
			if (!condition.startsWith(Where.TOKEN)) {
				builder.append(Where.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(condition);
		}
		/*
		 * add grouping part
		 */
		if (groupsDeclaration != null) {
			if (!groupsDeclaration.startsWith(Group.TOKEN)) {
				builder.append(Group.TOKEN);
				builder.append(WHITESPACE);
				builder.append(By.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(groupsDeclaration);
		}
		return builder.toString();
	}

	/**
	 * Building the query for the specified attributes
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param rowMinimum
	 *            the minimum row index of selection
	 * @param rowMaximum
	 *            the maximum row index of selection or <code>null</code>
	 * @param selectionPart
	 *            the selection part
	 * @param rows
	 *            the rows to select
	 * @return the built query
	 */
	public static final String buildQuery(final String sheetName, final Long rowMinimum, final Long rowMaximum, Long... rows) {
		StringBuilder builder = new StringBuilder();
		/*
		 * add selection part
		 */
		builder.append(Select.TOKEN);
		builder.append(WHITESPACE);
		List<Long> sorted = Arrays.asList(rows);
		Collections.sort(sorted);
		boolean first = true;
		for (Long row : sorted) {
			if (!first) {
				builder.append(Comma.TOKEN);
			}
			builder.append(DOLLAR);
			builder.append(row);
			first = false;
		}
		builder.append(WHITESPACE);
		/*
		 * add from clause
		 */
		builder.append(From.TOKEN);
		builder.append(WHITESPACE);
		builder.append(sheetName);
		builder.append(WHITESPACE);
		builder.append(RoundBracketOpen.TOKEN);
		builder.append(rowMinimum);
		builder.append(To.TOKEN);
		builder.append(rowMaximum == null ? Long.MAX_VALUE : rowMaximum);
		builder.append(RoundBracketClose.TOKEN);
		return builder.toString();
	}

	/**
	 * Returns the number of contained selection entries
	 * 
	 * @return the selectionEntriesCount
	 */
	public long getSelectionEntriesCount() {
		return selectionEntriesCount;
	}

	/**
	 * checks if the query builder is empty
	 * 
	 * @return <code>true</code> if the query does not contain any selection entry, <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return getSelectionEntriesCount() == 0;
	}

}
