package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionRelacional(var expresion1:Expresion,var operadorRelacional:Token,var expresion2:Expresion ):Expresion() {


    override fun toString(): String {
        return "ExpresionRelacional(expresion1=$expresion1, operadorRelacional=$operadorRelacional, expresion2=$expresion2)"
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Expresion Relacional")
        raiz.children.add(expresion1.getArbolVisual())
        raiz.children.add(TreeItem("Op Relacional: ${operadorRelacional.lexema}"))
        raiz.children.add(expresion2.getArbolVisual())
        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, erroresSemanticos:ArrayList<Error>): String {
        return "bool"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        expresion1.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        expresion2.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    override fun getJavaCode(): String {
        var codigo = expresion1.getJavaCode()+" "+operadorRelacional.getJavaCode()+ " "+expresion2.getJavaCode()

       return codigo
    }
}