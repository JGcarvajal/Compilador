package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.Simbolo
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Leer(var variable:Token):Sentencia() {
private var simbolo:Simbolo?= null
        override fun getArbolVisual(): TreeItem<String> {
            var raiz= TreeItem<String>("Leer")
            raiz.children.add(TreeItem("Variable: ${variable.lexema}"))
            return raiz
        }

    override fun toString(): String {
        return "Leer(variable=$variable)"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        simbolo=tablaSimbolos.buscarSimboloValor(variable.lexema, ambito)

        if (simbolo == null){
            erroresSemanticos.add(Error("La variable ${variable.lexema} no existe dentro del ambito $ambito", variable.fila, variable.columna,""))
        }
    }

    override fun getJavaCode(): String {
        var codigo=""
if (simbolo != null) {
    when (simbolo!!.tipo) {
        "int" -> codigo= variable.getJavaCode() + "= Integer.parseInt(JOptionPane.showInputDialog(null, \"Escriba un valor\")); \n"
        "demimal" -> codigo= variable.getJavaCode() + "= Double.parseDouble(JOptionPane.showInputDialog(null, \"Escriba un valor\")); \n"
        "bool" -> codigo= variable.getJavaCode() + "= Boolean.parseBoolean(JOptionPane.showInputDialog(null, \"Escriba un valor\")); \n"
        else -> { // Note the block
             variable.getJavaCode() + "= JOptionPane.showInputDialog(null, \"Escriba un valor\"); \n"
        }
    }
}
        return codigo
    }
}
