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
package de.topicmapslab.jexc.eXql.grammar.tokens;

import java.util.Map;
import java.util.Set;

import de.topicmapslab.jexc.exception.JeXcException;
import de.topicmapslab.jexc.utility.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class ExqlTokens {

	/**
	 * hidden constructor
	 */
	private ExqlTokens() {}

	public static final Set<Class<? extends ExqlToken>> tokenClasses = HashUtil.getHashSet();

	static {
		tokenClasses.add(Asterisk.class);
		tokenClasses.add(Comma.class);
		tokenClasses.add(DoubleDot.class);
		tokenClasses.add(From.class);
		tokenClasses.add(Select.class);
		tokenClasses.add(Where.class);
		tokenClasses.add(OperatorEquals.class);
		tokenClasses.add(OperatorUnEquals.class);
		tokenClasses.add(OperatorGreater.class);
		tokenClasses.add(OperatorGreaterEquals.class);
		tokenClasses.add(OperatorLess.class);
		tokenClasses.add(OperatorLessEquals.class);
		tokenClasses.add(OperatorRegularExpression.class);
		tokenClasses.add(RoundBracketOpen.class);
		tokenClasses.add(RoundBracketClose.class);
		tokenClasses.add(And.class);
		tokenClasses.add(Or.class);
		tokenClasses.add(To.class);
		tokenClasses.add(By.class);
		tokenClasses.add(Group.class);
		tokenClasses.add(OperatorAddition.class);
		tokenClasses.add(OperatorSubstraction.class);
		tokenClasses.add(OperatorMultiplication.class);
		tokenClasses.add(OperatorDivision.class);
		tokenClasses.add(OperatorModulo.class);
		tokenClasses.add(Distinct.class);
		tokenClasses.add(Last.class);
	}

	private static Map<String, ExqlToken> tokenInstances;

	/**
	 * Returns singleton instances for each token reference
	 * 
	 * @return a set containing all token references except the element token
	 * @throws JeXcException
	 *             the exception thrown if token cannot be instantiate
	 */
	public static Map<String, ExqlToken> tokenInstances() throws JeXcException {
		if (tokenInstances == null) {
			tokenInstances = HashUtil.getHashMap();
			for (Class<? extends ExqlToken> tokenClass : tokenClasses) {
				try {
					ExqlToken token = tokenClass.newInstance();
					tokenInstances.put(token.token(), token);
				} catch (Exception e) {
					throw new JeXcException("Invalid token implemenation found!", e);
				}
			}
		}
		return tokenInstances;
	}

	/**
	 * Returns the token instance of the given token literal
	 * 
	 * @param literal
	 *            the literal
	 * @return the token and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if the token with this literal cannot be instantiate.
	 */
	public static ExqlToken tokenInstance(final String literal) throws JeXcException {
		ExqlToken token = tokenInstances().get(literal);
		if (token == null) {
			throw new JeXcException("Token for literal '" + literal + "' cannot be instantiate!");
		}
		return token;
	}

}
