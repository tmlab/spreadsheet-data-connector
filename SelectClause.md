### Select-Clause ###

```
select-clause 		::=	'SELECT' select-part+
select-part		::= 	value-expression
```

The select-clause is a non-optional part of a eXql query and defines a projection of the selected rows of the where- and from-clause. The select-clause can contain a number of select-parts using value-expressions to extract values from the row objects.