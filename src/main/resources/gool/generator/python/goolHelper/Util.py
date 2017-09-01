# Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
# Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
#
# This file is part of GOOL.
#
# GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
# General Public License as published by the Free Software Foundation, version 3.
#
# GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
# even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License version 3 for more details.
#
# You should have received a copy of the GNU General Public License along with GOOL,
# in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.







class Scanner(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1 and isinstance(args[0], str):
			self.inializeFromStr()

	def inializeFromStr(self):
        	super(test, self).__init__()

	def nextLine(self):
		return raw_input()

	def nextInt(self):
		return int (raw_input())

	def nextDouble(self):
		return raw_input()
	
	def nexByte(self):
		return raw_input()
