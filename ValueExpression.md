### Value-Expressions ###

#### Grammar ####

```
value-expression	::=	cell-access | constant | function | numeric-expression
cell-access		::= 	'$'[0-9]+ ( cell-value | cell-style | cell-border | cell-address )
cell-value		::= 	'.value' ( '.' ( 'numerical' | 'string' |'date' ))?
cell-style		::= 	'.style.' ( 'foreground' | 'background') | '.height' 
cell-border		::= 	'.border.' ( 'top' | 'left' | 'bottom' | 'right' )
cell-address		::=	'.address' | '.row' | '.column'
constant		::=	color-constant | literal
color-constant		::=	'cell.color.' ( 'RED' | 'GREEN' | 'BLUE' | 'BLACK' )
literal			::= 	[0-9]+ | "[^"]*" | '[^']*'
```

The value-expression used to bound values from the underlying sheet, execute functions or define constants.

To access cell values the variable syntax should be used defining the column index of the cell to access. A variable accessor starts with the token '$' followed by the numerical index of the column. Please note that the index is zero-based and represent as numerical values in contrast to the Excel semantics using alpha-numerical indexes. The accessor works like an object-oriented variable. The dot-operator '.' can be used to access different contents and information of the addressed cell, like its value, address, style-information etc.

**Please note:** The value of a the cell can be extracted in different datatypes ( string, date, numerical and boolean ) using the dot-operator. As default the value will be a string literal.

The 'height' attribute represents the vertical cell-range of the merged -region, the cell is part of. If the cell isn't part of a merged-region the value will be 1.

The 'address' attribute represents the location of the cell represented by the following pattern:

```
sheet-name'/'row-index'/'cell-index
```

The 'style' attribute can be used to access different style information of the cell, like its foreground or background color.

The 'border' attribute can be used to access the border-width of the cell.

#### Overview ####

|**accessor**|**description**|
|:-----------|:--------------|
|value       | The string value of the cell.|
|value.string| The string value of the cell.|
|value.date  | The date value of the cell. If the value isn't a date an error will be occur.|
|value.numerical|The numerical value of the cell. If the value isn't a number an error will be occur.|
|value.boolean|The boolean value of the cell.|
|address     | The location of the cell.|
|height      | The vertical number of cells in the merged-region.|
|style.foreground| The foreground color of the cell.|
|style.background| The background color of the cell.|
|border.top  | The width of the border on the top of the cell.|
|border.left | The width of the border on the left of the cell.|
|border.right| The width of the border on the right of the cell.|
|border.bottom| The width of the border on the bottom of the cell.|