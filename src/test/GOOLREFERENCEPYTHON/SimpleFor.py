#!/usr/bin/env python

from __future__ import print_function
import goolHelper
import goolHelper.IO
import goolHelper.Util




class SimpleFor(object):

    def __init__(self):
        super(SimpleFor, self).__init__()
        pass


if __name__ == '__main__':
    from SimpleFor import *
# main program
    total = 0
    i = 0 # GOOL warning: semantic may have changed
    while (i < 4):
        total +=1 # GOOL warning: semantic may have changed
        i +=1
    print(total)
# end of main
    exit()

