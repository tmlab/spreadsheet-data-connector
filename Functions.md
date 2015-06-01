### Functions ###

Functions can be used to translate the information represented by a cell or check some conditions.

#### Grammar ####

```
function	::=		name '(' value-expression ( ',' value-expression )* ')'
```

#### The Concat Function ####

```
string concat( string... literals )
```

The concat function expects at least two string literals and returns a new string literal created using the given ones. The given strings will be concat to each other to create a new string.

```
concat ( 'Hallo' , ' ' ,  'Welt' ) --> 'Hallo Welt'
```

#### The Hash Function ####

```
integer hash( string literal )
```

The hash function requires exact one string argument and calculates the hash value of the given string. The calculated hash value will be returned.

#### The Minimum And Maximum Functions ####

```
integer min( integer... literal )
integer max( integer... literal )
```

The functions expect at least two numerical literals as constant or sub-expression and return the minimum or maximum of that.

```
min( 1, 2 , 3 , 5 )	--> 1
max( 1, 2 , 3 , 5 )	--> 5
```

#### The Next And Previous Functions ####

```
integer previous( condition )
integer next( condition )
```

The function can be used to get the next or previous row index starting by the current one satisfying the given condition. The function is useful in context of the group-by-clause.

```
cell(0,1) 	--> '1'
cell(1,1) 	--> '2'
cell(2,1) 	--> '1'
cell(3,1) 	--> '1'

previous($1.value == 1 ) 	--> [ 2, 0 ]
next($1.value == 1 ) 		--> [ 2, 3 ]
```

#### The Tokenize Function ####

```
string tokenize(string literal, string delimer ( , integer index )? )
```

The function tokenize the first string argument using the given delimer. The return value will be token at the required position. The position can be defined by the third argument. If the argument is missing the first token will be returned.

```
tokenize('Hallo Welt' , ' ' , 2 ) --> 'Welt'
```

#### The Trim Function ####

```
string trim(string literal)
```

The function requires exactly one string literal and removes all white-spaces at the beginning and the end of the string literal.

```
trim( '  Hallo Welt   ' ) --> 'Hallo Welt'
```

#### Overview ####

The number of expected arguments depends on the function itselfs.

|**function**|**parameters**|**description**|
|:-----------|:-------------|:--------------|
|concat      | at least two string | Creates a new string by concat the given string literals.|
|hash        | a string value | Calculates the hash value of the given string.|
|min         | at least two numbers | Returns the minimum of the given numerical values.|
|max         | at least two numbers | Returns the maximum of the given numerical values.|
|next        | a boolean-expression | Returns the index of the next cell satisfying the given condition.|
|previous    | a boolean-expression | Returns the index of the previous cell satisfying the given condition.|
|trim        |a string literal|Removes any white-spaces at the beginning and the end of the given string literal.|
|tokenize    |two string literals and a number| Tokenize the first string by using the second string as pattern. The number is optional and will be used to returns the string token at the given position.|
|value       |two numbers   |Returns the string value of the cell with the given row and column index. |