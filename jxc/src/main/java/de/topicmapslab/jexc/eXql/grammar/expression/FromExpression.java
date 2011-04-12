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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.Comma;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlTokens;
import de.topicmapslab.jexc.eXql.grammar.tokens.Last;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketClose;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketOpen;
import de.topicmapslab.jexc.eXql.grammar.tokens.To;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class FromExpression extends ExqlExpression {
	
	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public FromExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		IeXqlParserCallback callback = new IeXqlParserCallback() {

			int numberOfExpression = 0;

			public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {
				/*
				 * ignore sheet name
				 */
				if (numberOfExpression > 0) {
					addExpression(new ValueExpression(tokens));
				}
				numberOfExpression++;
			}
		};
		ExqlParser.parse(callback, tokens, true, RoundBracketClose.class, RoundBracketOpen.class, To.class, Comma.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Row> interpret(Workbook workBook, Object... input) throws JeXcException {
		/*
		 * get sheet by name
		 */
		String sheetName = getTokens().get(1).token();
		if ( sheetName.startsWith("\"")){
			sheetName = sheetName.substring(1,sheetName.length()-1);
		}
		Sheet sheet = workBook.getSheet(sheetName);
		if ( sheet == null ){
			return Collections.emptyList();
		}
		Collection<Row> rows = new LinkedList<Row>();
		/*
		 * is a TO b syntax
		 */
		if (containsToken(ExqlTokens.tokenInstance(To.TOKEN))) {
			if (getNumberOfExpressions() != 2) {
				throw new JeXcException("Invalid number of numeric indexes by using the keyword 'TO'! Expected 2 but was " + getNumberOfExpressions() + ".");
			}
			int from = Integer.parseInt(getExpressions().get(0).getTokens().get(0).token());
			int to;
			ExqlToken token = getExpressions().get(1).getTokens().get(0);
			if ( token instanceof Last ){
				to = sheet.getLastRowNum();
			}else{
				to = Integer.parseInt(token.token());
			}
			for (int i = from; i <= to; i++) {
				Row row = sheet.getRow(i);
				/*
				 * ignore non existing cells
				 */
				if ( row == null ){
					continue;
				}
				rows.add(row);
			}
		}
		/*
		 * is index syntax
		 */
		else {
			for (ExqlExpression e : getExpressions()) {
				int index = Integer.parseInt(e.getTokens().get(0).token());
				Row row = sheet.getRow(index);
				/*
				 * ignore non existing cells
				 */
				if ( row == null ){
					continue;
				}
				rows.add(row);
			}
		}
		return rows;
	}

}
