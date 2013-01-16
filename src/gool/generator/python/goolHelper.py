def test_args(args,*types):
    return len(args) == len(types) and False not in [isinstance(a,t) for a,t in zip(args,types)]
