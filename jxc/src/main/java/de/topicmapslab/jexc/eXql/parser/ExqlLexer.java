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

import java.util.List;

import de.topicmapslab.jexc.eXql.grammar.tokens.Element;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlTokens;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * XQL lexical scanner module
 * 
 * @author Sven Krosse
 * 
 */
public class ExqlLexer {

	/**
	 * Transform the given query to a list of tokens
	 * 
	 * @param query
	 *            the query
	 * @param tokens
	 *            the list of tokens
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	public static void lexicalScan(String query, List<ExqlToken> tokens) throws JeXcException {

		StringBuilder builder = new StringBuilder();
		boolean isSingleQuoted = false;
		boolean isDoubleQuoted = false;
		for (int index = 0; index < query.length(); index++) {
			char c = query.charAt(index);
			/*
			 * is double quote and not part of single-quoted string
			 */
			if ( c == '"' && !isSingleQuoted){
				isDoubleQuoted = !isDoubleQuoted;
			}
			/*
			 * is single quote and not part of double-quoted string
			 */
			else if ( c == '\'' && !isDoubleQuoted){
				isSingleQuoted = !isSingleQuoted;
			}
			/*
			 * token is whitespace but not protected by string
			 */
			else if ((c == ' ' || c == '\t' || c == '\r' || c == '\n') && ( !isSingleQuoted && !isDoubleQuoted )) {
				if (generateToken(builder, tokens)) {
					builder = new StringBuilder();
				}
				continue;
			}
			/*
			 * check if character is known as single char token
			 */
			else if (!isSingleQuoted && !isDoubleQuoted && ExqlTokens.tokenInstances().containsKey(c + "")) {
				if (generateToken(builder, tokens)) {
					builder = new StringBuilder();
				}
				tokens.add(ExqlTokens.tokenInstances().get(c + ""));
				continue;
			}
			/*
			 * any other character
			 */
			builder.append(c);
		}
		generateToken(builder, tokens);
	}

	/**
	 * Generate a new token from given string builder
	 * 
	 * @param builder
	 *            the string builder containing the token
	 * @param tokens
	 *            the tokens collection to add the new token
	 * @return <code>true</code> if a new token was added, <code>false</code>
	 *         otherwise
	 * @throws JeXcException
	 *             thrown if operation fails
	 */
	private static boolean generateToken(StringBuilder builder, List<ExqlToken> tokens) throws JeXcException {
		String token = builder.toString();
		if (!token.isEmpty()) {
			if (ExqlTokens.tokenInstances().containsKey(token)) {
				tokens.add(ExqlTokens.tokenInstances().get(token));
			}
			// TODO missing white spaces possible?
			else {
				tokens.add(new Element(token));
			}
			return true;
		}
		return false;
	}

}
