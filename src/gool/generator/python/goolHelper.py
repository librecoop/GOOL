def test_args(args,*types):
    for a, t in zip(args, types):
        if not isinstance(a, t):
            return False
    return len(args) == len(types)
