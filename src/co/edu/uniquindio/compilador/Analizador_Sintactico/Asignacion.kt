package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class Asignacion(var nombre:Token, operador:Token, var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Asignacion(nombre=$nombre, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {

        return TreeItem("Asignacion")
    }
}