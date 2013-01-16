def test_args(args,*types):
    return len(args) == len(types) and sum([isinstance(a,t) for a,t in zip(args,types)]) == len(types)

def increment(i):
	i += 1
	return i

def decrement(i):
	i -= 1
	return i
