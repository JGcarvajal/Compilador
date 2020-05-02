package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class CicloWhile(var expRelacional: ExpresionRelacional, var listaSentencias: ArrayList<Sentencia>?, var interrupcion:Token?):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Ciclo While")
        raiz.children.add(expRelacional.getArbolVisual())

        var raizSentencias= TreeItem("Sentencias")

        if (interrupcion != null) {
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