/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.ast.core;

/**
 * Enumeration of the operators that supported by the gool system.
 */
public enum Operator {
	UNKNOWN, PLUS, MINUS, MULT, AND, OR, EQUAL, NOT, LT, LEQ, GT, GEQ, DECIMAL_PLUS, DECIMAL_MINUS, DIV, DECIMAL_DIV, DECIMAL_MULT, DECIMAL_LT, DECIMAL_LEQ, DECIMAL_GT, DECIMAL_GEQ, PREFIX_DECREMENT, POSTFIX_DECREMENT, PREFIX_INCREMENT, POSTFIX_INCREMENT, NOT_EQUAL;
}
