import javax.swing.*;
public class Principal {
	public static void main (String[] args){
		int n1=0;

		int n2=0;

		int var=0;

		var= Integer.parseInt(JOptionPane.showInputDialog(null, "Escriba un valor"));
		imprimir (var);
		ejemplo ();
	}
	public static int resultado(int n1, int n2) {
		int resultado=0;

		if (((n1+1)) >= (n2) && (n2) > (0)) {
			resultado=(n1+n2);
		}
		else {
			resultado=(n1*n2);
		}
		return resultado;	 }
	public static void imprimir(int var) {
		int i=0;

		while (i <= var) {
			JOptionPane.showMessageDialog(null,i);
			i++;}
	}
	public static void ejemplo() {
		int contar=1;

		try {
			while (contar == 1) {
				contar--;
				JOptionPane.showMessageDialog(null,"Salio error pero solo necesitamos un 2");
				break;}
		}
		catch ( Exception exc) {
		}
	}
}