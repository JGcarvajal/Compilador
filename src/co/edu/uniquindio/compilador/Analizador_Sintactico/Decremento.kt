package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class Decremento(var nombre:Token,var operador:Token):Sentencia() {

    override fun toString(): String {
        return "Decremento(nombre=$nombre,operador=$operador)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Operacion de Decremento")
        raiz.children.add(TreeItem("nombre: ${nombre.lexema}"))
        return raiz
    }
}