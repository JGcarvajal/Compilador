package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

/**
 ** Esta es la clase que nos permite representar un parametro dentro del lenguaje propuesto
 */
class Parametro (var tipoDato:Token, var nombre:Token) {
    override fun toString(): String {
        return "Parametro(tipoDato=$tipoDato, nombre=$nombre), \n"
    }

    fun getArbolVisual(): TreeItem<String> {

        return TreeItem("${nombre.lexema} : ${tipoDato.lexema}")
    }

    fun getJavaCode():String{
        var codigo =""+tipoDato.getJavaCode()+" "+nombre.getJavaCode()

        return codigo
    }
}