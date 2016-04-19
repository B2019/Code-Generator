/*
 * @author Francis Poole
 * @version 1.0
 */

class CodegenException extends Exception {
    public String msg;
    public CodegenException ( String _msg ) { msg = _msg; } }

interface Codegen {
    public String codegenTask1 ( Program p ) throws CodegenException; 
    public String codegenTask2 ( Program p ) throws CodegenException; 
    public String codegenTask3 ( Program p ) throws CodegenException; }
