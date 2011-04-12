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

import de.topicmapslab.jexc.eXql.grammar.tokens.And;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.Or;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.exception.JeXcException;
import de.topicmapslab.jexc.utility.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class BooleanExpression extends ExqlExpression {

	private final ExqlToken operator;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public BooleanExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		BooleanParserCallback callback = new BooleanParserCallback();
		ExqlParser.parseWithBracketDetection(callback, tokens, true, And.class, Or.class);
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
		Collection<Row> results = HashUtil.getHashSet();
		for (ExqlExpression e : getExpressions()) {
			Collection<Row> tmp = (Collection<Row>) e.interpret(workBook, rows);
			if (results.isEmpty() || operator instanceof Or) {
				results.addAll(tmp);
			} else {
				results.retainAll(tmp);
			}
		}
		return results;
	}

	class BooleanParserCallback implements IeXqlParserCallback {
		public ExqlToken operator;
		int numberOfExpression = 0;

		public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
			if (numberOfExpression == 0 && foundDelimer == null) {
				addExpression(new BooleanPrimitive(tokens));
			} else {
				addExpression(new BooleanExpression(tokens));
				numberOfExpression++;
			}
			if (foundDelimer != null) {
				operator = foundDelimer;
			}
		}
	};

}
