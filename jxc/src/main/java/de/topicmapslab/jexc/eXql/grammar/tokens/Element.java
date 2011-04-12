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

/**
 * @author Sven Krosse
 *
 */
public class Element extends ExqlToken{

	private final String literal;
	
	/**
	 * constructor
	 * @param literal the literal of this element token
	 */
	public Element(final String literal) {
		this.literal = literal;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String token() {		
		return literal;
	}	
	
}
