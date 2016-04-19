/*
 * @author Francis Poole
 * @version 1.0
 */

public class Label {

	private static int i = 0;

	private String label;

	public Label() {
		label = "Label_" + i++;	
	}
	
	public String toString() {
		return label;
	}
}
