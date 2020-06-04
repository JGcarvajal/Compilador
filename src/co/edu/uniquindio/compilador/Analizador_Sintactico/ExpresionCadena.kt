package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.exp
import kotlin.test.expect

class ExpresionCadena(var cadena: Token, var expresion:Expresion?):Expresion() {
    override fun toString(): String {
        return "ExpresionCadena(cadena=$cadena)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Expesion Cadena")
        raiz.children.add(TreeItem("Cadena: ${cadena.lexema}"))
        if (expresion != null){
            raiz.children.addAll(expresion!!.getArbolVisual())
        }
        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, erroresSemanticos:ArrayList<Error>): String {
        return "string"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (expresion != null){
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = cadena.getJavaCode()

        if (expresion != null){
            codigo +="+" +expresion!!.getJavaCode()+";"
        }
        return codigo
    }
}