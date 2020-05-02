package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem
import java.util.*
import kotlin.collections.ArrayList

class CicloForeach(var lista:Token,var item:Token,  var tipoDato:Token,var listaSentencias:ArrayList<Sentencia>?, var interrupcion:Token?): Sentencia() {
    override fun toString(): String {
        return "CicloForeach(Lista=$lista, item=$item, tipoDato=$tipoDato, Sentencias$listaSentencias)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Ciclo Foreach")

        raiz.children.add(TreeItem("Lista: ${lista.lexema}"))
        raiz.children.add(TreeItem("Item: ${item.lexema}"))
        raiz.children.add(TreeItem("Tipo: ${tipoDato.lexema}"))

        var raizSentencias= TreeItem("Sentencias")

        if(listaSentencias != null) {
            for (s in listaSentencias!!) {
                raizSentencias.children.add(s.getArbolVisual())
            }
        }
        if (interrupcion != null){
            raiz.children.add(TreeItem("Break"))
        }
        raiz.children.add(raizSentencias)

        return raiz
    }
}