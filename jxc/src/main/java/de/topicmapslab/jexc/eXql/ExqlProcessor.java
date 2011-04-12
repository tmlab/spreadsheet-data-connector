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
package de.topicmapslab.jexc.eXql;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.expression.QueryExpression;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.parser.ExqlLexer;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.result.ExqlResultSet;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * the XQL processor module
 * 
 * @author Sven Krosse
 * 
 */
public class ExqlProcessor {

	/**
	 * Method executes the given query and return the result set
	 * 
	 * @param workbook
	 *            the work book
	 * @param query
	 *            the query
	 * @return the result set
	 * @throws JeXcException
	 *             thrown if anything fails
	 */
	public static ExqlResultSet<?> execute(final Workbook workbook, final String query) throws JeXcException {

		/*
		 * do lexical scan
		 */
		List<ExqlToken> tokens = new LinkedList<ExqlToken>();
		ExqlLexer.lexicalScan(query, tokens);
		/*
		 * do parsing task
		 */
		QueryExpression root = ExqlParser.parse(tokens);
		/*
		 * interpret
		 */
		StringBuilder builder = new StringBuilder();
		root.toStringTree(builder);
		/*
		 * return results
		 */
		return root.interpret(workbook);
	}

}
