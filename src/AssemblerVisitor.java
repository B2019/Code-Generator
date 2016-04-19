import java.util.List;
import java.util.Stack;

/*
 * @author Francis Poole
 * @version 1.0
 */

class ProgramAssemblerVisitor implements ProgramVisitor {

	@Override
	public String visitProgram(List<Declaration> decls) {
		String code = "jal " + decls.get(0).id + "\n" +
				"j sysexit\n";
		for (Declaration decl : decls) {
			code = code + decl.accept(new DeclarationAssemblerVisitor());
		}
		return code + 
				"sysexit:\n" +
				"li $v0 10\n" +
				"syscall";
	}
}

class DeclarationAssemblerVisitor implements DeclarationVisitor {
	public String visitDeclaration(String id, int numOfArgs, Exp body) {
		int sizeAR = (2 + numOfArgs) * 4;
		return (id + ":\n" +
				"move $fp $sp\n" +
				"sw $ra 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				body.accept(new ExpAssemblerVisitor()) +
				"lw $ra 4($sp)\n" +
				"addiu $sp $sp " + sizeAR + "\n" +
				"lw $fp 0($sp)\n" +
				"jr $ra\n");
	}
}

class ExpAssemblerVisitor implements ExpVisitor {

	static Stack<Label> theStack = new Stack<Label>();
	
	public String visitIntLiteral(int n) {
		return("li $a0 " + n + "\n");
	}

	public String visitVariable(int x) {
		return("lw $a0 " + (x * 4) + "($fp)\n");
	}

	public String visitIf(Exp l, Comp comp, Exp r, Exp thenBody, Exp elseBody) {
		Label elseLabel = new Label();
		Label thenLabel = new Label();
		Label endIfLabel = new Label();
		return( l.accept(new ExpAssemblerVisitor()) +
				"sw $a0 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				r.accept(new ExpAssemblerVisitor()) +
				"lw $t1 4($sp)\n" +
				"addiu $sp $sp 4\n" +
				comp.accept(new CompAssemblerVisitor(thenLabel)) +
			elseLabel + ":\n" +
				elseBody.accept(new ExpAssemblerVisitor()) +
				"b " + endIfLabel + "\n" +
			thenLabel + ":\n" +
				thenBody.accept(new ExpAssemblerVisitor()) +
			endIfLabel + ":\n"
				);
	}

	public String visitBinexp(Exp l, Binop binop, Exp r) {
		return(	l.accept(new ExpAssemblerVisitor()) +
				"sw $a0 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				r.accept(new ExpAssemblerVisitor()) +
				"lw $t1 4($sp)\n" +
				binop.accept(new BinopAssemblerVisitor()) +
				"addiu $sp $sp 4\n");
	}

	public String visitInvoke(String name, List<Exp> args) {
		String s = "";
		for (Exp arg : args) {
			s = arg.accept(new ExpAssemblerVisitor()) +
					"sw $a0 0($sp)\n" +
					"addiu $sp $sp -4\n" +
					s;
		}
		return("sw $fp 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				s +
				"jal " + name + "\n"
				);
	}

	public String visitWhile(Exp l, Comp comp, Exp r, Exp body) {
		Label loopLabel = new Label();
		Label loopBodyLabel = new Label();
		Label loopEndLabel = new Label();
		theStack.push(loopLabel);
		theStack.push(loopEndLabel);
		String c = ( 
			loopLabel + ":\n" +
				l.accept(new ExpAssemblerVisitor()) +
				"sw $a0 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				r.accept(new ExpAssemblerVisitor()) +
				"lw $t1 4($sp)\n" +
				"addiu $sp $sp 4\n" +
				comp.accept(new CompAssemblerVisitor(loopBodyLabel)) +
				"b " + loopEndLabel + "\n" +
			loopBodyLabel + ":\n" +
				body.accept(new ExpAssemblerVisitor()) +
				"b " + loopLabel + "\n" +
			loopEndLabel + ":\n");
		theStack.pop();
		theStack.pop();
		return c;
	}

	public String visitRepeatUntil(Exp body, Exp l, Comp comp, Exp r) {
		Label loopBodyLabel = new Label();
		Label loopLabel = new Label();
		Label loopEndLabel = new Label();
		theStack.push(loopLabel);
		theStack.push(loopEndLabel);
		String c = ( 
				"j " + loopBodyLabel + "\n" +
			loopLabel + ":\n" +
				l.accept(new ExpAssemblerVisitor()) +
				"sw $a0 0($sp)\n" +
				"addiu $sp $sp -4\n" +
				r.accept(new ExpAssemblerVisitor()) +
				"lw $t1 4($sp)\n" +
				"addiu $sp $sp 4\n" +
				comp.accept(new CompAssemblerVisitor(loopEndLabel)) +
			loopBodyLabel + ":\n" +
				body.accept(new ExpAssemblerVisitor()) +
				"j " + loopLabel + "\n" +
			loopEndLabel + ":\n");
		theStack.pop();
		theStack.pop();
		return c;
	}

	public String visitAssign(int x, Exp e) {
		return(e.accept(new ExpAssemblerVisitor())
				+ "sw $a0 " + (x * 4) + "($fp)\n");
	}

	public String visitSeq(Exp l, Exp r) {
		return(l.accept(new ExpAssemblerVisitor())
				+ r.accept(new ExpAssemblerVisitor()));
	}

	public String visitSkip() {
		return("");
	}

	public String visitBreak() {
		Label label = theStack.peek();
		return("j " + label + "\n");
	}

	public String visitContinue() {
		Label labelTemp = theStack.pop();
		Label label = theStack.peek();
		theStack.push(labelTemp);
		return("j " + label + "\n");
	}
}


class CompAssemblerVisitor implements CompVisitor {

	Label endLabel;
	
	public CompAssemblerVisitor(Label _endLabel) {
		endLabel = _endLabel;
	}

	public String visitLess() {
		return("slt $a0 $t1 $a0\n" +
				"bnez $a0 " + endLabel + "\n");
	}

	public String visitLessEq() {
		return("slt $a0 $a0 $t1\n" +
				"beqz $a0 " + endLabel + "\n");
	}

	public String visitEquals() {
		return("beq $a0 $t1 " + endLabel + "\n");
	}

	public String visitGreater() {
		return("slt $a0 $a0 $t1\n" +
				"bnez $a0 " + endLabel + "\n");
	}

	public String visitGreaterEq() {
		return("slt $a0 $t1 $a0\n" +
				"beqz $a0 " + endLabel + "\n");
	}
}


class BinopAssemblerVisitor implements BinopVisitor {

	public String visitPlus() {
		return("add $a0 $a0 $t1\n");
	}

	public String visitMinus() {
		// TODO Auto-generated method stub
		return("sub $a0 $t1 $a0\n");
	}

	public String visitTimes() {
		// TODO Auto-generated method stub
		return("mult $a0 $t1\n" +
				"mflo $a0\n");
	}

	public String visitDiv() {
		// TODO Auto-generated method stub
		return("div $t1 $a0\n" +
				"mflo $a0\n");
	}
}