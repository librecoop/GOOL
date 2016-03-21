#!/usr/bin/env python

import goolHelper
import goolHelper.IO
import goolHelper.Util




class SimpleWhile(object):

    def __init__(self):
        super(SimpleWhile, self).__init__()
        pass


if __name__ == '__main__':
    from SimpleWhile import *
# main program
    i = 0
    total = 0
    while (i < 4):
        total +=1 # GOOL warning: semantic may have changed
        i +=1 # GOOL warning: semantic may have changed
    print total
# end of main
    exit()

