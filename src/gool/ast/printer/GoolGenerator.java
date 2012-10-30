package gool.ast.printer;

import gool.ast.Dependency;
import gool.ast.gool.CustomDependency;
import gool.ast.gool.SystemOutPrintCall;
import gool.ast.gool.TypeDependency;
import gool.ast.list.ListRemoveAtCall;
import gool.ast.map.MapContainsKeyCall;
import gool.ast.map.MapEntryGetKeyCall;
import gool.ast.map.MapEntryGetValueCall;
import gool.ast.map.MapGetCall;
import gool.ast.map.MapGetIteratorCall;
import gool.ast.map.MapIsEmptyCall;
import gool.ast.map.MapPutCall;
import gool.ast.map.MapRemoveCall;
import gool.ast.map.MapSizeCall;
import gool.ast.type.IType;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.generator.java.JavaGenerator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class GoolGenerator extends JavaGenerator {
	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	@Override
	public void addCustomDependency(String key, Dependency value){
		customDependencies.put(key, value);
	}


	@Override
	public String getCode(CustomDependency customDependency) {
		return null;//customDependency.getFullName();
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.removeAt(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s.containsKey(%s)", mapContainsKeyCall
				.getExpression(), StringUtils.join(mapContainsKeyCall
				.getParameters(), ", "));
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String.format("%s.getKey()", mapEntryGetKeyCall
				.getExpression());
	}
	
	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		return String.format("%s.getValue()", mapEntryGetKeyCall
				.getExpression());
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("%s.get(%s)", mapGetCall.getExpression(),
				StringUtils.join(mapGetCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		return String.format("%s.getIterator()", mapGetIteratorCall
				.getExpression());
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		return String.format("(%s.isEmpty()", mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		return String.format("%s.put(%s)", mapPutCall.getExpression(),
				StringUtils.join(mapPutCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("%s.remove(%s)", mapRemoveCall.getExpression(),
				StringUtils.join(mapRemoveCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("%s.size()", mapSizeCall.getExpression());
	}
	
	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		return String.format("System.out.println(%s)", StringUtils.join(
				systemOutPrintCall.getParameters(), ","));
	}


	@Override
	public String getCode(TypeDecimal typeReal) {
		return "Double";
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			return "Gool.List";
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "Gool.Map";
		}
		return super.getCode(typeDependency);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("Map.Entry<%s, %s>",typeEntry.getKeyType(), typeEntry.getElementType());
	}

	@Override
	public String getCode(TypeList typeList) {
		IType elementType = typeList.getElementType();
		if (elementType != null) {
			return String.format("List<%s>", elementType);
		}
		return "List";
	}

	@Override
	public String getCode(TypeMap typeMap) {
		IType elementType = typeMap.getElementType();
		if (elementType != null) {
			return String.format("Map<%s, %s>", typeMap.getKeyType(), typeMap
					.getElementType());
		}
		return "Map";
	}
}