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
import de.topicmapslab.jexc.eXql.grammar.tokens.Where;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class WhereExpression extends ExqlExpression {

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public WhereExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		IeXqlParserCallback callback = new IeXqlParserCallback() {
			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
				addExpression(new BooleanExpression(tokens));
			}
		};

		ExqlParser.parse(callback, tokens, true, Where.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<Row> interpret(Workbook workBook, Object... input) throws JeXcException {
		/*
		 * redirect to boolean-expression
		 */
		return (Collection<Row>)getExpressions().get(0).interpret(workBook, input);
	}
	
}
