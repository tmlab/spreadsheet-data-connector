### Comparison ###

```
comparison		::=	value-expression ( '==' | '>=' | '>' | '<=' | '<' | '!=' | '~=' ) value-expression
```

The comparison expression can be used as a boolean-primitive. The result of a comparison expression will be 'true' or 'false'. In context of a boolean-primitive the comparison expression expects a collection of row objects and filter them by the comparison condition. The result will be a possible empty collection of row objects.