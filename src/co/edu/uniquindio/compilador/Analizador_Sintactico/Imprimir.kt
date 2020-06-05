package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

class Imprimir(var expresion: Expresion):Sentencia() {

    override fun toString(): String {
        return "imprimir(expresion=$expresion)"
    }

    override fun getJavaCode(): String {
        return "JOptionPane.showMessageDialog(null,"+expresion.getJavaCode()+"); \n"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Imprimir")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }
}