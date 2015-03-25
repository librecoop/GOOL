#!/usr/bin/env python

import goolHelper
import goolHelper.IO
import goolHelper.Util


if __name__ == '__main__':
    from SimpleMod import *
# main program
    n = (4 % 5) # Unrecognized by GOOL, passed on: %
    print n
# end of main
    exit()

class SimpleMod(object):

    def __init__(self):
        super(SimpleMod, self).__init__()

