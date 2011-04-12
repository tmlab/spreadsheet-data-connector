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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.Comma;
import de.topicmapslab.jexc.eXql.grammar.tokens.Distinct;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.Select;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.result.ExqlResultSet;
import de.topicmapslab.jexc.eXql.result.ExqlResultSetImpl;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class SelectExpression extends ExqlExpression {

	/**
	 * internal flag indicates the distinct selection of entries
	 */
	private final boolean distinct;
	
	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public SelectExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		IeXqlParserCallback callback = new IeXqlParserCallback() {
			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
				addExpression(new ValueExpression(tokens));			
			}
		};

		ExqlParser.parseWithBracketDetection(callback, tokens, true, Select.class, Comma.class, Distinct.class);
		distinct = Distinct.class.equals(tokens.get(1).getClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ExqlResultSet<?> interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 1 && input[0] instanceof Collection<?>) {
			throw new JeXcException("Number of arguments is invalid, expect one collection!");
		}
		Collection<Row> rows = (Collection<Row>) input[0];
		List<List<Object>> results = new LinkedList<List<Object>>();
		List<Integer> rowIndexes = new LinkedList<Integer>();
		for (Row row : rows) {
			if (row == null) {
				continue;
			}
			List<Object> object = new LinkedList<Object>();
			boolean valid = true;
			for (ExqlExpression ex : getExpressions()) {
				Object result = ex.interpret(workBook, row);
				/*
				 * avoid null values
				 */
				if (result == null ) {
					valid = false;
					break;
				}
				object.add(result);
			}
			if (valid) {
				/*
				 * filter duplicates if DISTINCT is used
				 */
				if ( distinct && results.contains(object)){
					continue;
				}
				results.add(object);
				rowIndexes.add(row.getRowNum());
			}
		}
		return new ExqlResultSetImpl(rowIndexes, results);
	}
}
