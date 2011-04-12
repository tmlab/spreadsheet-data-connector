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
package de.topicmapslab.jexc;

import de.topicmapslab.jexc.eXql.grammar.tokens.And;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorEquals;
import de.topicmapslab.jexc.eXql.grammar.tokens.Or;

/**
 * @author Sven Krosse
 * 
 */
public class WhereCondition {

	private final static String WHITESPACE = " ";
	
	private final static String DOLLAR = "$";
	
	private final static String VALUE = ".value";

	private StringBuilder builder;

	private final boolean conjunction;

	/**
	 * constructor
	 * 
	 * @param conjunction
	 *            boolean attribute indicates if the given condition should be
	 s*            concat by AND or OR
	 */
	public WhereCondition(boolean conjunction) {
		this.conjunction = conjunction;
	}
	/**
	 * Adding a new equality condition to the internal buffer
	 * @param cellIndex the index of cell
	 * @param value the right hand argument
	 */
	public void equality(final long cellIndex, final String value) {
		equality(DOLLAR + cellIndex + VALUE, value);
	}
	
	/**
	 * Adding a new equality condition to the internal buffer
	 * @param leftHand the left hand argument
	 * @param rightHand the right hand argument
	 */
	public void equality(final String leftHand, final String rightHand) {
		/*
		 * is first condition
		 */
		if (builder == null) {
			builder = new StringBuilder();
		}
		/*
		 * add binding token
		 */
		else {
			builder.append(WHITESPACE);
			builder.append(isConjunction() ? And.TOKEN : Or.TOKEN);
			builder.append(WHITESPACE);
		}
		
		builder.append(leftHand);
		builder.append(WHITESPACE);
		builder.append(OperatorEquals.TOKEN);
		builder.append(WHITESPACE);
		builder.append(rightHand);
	}
	
	/**
	 *  Boolean attribute indicates if the given condition should be
	 *            concat by AND or OR
	 * @return <code>true</code> if token AND schould be used, <code>false</code> if OR schould be used.
	 */
	public boolean isConjunction() {
		return conjunction;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if ( builder != null ){
			return builder.toString();
		}
		return "";
	}
}
