 import javax.swing.*; 
 public class Principal { 
public static void main (String[] args){ 
	String max=""; 

max= JOptionPane.showInputDialog(null, "Escriba un valor"); 
	 	imprimir (max); 
	 } 
	public static void imprimir(String max) { 
JOptionPane.showMessageDialog(null,max); 
	 } 
}