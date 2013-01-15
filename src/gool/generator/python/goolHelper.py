def test_args(args,*types):
    return len(args) == len(types) and [isinstance(a,t) for a,t in zip(args,types)] == [True]*len(types)
