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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.By;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.Group;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.result.ExqlGroupResultSet;
import de.topicmapslab.jexc.eXql.result.ExqlResultSet;
import de.topicmapslab.jexc.eXql.result.ExqlResultSetImpl;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class GroupByExpression extends ExqlExpression {

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public GroupByExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);
		IeXqlParserCallback callback = new IeXqlParserCallback() {

			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
				addExpression(new ValueExpression(tokens));
			}
		};
		ExqlParser.parseWithBracketDetection(callback, tokens, true, Group.class, By.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ExqlResultSet<?> interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 2 || !(input[0] instanceof Collection<?>) || !(input[1] instanceof ExqlResultSet<?>)) {
			throw new JeXcException("Number of arguments is invalid expects one collection and one result set!");
		}
		ExqlResultSetImpl resultSet = (ExqlResultSetImpl) input[1];
		Collection<Row> rows = (Collection<Row>) input[0];
		Iterator<Row> iterator = rows.iterator();

		ExqlGroupResultSet groupResultSet = new ExqlGroupResultSet(resultSet.getRows());

		while (iterator.hasNext()) {
			Row row = iterator.next();
			long height = Long.parseLong(getExpressions().get(0).interpret(workBook, row).toString());
			groupResultSet.addResult(createExqlResultGroup(resultSet, height));
			for (int i = 1; i < height && iterator.hasNext(); i++) {
				iterator.next();
			}
		}
		return groupResultSet;
	}

	/**
	 * Internal method to create a sub result of the given one contains at most
	 * the given number of members
	 * 
	 * @param height
	 *            the number of members
	 * @return the created sub result
	 */
	private ExqlResultSetImpl createExqlResultGroup(ExqlResultSetImpl resultSet, long height) {
		List<Integer> rows = new LinkedList<Integer>();
		List<List<Object>> results = new LinkedList<List<Object>>();
		for (int i = 0; i < height && resultSet.next(); i++) {
			List<Object> result = new LinkedList<Object>();
			for (int j = 0; j < resultSet.getNumberOfResults(); j++) {
				result.add(resultSet.getValue(j));
			}
			rows.add(resultSet.getRow());
			results.add(result);
		}
		return new ExqlResultSetImpl(rows, results);
	}

}
