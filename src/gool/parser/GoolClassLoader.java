package gool.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import gool.ast.type.IType;

class GoolClassLoader extends ClassLoader {
	private Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
	
	public Class<?> loadClass(String name, byte[] classBytes) {
		Class<?> c = classCache.get(name);
		if (c == null) {
			c = defineClass(null, classBytes, 0, classBytes.length);
			classCache.put(name, c);
		}
		return c;
	}

	public void ensureMethodExists(IType type, String identifier) {
		if (type.isReferenceType()) {
			Class<?>[] parameterTypes = null;
			
			if (identifier.equals("toString")) {
				// do nothing
			} else if (identifier.equals("equals")) {
				parameterTypes = new Class<?>[]{Object.class};
			} else {
				return;
			}
			
			Class<?> c = classCache.get(type.getName());
			
			if (c == null) {
				throw new IllegalStateException(String.format("The target type %s does not contain a method definition %s.", type, identifier));
			}
			try {
				Method method = c.getMethod(identifier, parameterTypes);
				
				if (!method.getDeclaringClass().equals(Object.class)) {
					throw new IllegalStateException(String.format("The target type %s does not contain a method definition %s.", type, identifier));
				}
			} catch (Exception e) {
				throw new IllegalStateException(String.format("The target type %s does not contain a method definition %s.", type, identifier), e);
			}
		}
	}
}