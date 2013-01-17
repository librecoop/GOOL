def test_args(args,*types):
	for a, t in zip(args, types):
		if not isinstance(a, t):
			return False
    return len(args) == len(types)

# i n'est pas modifié !
def increment(i):
	i += 1
	return i

def decrement(i):
	i -= 1
	return i

