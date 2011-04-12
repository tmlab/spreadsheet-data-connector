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
package de.topicmapslab.jexc.exception;

/**
 * @author Sven Krosse
 * 
 */
public class JeXcException extends Exception {

	/**
	 * default serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            a message containing information about the cause
	 */
	public JeXcException(String message) {
		super(message);
	}

	/**
	 * exception
	 * 
	 * @param cause
	 *            the cause
	 */
	public JeXcException(Throwable cause) {
		super(cause);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 *            a message containing information about the cause
	 * @param cause
	 *            the cause
	 */
	public JeXcException(String message, Throwable cause) {
		super(message, cause);
	}

}
