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
package de.topicmapslab.jxc.xql.parser;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.topicmapslab.jexc.connection.JeXcConnection;
import de.topicmapslab.jexc.connection.JeXcStatement;
import de.topicmapslab.jexc.eXql.result.ExqlGroupResultSet;
import de.topicmapslab.jexc.eXql.result.ExqlResultSetImpl;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class TestParserOutput {

	private static JeXcConnection con;
	private static JeXcStatement stmt;

	@BeforeClass
	public static void init() throws JeXcException {
		con = JeXcConnection.openConnection("src/test/resources/sample.xlsx", false);
		stmt = con.createStatement();
	}

	@AfterClass
	public static void shutdown() throws JeXcException {
		con.close();
	}

	@Test
	public void testParserOutput() throws JeXcException {
		ExqlResultSetImpl rs = stmt.executeQuery("SELECT $2.value, $3.value, $4.value FROM Kunden (1 TO 13)");
		while (rs.next()) {
			System.out.println("Result: (" + rs.getString(0) + " , " + rs.getString(1) + " , " + rs.getString(2) + ")");
		}
	}

	@Test
	public void testGroupBy() throws JeXcException {
		ExqlGroupResultSet rs = stmt.executeQuery("SELECT $2.value, $3.value, $4.value FROM Kunden (1 TO 100) GROUP BY max ( $2.height, $3.height , $4.height )");
		while (rs.next()) {
			System.out.println("Group");
			ExqlResultSetImpl set = rs.getResult();
			while (set.next()) {
				System.out.println("Result: (" + set.getString(0) + " , " + set.getString(1) + " , " + set.getString(2) + ")");
			}
		}
	}

	@Test
	public void testNumericals() throws JeXcException {
		ExqlGroupResultSet rs = stmt.executeQuery("SELECT $0.value % 2 FROM Kunden (1 TO 100) GROUP BY max ( $2.height, $3.height , $4.height )");
		while (rs.next()) {
			System.out.println("Group");
			ExqlResultSetImpl set = rs.getResult();
			while (set.next()) {
				System.out.println("Result: (" + set.getString(0) + ")");
			}
		}
	}

	@Test
	public void testBorders() throws JeXcException {
		ExqlResultSetImpl rs = stmt.executeQuery("SELECT $0.border.top , $0.border.right , $0.border.bottom , $0.border.left FROM Kunden (1 TO 100) ");
		while (rs.next()) {
			System.out.println("Result: (" + rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ")");
		}
		
		rs = stmt.executeQuery("SELECT \"http://psi.lala.com\" , $0.border.top , $0.border.right , $0.border.bottom , $0.border.left FROM Kunden (1 TO 100) ");
		while (rs.next()) {
			System.out.println("Result: (" + rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ")");
		}
	}

	@Test
	public void testNextFunction() throws JeXcException {
		ExqlGroupResultSet rs = stmt
				.executeQuery("SELECT DISTINCT previous( $0.border.top == 1 ) + 1, next( $0.border.top == 1 ) FROM Firmen (0 TO 10) WHERE $0.value == \"Microsoft\" OR $0.value == \"Apple\" OR $1.value == \"2010\" GROUP BY next( $1.border.top == 1 ) - $1.row");
		while (rs.next()) {
			System.out.println("Group");
			ExqlResultSetImpl set = rs.getResult();
			while (set.next()) {
				System.out.println("Result: (" + set.getString(0) + " , " + set.getString(1) + ")");
				ExqlResultSetImpl s = stmt.executeQuery("SELECT $1.value , $2.value FROM Firmen (" + set.getLong(0) + " TO " + set.getLong(1) + ")");
				while (s.next()) {
					System.out.println("Result: (" + s.getString(0) + " , " + s.getString(1) + ")");
				}
			}
		}
	}
}
