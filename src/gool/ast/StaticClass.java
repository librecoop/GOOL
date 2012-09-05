package gool.ast;

import gool.platform.Platform;

public class StaticClass extends ClassDef {

	public StaticClass(Modifier modifier, String name, Platform platform) {
		super(modifier, name, platform);
		addModifier(Modifier.STATIC);
	}

	public void addMethod(Meth method){
		super.addMethod(method);
		method.addModifier(Modifier.STATIC);
	}
	
	public void addField(Field field){
		super.addField(field);
		field.addModifier(Modifier.STATIC);
	}
}
