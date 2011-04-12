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
package de.topicmapslab.jexc.eXql.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.jexc.eXql.grammar.expression.QueryExpression;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.IXqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketClose;
import de.topicmapslab.jexc.eXql.grammar.tokens.RoundBracketOpen;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * XQL Parser module
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExqlParser {

	/**
	 * Parse the given token list to a parser tree representing the given query.
	 * 
	 * @param tokens
	 *            the tokens
	 * @return the root expression
	 * @throws JeXcException
	 *             thrown if parsing fails
	 */
	public static QueryExpression parse(final List<ExqlToken> tokens) throws JeXcException {
		return new QueryExpression(tokens);
	}

	/**
	 * Method parse the given token chain at each position of the given delimer.
	 * If a delimer is found the callback will be informed.
	 * 
	 * @param callback
	 *            the handler of a found delimer
	 * @param tokens
	 *            the language-specific tokens to split
	 * @param delimers
	 *            the delimer tokens
	 * @throws JeXcException
	 *             redirect from the {@link IeXqlParserCallback}
	 */
	public static void parse(final IeXqlParserCallback callback, final List<ExqlToken> tokens, boolean ignoreDelimer, Class<? extends IXqlToken>... delimers) throws JeXcException {

		Collection<Class<? extends IXqlToken>> toksAsDelimer = Arrays.asList(delimers);
		List<ExqlToken> tokens_ = new LinkedList<ExqlToken>();

		Iterator<ExqlToken> tokIterator = tokens.iterator();

		while (tokIterator.hasNext()) {
			ExqlToken token = tokIterator.next();
			if (toksAsDelimer.contains(token.getClass())) {
				if (!tokens_.isEmpty()) {
					callback.newToken(tokens_, token);
					tokens_ = new LinkedList<ExqlToken>();
				}
				if (ignoreDelimer) {
					continue;
				}
			}
			tokens_.add(token);
		}

		if (!tokens_.isEmpty()) {
			callback.newToken(tokens_, null);
		}
	}
	
	/**
	 * Method parse the given token chain at each position of the given delimer.
	 * If a delimer is found the callback will be informed. The parser started with option to protect delimer contained by brackets.
	 * 
	 * @param callback
	 *            the handler of a found delimer
	 * @param tokens
	 *            the language-specific tokens to split
	 * @param delimers
	 *            the delimer tokens
	 * @throws JeXcException
	 *             redirect from the {@link IeXqlParserCallback}
	 */
	public static void parseWithBracketDetection(final IeXqlParserCallback callback, final List<ExqlToken> tokens, boolean ignoreDelimer, Class<? extends IXqlToken>... delimers) throws JeXcException {

		Collection<Class<? extends IXqlToken>> toksAsDelimer = Arrays.asList(delimers);
		List<ExqlToken> tokens_ = new LinkedList<ExqlToken>();

		Iterator<ExqlToken> tokIterator = tokens.iterator();

		int protectionLevel = 0;
		while (tokIterator.hasNext()) {
			ExqlToken token = tokIterator.next();
			if ( token.getClass().equals(RoundBracketOpen.class)){
				protectionLevel++;
			}else if (token.getClass().equals(RoundBracketClose.class)){
				protectionLevel--;
			}
			if (protectionLevel == 0 && toksAsDelimer.contains(token.getClass())) {
				if (!tokens_.isEmpty()) {
					callback.newToken(tokens_, token);
					tokens_ = new LinkedList<ExqlToken>();
				}
				if (ignoreDelimer) {
					continue;
				}
			}
			tokens_.add(token);
		}

		if (!tokens_.isEmpty()) {
			callback.newToken(tokens_, null);
		}
	}
}
