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
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.From;
import de.topicmapslab.jexc.eXql.grammar.tokens.Group;
import de.topicmapslab.jexc.eXql.grammar.tokens.Select;
import de.topicmapslab.jexc.eXql.grammar.tokens.Where;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.result.ExqlResultSet;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class QueryExpression extends ExqlExpression {

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public QueryExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		IeXqlParserCallback callback = new IeXqlParserCallback() {
			Class<? extends ExqlToken> lastDelimer;

			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {

				/*
				 * end of query but last token was WHERE -> where-clause
				 */
				if (foundDelimer == null && lastDelimer.equals(Where.class)) {
					addExpression(new WhereExpression(tokens));
				}
				/*
				 * end of query but last token was BY -> group-by-clause
				 */
				else if (foundDelimer == null && lastDelimer.equals(Group.class)) {
					addExpression(new GroupByExpression(tokens));
				}
				/*
				 * end of query but last token was FROM -> from-clause
				 */
				else if (foundDelimer == null && lastDelimer.equals(From.class)) {
					addExpression(new FromExpression(tokens));
				}
				/*
				 * FROM token was found -> select-clause
				 */
				else if (foundDelimer.getClass().equals(From.class)) {
					addExpression(new SelectExpression(tokens));
				}
				/*
				 * WHERE token was found -> from-clause
				 */
				else if (foundDelimer.getClass().equals(Where.class)) {
					addExpression(new FromExpression(tokens));
				}
				/*
				 * BY token was found but last was token was FROM -> from-clause
				 */
				else if (foundDelimer.getClass().equals(Group.class) && lastDelimer.equals(From.class)) {
					addExpression(new FromExpression(tokens));
				}
				/*
				 * BY token was found but last was token was WHERE ->
				 * where-clause
				 */
				else if (foundDelimer.getClass().equals(Group.class) && lastDelimer.equals(Where.class)) {
					addExpression(new WhereExpression(tokens));
				}
				if (foundDelimer != null) {
					lastDelimer = foundDelimer.getClass();
				}
			}
		};

		ExqlParser.parse(callback, tokens, false, Select.class, From.class, Where.class, Group.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public ExqlResultSet<?> interpret(Workbook workBook, Object... input) throws JeXcException {
		FromExpression fromExpression = (FromExpression) getExpressions().get(1);
		Collection<Row> rows = fromExpression.interpret(workBook);
		/*
		 * contains where-clause
		 */
		if (containsExpression(WhereExpression.class)) {
			WhereExpression whereExpression = (WhereExpression) getExpressions().get(2);
			rows = whereExpression.interpret(workBook, rows);
		}
		/*
		 * interpret select-clause
		 */
		SelectExpression selectExpression = (SelectExpression) getExpressions().get(0);
		ExqlResultSet<?> resultSet = selectExpression.interpret(workBook, rows);
		;
		/*
		 * contains group by-clause
		 */
		if (containsExpression(GroupByExpression.class)) {
			GroupByExpression groupByExpression = (GroupByExpression) getExpressions().get(containsExpression(WhereExpression.class) ? 3 : 2);
			resultSet = groupByExpression.interpret(workBook, rows, resultSet);
		}
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public void toStringTree(StringBuilder builder) {
		builder.append(getClass().getSimpleName());
		builder.append("(");
		builder.append(getTokens().toString());
		builder.append(")\r\n");
		List<ExqlExpression> nodes = getExpressions();

		for (int index = 0; index < nodes.size(); index++) {
			ExqlExpression node = nodes.get(index);
			treeNodeToString(node, new boolean[] { false, index < (nodes.size() - 1) }, builder);
		}
	}

	/**
	 * Internal method to transform a tree node ( {@link ExqlExpression} ) to a
	 * representative string. The string representation contains the name of the
	 * production rule and a list of contained lexical tokens.
	 * 
	 * @param expression
	 *            the expression representing the tree node
	 * @param hasBrothers
	 *            a array representing the upper-hierarchy. If the expression at
	 *            the specific level has brothers the array contains
	 *            <code>true</code> at this position, <code>false</code>
	 *            otherwise.
	 * @param builder
	 *            the string builder where the generated string will be written
	 *            in
	 */
	private void treeNodeToString(final ExqlExpression expression, boolean[] hasBrothers, final StringBuilder builder) {

		/*
		 * iterate over array
		 */
		for (int index = 0; index < hasBrothers.length - 1; index++) {
			boolean hasBrother = hasBrothers[index];
			if (hasBrother)
				/*
				 * add tree like symbolic lines for brothers
				 */
				builder.append("| ");
			else {
				/*
				 * add white-spaces
				 */
				builder.append("  ");
			}
		}
		/*
		 * create current node pattern
		 */
		builder.append("|--");
		/*
		 * add expression type
		 */
		builder.append(expression.getClass().getSimpleName());
		/*
		 * add tokens
		 */
		builder.append("(");
		builder.append(expression.getTokens().toString());
		builder.append(")\r\n");

		boolean hasBrothers_[] = arrayCopy(hasBrothers);

		List<ExqlExpression> nodes = expression.getExpressions();

		/*
		 * iterate over contained sub-expressions
		 */
		for (int index = 0; index < nodes.size(); index++) {
			ExqlExpression node = nodes.get(index);
			hasBrothers_[hasBrothers.length] = index < (nodes.size() - 1);
			treeNodeToString(node, hasBrothers_, builder);
		}

	}

	/**
	 * Internal utility method to copy all values of the given array to a new
	 * instance and add a new empty field at the end of the new array. The
	 * length of the new array will be the same like the origin one extended by
	 * one.
	 * 
	 * @param array
	 *            the array to copy
	 * @return the new array
	 */
	private boolean[] arrayCopy(boolean[] array) {
		boolean copy[] = new boolean[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			copy[i] = array[i];
		}
		return copy;
	}

}
