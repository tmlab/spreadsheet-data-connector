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
import java.util.regex.PatternSyntaxException;

/**
 * Utility class for comparison operations for TMQL literals.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ComparisonUtils {

	/**
	 * Method checks if the left hand argument is equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(Object left, Object right) throws ParseException {
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
				return isEquals(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isEquals(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isEquals(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
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
				return isEquals(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isEquals(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isEquals(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
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
				return isEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isEquals(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
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
				return isEquals(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isEquals(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
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
				return isEquals(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isEquals(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return isEquals(l, r);
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is  equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(Calendar left, Calendar right) {
		return left.getTimeInMillis() == right.getTimeInMillis();
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is  equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(BigInteger left, BigInteger right) {
		return left.longValue() == right.longValue();
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is  equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(BigDecimal left, BigInteger right) {
		return left.doubleValue() == right.longValue();
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is  equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(BigInteger left, BigDecimal right) {
		return left.longValue() == right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is  equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(BigDecimal left, BigDecimal right) {
		return left.doubleValue() == right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is  equal to the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is equal to the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isEquals(String left, String right) {
		return left.compareTo(right) == 0;
	}
	
	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(Object left, Object right) throws ParseException {
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
				return isLowerThan(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerThan(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerThan(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
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
				return isLowerThan(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerThan(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerThan(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
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
				return isLowerThan(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerThan(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerThan(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
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
				return isLowerThan(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isLowerThan(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
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
				return isLowerThan(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isLowerThan(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return isLowerThan(l, r);
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(Calendar left, Calendar right) {
		return left.getTimeInMillis() < right.getTimeInMillis();
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(BigInteger left, BigInteger right) {
		return left.longValue() < right.longValue();
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(BigDecimal left, BigInteger right) {
		return left.doubleValue() < right.longValue();
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(BigInteger left, BigDecimal right) {
		return left.longValue() < right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(BigDecimal left, BigDecimal right) {
		return left.doubleValue() < right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is lower than the right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerThan(String left, String right) {
		return left.compareTo(right) < 0;
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(Object left, Object right) throws ParseException {
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
				return isLowerOrEquals(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerOrEquals(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerOrEquals(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
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
				return isLowerOrEquals(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerOrEquals(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerOrEquals(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
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
				return isLowerOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isLowerOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isLowerOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
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
				return isLowerOrEquals(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isLowerOrEquals(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
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
				return isLowerOrEquals(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isLowerOrEquals(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return isLowerOrEquals(l, r);
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(Calendar left, Calendar right) {
		return left.getTimeInMillis() <= right.getTimeInMillis();
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(BigInteger left, BigInteger right) {
		return left.longValue() <= right.longValue();
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(BigDecimal left, BigInteger right) {
		return left.doubleValue() <= right.longValue();
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(BigInteger left, BigDecimal right) {
		return left.longValue() <= right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(BigDecimal left, BigDecimal right) {
		return left.doubleValue() <= right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is lower or equals than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is lower or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isLowerOrEquals(String left, String right) {
		return left.compareTo(right) <= 0;
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(Object left, Object right) throws ParseException {
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
				return isGreaterThan(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterThan(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterThan(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
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
				return isGreaterThan(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterThan(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterThan(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
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
				return isGreaterThan(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterThan(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterThan(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
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
				return isGreaterThan(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isGreaterThan(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
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
				return isGreaterThan(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isGreaterThan(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return isGreaterThan(l, r);
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(Calendar left, Calendar right) {
		return left.getTimeInMillis() > right.getTimeInMillis();
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(BigInteger left, BigInteger right) {
		return left.longValue() > right.longValue();
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(BigInteger left, BigDecimal right) {
		return left.longValue() > right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(BigDecimal left, BigInteger right) {
		return left.doubleValue() > right.longValue();
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(BigDecimal left, BigDecimal right) {
		return left.doubleValue() > right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is greater than the right
	 * argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater than the right
	 *         hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterThan(String left, String right) {
		return left.compareTo(right) > 0;
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(Object left, Object right) throws ParseException {
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
				return isGreaterOrEquals(LiteralUtils.asDate(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asDate(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asDate(l), LiteralUtils.asDateTime(r));
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
				return isGreaterOrEquals(LiteralUtils.asTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asTime(l), LiteralUtils.asDateTime(r));
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
				return isGreaterOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDate(r));
			}
			/*
			 * right is time literal
			 */
			else if (LiteralUtils.isTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asTime(r));
			}
			/*
			 * right is dateTime literal
			 */
			else if (LiteralUtils.isDateTime(r)) {
				return isGreaterOrEquals(LiteralUtils.asDateTime(l), LiteralUtils.asDateTime(r));
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
				return isGreaterOrEquals(LiteralUtils.asInteger(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isGreaterOrEquals(LiteralUtils.asInteger(l), LiteralUtils.asDecimal(r));
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
				return isGreaterOrEquals(LiteralUtils.asDecimal(l), LiteralUtils.asInteger(r));
			}
			/*
			 * right is decimal literal
			 */
			else if (LiteralUtils.isDecimal(r)) {
				return isGreaterOrEquals(LiteralUtils.asDecimal(l), LiteralUtils.asDecimal(r));
			}
		}
		/*
		 * left is string literal
		 */
		return isGreaterOrEquals(l, r);
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(Calendar left, Calendar right) {
		return left.getTimeInMillis() >= right.getTimeInMillis();
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(BigInteger left, BigInteger right) {
		return left.longValue() >= right.longValue();
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(BigDecimal left, BigInteger right) {
		return left.doubleValue() >= right.longValue();
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(BigInteger left, BigDecimal right) {
		return left.longValue() >= right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(BigDecimal left, BigDecimal right) {
		return left.doubleValue() >= right.doubleValue();
	}

	/**
	 * Method checks if the left hand argument is greater or equals than the
	 * right argument
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean isGreaterOrEquals(String left, String right) {
		return left.compareTo(right) >= 0;
	}

	/**
	 * Method checks if the left hand argument as string matches the regular
	 * expression given by the second argument.
	 * 
	 * @param left
	 *            the left hand argument
	 * @param right
	 *            the right hand argument
	 * @return <code>true</code> if left hand argument is greater or equals than
	 *         the right hand argument, <code>false</code> otherwise.
	 */
	public static boolean matchesRegExp(Object left, Object right) throws PatternSyntaxException {
		String l = left.toString();
		String r = right.toString();
		return l.matches(r);
	}

}
