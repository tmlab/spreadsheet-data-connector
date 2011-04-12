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

package de.topicmapslab.jexc.utility;

import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 */
public class QueryUtils {

	/**
	 * constant value for $
	 */
	private static final String DOLLAR = "$";

	/**
	 * constant value for .
	 */
	private static final String DOT = ".";

	/**
	 * Method replace the query contained cell addresses by moving their column
	 * index by the given difference value
	 * 
	 * @param query
	 *            the query
	 * @param difference
	 *            the negative or positive index value
	 * @return the new query
	 */
	public static final String moveColumnIndex(final String query, long difference) throws JeXcException {
		StringBuilder builder = new StringBuilder();
		int last = 0;
		int index = query.indexOf(DOLLAR);
		while (index != -1) {
			String part = query.substring(last, index);
			builder.append(part);
			builder.append(DOLLAR);
			int nextDot = query.indexOf(DOT, index);
			long value = Long.parseLong(query.substring(index + 1, nextDot));
			value += (difference-2);
			if (value < 0) {
				throw new JeXcException("Invalid cell address. Negative indexes not allowed!");
			}
			builder.append(value);
			last = nextDot;
			index = query.indexOf(DOLLAR, last);
		}
		builder.append(query.substring(last));
		return builder.toString();
	}

}
