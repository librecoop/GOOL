package gool.recognizer.java;

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.Assign;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Block;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Comment;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dec;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.Field;
import gool.ast.constructs.For;
import gool.ast.constructs.INode;
import gool.ast.constructs.If;
import gool.ast.constructs.InitCall;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.Package;
import gool.ast.constructs.Parameterizable;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Return;
import gool.ast.constructs.Statement;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.UnaryOperation;
import gool.ast.constructs.VarAccess;
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
import gool.ast.type.IType;
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
import gool.ast.type.TypeVoid;
import gool.generator.common.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.apache.log4j.Logger;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;

class Context {
	private ClassDef classDef;
	private Block block;

	public Context(ClassDef currentClass, Block currentBlock) {
		this.classDef = currentClass;
		this.block = currentBlock;
	}

	public Block getBlock() {
		return block;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

}

public class JavaRecognizer extends TreePathScanner<Object, Context> {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(JavaRecognizer.class);
	/**
	 * The list of keywords that may cause problems when generating target code
	 * from concrete or abstract GOOL.
	 */
	private static final Set<String> FORBIDDEN_KEYWORDS = new HashSet<String>();

	/**
	 * The default platform used to specify the platform of newly created
	 * classes.
	 */
	private Platform defaultPlatform;
	/**
	 * The compilation unit that is currently analyzed.
	 */
	private CompilationUnitTree currentCompilationUnit;
	/**
	 * The abstract syntax tree.
	 */
	private Trees trees;
	/**
	 * The list of abstract GOOL classes that are generated.
	 */
	private Map<IType, ClassDef> goolClasses = new HashMap<IType, ClassDef>();
	private Map<String, Package> packagesCache = new HashMap<String, Package>();

	static {
		// C#
		FORBIDDEN_KEYWORDS.add("out");
		FORBIDDEN_KEYWORDS.add("params");
		FORBIDDEN_KEYWORDS.add("value");
		FORBIDDEN_KEYWORDS.add("out");
		FORBIDDEN_KEYWORDS.add("ref");
		FORBIDDEN_KEYWORDS.add("object");
		FORBIDDEN_KEYWORDS.add("string");
	}

	public static void addForbiddenKeyword(String keyword) {
		FORBIDDEN_KEYWORDS.add(keyword);
	}

	public static void addForbiddenKeyword(File keywordsFile)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(keywordsFile));

		String keyword;
		while ((keyword = reader.readLine()) != null) {
			addForbiddenKeyword(keyword);
		}
	}
	
	private void addParameters(List<? extends ExpressionTree> list,
			Parameterizable expr, Context context) {
		if (list != null) {
			for (ExpressionTree p : list) {
				Expression arg = (Expression) p.accept(this, context);
				if (arg != null) {
					expr.addParameter(arg);
				}
			}
		}
	}

	private String error(String format, Object... message) {
		return String.format("%s [File %s]", String.format(format, message),
				currentCompilationUnit.getSourceFile().getName());
	}

	public final void setCurrentCompilationUnit(
			CompilationUnitTree currentCompilationUnit) {
		this.currentCompilationUnit = currentCompilationUnit;
	}

	public final void setDefaultPlatform(Platform defaultPlatform) {
		this.defaultPlatform = defaultPlatform;
	}

	public final void setTrees(Trees trees) {
		this.trees = trees;
	}

	public final Collection<ClassDef> getGoolClasses() {
		return goolClasses.values();
	}

	static final private Map<Kind, Operator> operatorMap = new HashMap<Kind, Operator>();
	static {
		operatorMap.put(Kind.PLUS, Operator.PLUS);
		operatorMap.put(Kind.MINUS, Operator.MINUS);
		operatorMap.put(Kind.UNARY_MINUS, Operator.MINUS);
		operatorMap.put(Kind.MULTIPLY, Operator.MULT);
		operatorMap.put(Kind.DIVIDE, Operator.DIV);
		operatorMap.put(Kind.CONDITIONAL_AND, Operator.AND);
		operatorMap.put(Kind.CONDITIONAL_OR, Operator.OR);
		operatorMap.put(Kind.EQUAL_TO, Operator.EQUAL);
		operatorMap.put(Kind.NOT_EQUAL_TO, Operator.NOT_EQUAL);
		operatorMap.put(Kind.LESS_THAN, Operator.LT);
		operatorMap.put(Kind.LESS_THAN_EQUAL, Operator.LEQ);
		operatorMap.put(Kind.GREATER_THAN, Operator.GT);
		operatorMap.put(Kind.GREATER_THAN_EQUAL, Operator.GEQ);
		operatorMap.put(Kind.LOGICAL_COMPLEMENT, Operator.NOT);
		operatorMap.put(Kind.PREFIX_DECREMENT, Operator.PREFIX_DECREMENT);
		operatorMap.put(Kind.POSTFIX_DECREMENT, Operator.POSTFIX_DECREMENT);
		operatorMap.put(Kind.PREFIX_INCREMENT, Operator.PREFIX_INCREMENT);
		operatorMap.put(Kind.POSTFIX_INCREMENT, Operator.POSTFIX_INCREMENT);
	}

	private Operator getOperator(Kind kind) {
		Operator result = operatorMap.get(kind);
		if (result == null) {
			throw new IllegalStateException(error(
					"%s operator is not supported", kind));
		}
		return result;
	}

	private TypeMirror getTypeMirror(Tree n) {
		TreePath path = TreePath.getPath(currentCompilationUnit, n);
		TypeMirror typeMirror = trees.getTypeMirror(path);

		return typeMirror;
	}

	private IType goolType(Tree n, Context context) {
		/*
		 * A null tree usually means that we are dealing with a constructor.
		 */
		if (n == null) {
			return TypeNone.INSTANCE;
		}
		return goolType(getTypeMirror(n), context);
	}

	private IType goolType(TypeKind typeKind) {
		switch (typeKind) {
		case BOOLEAN:
			return TypeBool.INSTANCE;
		case DOUBLE:
		case FLOAT:
			return TypeDecimal.INSTANCE;
		case SHORT:
		case INT:
		case LONG:
			return TypeInt.INSTANCE;
		case VOID:
			return TypeVoid.INSTANCE;
		case BYTE:
			return TypeByte.INSTANCE;
		default:
			throw new IllegalStateException(error(
					"%s PrimitiveType not supported.", typeKind));
		}
	}

	private IType goolType(TypeMirror typeMirror, Context context) {
		if (typeMirror == null) {
			return TypeNone.INSTANCE;
		} else if (typeMirror.getKind().isPrimitive()) {
			return goolType(typeMirror.getKind());
		}

		switch (typeMirror.getKind()) {
		case PACKAGE:
		case DECLARED:

			Type type = (Type) typeMirror;

			Symbol classSymbol = (Symbol) type.asElement();

			String typeName = classSymbol.getSimpleName().toString();

			IType goolType = string2IType(typeName, context);
			boolean isEnum = ((classSymbol.flags() & Flags.ENUM) != 0);

			if (isEnum && goolType instanceof TypeClass) {
				((TypeClass) goolType).setIsEnum(isEnum);
			}

			for (Type t : type.getTypeArguments()) {
				goolType.addArgument(goolType(t, context));
			}

			if (goolType.getName().equals("Iterable")) {
				throw new RuntimeException(goolType.toString());
			}

			if (!type.toString().startsWith("java.lang")) {
				if (!goolType.toString().equalsIgnoreCase("gool")
						&& !context.getClassDef().getType().equals(goolType)) {
					context.getClassDef().addDependency(
							new TypeDependency(goolType));
				}
			}

			return goolType;
		case EXECUTABLE:
		case TYPEVAR:
			return TypeObject.INSTANCE;
		case VOID:
			return TypeVoid.INSTANCE;
		case ARRAY:
			ArrayType arrayType = (ArrayType) typeMirror;
			return new TypeArray(
					goolType(arrayType.getComponentType(), context));
		case NULL:
			return TypeNull.INSTANCE;
		default:
			throw new IllegalStateException(error(
					"%s type of kind %s not supported", typeMirror, typeMirror
							.getKind()));
		}
	}

	private void addDependencyToContext(Context context, Dependency newDep) {
		if (newDep != null && context != null && context.getClassDef() != null) {
			context.getClassDef().addDependency(newDep);
		}
	}

	private static abstract class Otd {
		abstract public IType getType();
	};

	private static final Map<String, Otd> string2otdMap = new HashMap<String, Otd>();
	static {
		Otd tmpOtd = new Otd() {
			public IType getType() {
				return TypeString.INSTANCE;
			}
		};
		string2otdMap.put("String", tmpOtd);
		string2otdMap.put("java.lang.String", tmpOtd);
		tmpOtd = new Otd() {
			public IType getType() {
				return TypeDecimal.INSTANCE;
			}
		};
		string2otdMap.put("Double", tmpOtd);
		string2otdMap.put("java.lang.Double", tmpOtd);
		tmpOtd = new Otd() {
			public IType getType() {
				return TypeInt.INSTANCE;
			}
		};
		string2otdMap.put("Integer", tmpOtd);
		string2otdMap.put("java.lang.Integer", tmpOtd);
		tmpOtd = new Otd() {
			public IType getType() {
				return new TypeList();
			}
		};
		string2otdMap.put("List", tmpOtd);
		string2otdMap.put("ArrayList", tmpOtd);
		string2otdMap.put("java.util.ArrayList", tmpOtd);
		string2otdMap.put("gool.imports.java.util.ArrayList", tmpOtd);
		tmpOtd = new Otd() {
			public IType getType() {
				return new TypeMap();
			}
		};
		string2otdMap.put("Map", tmpOtd);
		string2otdMap.put("HashMap", tmpOtd);
		string2otdMap.put("java.util.HashMap", tmpOtd);
		string2otdMap.put("gool.imports.java.util.HashMap", tmpOtd);
		tmpOtd = new Otd() {
			public IType getType() {
				return new TypeEntry();
			}
		};
		string2otdMap.put("Entry", tmpOtd);
		string2otdMap.put("gool.imports.java.util.Map.Entry", tmpOtd);
	}

	private IType string2IType(String typeName, Context context) {

		if (string2otdMap.containsKey(typeName)) {
			IType type = string2otdMap.get(typeName).getType();
			addDependencyToContext(context, new TypeDependency(type));
			return type;
		} else if (typeName.equalsIgnoreCase("Object")) {
			return TypeObject.INSTANCE;
		} else if (typeName.equalsIgnoreCase("Boolean")) {
			return TypeBool.INSTANCE;
		} else if (typeName.equalsIgnoreCase("Byte")) {
			return TypeByte.INSTANCE;
		} else {
			return new TypeClass(typeName);
		}
	}

	public boolean findAnnotation(List<? extends AnnotationTree> list,
			String annotation) {
		for (AnnotationTree n : list) {
			if (n.getAnnotationType().toString().equals(annotation)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object visitArrayAccess(ArrayAccessTree n, Context context) {
		return new ArrayAccess((Expression) n.getExpression().accept(this,
				context), (Expression) n.getIndex().accept(this, context));
	}

	@Override
	public Object visitArrayType(ArrayTypeTree n, Context context) {
		return goolType(n, context);
	}

	@Override
	public Object visitAssert(AssertTree n, Context context) {
		throw new IllegalStateException(
				error("Assert statement is not supported"));
	}

	@Override
	public Object visitAssignment(AssignmentTree n, Context context) {
		INode variable = (INode) n.getVariable().accept(this, context);
		Expression expression = (Expression) n.getExpression().accept(this,
				context);
		return new Assign(variable, expression);
	}

	@Override
	public Object visitBinary(BinaryTree n, Context context) {
		Expression leftExp = (Expression) n.getLeftOperand().accept(this,
				context);
		Expression rightExp = (Expression) n.getRightOperand().accept(this,
				context);
		Operator operator = getOperator(n.getKind());

		return new BinaryOperation(operator, leftExp, rightExp);
	}

	@Override
	public Object visitBlock(BlockTree n, Context context) {
		Block block = new Block();
		for (StatementTree stmt : n.getStatements()) {
			Statement statement = (Statement) stmt.accept(this, context);
			if (statement != null) {
				block.addStatement(statement);
			}
		}
		return block;
	}

	@Override
	public Object visitBreak(BreakTree n, Context context) {
		throw new IllegalStateException(
				error("The 'break' statement is not supported"));
	}

	@Override
	public Object visitCase(CaseTree n, Context context) {
		throw new IllegalStateException(
				error("The 'case' instruction is not supported"));
	}

	@Override
	public Object visitCatch(CatchTree n, Context context) {
		throw new IllegalStateException(
				error("The 'catch' instruction is not supported"));
	}

	@Override
	public Object visitClass(ClassTree n, Context context) {
		JCClassDecl c = (JCClassDecl) n;
		ClassDef classDef = new ClassDef(n.getSimpleName().toString());
		Context newContext = new Context(classDef, null);

		classDef.setIsEnum((c.mods.flags & Flags.ENUM) != 0);

		LOG.debug(String.format("Parsing class %s", n.getSimpleName()));

		Collection<Modifier> modifiers = (Collection<Modifier>) n
				.getModifiers().accept(this, newContext);

		/*
		 * Do not allow 'static' modifier for classes, it means different things
		 * in the target languages.
		 */
		modifiers.remove(Modifier.STATIC);

		/*
		 * classes must be 'public' in order to solve accessibility in the
		 * target languages.
		 */
		modifiers.add(Modifier.PUBLIC);

		for (AnnotationTree annotationTree : n.getModifiers().getAnnotations()) {
			if (annotationTree.getAnnotationType().toString().equals(
					"ForcePlatform")) {
				if (annotationTree.getArguments().size() > 0) {
					INode an = (INode) annotationTree.getArguments().get(0)
							.accept(this, newContext);

					if (an instanceof Assign) {
						Platform platform = Platform
								.valueOf(((Constant) ((Assign) an).getValue())
										.getValue().toString());
						if (platform != null) {
							classDef.setPlatform(platform);
						}
						break;
					}
				}
				throw new IllegalStateException(
						error("The ForcePlatform annotation should have a platform as its parameter. For example @ForcePlatform(platform=\"JAVA\")"));
			}
		}

		classDef.setModifiers(modifiers);

		JCModifiers mtree = (JCModifiers) n.getModifiers();
		classDef.setIsInterface((mtree.flags & Flags.INTERFACE) != 0);

		// Assign a default platform
		if (classDef.getPlatform() == null) {
			classDef.setPlatform(defaultPlatform);
		}

		goolClasses.put(classDef.getType(), classDef);

		/*
		 * If the class has the CustomCode attribute, we only generate an empty
		 * class.
		 */
		boolean customCode = findAnnotation(n.getModifiers().getAnnotations(),
				"CustomCode");

		if (customCode) {
			for (Tree tree : n.getMembers()) {
				if (tree instanceof MethodTree) {
					MethodTree method = (MethodTree) tree;
					classDef.addMethod(createMethod(method, newContext, true,
							false));
				}
			}
			return classDef;
		}

		if (n.getExtendsClause() != null) {
			IType parentType = goolType(n.getExtendsClause(), newContext);
			classDef.setParentClass(parentType);
		}

		for (Tree iface : n.getImplementsClause()) {
			classDef.addInterface(goolType(iface, newContext));
		}

		for (Tree tree : n.getMembers()) {
			INode member = (INode) tree.accept(this, newContext);
			if (member instanceof Meth) {
				classDef.addMethod((Meth) member);
			} else if (member instanceof Field) {
				classDef.addField((Field) member);
			} else if (member instanceof VarDeclaration) {
				classDef.addField(new Field(Arrays.asList(Modifier.PRIVATE),
						(VarDeclaration) member));
			} else if (member != null) {
				LOG.debug(String.format(
						"Unrecognized member for class %s: %s ", classDef
								.getName(), member));
			}
		}

		return classDef;
	}

	@Override
	public Object visitCompilationUnit(CompilationUnitTree n, Context context) {
		String ppackage = null;
		if (n.getPackageName() != null) {
			ppackage = n.getPackageName().accept(this, context).toString();
		}
		List<Dependency> dependencies = new ArrayList<Dependency>();
		for (ImportTree imp : n.getImports()) {
			String dependencyString = imp.getQualifiedIdentifier().toString();
			if (!dependencyString.contains("gool.imports.java")
					&& !dependencyString
							.contains("gool.imports.java.annotations")) {
				dependencies.add(new CustomDependency(dependencyString));
			}
		}
		for (Tree unit : n.getTypeDecls()) {
			ClassDef classDef = (ClassDef) unit.accept(this, context);
			if (ppackage != null) {
				Package p = packagesCache.get(ppackage);
				if (p == null) {
					p = new Package(ppackage);
					packagesCache.put(ppackage, p);
				}
				p.addClass(classDef);
				classDef.setPpackage(p);
			}
			classDef.addDependencies(dependencies);
		}
		return null;
	}

	@Override
	public Object visitCompoundAssignment(CompoundAssignmentTree n,
			Context context) {
		throw new IllegalStateException(error(
				"Compound assignments (%s) are not supported.", n));
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpressionTree n,
			Context context) {
		throw new IllegalStateException(
				error("The conditional expression is not currently supported"));
	}

	@Override
	public Object visitContinue(ContinueTree n, Context context) {
		throw new IllegalStateException(
				error("The 'continue' instruction is not supported"));
	}

	@Override
	public Object visitDoWhileLoop(DoWhileLoopTree n, Context context) {
		throw new IllegalStateException(error(
				"The 'do/while' instruction is not supported.", n));
	}

	@Override
	public Object visitEmptyStatement(EmptyStatementTree n, Context context) {
		// TODO What is the equivalent in GOOL?
		return null;
	}

	@Override
	public Object visitEnhancedForLoop(EnhancedForLoopTree n, Context context) {
		VarDeclaration varDec = (VarDeclaration) n.getVariable().accept(this,
				context);
		Expression expr = (Expression) n.getExpression().accept(this, context);

		Statement statements = (Statement) n.getStatement().accept(this,
				context);
		return new EnhancedForLoop(varDec, expr, statements);
	}

	@Override
	public Object visitErroneous(ErroneousTree n, Context context) {
		throw new IllegalStateException();
	}

	@Override
	public Object visitExpressionStatement(ExpressionStatementTree n,
			Context context) {
		return n.getExpression().accept(this, context);
	}

	@Override
	public Object visitForLoop(ForLoopTree node, Context p) {
		List<? extends StatementTree> initializers = node.getInitializer();
		if (initializers.size() > 1) {
			throw new IllegalStateException(
					error("More than one initializer is not supported"));
		}
		List<? extends StatementTree> updaters = node.getUpdate();
		if (updaters.size() > 1) {
			throw new IllegalStateException(
					error("More than one updater is not supported"));
		}
		Statement initializer = (Statement) initializers.get(0).accept(this, p);
		Expression condition = (Expression) node.getCondition().accept(this, p);
		Statement updater = (Statement) updaters.get(0).accept(this, p);
		return new For(initializer, condition, updater, (Statement) node
				.getStatement().accept(this, p));
	}

	@Override
	public Object visitIdentifier(IdentifierTree n, Context context) {
		if (FORBIDDEN_KEYWORDS.contains(n.getName().toString())) {
			throw new IllegalArgumentException(error(
					"The variable named '%s' uses reserved keyword.", n
							.getName()));
		}

		IType type = goolType(n, context);

		/*
		 * TODO identifiers. Create a specific node to access to class literals
		 * (i.e. when calling static members).
		 */
		if (type.getName().equals(n.getName().toString())) {
			return new Constant(type, n.getName().toString());
		}

		VarDeclaration varDec = new VarDeclaration(goolType(n, context), n
				.getName().toString());
		return new VarAccess(varDec);
	}

	@Override
	public Object visitIf(IfTree n, Context context) {
		Expression condition = (Expression) n.getCondition().accept(this,
				context);
		Statement thenStmt = (Statement) n.getThenStatement().accept(this,
				context);
		Statement elseStmt = null;
		if (n.getElseStatement() != null) {
			elseStmt = (Statement) n.getElseStatement().accept(this, context);
		}

		return new If(condition, thenStmt, elseStmt);
	}

	@Override
	public Object visitImport(ImportTree n, Context context) {
		throw new IllegalStateException(
				"return TypeDependency(TypeClass(n.getQualifiedIdentifier().accept(this, context).toString()))");
	}

	@Override
	public Object visitInstanceOf(InstanceOfTree node, Context p) {
		throw new IllegalStateException(error("InstanceOf is not supported"));
	}

	@Override
	public Object visitLabeledStatement(LabeledStatementTree node, Context p) {
		throw new IllegalStateException(
				error("LabeledStatement is not supported"));
	}

	@Override
	public Object visitLiteral(LiteralTree n, Context context) {
		String value = n.getValue() != null ? n.getValue().toString() : "null";
		return new Constant(goolType(n, context), value);
	}

	@Override
	public Object visitMemberSelect(MemberSelectTree n, Context context) {
		Expression target = (Expression) n.getExpression()
				.accept(this, context);
		String identifier = n.getIdentifier().toString();

		if (identifier.equalsIgnoreCase("equals")) {
			return new EqualsCall(target);
		}

		IType type = target.getType();
		
//		loader.ensureMethodExists(type, identifier);

		if (type != null) {
			/*
			 * TODO Do not assume that these methods are the same to the methods
			 * "toString" and "equals" belonging to the Object class.
			 */
			if (identifier.equals("toString")) {
				return new ToStringCall(target);
			}
			if (type instanceof TypeList) {
				if (identifier.equals("add")) {
					return new ListAddCall(target);
				}
				if (identifier.equals("remove")) {
					return new ListRemoveCall(target);
				}
				if (identifier.equals("removeAt")) {
					return new ListRemoveAtCall(target);
				}
				if (identifier.equals("get")) {
					return new ListGetCall(target);
				}
				if (identifier.equals("size")) {
					return new ListSizeCall(target);
				}
				if (identifier.equals("isEmpty")) {
					return new ListIsEmptyCall(target);
				}
				if (identifier.equals("getIterator")) {
					return new ListGetIteratorCall(target);
				}
				if (identifier.equals("contains")) {
					return new ListContainsCall(target);
				}
			}
			if (type instanceof TypeMap) {
				if (identifier.equals("put")) {
					return new MapPutCall(target);
				}
				if (identifier.equals("remove")) {
					return new MapRemoveCall(target);
				}
				if (identifier.equals("get")) {
					return new MapGetCall(target);
				}
				if (identifier.equals("size")) {
					return new MapSizeCall(target);
				}
				if (identifier.equals("isEmpty")) {
					return new MapIsEmptyCall(target);
				}
				if (identifier.equals("getIterator")) {
					return new MapGetIteratorCall(target);
				}
				if (identifier.equals("containsKey")) {
					return new MapContainsKeyCall(target);
				}
				if (identifier.equals("entrySet")) {
					return target;
				}
			}
			if (type instanceof TypeEntry) {
				if (identifier.equals("getKey")) {
					return new MapEntryGetKeyCall(target);
				}
				if (identifier.equals("getValue")) {
					return new MapEntryGetValueCall(target);
				}
			}
		}
		IType goolType = goolType(n, context);
		MemberSelect f = new MemberSelect(goolType, target, n.getIdentifier()
				.toString());
		return f;
	}

	@Override
	public Object visitMethod(MethodTree n, Context context) {
		boolean customCode = findAnnotation(n.getModifiers().getAnnotations(),
				"CustomCode");
		boolean isInherited = findAnnotation(n.getModifiers().getAnnotations(),
				"Override");

		Meth method = createMethod(n, context, customCode, true);
		method.setInherited(isInherited);

		return method;
	}

	private Meth createMethod(MethodTree n, Context context,
			boolean createOnlySignature, boolean commentOriginalCode) {
		Meth method;

		Collection<Modifier> modifiers = (Collection<Modifier>) n
				.getModifiers().accept(this, context);

		if (n.getReturnType() == null) {
			method = new Constructor();
		} else {
			IType type = goolType(n.getReturnType(), context);
			/*
			 * The hard way to determine if the current method is the entry
			 * point.
			 * 
			 * It may be better if we implement this as an annotation.
			 */
			boolean isMainMethod = type.equals(TypeVoid.INSTANCE)
					&& modifiers.contains(Modifier.PUBLIC)
					&& modifiers.contains(Modifier.STATIC);

			if (n.getParameters() != null && n.getParameters().size() == 1) {
				IType t = goolType(n.getParameters().get(0).getType(), context);

				if (t instanceof TypeArray) {
					isMainMethod &= ((TypeArray) t).getElementType().equals(
							TypeString.INSTANCE);
				} else {
					isMainMethod = false;
				}
			} else {
				isMainMethod = false;
			}

			if (isMainMethod) {
				method = new MainMeth();
			} else {
				method = new Meth(type, n.getName().toString());
			}
		}

		if (n.getParameters() != null) {
			for (VariableTree p : n.getParameters()) {
				method.addParameter(new VarDeclaration((Dec) p.accept(this,
						context)));
			}
		}

		method.setModifiers(modifiers);

		if (n.getBody() != null) {
			if (createOnlySignature) {
				if (commentOriginalCode) {
					method.addStatement(new Comment(n.getBody().toString()));
				}

				if (!TypeVoid.INSTANCE.equals(method.getType())
						&& !method.isConstructor()) {
					String returnValue = "null";
					IType returnType;
					if (method.getType() instanceof TypeInt) {
						returnValue = "-1";
						returnType = TypeInt.INSTANCE;
					} else if (method.getType() instanceof TypeBool) {
						returnValue = "false";
						returnType = TypeBool.INSTANCE;
					} else if (method.getType() instanceof TypeByte) {
						returnValue = "0";
						returnType = TypeByte.INSTANCE;
					} else {
						returnType = TypeNull.INSTANCE;
					}
					method.addStatement(new Return(new Constant(returnType,
							returnValue)));
				}

			} else {
				for (StatementTree stmt : n.getBody().getStatements()) {
					Statement statement = (Statement) stmt
							.accept(this, context);
					if (statement instanceof InitCall
							&& method instanceof Constructor) {
						((Constructor) method)
								.addInitCall((InitCall) statement);
					} else if (statement != null) {
						method.addStatement(statement);
					}
				}
			}
		}
		return method;
	}

	@Override
	public Object visitMethodInvocation(MethodInvocationTree n, Context context) {
		Symbol method = (Symbol) TreeInfo.symbol((JCTree) n.getMethodSelect());	
		Expression target;
		if (n.getMethodSelect().toString().equals("System.out.println")) {
			context.getClassDef().addDependency(new SystemOutDependency());
			target = new SystemOutPrintCall();
		}
		else {
			target = (Expression) n.getMethodSelect().accept(this,
						context);
			if (n.getMethodSelect().toString().equals("super")) {
				target = new ParentCall(goolType(((MethodSymbol) method)
						.getReturnType(), context));
			} else if (n.getMethodSelect().toString().equals("this")) {
				target = new ThisCall(goolType(((MethodSymbol) method)
						.getReturnType(), context));
			}
	
			if (!(target instanceof Parameterizable)) {
				target = new MethCall(goolType(((MethodSymbol) method)
						.getReturnType(), context), target);
			}
		}
		addParameters(n.getArguments(), (Parameterizable) target, context);
		return target;
	}

	@Override
	public Object visitModifiers(ModifiersTree n, Context context) {
		Collection<Modifier> result = new HashSet<Modifier>();

		Set<javax.lang.model.element.Modifier> flags = n.getFlags();

		for (javax.lang.model.element.Modifier modifier : flags) {
			switch (modifier) {
			case PRIVATE:
				result.add(Modifier.PRIVATE);
				break;
			case PUBLIC:
				result.add(Modifier.PUBLIC);
				break;
			case PROTECTED:
				result.add(Modifier.PROTECTED);
				break;
			case STATIC:
				result.add(Modifier.STATIC);
				break;
			case ABSTRACT:
				result.add(Modifier.ABSTRACT);
				break;
			case FINAL:
				result.add(Modifier.FINAL);
				break;
			default:
				throw new IllegalStateException(error(
						"%s modifier not supported.", modifier));
			}
		}

		return result;
	}

	@Override
	public Object visitNewArray(NewArrayTree node, Context p) {
		List<Expression> initialiList = new ArrayList<Expression>();
		if (node.getInitializers() != null) {
			for (ExpressionTree expression : node.getInitializers()) {
				initialiList.add((Expression) expression.accept(this, p));
			}
		}
		List<Expression> dimesExpressions = new ArrayList<Expression>();
		if (node.getDimensions() != null) {

			for (ExpressionTree expression : node.getDimensions()) {
				dimesExpressions.add((Expression) expression.accept(this, p));
			}
		}
		return new ArrayNew(goolType(node.getType(), p), dimesExpressions,
				initialiList);
	}

	@Override
	public Object visitNewClass(NewClassTree n, Context context) {
		IType type = goolType(n.getIdentifier(), context);
		ClassNew c = new ClassNew(type);

		addParameters(n.getArguments(), c, context);

		return c;
	}

	@Override
	public Object visitOther(Tree node, Context p) {
		throw new IllegalStateException(error("Other is not supported."));

	}

	@Override
	public Object visitParameterizedType(ParameterizedTypeTree node,
			Context context) {
		return goolType(node, context);
	}

	@Override
	public Object visitParenthesized(ParenthesizedTree n, Context context) {
		return n.getExpression().accept(this, context);
	}

	@Override
	public Object visitPrimitiveType(PrimitiveTypeTree n, Context context) {
		return goolType(n.getPrimitiveTypeKind());
	}

	@Override
	public Object visitReturn(ReturnTree node, Context p) {
		return new Return((Expression) node.getExpression().accept(this, p));
	}

	@Override
	public Object visitSwitch(SwitchTree node, Context p) {
		throw new IllegalStateException(error("Switch is not supported"));
	}

	@Override
	public Object visitSynchronized(SynchronizedTree node, Context p) {
		throw new IllegalStateException(error("Synchronized is not supported"));
	}

	@Override
	public Object visitThrow(ThrowTree node, Context p) {
		throw new IllegalStateException(error("THROW is not supported"));
	}

	@Override
	public Object visitTry(TryTree node, Context p) {
		throw new IllegalStateException(error("TRY is not supported"));
	}

	@Override
	public Object visitTypeCast(TypeCastTree node, Context context) {
		return new CastExpression(goolType(node.getType(), context),
				(Expression) node.getExpression().accept(this, context));
	}

	@Override
	public Object visitTypeParameter(TypeParameterTree node, Context p) {
		throw new IllegalStateException(error("Generics is not supported"));
	}

	@Override
	public Object visitUnary(UnaryTree n, Context context) {
		Expression expression = (Expression) n.getExpression().accept(this,
				context);
		Operator operator = getOperator(n.getKind());

		return new UnaryOperation(operator, expression);
	}

	@Override
	public Object visitVariable(VariableTree n, Context context) {
		IType type = goolType(n.getType(), context);

		VarDeclaration variable = new VarDeclaration(type, n.getName()
				.toString());

		if (n.getInitializer() != null) {
			Expression initializer = (Expression) n.getInitializer().accept(
					this, context);
			variable.setInitialValue(initializer);
		}
		Collection<Modifier> modifiers = (Collection<Modifier>) n
				.getModifiers().accept(this, context);
		if (n.getType() instanceof MemberSelectTree || !modifiers.isEmpty()) {
			return new Field(modifiers, variable);
		}
		return variable;
	}

	@Override
	public Object visitWhileLoop(WhileLoopTree n, Context context) {
		return new While((Expression) n.getCondition().accept(this, context),
				(Statement) n.getStatement().accept(this, context));
	}

	@Override
	public Object visitWildcard(WildcardTree node, Context p) {
		throw new IllegalStateException(error("Wildcard is not supported"));
	}	
}