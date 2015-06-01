### Numerical Expression ###

```
numeric-expression	::=	value-expression operator value-expression
operator:		::=	'+' | '-' | '*' | '/' | '%'
```

Numerical expression can be used to execute numerical operations.

The operator **+** calculates the sum of two numbers, concat two strings or calculates the sum of two date values.

The operator **-** calculates the difference of two numbers or date values.

The operator **`*`** only supports numerical values and calculates the product of them.

The operator **/** calculates the quotient of two numerical values. If the given values are floating point numbers, the returned value will be a floating point number too. If the given values are integer values, the result will be an integer too.

The operator **%** calculates the modulo result of the two numerical values.