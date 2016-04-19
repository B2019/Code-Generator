/*
 * @author Francis Poole
 * @version 1.0
 */

class CodegenImpl implements Codegen {
    public String codegenTask1 ( Program p ) throws CodegenException {
    	return (p.accept(new ProgramAssemblerVisitor()));
    } 
    public String codegenTask2 ( Program p ) throws CodegenException {
    	return (p.accept(new ProgramAssemblerVisitor()));
    }
    public String codegenTask3 ( Program p ) throws CodegenException {
    	return (p.accept(new ProgramAssemblerVisitor()));
	}
}