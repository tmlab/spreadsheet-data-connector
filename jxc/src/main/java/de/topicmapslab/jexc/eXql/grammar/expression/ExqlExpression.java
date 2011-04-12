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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.exception.JeXcException;
import de.topicmapslab.jexc.utility.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public abstract class ExqlExpression {

	private List<ExqlToken> tokens;
	private List<ExqlExpression> expressions;
	private Set<Class<? extends ExqlExpression>> containedExpressionTypes;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	public ExqlExpression(List<ExqlToken> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Add a new expression node to parsing tree
	 * 
	 * @param expression
	 *            the expression
	 */
	protected void addExpression(ExqlExpression expression) {
		if (expressions == null) {
			expressions = new LinkedList<ExqlExpression>();
		}
		expressions.add(expression);
		if (containedExpressionTypes == null) {
			containedExpressionTypes = HashUtil.getHashSet();
		}
		containedExpressionTypes.add(expression.getClass());
	}

	/**
	 * Returns all expression located in sub-tree
	 * 
	 * @return the expressions the expression
	 */
	public List<ExqlExpression> getExpressions() {
		if (expressions == null) {
			return Collections.emptyList();
		}
		return expressions;
	}

	/**
	 * @return the tokens
	 */
	public List<ExqlToken> getTokens() {
		return tokens;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getTokens().toString();
	}

	/**
	 * Method call to start interpretation task for this expression.
	 * 
	 * @param workBook
	 *            the work book
	 * @param input
	 *            some input arguments
	 * @return the result of interpretation
	 */
	public abstract Object interpret(Workbook workBook, Object... input) throws JeXcException;

	/**
	 * Returns the number of expressions located on sub-level of this tree node.
	 * 
	 * @return the number of expressions
	 */
	public int getNumberOfExpressions() {
		return expressions == null ? 0 : expressions.size();
	}

	/**
	 * Checks if the given token is contained in the current expression
	 * 
	 * @param token
	 *            the token
	 * @return <code>true</code> if the token is contained, <code>false</code>
	 *         otherwise.
	 */
	public boolean containsToken(ExqlToken token) {
		return getTokens().contains(token);
	}

	/**
	 * Checks if the given expression is part of this expression
	 * 
	 * @param type
	 *            the type
	 * @return <code>true</code> if this expression contains at least one
	 *         expression of this type, <code>false</code> otherwise
	 */
	public boolean containsExpression(Class<? extends ExqlExpression> type) {
		if (containedExpressionTypes == null) {
			return false;
		}
		return containedExpressionTypes.contains(type);
	}
}
