/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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





package gool.ast.constructs;

import gool.ast.type.IType;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * This interface accounts for all declarations in abstract GOOL.
 * Because declarations are usually considered as statements in OO languages, 
 * so we leave it to extend Statement.
 */
public abstract class Dec extends Statement {
	private static final List<Modifier> ACCESS_MODIFIERS = Arrays.asList(Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED);
	/**
	 * The list of modifiers.
	 */
	private Collection<Modifier> modifiers = new HashSet<Modifier>();
	private IType type;
	private String name;
	
	public Dec(IType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public IType getType() {
		return type;
	}
	
	public void setType(IType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<Modifier> getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(Collection<Modifier> modifiers) {
		this.modifiers = modifiers;
	}
	
	public void addModifier(Modifier modifier) {
		this.modifiers.add(modifier);
	}
	
	public void addModifiers(Collection<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
	}
	
	public Modifier getAccessModifier() {
		// make private if it does not have any of the allowed modifiers.
		Modifier modifier = Modifier.PRIVATE;
		
		for (Modifier m : modifiers) {
			if (ACCESS_MODIFIERS.contains(m)) {
				modifier = m;
				break;
			}
		}
		
		return modifier;
	}
}
