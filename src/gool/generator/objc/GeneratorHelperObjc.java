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

package gool.generator.objc;

import gool.ast.core.ArrayAccess;
import gool.ast.core.BinaryOperation;
import gool.ast.core.Expression;
import gool.ast.core.MemberSelect;
import gool.ast.core.MethCall;
import gool.ast.core.VarAccess;
import gool.ast.type.IType;
import gool.ast.type.PrimitiveType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;

public final class GeneratorHelperObjc extends GeneratorHelper {

	public static String type(IType type) {
		if (type instanceof TypeInt) {
			return "Int";
		} else if (type instanceof TypeChar) {
			return "Char";
		} else if (type instanceof TypeDecimal) {
			return "Double";
		} else if (type instanceof TypeBool) {
			return "Bool";
		} else if (type instanceof TypeException) {
			return "NSException *";
		} else {
			return "/* Unrecognized by gool */";
		}
	}

	public static String format(Expression e) {
		return format(e.getType());
	}

	public static String format(IType t) {
		if (t.equals(TypeString.INSTANCE)) {
			return "%@";
		} else if (t.equals(TypeInt.INSTANCE)) {
			return "%d";
		} else if (t.equals(TypeChar.INSTANCE)) {
			return "%c";
		} else if (t.equals(TypeDecimal.INSTANCE)) {
			return "%f";
		} else if (t.equals(TypeBool.INSTANCE)) {
			return "%d";
		} else if (t instanceof TypeClass) {
			return "%@";
		} else {
			return "/* Unrecognized by gool : " + t + " */";
		}
	}

	public static String evalIntExpr(Expression e) {
		if (e instanceof BinaryOperation)
			return "(" + evalIntExpr(((BinaryOperation) e).getLeft())
					+ ((BinaryOperation) e).getTextualoperator()
					+ evalIntExpr(((BinaryOperation) e).getRight()) + ")";
		else
			return e.toString();
	}

	public static String staticStringMini(Expression e) {
		return ((e.getType() instanceof TypeString)
				&& !(e instanceof VarAccess) && !(e instanceof MethCall) && !(e instanceof MemberSelect)) ? "@"
				: "";
	}

	public static String staticString(Expression e) {
		return ((e.getType() instanceof PrimitiveType)
				&& !(e instanceof VarAccess) && !(e instanceof MethCall))
				&& !(e instanceof ArrayAccess)
				&& !(e instanceof MemberSelect)
				&& !(e.toString().contains("[NSString stringWithFormat")) ? "@"
				: "";
	}

	public static String initWithObject(Expression e) {
		if (e.getType() instanceof PrimitiveType
				&& !(e.getType() instanceof TypeString))
			return "[[NSNumber alloc]initWith"
					+ GeneratorHelperObjc.type(e.getType()) + ":" + e + "]";
		else
			return GeneratorHelperObjc.staticString(e) + e;
	}
}
