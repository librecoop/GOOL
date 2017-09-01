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

package gool.generator.common;

import gool.ast.core.ArrayAccess;
import gool.ast.core.ArrayNew;
import gool.ast.core.Assign;
import gool.ast.core.BinaryOperation;
import gool.ast.core.Block;
import gool.ast.core.CastExpression;
import gool.ast.core.Catch;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassFree;
import gool.ast.core.ClassNew;
import gool.ast.core.Comment;
import gool.ast.core.CompoundAssign;
import gool.ast.core.Constant;
import gool.ast.core.Constructor;
import gool.ast.core.Dependency;
import gool.ast.core.EnhancedForLoop;
import gool.ast.core.EqualsCall;
import gool.ast.core.ExpressionUnknown;
import gool.ast.core.Field;
import gool.ast.core.FieldAccess;
import gool.ast.core.For;
import gool.ast.core.GoolCall;
import gool.ast.core.Identifier;
import gool.ast.core.If;
import gool.ast.core.ListMethCall;
import gool.ast.core.MainMeth;
import gool.ast.core.MapEntryMethCall;
import gool.ast.core.MapMethCall;
import gool.ast.core.MemberSelect;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.NewInstance;
import gool.ast.core.Package;
import gool.ast.core.ParentCall;
import gool.ast.core.RecognizedDependency;
import gool.ast.core.Return;
import gool.ast.core.StringIsEmptyCall;
import gool.ast.core.This;
import gool.ast.core.ThisCall;
import gool.ast.core.Throw;
import gool.ast.core.ToStringCall;
import gool.ast.core.Try;
import gool.ast.core.TypeDependency;
import gool.ast.core.UnImplemented;
import gool.ast.core.UnaryOperation;
import gool.ast.core.UnrecognizedDependency;
import gool.ast.core.VarAccess;
import gool.ast.core.VarDeclaration;
import gool.ast.core.While;
import gool.ast.list.ListAddCall;
import gool.ast.list.ListClearCall;
import gool.ast.list.ListContainsCall;
import gool.ast.list.ListGetCall;
import gool.ast.list.ListGetIteratorCall;
import gool.ast.list.ListIndexOfCall;
import gool.ast.list.ListIsEmptyCall;
import gool.ast.list.ListRemoveAtCall;
import gool.ast.list.ListRemoveCall;
import gool.ast.list.ListSetCall;
import gool.ast.list.ListSizeCall;
import gool.ast.map.MapContainsKeyCall;
import gool.ast.map.MapEntryGetKeyCall;
import gool.ast.map.MapEntryGetValueCall;
import gool.ast.map.MapGetCall;
import gool.ast.map.MapGetIteratorCall;
import gool.ast.map.MapIsEmptyCall;
import gool.ast.map.MapPutCall;
import gool.ast.map.MapRemoveCall;
import gool.ast.map.MapSizeCall;
import gool.ast.system.SystemCommandDependency;
import gool.ast.system.SystemOutDependency;
import gool.ast.system.SystemOutPrintCall;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeGoolLibraryClass;
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypePackage;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVar;
import gool.ast.type.TypeVoid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Generates the concrete target from the abstract GOOL. Many things are common
 * to various target languages, those are dealt with by CommonCodeGenerator.
 */
public interface CodeGenerator {

	/**
	 * Adding dependencies that are specifics to the target language
	 * @param dep
	 */
	void addCustomDependency(String dep);
	Set<String> getCustomDependencies();
	void clearCustomDependencies();
	
	String getCode(ArrayAccess arrayAccess);

	String getCode(ArrayNew arrayNew);

	String getCode(Assign assign);

	/**
	 * Produces code for a binary operation.
	 * 
	 * @param binaryOp
	 *            a binary operation.
	 * @return the formatted binary operation.
	 */
	String getCode(BinaryOperation binaryOp);

	/**
	 * Produces code for a block of statements.
	 * 
	 * @param block
	 *            the block of statements.
	 * @return the formatted block of statements.
	 */
	String getCode(Block block);

	/**
	 * Produces code for a cast expression.
	 * 
	 * @param cast
	 *            the cast expression.
	 * @return the formatted cast expression.
	 */
	String getCode(CastExpression cast);

	/**
	 * Produces code for an object instantiation.
	 * 
	 * @param classNew
	 *            the object instantiation node.
	 * @return the formatted object instantiation.
	 */
	String getCode(ClassNew classNew);

	String getCode(Comment comment);

	/**
	 * Produces code for a constant.
	 * 
	 * @param constant
	 *            a constant value.
	 * @return the formatted constant value.
	 */
	String getCode(Constant constant);

	String getCode(Constructor cons);

	String getCode(EnhancedForLoop enhancedForLoop);

	String getCode(EqualsCall equalsCall);

	/**
	 * Produces code for a class attribute declaration.
	 * 
	 * @param field
	 *            the declared class attribute
	 * @return the formatted class attribute.
	 */
	String getCode(Field field);

	String getCode(FieldAccess sfa);

	String getCode(For forr);

	String getCode(GoolCall goolCall);

	/**
	 * Produces code for an if statement.
	 * 
	 * @param pif
	 *            the if statement.
	 * @return the formatted if statement.
	 */
	String getCode(If pif);

	String getCode(Collection<Modifier> modifiers);

	String getCode(ListAddCall lac);

	String getCode(ListContainsCall lcc);

	String getCode(ListGetCall lgc);

	String getCode(ListGetIteratorCall lgic);

	String getCode(ListIsEmptyCall liec);

	String getCode(ListMethCall lmc);

	String getCode(ListRemoveAtCall lrc);

	String getCode(ListRemoveCall lrc);

	String getCode(ListSetCall lsc);

	String getCode(ListSizeCall lsc);
	
	String getCode(ListClearCall lcc);
	
	String getCode(ListIndexOfCall lioc);

	String getCode(MainMeth mainMeth);

	String getCode(MapContainsKeyCall mapContainsKeyCall);

	String getCode(MapEntryGetKeyCall mapEntryGetKeyCall);

	String getCode(MapEntryGetValueCall mapEntryGetKeyCall);

	String getCode(MapEntryMethCall mapEntryMethCall);

	String getCode(MapGetCall mapGetCall);

	String getCode(MapGetIteratorCall mapGetIteratorCall);

	String getCode(MapIsEmptyCall mapIsEmptyCall);

	String getCode(MapMethCall mapMethCall);

	String getCode(MapPutCall mapPutCall);

	String getCode(MapRemoveCall mapRemoveCall);

	String getCode(MapSizeCall mapSizeCall);

	String getCode(StringIsEmptyCall stringIsEmptyCall);
	
	String getCode(MemberSelect memberSelect);

	String getCode(Meth meth);

	/**
	 * Produces code for a method invocation.
	 * 
	 * @param methodCall
	 *            the method to be invoked.
	 * @return the formatted method invocation.
	 */
	String getCode(MethCall methodCall);

	String getCode(Modifier modifier);

	/**
	 * Produces code for an object instantiation. This is different from
	 * ClassNew in the sense that it includes a variable declaration and
	 * assignment in the same line.
	 * 
	 * @param newInstance
	 *            the object instantiation.
	 * @return the formatted object instantiation.
	 */
	String getCode(NewInstance newInstance);

	String getCode(ParentCall parentCall);

	/**
	 * Produces code for a return statement.
	 * 
	 * @param returnExpr
	 *            the return statement.
	 * @return the formatted return statement.
	 */
	String getCode(Return returnExpr);

	String getCode(SystemOutDependency systemOutDependency);

	String getCode(SystemOutPrintCall systemOutPrintCall);

	/**
	 * Produces code for the reference to the current object.
	 * 
	 * @param pthis
	 *            the reference to the current object.
	 * @return the formatted self reference expression.
	 */
	String getCode(This pthis);

	String getCode(ThisCall thisCall);

	String getCode(ToStringCall tsc);

	/**
	 * Produces code for a boolean type in the target language.
	 * 
	 * @param typeBool
	 *            the boolean type.
	 * @return the formatted boolean type.
	 */
	String getCode(TypeBool typeBool);

	String getCode(TypeByte typeByte);

	/**
	 * @param typeClass
	 *            the class to be formatted.
	 * @return the formatted class type.
	 */
	String getCode(TypeClass typeClass);

	String getCode(TypeDecimal typeReal);

	String getCode(TypeChar typeChar);

	String getCode(TypeDependency typeDependency);

	String getCode(TypeEntry typeEntry);


	/**
	 * Produces code for an integer type in the target language.
	 * 
	 * @param typeInt
	 *            the integer type.
	 * @return the formatted integer type.
	 */
	String getCode(TypeInt typeInt);

	/**
	 * Produces code for a List type.
	 * 
	 * @param typeList
	 *            the list type.
	 * @return the formatted list type.
	 */
	String getCode(TypeList typeList);

	String getCode(TypeMap typeMap);

	/**
	 * Produces code for the pseudo-type.
	 * 
	 * @param type
	 *            a pseudo-type.
	 * @return an empty string.
	 */
	String getCode(TypeNone type);

	String getCode(TypeNull typeNull);

	/**
	 * Produces code for the root type of the target language (i.e. Object in
	 * Java).
	 * 
	 * @param typeObject
	 *            the root type.
	 * @return the formatted root type.
	 */
	String getCode(TypeObject typeObject);

	/**
	 * Produces code for a string type in the target language.
	 * 
	 * @param typeString
	 *            the string type.
	 * @return the formatted string type.
	 */
	String getCode(TypeString typeString);

	/**
	 * Produces code for a type that does not return anything.
	 * 
	 * @param typeVoid
	 *            the void type
	 * @return the formatted void type.
	 */
	String getCode(TypeVoid typeVoid);

	String getCode(UnaryOperation unaryOperation);

	String getCode(VarAccess varAccess);

	/**
	 * Produces code for a variable declaration.
	 * 
	 * @param varDec
	 *            the variable to be declared.
	 * @return the formatted variable declaration.
	 */
	String getCode(VarDeclaration varDec);

	String getCode(While whilee);

	String getCode(TypeArray typeArray);

	//String getCode(CustomDependency customDependency);

	String getCode(Identifier identifier);

	String getCode(TypeUnknown typeUnknown);

	String getCode(ExpressionUnknown unknownExpression);

	String getCode(ClassFree classFree);

	String getCode(Platform platform);

	String getCode(ClassDef classDef);

	String getCode(Package _package);

	String getCode(SystemCommandDependency systemCommandDependency);

	String getCode(TypePackage typePackage);

	String getCode(TypeMethod typeMethod);

	String getCode(TypeVar typeVar);

	String getCode(CompoundAssign compoundAssign);

	String getCode(Throw throwStatement);

	String getCode(Catch catchStatement);

	String getCode(Try tryStatement);

	String getCode(TypeException typeException);

	String getCode(TypeGoolLibraryClass typeMatchedGoolClass);

	String getCode(RecognizedDependency recognizedDependency);

	String getCode(UnrecognizedDependency unrecognizedDependency);

	String getCode(UnImplemented unImplemented);

}
