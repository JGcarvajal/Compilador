package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class ExpresionCadena(var cadena: Token):Expresion() {
    override fun toString(): String {
        return "ExpresionCadena(cadena=$cadena)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Expesion Cadena")
        raiz.children.add(TreeItem("Cadena: ${cadena.lexema}"))

        return raiz
    }
}