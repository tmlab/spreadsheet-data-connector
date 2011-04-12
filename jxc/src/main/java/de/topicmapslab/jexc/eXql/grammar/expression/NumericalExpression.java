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

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import de.topicmapslab.jexc.eXql.grammar.tokens.ExqlToken;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorAddition;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorDivision;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorModulo;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorMultiplication;
import de.topicmapslab.jexc.eXql.grammar.tokens.OperatorSubstraction;
import de.topicmapslab.jexc.eXql.parser.ExqlParser;
import de.topicmapslab.jexc.eXql.parser.IeXqlParserCallback;
import de.topicmapslab.jexc.eXql.utility.MathematicUtils;
import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class NumericalExpression extends ExqlExpression {

	private final ExqlToken operator;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the tokens
	 */
	@SuppressWarnings("unchecked")
	public NumericalExpression(List<ExqlToken> tokens) throws JeXcException {
		super(tokens);

		NumericalParserCallback callback = new NumericalParserCallback();

		ExqlParser.parseWithBracketDetection(callback, tokens, true, OperatorSubstraction.class, OperatorAddition.class, OperatorMultiplication.class, OperatorDivision.class, OperatorModulo.class);

		operator = callback.operator;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object interpret(Workbook workBook, Object... input) throws JeXcException {
		if (input.length != 1 && input[0] instanceof Row) {
			throw new JeXcException("Number of arguments is invalid, expect one row!");
		}
		Row row = (Row) input[0];

		Object leftHand = getExpressions().get(0).interpret(workBook, row);
		Object rightHand = getExpressions().get(1).interpret(workBook, row);
		/*
		 * value expression returns null if cell does not exists
		 */
		if (leftHand == null || rightHand == null) {
			return null;
		}
		try {
			if (operator instanceof OperatorAddition) {
				return MathematicUtils.summation(leftHand, rightHand);
			} else if (operator instanceof OperatorSubstraction) {
				return MathematicUtils.subtraction(leftHand, rightHand);
			} else if (operator instanceof OperatorMultiplication) {
				return MathematicUtils.multiplication(leftHand, rightHand);
			} else if (operator instanceof OperatorDivision) {
				return MathematicUtils.division(leftHand, rightHand);
			} else if (operator instanceof OperatorModulo) {
				return MathematicUtils.modulo(leftHand, rightHand);
			}
		} catch (Exception e) {
			// CONTINUE
		}
		return null;
	}

	class NumericalParserCallback implements IeXqlParserCallback {
		public ExqlToken operator;

		public void newToken(List<ExqlToken> tokens, ExqlToken foundDelimer) throws JeXcException {

			addExpression(new ValueExpression(tokens));

			if (foundDelimer != null) {
				operator = foundDelimer;
			}
		}
	};

}
