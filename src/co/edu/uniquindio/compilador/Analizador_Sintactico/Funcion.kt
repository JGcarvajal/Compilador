package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

/**
 * Esta es la clase que nos permite representar uan funcion dentro del lenguaje propuesto
 */
class Funcion (var nombreFuncion:Token, var tipoRetorno: Token, var listaParametros: ArrayList<Parametro>, var listaSentencias: ArrayList<Sentencia>) {
    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros, listaSentencias=$listaSentencias), \n"
    }

    fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Funcion")

        raiz.children.add(TreeItem("Nombre: "+nombreFuncion.lexema))
        raiz.children.add(TreeItem("Tipo Retorno: "+tipoRetorno.lexema))

        var raizParametros= TreeItem("Parametros")
        for(p in listaParametros){
            raizParametros.children.add(p.getArbolVisual())
        }
        raiz.children.add(raizParametros)

        var raizSentencias= TreeItem("Sentencias")
        for(s in listaSentencias){
            raizSentencias.children.add(s.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        return raiz
    }
}