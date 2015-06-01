## Spreadsheet Query Language - eXql ##

The eXql specifies a simple and light-weight query language to access XLSX documents in a simple way. Using a JXC statement the query can be executed directly on the Excel document.

The grammar of the Excel Query Language is quite similar to the synatx of SQL.

### Grammar ###

This chapter contains an overview of the whole grammar of eXql.

```
query 			::=	select-clause from-clause where-clause? group-by-clause?
select-clause 		::=	'SELECT' select-part+
select-part		::= 	value-expression
value-expression	::=	cell-access | constant | function | numeric-expression
cell-access		::= 	'$'[0-9]+ ( cell-value | cell-style | cell-border | cell-address )
cell-value		::= 	'.value' ( '.' ( 'numerical' | 'string' |'date' ))?
cell-style		::= 	'.style.' ( 'foreground' | 'background') | '.height' 
cell-border		::= 	'.border.' ( 'top' | 'left' | 'bottom' | 'right' )
cell-address		::=	'.address' | '.row' | '.column'
constant		::=	color-constant | literal
color-constant		::=	'cell.color.' ( 'RED' | 'GREEN' | 'BLUE' | 'BLACK' )
literal			::= 	[0-9]+ | "[^"]*" | '[^']*'
function		::=	... see chapter functions
numeric-expression	::=	value-expression operator value-expression
operator:		::=	'+' | '-' | '*' | '/' | '%'
form-clause		::=	'FROM' literal '(' [0-9]+ ( 'TO' ( [0-9]+ | 'LAST' ) | ( ',' [0-9]+ )* )?
where-clause		::=	'WHERE' boolean-expression
boolean-expression	::=	 '(' boolean-expression ')' |
                                 boolean-expression ( 'AND' | 'OR' ) boolean-expression |
                                 boolean-primitive
boolean-primitive	::=	comparison
comparison		::=	value-expression ( '==' | '>=' | '>' | '<=' | '<' | '!=' | '~=' ) value-expression
group-by-expression	::=	'GROUP ' 'BY' value-expression
```