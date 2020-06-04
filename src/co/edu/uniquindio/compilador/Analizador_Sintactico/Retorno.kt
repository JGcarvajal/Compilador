package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.exp

class Retorno(var expresion: Expresion?):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Retorno")
        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        }
        return raiz
    }

    override fun getJavaCode(): String {
       var codigo=""
        if (expresion != null) {
            codigo= "return "+expresion!!.getJavaCode()+";"
        }

        return codigo
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (expresion != null) {
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }


}