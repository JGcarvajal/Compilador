package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
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


}