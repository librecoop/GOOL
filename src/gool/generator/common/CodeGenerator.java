package gool.generator.common;

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.Assign;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Block;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Comment;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.ExpressionUnknown;
import gool.ast.constructs.Field;
import gool.ast.constructs.FieldAccess;
import gool.ast.constructs.For;
import gool.ast.constructs.GoolCall;
import gool.ast.constructs.Identifier;
import gool.ast.constructs.If;
import gool.ast.constructs.ListMethCall;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.constructs.MapMethCall;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.NewInstance;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Return;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.UnaryOperation;
import gool.ast.constructs.VarDeclaration;
import gool.ast.constructs.While;
import gool.ast.list.ListAddCall;
import gool.ast.list.ListContainsCall;
import gool.ast.list.ListGetCall;
import gool.ast.list.ListGetIteratorCall;
import gool.ast.list.ListIsEmptyCall;
import gool.ast.list.ListRemoveAtCall;
import gool.ast.list.ListRemoveCall;
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
import gool.ast.system.SystemOutDependency;
import gool.ast.system.SystemOutPrintCall;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVoid;

import java.util.Collection;

public interface CodeGenerator {

	void addCustomDependency(String key, Dependency value);

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

	String getCode(ListSizeCall lsc);

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

	String getCode(CustomDependency customDependency);

	String getCode(Dependency dependency);

	String getCode(Identifier identifier);

	String getCode(TypeUnknown typeUnknown);
	
	String getCode(ExpressionUnknown unknownExpression);
	
}
