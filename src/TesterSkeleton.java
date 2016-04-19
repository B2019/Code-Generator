import java.util.List;
import java.util.ArrayList;
import static java.util.Arrays.asList;

/*
 * @author Francis Poole
 * @version 1.0
 */

class Tester {

    public void run () {
	try {
	    Codegen codegen = new CodegenImpl ();
	    ArrayList<Exp> list = new ArrayList<Exp>();
	    list.add(new IntLiteral(15));
	    list.add(new IntLiteral(5));
	    Declaration main = new Declaration ("main", 0,
	    		
	    		new Invoke("g", list)
	    		);
	    
	    Declaration decl1 = new Declaration ("g", 2,
	    		new Seq(new Assign(1, new IntLiteral(0)),
	    				new Seq( new While( new Variable(1), new Less(), new IntLiteral(10), 
	    							new Seq(new If(new Variable(1), new Equals(), new IntLiteral(4), new Continue(), new Skip()),
	    									new Assign(1, new Binexp(new Variable(1), new Plus(),new IntLiteral(1)))
	    							)
	    				), new Variable(1))
	    		)
	    );
	    
	    
	    
	    
	    Declaration decl2 = new Declaration ( "f", 
						 3,
						 new If ( new IntLiteral ( 5 ),
							  new Equals (),
							  new IntLiteral ( 6 ),
							  new Binexp(new IntLiteral(0), new Plus(), new IntLiteral(10)),
							  new Binexp(new IntLiteral(0), new Plus(), new IntLiteral(1))
						 )
	    );
	    
	    Program p = new Program ( new ArrayList<Declaration> ( asList (main, decl1 ) ) );
	    System.out.println( codegen.codegenTask1 ( p ));
	    //String result2 = codegen.codegenTask2 ( p );
	    //String result3 = codegen.codegenTask3 ( p );
	    CodegenException codegenException = new CodegenException ( "msg" );	    
	    /*String tmp = decl1.id;
	    int n = decl1.numOfArgs;
	    Exp exp = decl1.body;
	    IntLiteral intLiteral = new IntLiteral ( 5 );
	    n = intLiteral.n;
	    Variable variable = new Variable ( 7 ); 
	    n = variable.x;
	    Comp comp = new Equals ();
	    If ifAST = new If ( intLiteral, comp, variable, intLiteral, variable );
	    exp = ifAST.l;
	    comp = ifAST.comp;
	    exp = ifAST.r;
	    exp = ifAST.thenBody;
	    exp = ifAST.elseBody;
	    Binop binop = new Plus ();
	    Binexp binexp = new Binexp ( variable, binop, intLiteral );
	    exp = binexp.l;
	    comp = ifAST.comp;
	    exp = binexp.r;
	    Invoke invoke = new Invoke ( "name", new ArrayList<Exp> () );
	    tmp = invoke.name;
	    List<Exp> l = invoke.args; 
	    While whileAST = new While ( intLiteral, comp, variable, binexp );
	    exp = whileAST.l;
	    comp = whileAST.comp;
	    exp = whileAST.r;
	    exp = whileAST.body;
	    RepeatUntil repeatAST = new RepeatUntil ( binexp, intLiteral, comp, variable );
	    exp = repeatAST.l;
	    comp = repeatAST.comp;
	    exp = repeatAST.r;
	    exp = repeatAST.body;
	    Assign assign = new Assign ( 7, exp );
	    int x = assign.x;
	    exp = assign.e;
	    Seq seq = new Seq ( exp, exp );
	    exp = seq.l;
	    exp = seq.r;
	    Break breakAST = new Break ();
	    Continue continueAST = new Continue ();
	    comp = new Less ();
	    comp = new LessEq ();
	    comp = new Equals ();
	    comp = new Greater ();
	    comp = new GreaterEq ();
	    binop = new Plus ();
	    binop = new Minus ();
	    binop = new Times ();
	    binop = new Div ();*/
	} 
	catch ( CodegenException e ) {
	    String tmp = e.msg; 
	    System.out.println(tmp);} }

}

class Main {

    public static void main ( String [] args ) {
	Tester tester = new Tester (); 
	tester.run (); } 

}