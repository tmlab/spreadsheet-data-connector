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
public interface IXqlToken {

	/**
	 * Returns the token string
	 * 
	 * @return the token string
	 */
	public String token();

	/**
	 * Method checks if the given token matches to the internal token
	 * definition.
	 * 
	 * @param token
	 *            the token definition
	 * @return <code>true</code> if the token matches the given string,
	 *         <code>false</code> otherwise.
	 */
	public boolean isToken(final String token);
	
	/**
	 * Method checks if the given token matches to the internal token
	 * definition.
	 * 
	 * @param token
	 *            the token definition
	 * @return <code>true</code> if the token matches the given string,
	 *         <code>false</code> otherwise.
	 */
	public boolean isToken(final char token);

}
