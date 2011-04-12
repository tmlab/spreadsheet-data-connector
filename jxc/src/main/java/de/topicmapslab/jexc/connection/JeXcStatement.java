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
package de.topicmapslab.jexc.connection;

import de.topicmapslab.jexc.eXql.ExqlProcessor;
import de.topicmapslab.jexc.eXql.result.ExqlResultSet;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 *
 */
public class JeXcStatement {

	private final JeXcConnection connection;
	
	/**
	 * constructor
	 * @param connection the connection
	 */
	JeXcStatement(JeXcConnection connection) {
		this.connection = connection;
	}
	
	/**
	 * Method executes the given query.
	 * @param query the query
	 * @return the result
	 * @throws JeXcException thrown if operation fails
	 */
	@SuppressWarnings("unchecked")
	public <T extends ExqlResultSet<?>> T executeQuery(final String query) throws JeXcException{
		return (T) ExqlProcessor.execute(connection.getWorkBook(), query);
	}
	
	
}
