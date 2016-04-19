import java.util.List;

/*
 * @author Francis Poole
 * @version 1.0
 */

interface ProgramVisitor {
	String visitProgram(List<Declaration> decls);
}

class Program {
    public List <Declaration> decls;
    
    public Program ( List <Declaration> _decls ) { 
		assert ( _decls.size () > 0 ); 
		assert ( _decls.get ( 0 ).numOfArgs == 0 ); 
		decls = _decls; 
	}
    
    public String accept(ProgramVisitor v) {
		return v.visitProgram(decls);		
	}
}



interface DeclarationVisitor {
	String visitDeclaration(String id, int numOfArgs, Exp body);
}


class Declaration {
    public String id;
    public int numOfArgs;
    public Exp body;

    public Declaration ( String _id, int _numOfargs, Exp _body ) { 
		assert ( _numOfargs >= 0 );
		id = _id;
		numOfArgs = _numOfargs;
		body = _body; 
	} 
    
    public String accept(DeclarationVisitor v) {
		return v.visitDeclaration(id, numOfArgs, body);		
	} 
}




interface ExpVisitor {
	public abstract String visitIntLiteral( int _n);
	public abstract String visitVariable(int x);
	public abstract String visitIf(Exp l, Comp comp, Exp r, Exp thenBody,
			Exp elseBody);
	public abstract String visitBinexp(Exp l, Binop binop, Exp r);
	public abstract String visitInvoke(String name, List<Exp> args);
	public abstract String visitWhile(Exp l, Comp comp, Exp r, Exp body);
	public abstract String visitRepeatUntil(Exp body, Exp l, Comp comp, Exp r);
	public abstract String visitAssign(int x, Exp e);
	public abstract String visitSeq(Exp l, Exp r);
	public abstract String visitSkip();
	public abstract String visitBreak();
	public abstract String visitContinue();
}


abstract class Exp {
	public abstract String accept(ExpVisitor v);
}





class IntLiteral extends Exp {
    public int n;
    IntLiteral ( int _n ) {
    	n = _n; 
	}

	public String accept(ExpVisitor v) {
		return v.visitIntLiteral(n);
	} 
}


class Variable extends Exp {
    public int x;
    
    public Variable ( int _x ) { 
		assert ( _x > 0 ); 
		x = _x; 
	}

	public String accept(ExpVisitor v) {
		return v.visitVariable(x);		
	} 
}


class If extends Exp {
    public Exp l;
    public Comp comp;
    public Exp r;
    public Exp thenBody;
    public Exp elseBody;
    
    public If ( Exp _l, Comp _comp, Exp _r, Exp _thenBody, Exp _elseBody ) {
        l = _l;
		comp = _comp;
		r = _r;
		thenBody = _thenBody;
		elseBody = _elseBody;
	}

	public String accept(ExpVisitor v) {
		return v.visitIf(l, comp, r, thenBody, elseBody);		
	} 
}


class Binexp extends Exp {
    public Exp l;
    public Binop binop;
    public Exp r;
    
    public Binexp ( Exp _l, Binop _binop, Exp _r ) {
		l = _l;
		binop = _binop; 
		r = _r; 
	}
    
	public String accept(ExpVisitor v) {
		return v.visitBinexp(l, binop, r);
	} 
}


class Invoke extends Exp {
    public String name;
    public List<Exp> args;
    
    public Invoke ( String _name, List<Exp> _args ) {
		name = _name;
		args = _args; 
	}

	public String accept(ExpVisitor v) {
		return v.visitInvoke(name, args);
	} 
}


class While extends Exp {
    public Exp l;
    public Comp comp;
    public Exp r;
    public Exp body;
    
    public While ( Exp _l, Comp _comp, Exp _r, Exp _body ) {
        l = _l;
        comp = _comp;
        r = _r;
        body = _body;
    }

	public String accept(ExpVisitor v) {
		return v.visitWhile(l, comp, r, body);
	}
}


class RepeatUntil extends Exp {
    public Exp body;
    public Exp l;
    public Comp comp;
    public Exp r;
    
    public RepeatUntil ( Exp _body, Exp _l, Comp _comp, Exp _r ) {
    	body = _body; 
        l = _l;
        comp = _comp;
        r = _r;
    }
    
	public String accept(ExpVisitor v) {
		return v.visitRepeatUntil(body, l, comp, r);
	}	
}


class Assign extends Exp {
    public int x;
    public Exp e;
    
    public Assign ( int _x, Exp _e ) {
		assert ( _x > 0 );
		x = _x;
		e = _e;
	}

	public String accept(ExpVisitor v) {
		return v.visitAssign(x, e);
	}
}


class Seq extends Exp {
    public Exp l;
    public Exp r;
    
    public Seq ( Exp _l, Exp _r ) {
		l = _l;
		r = _r;
	}
    
	public String accept(ExpVisitor v) {
		return v.visitSeq(l, r);	
	}
}


class Skip extends Exp {
    public Skip () {}
    
	public String accept(ExpVisitor v) {
		return v.visitSkip();
	}
}

class Break extends Exp {
    public Break () {} 
    
	public String accept(ExpVisitor v) {
		return v.visitBreak();
	}
}

class Continue extends Exp {
    public Continue () {}
    
	public String accept(ExpVisitor v) {
		return v.visitContinue();
	}
}


///////////////////////////////////////////////////////////////////////
//Comp

interface CompVisitor {

	String visitLess();

	String visitLessEq();

	String visitEquals();

	String visitGreater();

	String visitGreaterEq();
}


abstract class Comp {
	public abstract String accept(CompVisitor v);
}





class Less extends Comp {
    public Less () {}

	public String accept(CompVisitor v) {
		return v.visitLess();
	}
}

class LessEq extends Comp {
    public LessEq () {} 
	
    public String accept(CompVisitor v) {
    	return v.visitLessEq();	
	}    
}

class Equals extends Comp {
    public Equals () {} 

	public String accept(CompVisitor v) {
		return v.visitEquals();
	}
}

class Greater extends Comp {
    public Greater () {} 
    
	public String accept(CompVisitor v) {
		return v.visitGreater();
	}
}

class GreaterEq extends Comp {
    public GreaterEq () {} 
    
	public String accept(CompVisitor v) {
		return v.visitGreaterEq();	
	}
}


///////////////////////////////////////////////////////////////////////
//Binop

interface BinopVisitor {

	String visitPlus();

	String visitMinus();

	String visitTimes();

	String visitDiv();
}


abstract class Binop {
	public abstract String accept(BinopVisitor v);
}




class Plus extends Binop {
    public Plus () {} 
    
	public String accept(BinopVisitor v) {
		return v.visitPlus();	
	}
}

class Minus extends Binop {
    public Minus () {}  
    
	public String accept(BinopVisitor v) {
		return v.visitMinus();	
	}
}

class Times extends Binop {
    public Times () {}  
    
	public String accept(BinopVisitor v) {
		return v.visitTimes();	
	}
}

class Div extends Binop {
    public Div () {}  
    
	public String accept(BinopVisitor v) {
		return v.visitDiv();	
	}
}