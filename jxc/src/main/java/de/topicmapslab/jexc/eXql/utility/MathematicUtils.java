/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.jexc.eXql.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class to handle all mathematical operations.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MathematicUtils {

	/**
	 * Method execute mathematical operation of sign
	 * 
	 * @param left
	 *            the left hand argument
	 * @return the result of number * -1 or <code>null</code>
	 */
	public static Object sign(Object left) throws ParseException {
		String l = left.toString();
		/*
		 * left is integer literal
		 */
		if (LiteralUtils.isInteger(l)) {
			return sign(LiteralUtils.asInteger(l));
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			return sign(LiteralUtils.asDecimal(l));
		}
		return null;
	}

	/**
	 * Method execute mathematical operation of sign
	 * 
	 * @param left
	 *            the left hand argument
	 * @return the result of number * -1
	 */
	public static BigInteger sign(BigInteger left) {
		return BigInteger.valueOf(left.longValue() * -1);
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @return the result of number * -1
	 */
	public static BigDecimal sign(BigDecimal left) {
		return BigDecimal.valueOf(left.doubleValue() * -1);
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static Object summation(Object left, Object right) throws ParseException {
		String l = left.toString();
		String r = right.toString();
		/*
		 * left is date literal
		 */
		if (LiteralUtils.isDate(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return summation(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return summation(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return summation(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is time literal
		 */
		else if (LiteralUtils.isTime(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return summation(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return summation(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return summation(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is dateTime literal
		 */
		else if (LiteralUtils.isDateTime(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return summation(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return summation(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return summation(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is integer literal
		 */
		else if (LiteralUtils.isInteger(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return summation(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return summation(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return summation(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return summation(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return summation(l, r);
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static Calendar summation(Calendar left, Calendar right) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(left.getTimeInMillis() + right.getTimeInMillis()));
		return c;
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static BigInteger summation(BigInteger left, BigInteger right) {
		return BigInteger.valueOf(left.longValue() + right.longValue());
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static BigDecimal summation(BigDecimal left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() + right.longValue());
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static BigDecimal summation(BigInteger left, BigDecimal right) {
		return BigDecimal.valueOf(left.longValue() + right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static BigDecimal summation(BigDecimal left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() + right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of summation
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the sum of the left and the right argument
	 */
	public static String summation(String left, String right) {
		return left.concat(right);
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument or
	 *         <code>null</code>
	 */
	public static Object subtraction(Object left, Object right) throws ParseException {
		String l = left.toString();
		String r = right.toString();
		/*
		 * left is date literal
		 */
		if (LiteralUtils.isDate(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return subtraction(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return subtraction(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return subtraction(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is time literal
		 */
		else if (LiteralUtils.isTime(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return subtraction(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return subtraction(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return subtraction(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is dateTime literal
		 */
		else if (LiteralUtils.isDateTime(l)) {
			/*
			 * right is date literal
			 */
			if (LiteralUtils.isDate(r)) {
				return subtraction(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return subtraction(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return subtraction(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
			}
		}
		/*
		 * left is integer literal
		 */
		else if (LiteralUtils.isInteger(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return subtraction(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return subtraction(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return subtraction(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return subtraction(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		return null;
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument
	 */
	public static Calendar subtraction(Calendar left, Calendar right) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(left.getTimeInMillis() - right.getTimeInMillis()));
		return c;
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument
	 */
	public static BigInteger subtraction(BigInteger left, BigInteger right) {
		return BigInteger.valueOf(left.longValue() - right.longValue());
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument
	 */
	public static BigDecimal subtraction(BigDecimal left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() - right.longValue());
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument
	 */
	public static BigDecimal subtraction(BigInteger left, BigDecimal right) {
		return BigDecimal.valueOf(left.longValue() - right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of subtraction
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the difference of the left and the right argument
	 */
	public static BigDecimal subtraction(BigDecimal left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() - right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of multiplication
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the product of the left and the right argument or
	 *         <code>null</code>
	 */
	public static Object multiplication(Object left, Object right) throws ParseException {
		String l = left.toString();
		String r = right.toString();
		/*
		 * left is integer literal
		 */
		if (LiteralUtils.isInteger(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return multiplication(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return multiplication(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return multiplication(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return multiplication(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		return null;
	}

	/**
	 * Method execute mathematical operation of multiplication
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the product of the left and the right argument
	 */
	public static BigInteger multiplication(BigInteger left, BigInteger right) {
		return BigInteger.valueOf(left.longValue() * right.longValue());
	}

	/**
	 * Method execute mathematical operation of multiplication
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the product of the left and the right argument
	 */
	public static BigDecimal multiplication(BigDecimal left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() * right.longValue());
	}

	/**
	 * Method execute mathematical operation of multiplication
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the product of the left and the right argument
	 */
	public static BigDecimal multiplication(BigInteger left, BigDecimal right) {
		return BigDecimal.valueOf(left.longValue() * right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of multiplication
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the product of the left and the right argument
	 */
	public static BigDecimal multiplication(BigDecimal left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() * right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of division
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient of the left and the right argument or
	 *         <code>null</code>
	 */
	public static Object division(Object left, Object right) throws ParseException {
		String l = left.toString();
		String r = right.toString();
		/*
		 * left is integer literal
		 */
		if (LiteralUtils.isInteger(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return division(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return division(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return division(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return division(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		return null;
	}

	/**
	 * Method execute mathematical operation of division
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient of the left and the right argument
	 */
	public static BigDecimal division(BigInteger left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() / right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of division
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient of the left and the right argument
	 */
	public static BigDecimal division(BigDecimal left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() / right.longValue());
	}

	/**
	 * Method execute mathematical operation of division
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient of the left and the right argument
	 */
	public static BigDecimal division(BigInteger left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() / right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of division
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient of the left and the right argument
	 */
	public static BigDecimal division(BigDecimal left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() / right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient without remainder of the left and the right argument
	 *         or <code>null</code>
	 */
	public static Object modulo(Object left, Object right) throws ParseException {
		String l = left.toString();
		String r = right.toString();
		/*
		 * left is integer literal
		 */
		if (LiteralUtils.isInteger(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return modulo(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return modulo(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is decimal literal
		 */
		else if (LiteralUtils.isDecimal(l)) {
			/*
			 * right is integer literal
			 */
			if (LiteralUtils.isInteger(r)) {
				return modulo(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return modulo(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		return null;
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient without remainder of the left and the right argument
	 */
	public static BigDecimal modulo(BigInteger left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() % right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient without remainder of the left and the right argument
	 */
	public static BigDecimal modulo(BigDecimal left, BigInteger right) {
		return BigDecimal.valueOf(left.doubleValue() % right.longValue());
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient without remainder of the left and the right argument
	 */
	public static BigDecimal modulo(BigInteger left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() % right.doubleValue());
	}

	/**
	 * Method execute mathematical operation of modulo
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return the quotient without remainder of the left and the right argument
	 */
	public static BigDecimal modulo(BigDecimal left, BigDecimal right) {
		return BigDecimal.valueOf(left.doubleValue() % right.doubleValue());
	}
}
