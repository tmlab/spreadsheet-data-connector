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
package de.topicmapslab.jexc.eXql.grammar.expression;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.Comma;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlTokens;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorGreater;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorGreaterEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorLess;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorLessEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorUnEquals;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.utility.LiteralUtils;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class FunctionExpression extends ExqlExpression {

	/**
	 * identifier for hash function
	 */
	public static final String HASH = "hash";
	/**
	 * identifier for max function
	 */
	public static final String MAX = "max";
	/**
	 * identifier for minimum function
	 */
	public static final String MIN = "min";
	/**
	 * identifier for concat function
	 */
	public static final String CONCAT = "concat";
	/**
	 * identifier for next function
	 */
	public static final String NEXT = "next";
	/**
	 * identifier for previous function
	 */
	public static final String PREVIOUS = "previous";
	/**
	 * identifier for value function
	 */
	public static final String VALUE = "value";

	/**
	 * identifier for tokenize function
	 */
	public static final String TOKENIZE = "tokenize";
	/**
	 * identifier for trim function
	 */
	public static final String TRIM = "trim";

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public FunctionExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		IeXqlParserCallback callback = new IeXqlParserCallback() {

			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
				/*
				 * is comparison expression
				 */
				if (tokens.contains(ExqlTokens.tokenInstance(OperatorEquals.TOKEN)) || tokens.contains(ExqlTokens.tokenInstance(OperatorUnEquals.TOKEN))
						|| tokens.contains(ExqlTokens.tokenInstance(OperatorLess.TOKEN)) || tokens.contains(ExqlTokens.tokenInstance(OperatorLessEquals.TOKEN))
						|| tokens.contains(ExqlTokens.tokenInstance(OperatorGreater.TOKEN)) || tokens.contains(ExqlTokens.tokenInstance(OperatorGreaterEquals.TOKEN))) {
					addExpression(new ComparisonExpression(tokens));
				}
				/*
				 * is value expression
				 */
				else {
					addExpression(new ValueExpression(tokens));
				}
			}
		};

		ExqlParser.parseWithBracketDetection(callback, tokens.subList(2, tokens.size() - 1), true, Comma.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 1 && input[0] instanceof Row) {
			throw new JeXcException("Number of arguments is invalid, expect one collection!");
		}
		if (CONCAT.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretConcateFunction(workBook, (Row) input[0]);
		}
		if (HASH.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretHashFunction(workBook, (Row) input[0]);
		}
		if (MAX.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretMaximumFunction(workBook, (Row) input[0]);
		}
		if (MIN.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretMinimumFunction(workBook, (Row) input[0]);
		}
		if (NEXT.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretNextFunction(workBook, (Row) input[0]);
		}
		if (PREVIOUS.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretPreviousFunction(workBook, (Row) input[0]);
		}
		if (VALUE.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretValueFunction(workBook, (Row) input[0]);
		}
		if (TOKENIZE.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretTokenizeFunction(workBook, (Row) input[0]);
		}
		if (TRIM.equalsIgnoreCase(getTokens().get(0).token())) {
			return interpretTrimFunction(workBook, (Row) input[0]);
		}
		throw new JeXcException("Unknown function name '" + getTokens().get(0).token() + "'.");
	}

	/**
	 * Interpretation method for concat function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result string
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretConcateFunction(Workbook workBook, Row row) throws JeXcException {
		StringBuilder builder = new StringBuilder();
		for (ExqlExpression ex : getExpressions()) {
			Object result = ex.interpret(workBook, row);
			/*
			 * value expression returns null if cell does not exist
			 */
			if (result == null) {
				return null;
			}
			builder.append(result.toString());
		}
		return builder.toString();
	}

	/**
	 * Interpretation method for max function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result string
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretMaximumFunction(Workbook workBook, Row row) throws JeXcException {
		Long maximum = Long.MIN_VALUE;
		for (ExqlExpression ex : getExpressions()) {
			try {
				Long value = Long.parseLong(ex.interpret(workBook, row).toString());
				/*
				 * check if the new value is bigger than the current maximum
				 * value
				 */
				if (maximum < value) {
					maximum = value;
				}
			} catch (NumberFormatException e) {
				throw new JeXcException("Invalid return value of value expression for max function. Only numerical values are supported!");
			}
		}
		return maximum;
	}

	/**
	 * Interpretation method for minimum function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result string
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretMinimumFunction(Workbook workBook, Row row) throws JeXcException {
		Long minimum = Long.MAX_VALUE;
		for (ExqlExpression ex : getExpressions()) {
			try {
				Long value = Long.parseLong(ex.interpret(workBook, row).toString());
				/*
				 * check if the new value is less than the current maximum value
				 */
				if (minimum > value) {
					minimum = value;
				}
			} catch (NumberFormatException e) {
				throw new JeXcException("Invalid return value of value expression for min function. Only numerical values are supported!");
			}
		}
		return minimum;
	}

	/**
	 * Interpretation method for hash function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result hash of the given string
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretHashFunction(Workbook workBook, Row row) throws JeXcException {
		ExqlExpression ex = getExpressions().get(0);
		Object result = ex.interpret(workBook, row);
		/*
		 * value expression returns null if cell does not exists
		 */
		if (result == null) {
			return null;
		}
		return result.toString().hashCode();
	}

	/**
	 * Interpretation method for trim function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result is the string without whitespace
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretTrimFunction(Workbook workBook, Row row) throws JeXcException {
		ExqlExpression ex = getExpressions().get(0);
		Object result = ex.interpret(workBook, row);
		/*
		 * value expression returns null if cell does not exists
		 */
		if (result == null) {
			return null;
		}
		return result.toString().trim();
	}

	/**
	 * Interpretation method for tokenize function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result is a substring of the given one, split by the given
	 *         token. The token to extract will be defined third parameter
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretTokenizeFunction(Workbook workBook, Row row) throws JeXcException {
		/*
		 * get string literal and string delimer
		 */
		ExqlExpression ex = getExpressions().get(0);
		Object oValue = ex.interpret(workBook, row);
		Object oDelimer = getExpressions().get(1).interpret(workBook, row);
		/*
		 * value expression returns null if cell does not exists
		 */
		if (oValue == null || oDelimer == null) {
			return null;
		}
		/*
		 * convert values to string and tokenize them
		 */
		String value = oValue.toString();
		String delimer = oDelimer.toString();
		StringTokenizer tokenizer = new StringTokenizer(value, delimer);
		/*
		 * get optional argument represents the index of item to select
		 */
		int item = 1;
		if (getNumberOfExpressions() == 3) {
			try {
				item = Integer.parseInt(getExpressions().get(2).interpret(workBook, row).toString());
			} catch (NumberFormatException e) {
				throw new JeXcException("Invalid index format, should be an integer.");
			}
		}
		/*
		 * extract token
		 */
		String token = "";
		if (tokenizer.countTokens() < item) {
			return token;
		}
		for (int i = 0; i < item && tokenizer.hasMoreTokens(); i++) {
			token = tokenizer.nextToken();
		}
		return token;
	}

	/**
	 * Interpretation method for value function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the result value of the cell addressed by internal value
	 *         expressions
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretValueFunction(Workbook workBook, Row row) throws JeXcException {
		if (getExpressions().size() != 2) {
			throw new JeXcException("Invalid number of contained value expression, expects 2 but was " + getExpressions().size() + ".");
		}
		Object oRowIndex = getExpressions().get(0).interpret(workBook, row);
		Object oColumnIndex = getExpressions().get(1).interpret(workBook, row);
		BigInteger rowIndex = LiteralUtils.asInteger(oRowIndex.toString());
		BigInteger columnIndex = LiteralUtils.asInteger(oColumnIndex.toString());
		Sheet sheet = row.getSheet();
		Row r = sheet.getRow(rowIndex.intValue());
		if (r == null) {
			return null;
		}
		Cell c = r.getCell(columnIndex.intValue());
		if (c == null) {
			return null;
		}
		if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Double d = c.getNumericCellValue();
			Long l = d.longValue();
			/*
			 * check if long value represents the same numeric value then the
			 * double origin
			 */
			if (d.doubleValue() == l.longValue()) {
				return String.valueOf(l);
			}
			return String.valueOf(d);
		} else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return c.getStringCellValue();
		} else if (c.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return c.getBooleanCellValue();
		}
		return c.getStringCellValue();
	}

	/**
	 * Interpretation method for next function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the index of the next row satisfying the given property or in
	 *         maximum the last row
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretNextFunction(Workbook workBook, Row row) throws JeXcException {
		ExqlExpression ex = getExpressions().get(0);

		Sheet sheet = row.getSheet();
		for (int i = row.getRowNum() + 1; i < sheet.getLastRowNum() + 1; i++) {
			Row r = sheet.getRow(i);
			Object result = ex.interpret(workBook, Arrays.asList(new Row[] { r }));
			if (result instanceof Collection && !((Collection<?>) result).isEmpty()) {
				return r.getRowNum();
			}
		}
		/*
		 * get maximum rows
		 */
		return sheet.getLastRowNum() + 1;
	}

	/**
	 * Interpretation method for previous function
	 * 
	 * @param workBook
	 *            the workbook
	 * @param row
	 *            the row
	 * @return the index of the previous row satisfying the given property or in
	 *         minimum the first row
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object interpretPreviousFunction(Workbook workBook, Row row) throws JeXcException {
		ExqlExpression ex = getExpressions().get(0);

		Sheet sheet = row.getSheet();
		for (int i = row.getRowNum(); i >= sheet.getFirstRowNum() + 1; i--) {
			Row r = sheet.getRow(i);
			Object result = ex.interpret(workBook, Arrays.asList(new Row[] { r }));
			if (result instanceof Collection && !((Collection<?>) result).isEmpty()) {
				return r.getRowNum();
			}
		}
		/*
		 * get minimum rows
		 */
		return sheet.getFirstRowNum();
	}
}
