package gool.recognizer.cpp.ast.statement;

import gool.recognizer.cpp.visitor.DebugASTCpp;
import gool.recognizer.cpp.visitor.IVisitorASTCpp;
import gool.recognizer.cpp.visitor.DebugASTCpp.EASTstatu;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;

public class ASTCppReturnStatement extends ASTCppStatement {

	public ASTCppReturnStatement(IASTNode node) {
		super(node);
		setNode((IASTReturnStatement) node);
	}

	private IASTReturnStatement node ;
	
	
	public IASTReturnStatement getNode() {
		return node;
	}

	public void setNode(IASTReturnStatement node) {
		this.node = node;
	}
	
	@Override
	public Object accept(IVisitorASTCpp visitor, Object data) {
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.VISIT, "ASTCppReturnStatement",this);
		Object toReturn = visitor.visit(this, data);
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.LEAVE, "ASTCppReturnStatement",this);
		return toReturn;
	}
}
