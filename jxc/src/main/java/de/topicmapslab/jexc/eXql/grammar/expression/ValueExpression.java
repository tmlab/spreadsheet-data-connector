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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlTokens;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorAddition;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorDivision;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorModulo;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorMultiplication;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorSubstraction;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketOpen;
import de.topicmapslab.jexc.exception.JeXcException;
import de.topicmapslab.jexc.utility.Tokenizer;
import de.topicmapslab.jexc.utility.XlsxCellUtils;

/**
 * @author Sven Krosse
 * 
 */
public class ValueExpression extends ExqlExpression {

	/**
	 * 
	 */
	private static final String SLASH = "/";
	/**
	 * 
	 */
	private static final String COLON = ":";
	private static final String DOLLAR = "$";
	private static final String CELL_COLOR = "cell.color";
	private static final String RED = "RED";
	private static final String BLUE = "BLUE";
	private static final String BLACK = "BLACK";
	private static final String GREEN = "GREEN";
	private static final String NULL = "NULL";
	private static final String STYLE_BACKGROUND = "style.background";
	private static final String STYLE_FOREGROUND = "style.foreground";
	private static final String BORDER_BOTTOM = "border.bottom";
	private static final String BORDER_TOP = "border.top";
	private static final String BORDER_LEFT = "border.left";
	private static final String BORDER_RIGHT = "border.right";
	private static final String VALUE_NUMERICAL = "value.numerical";
	private static final String VALUE_STRING = "value.string";
	private static final String VALUE_DATE = "value.date";
	private static final String VALUE = "value";
	private static final String ROW = "row";
	private static final String COLUMN = "column";
	private static final String ADDRESS = "address";
	private static final String HEIGHT = "height";

	private static final String SINGLE_QUOTE = "'";
	private static final String DOUBLE_QUOTE = "\"";

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	public ValueExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);
		/*
		 * is numerical expression
		 */
		if (tokens.contains(ExqlTokens.tokenInstance(OperatorAddition.TOKEN)) || tokens.contains(ExqlTokens.tokenInstance(OperatorSubstraction.TOKEN))
				|| tokens.contains(ExqlTokens.tokenInstance(OperatorMultiplication.TOKEN)) || tokens.contains(ExqlTokens.tokenInstance(OperatorDivision.TOKEN))
				|| tokens.contains(ExqlTokens.tokenInstance(OperatorModulo.TOKEN))) {
			addExpression(new NumericalExpression(tokens));
		}
		/*
		 * is function
		 */
		else if (tokens.contains(ExqlTokens.tokenInstances().get(RoundBracketOpen.TOKEN))) {					
				addExpression(new FunctionExpression(tokens));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 1 || !(input[0] instanceof Row)) {
			throw new JeXcException("Invalid number or type of input argument, expected one row.");
		}
		
		/*
		 * is numerical expression
		 */
		if ( containsExpression(NumericalExpression.class)){
			return getExpressions().get(0).interpret(workBook, input);
		}
		/*
		 * is function
		 */
		if ( containsExpression(FunctionExpression.class)){
			return getExpressions().get(0).interpret(workBook, input);
		}
		
		try {
			Row row = (Row) input[0];
			String token = getTokens().get(0).token();
			Tokenizer tokenizer = new Tokenizer(token, ".");
			String[] parts = tokenizer.asArray(2);
			String firstPart = parts[0];
			/*
			 * firstPart is only a literal
			 */
			if (!firstPart.startsWith(DOLLAR)) {
				if (parts.length == 1) {
					return cleanConstantValue(firstPart);
				}
				return getConstantValue(row, token);
			}
			/*
			 * first part should be $<numberOfCell>
			 */
			int number = Integer.parseInt(firstPart.substring(1));
			Cell cell = row.getCell(number);
			/*
			 * return null if cell does not exists
			 */
			if (cell == null) {				
				return null;
			}
			/*
			 * default is the cell itself
			 */
			if (parts.length == 1) {
				return cell;
			}
			/*
			 * specific definition of cell content
			 */
			return getCellValue(cell, parts[1]);
		} catch (JeXcException e) {
			throw e;
		} catch (Exception e) {
			throw new JeXcException("Error during execution of expression!", e);
		}
	}

	/**
	 * Internal method to get constant value for given literal
	 * 
	 * @param row
	 *            the row
	 * @param constant
	 *            the constant
	 * @return the value and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private Object getConstantValue(final Row row, final String constant) throws JeXcException {
		/*
		 * defines a color
		 */
		if (constant.startsWith(CELL_COLOR)) {
			String colorName = constant.substring(11);
			if (RED.equalsIgnoreCase(colorName)) {
				return HSSFColor.RED.index;
			} else if (BLUE.equalsIgnoreCase(colorName)) {
				return HSSFColor.BLUE.index;
			} else if (BLACK.equalsIgnoreCase(colorName)) {
				return HSSFColor.BLACK.index;
			} else if (GREEN.equalsIgnoreCase(colorName)) {
				return HSSFColor.GREEN.index;
			}
		}
		return constant;
	}

	/**
	 * Returns the cell value represent by the given token
	 * 
	 * @param cell
	 *            the cell to extract the values from cell
	 * @param token
	 *            the token specifies the value to extract
	 * @return the cell value
	 * @throws JeXcException
	 *             thrown if cell value token is unknown
	 */
	public Object getCellValue(final Cell cell, final String token) throws JeXcException {
		if (VALUE.equalsIgnoreCase(token) || VALUE_STRING.equalsIgnoreCase(token)) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				Double d = cell.getNumericCellValue();
				Long l = d.longValue();
				/*
				 * check if long value represents the same numeric value then
				 * the double origin
				 */
				if (d.doubleValue() == l.longValue()) {
					return String.valueOf(l);
				}
				return String.valueOf(d);
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_STRING:
			default:
				return cell.getStringCellValue();
			}
		} else if (VALUE_DATE.equalsIgnoreCase(token)) {
			return cell.getDateCellValue();
		} else if (VALUE_NUMERICAL.equalsIgnoreCase(token)) {
			return cell.getNumericCellValue();
		} else if (STYLE_FOREGROUND.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? NULL : style.getFillForegroundColor();
		} else if (STYLE_BACKGROUND.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? NULL : style.getFillBackgroundColor();
		} else if (BORDER_TOP.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? 0 : style.getBorderTop();
		}else if (BORDER_BOTTOM.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? 0 : style.getBorderBottom();
		}else if (BORDER_LEFT.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? 0 : style.getBorderLeft();
		}else if (BORDER_RIGHT.equalsIgnoreCase(token)) {
			CellStyle style = cell.getCellStyle();
			return style == null ? 0 : style.getBorderRight();
		} else if (ADDRESS.equalsIgnoreCase(token)) {
			StringBuilder builder = new StringBuilder();
			builder.append(cell.getSheet().getSheetName());
			builder.append(SLASH);
			builder.append(cell.getRow().getRowNum());
			builder.append(COLON);
			builder.append(cell.getColumnIndex());
			return builder.toString();
		} else if (HEIGHT.equalsIgnoreCase(token)) {
			CellRangeAddress address = XlsxCellUtils.getCellRange(cell);
			if (address != null) {
				return address.getLastRow() - address.getFirstRow() + 1;
			}
			return 1;
		} else if (ROW.equalsIgnoreCase(token)) {			
			return cell.getRowIndex();
		} else if (COLUMN.equalsIgnoreCase(token)) {
			return cell.getColumnIndex();
		} 
		throw new JeXcException("Unknown constant '" + token + "'!");
	}

	/**
	 * Cleans the given string constant and remove any quote symbols
	 * 
	 * @param constant
	 *            the constant
	 * @return the cleaned string
	 */
	private String cleanConstantValue(final String constant) {
		/*
		 * is string literal encapsulated by '
		 */
		if (constant.startsWith(SINGLE_QUOTE) && constant.endsWith(SINGLE_QUOTE)) {
			return constant.substring(1, constant.length() - 1);
		}
		/*
		 * is string literal encapsulated by "
		 */
		else if (constant.startsWith(DOUBLE_QUOTE) && constant.endsWith(DOUBLE_QUOTE)) {
			return constant.substring(1, constant.length() - 1);
		}
		/*
		 * other value
		 */
		return constant;
	}

}
