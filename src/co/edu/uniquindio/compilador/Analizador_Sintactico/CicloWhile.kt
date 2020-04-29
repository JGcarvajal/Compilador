package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

class CicloWhile(var expRelacional: ExpresionRelacional, var listaSentencias: ArrayList<Sentencia>):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Ciclo While")
        raiz.children.add(expRelacional.getArbolVisual())

        var raizSentencias= TreeItem("Sentencias")

        for(s in listaSentencias){
            raizSentencias.children.add(s.getArbolVisual())
        }
        raiz.children.add(raizSentencias)
        return raiz
    }
}