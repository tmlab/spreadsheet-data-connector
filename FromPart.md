### From-Clause ###

```
form-clause		::=	'FROM' literal '(' [0-9]+ ( 'TO' ( [0-9]+ | 'LAST' ) | ( ',' [0-9]+ )* )?
```

The from clause is used to define the sheet and the rows of the sheet to access.  The sheet name will be represented by a string literal following the keyword _FROM_.

The selection range can be defined in two different ways. The query can use the range syntax using the keyword 'TO' to specify only the first and the last row index to select. Please note, that the index are zero-based in different to the Excel semantics. The keyword 'LAST' is reserved as last index which will be bound dynamically to the last row of the underlying sheet. The second possibility to define the indexes to select is a comma-separated list of numerical values which has to contain at least one index.

**Sample**:

```
// Select all rows of the underlying sheet named 'sheet'
... FROM sheet ( 0 TO LAST )
// Select only the first row of the underlying sheet named 'sheet'	 
... FROM sheet ( 0 )
// Select the rows 0,1,2,3,4,5,9 of the underlying sheet named 'sheet'
... FROM sheet ( 0,1,2,3,4,5,9 )
```