grammar SupplyQL;

anfrage : anfrage 'and' klausel 		# And
		| anfrage 'or' klausel			# Or		
		| anfrage 'not' klausel			# Not
		| klausel						# Simple
		;						

klausel : '(' anfrage ')'				# Klammer
		| index 'any' STRING			# Any		
		| index 'all' STRING			# All
		| index 'adj' STRING			# Adj
		| index 'eq' ZAHL				# Eq
		| index 'le' ZAHL				# Le
		| index 'gt' ZAHL				# Gt
		;

index : INDEX;
		   
STRING : '"' (ESC|.)+? '"';

fragment
ESC : '\\"' | '\\\\' | '\\*';
ZAHL : [1-9][0-9]*;
INDEX : [a-z]+;
WS : [ ]+ -> skip;