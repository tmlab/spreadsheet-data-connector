### Group-By-Clause ###

```
group-by-expression	::=	'GROUP ' 'BY' value-expression
```

The group-by-clause can be used to cluster results of the select clause to groups. The cluster contains a set of tuples of a selection. The number of tuples can be defined by a numerical value or dynamically by a condition returning a numerical value ( like functions ).