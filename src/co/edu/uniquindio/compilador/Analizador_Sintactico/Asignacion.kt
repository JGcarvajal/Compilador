package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class Asignacion(var nombre:Token,var operador:Token, var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Asignacion(nombre=$nombre, operador=$operador expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Asignacion")
        raiz.children.add(TreeItem("Nombre: ${nombre.lexema}"))
        raiz.children.add(TreeItem("Operador: ${operador.lexema}"))
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }
}