import os

def test_args(args,*types):
    for a, t in zip(args, types):
        if not isinstance(a, t):
            return False
    return len(args) == len(types)

def mkdir(path):
    if not os.path.exists(path):
        os.makedirs(path)
        return True
    return False
