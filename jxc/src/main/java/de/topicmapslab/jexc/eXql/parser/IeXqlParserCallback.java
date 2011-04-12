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

import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * Interface definition of a callback handler of the parser utility methods.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IeXqlParserCallback {

	/**
	 * Method will be called by the
	 * {@link ExqlParser#split(IParserUtilsCallback, List, List, Class, boolean)}
	 * if a new delimer was found.
	 * 
	 * @param tokens
	 *            the language-specific tokens of the sub-expression
	 * @param foundDelimer
	 *            the founded delimer or <code>null</code> if it is the last
	 *            sub-section
	 * @throws JeXcException
	 *             thrown if given syntax is invalid
	 */
	public void newToken(final List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException;

}