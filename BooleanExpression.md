### Boolean-Expression ###

```
boolean-expression	::=	'(' boolean-expression ')' |
                                 boolean-expression ( 'AND' | 'OR' ) boolean-expression |
                                 boolean-primitive
boolean-primitive	::=	comparison
```

Boolean expressions can be used as part of the where-clause and the functions 'next' and 'previous'. Boolean-expressions supports the two boolean operators 'AND' and 'OR'.