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

import gool.ast.type.IType;
import gool.ast.type.TypeClass;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;
import logger.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This captures classes in the intermediate language. For each object member of
 * ClassDef the compiler will have to generate a separate file containing the
 * code of the class in the target language.
 */
public class ClassDef extends Dependency {

	/**
	 * Class' dependencies (imports).
	 */
	private List<Dependency> dependencies = new ArrayList<Dependency>();

	/**
	 * Class' modifiers.
	 */
	private Collection<Modifier> modifiers = new HashSet<Modifier>();
	/**
	 * Class' name.
	 */
	private String name;

	/**
	 * List of fields.
	 */
	private List<Field> fields = new ArrayList<Field>();
	/**
	 * List of methods.
	 */
	private List<Meth> methods = new ArrayList<Meth>();
	/**
	 * List of implemented interfaces.
	 */
	private List<IType> interfaces = new ArrayList<IType>();
	/**
	 * Parent class.
	 */
	private IType parentClass;

	/**
	 * The class' type.
	 */
	private IType classType;

	/**
	 * The destination platform.
	 */
	private Platform platform;

	/**
	 * Flag to know if it is an interface.
	 */
	private boolean isInterface;

	/**
	 * Flag to know if it is the main class.
	 */
	private boolean isMainClass;
	
	/**
	 * Flag to know if it is a gool library class.
	 */
	private boolean isGoolLibraryClass;

	/**
	 * The package used by the class.
	 */
	private Package ppackage;

	/**
	 * The constructor of a "class definition".
	 * @param modifier
	 * 		: The modifier used by the class.
	 * @param name
	 * 		: The name of the class.
	 */
	public ClassDef(Modifier modifier, String name) {
		this.name = name;
		addModifier(modifier);
	}
	
	/**
	 * The constructor of a "class definition".
	 * @param modifier
	 * 		: The modifier used by the class.
	 * @param name
	 * 		: The name of the class.
	 * @param platform
	 * 		: The destination platform.
	 */
	public ClassDef(Modifier modifier, String name, Platform platform) {
		this(name, platform);
		addModifier(modifier);
	}

	/**
	 * The constructor of a "class definition".
	 * @param name
	 * 		: The name of the class.
	 */
	public ClassDef(String name) {
		this.name = name;
		setType(new TypeClass(name));
	}

	/**
	 * The constructor of a "class definition".
	 * @param name
	 * 		: The name of the class.
	 * @param platform
	 * 		: The destination platform.
	 */
	public ClassDef(String name, Platform platform) {
		this(name);
		this.platform = platform;
	}

	/**
	 * Adds a new class modifier.
	 * 
	 * @param modifier
	 *            the modifier to be added.
	 */
	public final void addModifier(Modifier modifier) {
		this.modifiers.add(modifier);
	}

	/**
	 * Appends the specified method to the list of methods.
	 * 
	 * @param method
	 *            method to be appended.
	 */
	public void addMethod(Meth method) {
		if (method instanceof Constructor) {
			method.setName(getName());
		} else if (method instanceof MainMeth) {
			setMainClass(true);
		}
		method.setClassDef(this);
		methods.add(method);
	}

	/**
	 * Appends the specified field to the list of fields.
	 * 
	 * @param field
	 *            field to be appended.
	 */
	public void addField(Field field) {
		fields.add(field);
	}

	/**
	 * Initializes the default constructor by using a list of undeclared
	 * variables.
	 * 
	 * @param freeVars
	 *            list of undeclared variables that are used inside the class.
	 * @return the default constructor.
	 */
	public Constructor createDefaultConstructor() {
		Constructor constructor = new Constructor();
		for (Field field : getFields()) {
			constructor.addParameter(new VarDeclaration(field));
			Assign assign = new Assign(new FieldAccess(field.getType(),
					new This(getType()), field.getName()), new VarAccess(field));
			constructor.addStatement(assign);
		}
		addMethod(constructor);

		return constructor;
	}

	/**
	 * Initializes the class' fields and adds a default constructor initializing
	 * those fields.
	 * 
	 * @param parameters
	 *            the list of undeclared variables that are used in the class.
	 * @return the default constructor.
	 */
	public void addFields(Collection<Dec> parameters) {
		for (Dec freeVar : parameters) {
			addField(new Field(Modifier.PRIVATE, freeVar.getName(),
					freeVar.getType()));
		}
	}

	/**
	 * Sets the package used by the class.
	 * @param ppackage
	 * 		: The new package used by the class.
	 */
	public void setPpackage(Package ppackage) {
		this.ppackage = ppackage;
	}

	/**
	 * Gets the package used by the class.
	 * @return
	 * 		The package used by the class.
	 */
	public Package getPpackage() {
		return ppackage;
	}

	/**
	 * Gets the name of the package used by the class.
	 * @return
	 * 		The name of the package used by the class.
	 */
	public String getPackageName() {
		return ppackage == null ? "" : ppackage.getName();
	}

	/**
	 * Gets the list of modifiers.
	 * 
	 * @return a list of modifiers.
	 */
	public Collection<Modifier> getModifiers() {
		return modifiers;
	}

	/**
	 * Gets the class' name.
	 * 
	 * @return the class' name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name of the class.
	 * @param name
	 * 		: The new name of the class.
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the list of the constructors defined in the class.
	 * @return
	 * 		The list of the constructors defined in the class.
	 */
	public List<Constructor> getConstructors() {
		List<Constructor> constructors = new ArrayList<Constructor>();
		for (Meth m : methods) {
			if (m instanceof Constructor) {
				constructors.add((Constructor) m);
			}
		}
		return constructors;
	}

	/**
	 * Determines if the class is an interface.
	 * @return
	 * 		True if the class is an interface, else false.
	 */
	public final boolean isInterface() {
		return isInterface;
	}

	/**
	 * Sets the flag to know if the class is an interface.
	 * @param isInterface
	 *   	True if the class is an interface, else false.
	 */
	public final void setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	/**
	 * Gets the platform.
	 * 
	 * @return
	 */
	public final Platform getPlatform() {
		return platform;
	}

	/**
	 * Assigns the platform of the current class.
	 * 
	 * @param platform
	 *            the platform to be assigned.
	 */
	public final void setPlatform(Platform platform) {
		this.platform = platform;
	}

	/**
	 * Determines if the class is the main class.
	 * @return
	 * 		True if the class is the main class, else false.
	 */
	public final boolean isMainClass() {
		return isMainClass;
	}

	/**
	 * Sets the flag to know if the class is the main class.
	 * @param isMainClass
	 * 		True if the class is the main class, else false.
	 */
	public final void setMainClass(boolean isMainClass) {
		this.isMainClass = isMainClass;
	}

	/**
	 * Gets the list of fields.
	 * 
	 * @return the list of fields.
	 */
	public final List<Field> getFields() {
		return fields;
	}

	/**
	 * Gets the list of methods defined in the class.
	 * 
	 * @return the list of methods.
	 */
	public final List<Meth> getMethods() {
		return methods;
	}

	/**
	 * Adds a new interface that should be implemented by the current class.
	 * 
	 * @param type
	 *            the type of the interface.
	 */
	public final void addInterface(IType type) {
		interfaces.add(type);
	}

	/**
	 * Sets the class to inherit from.
	 * 
	 * @param parentClass
	 *            the parent class.
	 */
	public final void setParentClass(IType parentClass) {
		this.parentClass = parentClass;
	}

	/**
	 * Gets the parent class.
	 * 
	 * @return the parent class.
	 */
	public final IType getParentClass() {
		return parentClass;
	}

	/**
	 * Gets the list of interfaces implemented by the class.
	 * 
	 * @return a list of interfaces implemented by the class.
	 */
	public final List<IType> getInterfaces() {
		return interfaces;
	}

	/**
	 * Generates the target code using the specific CodePrinter related to the
	 * class' platform.
	 * 
	 * Instead of concatenating strings, the code generation is implemented
	 * using velocity templates, unless the generator implements the interface
	 * CodeGeneratorNoVelocity.
	 * 
	 * @return the generated target code.
	 * @throws Exception
	 *             when velocity fails to render or find the relevant template.
	 */
	public String getCode() {
		CodePrinter printer = getPlatform().getCodePrinter();
		if (printer.getCodeGenerator() instanceof CodeGeneratorNoVelocity)
			return ((CodeGeneratorNoVelocity) printer.getCodeGenerator())
					.printClass(this);
		else
			return printer.processTemplate("class.vm", this);
	}
	
	/**
	 * Adds a field to the "class" representation.
	 * @param fieldName
	 * 		: The name of the "field" to add.
	 * @param type
	 * 		: The type of the "field" to add.
	 */
	public final void addField(String fieldName, IType type) {
		fields.add(new Field(Modifier.PRIVATE, fieldName, type));
	}

	/**
	 * Gets the type of the class definition.
	 * @return
	 * 		The class' type.
	 */
	public final IType getType() {
		return classType;
	}

	/**
	 * Adds a dependency to the list of dependencies in the class definition.
	 * @param dependency
	 * 		: The dependency to add.
	 */
	public final void addDependency(Dependency dependency) {
		if ((dependency instanceof TypeDependency)
				&& (((TypeDependency) dependency).getType() instanceof TypeClass)
				&& ((TypeClass) ((TypeDependency) dependency).getType())
						.getClassDef() != null
				&& ((TypeClass) ((TypeDependency) dependency).getType())
						.getClassDef().getPlatform() != null
				&& (!((TypeClass) ((TypeDependency) dependency).getType())
						.getClassDef().getPlatform().equals(getPlatform()))) {
			throw new IllegalArgumentException(
					"There should not be dependencies between classes of different platforms.");
		}

		if (dependency != null && !dependencies.contains(dependency)) {
			dependencies.add(0, dependency);
		}
	}

	/**
	 * Gets the list of dependencies in the class definition.
	 * @return
	 * 		The list of dependencies in the class definition.
	 */
	public final List<Dependency> getDependencies() {
		return dependencies;
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}

	/**
	 * Gets a method defined in the class.
	 * @param methName
	 * 		: The name of the method to get.
	 * @return
	 * 		The method definition if exists, else return null.
	 */
	public final Meth getMethod(String methName) {
		for (Meth method : methods) {
			if (method.getName().equals(methName)) {
				return method;
			}
		}
		;
		return null;
	}

	/**
	 * Sets the class modifiers.
	 * @param modifiers
	 * 		: The new list of modifiers used by the class.
	 */
	public final void setModifiers(Collection<Modifier> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Adds a list of dependencies to the list of dependencies 
	 * in the class definition.
	 * @param dependencies
	 * 		: The list of dependencies to be added.
	 */
	public final void addDependencies(List<Dependency> dependencies) {
		for (Dependency dependency : dependencies) {
			addDependency(dependency);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClassDef)) {
			return false;
		}
		return ((ClassDef) obj).getType().equals(getType());
	}

	@Override
	public int hashCode() {
		return getType().getName().hashCode();
	}

	/**
	 * Sets the type of the class.
	 * @param type
	 * 		: The new class' type.
	 */
	public final void setType(IType type) {
		classType = type;
		if (type instanceof TypeClass) {
			((TypeClass) classType).setClassDef(this);
		}
	}

	/**
	 * Sets the flag to know if the class is an "enum" class.
	 * @param isEnum
	 * 		: True if the class is an "enum" class, else false.
	 */
	public void setIsEnum(boolean isEnum) {
		if (classType instanceof TypeClass) {
			((TypeClass) classType).setIsEnum(isEnum);
		}
	}

	/**
	 * Determines if the class is an "enum" class.
	 * @return
	 * 		True if the class is an "enum" class, else false.
	 */
	public boolean isEnum() {
		return (classType instanceof TypeClass)
				&& ((TypeClass) classType).isEnum();
	}
	
	/**
	 * Sets the flag to know if it is a gool library class.
	 * @param isGoolLibraryClass
	 * 		: True if the it is a gool library class, else false.
	 */
	public void setIsGoolLibraryClass(boolean isGoolLibraryClass) {
		this.isGoolLibraryClass = isGoolLibraryClass;
	}

	/**
	 * Determines if it is a gool library class.
	 * @return
	 * 		True if the it is a gool library class, else false.
	 */
	public boolean isGoolLibraryClass() {
		return this.isGoolLibraryClass;
	}
}
