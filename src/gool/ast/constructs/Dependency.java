package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;

public abstract class Dependency {
	private Package ppackage;

	public void setPpackage(Package ppackage) {
		this.ppackage = ppackage;
	}

	public Package getPpackage() {
		return ppackage;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Dependency) || !(obj instanceof String)) {
			return false;
		} else if (obj instanceof String) {
			return toString().equals(obj.toString());
		}
		return toString().equals(((Dependency) obj).toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public String getFullName() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public String getPackageName() {
		return ppackage == null ? "" : ppackage.getName();
	}
}
