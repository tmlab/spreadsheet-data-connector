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
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorGreater;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorGreaterEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorLess;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorLessEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorRegularExpression;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorUnEquals;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.utility.ComparisonUtils;
import de.topicmapslab.jexc.exception.JeXcException;
import de.topicmapslab.jexc.utility.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class ComparisonExpression extends ExqlExpression {

	private final ExqlToken operator;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public ComparisonExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		ComparisionParserCallback callback = new ComparisionParserCallback();

		ExqlParser.parseWithBracketDetection(callback, tokens, true, OperatorEquals.class, OperatorGreater.class, OperatorGreaterEquals.class, OperatorLess.class, OperatorLessEquals.class,
				OperatorRegularExpression.class, OperatorUnEquals.class);

		operator = callback.operator;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<Row> interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 1 && input[0] instanceof Collection<?>) {
			throw new JeXcException("Number of arguments is invalid, expect one collection!");
		}
		Collection<Row> rows = (Collection<Row>) input[0];
		Collection<Row> result = HashUtil.getHashSet();
		for (Row row : rows) {
			Object leftHand = getExpressions().get(0).interpret(workBook, row);
			Object rightHand = getExpressions().get(1).interpret(workBook, row);
			/*
			 * value expression returns null if cell does not exists
			 */
			if (leftHand == null || rightHand == null) {
				continue;
			}
			try {
				if (operator instanceof OperatorEquals) {
					if (ComparisonUtils.isEquals(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorUnEquals) {
					if (!ComparisonUtils.isEquals(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorGreater) {
					if (ComparisonUtils.isGreaterThan(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorGreaterEquals) {
					if (ComparisonUtils.isGreaterOrEquals(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorLess) {
					if (ComparisonUtils.isLowerThan(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorLessEquals) {
					if (ComparisonUtils.isLowerOrEquals(leftHand, rightHand)) {
						result.add(row);
					}
				} else if (operator instanceof OperatorRegularExpression) {
					if (ComparisonUtils.matchesRegExp(leftHand, rightHand)) {
						result.add(row);
					}
				}
			} catch (Exception e) {
				// CONTINUE
			}
		}
		return result;
	}

	class ComparisionParserCallback implements IeXqlParserCallback {
		public ExqlToken operator;

		public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
			addExpression(new ValueExpression(tokens));
			if (foundDelimer != null) {
				operator = foundDelimer;
			}
		}
	};

}
