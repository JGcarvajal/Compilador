package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem


class TryCatch(var listaSentencias:ArrayList<Sentencia>?, var tipoException:Token, var nombreException: Token,
               var listaSentenciasException: ArrayList<Sentencia>?, var listaSentenciasFinally: ArrayList<Sentencia>?):Sentencia() {
    override fun toString(): String {
        return "TryCatch(listaFunciones=$listaSentencias, nombreException=$nombreException, Tipo Exception: $tipoException," +
                "sentencias Excepcion: $listaSentenciasException, sentencias Finally: $listaSentenciasFinally)"
    }

    override fun getArbolVisual():TreeItem<String>{
        var raiz=TreeItem("Try")
        var raizSentencias=TreeItem("Sentencias")
        if (listaSentencias != null){
        for (s:Sentencia in listaSentencias!!){
            raizSentencias.children.add( s.getArbolVisual())
        }
            raiz.children.add(raizSentencias)
        }

        var raizCatch=TreeItem("Catch")
        raizCatch.children.add(TreeItem("Nombre Excepcion: ${nombreException.lexema}, Tipo Excecion: ${tipoException.lexema }"))
        var raizSentenciasCatch=TreeItem("Sentencias")
        if (listaSentenciasException != null){
            for (s:Sentencia in listaSentenciasException!!){
                raizSentenciasCatch.children.add( s.getArbolVisual())
            }
            raizCatch.children.add(raizSentenciasCatch)
        }
        raiz.children.add(raizCatch)


        var raizFinally=TreeItem("Finally")
        var raizSentenciasFinally=TreeItem("Sentencias")
        if (listaSentenciasFinally != null){
            for (s:Sentencia in listaSentenciasFinally!!){
                raizSentenciasFinally.children.add( s.getArbolVisual())
            }
            raizFinally.children.add(raizSentenciasFinally)
        }
        raiz.children.add(raizFinally)
        return raiz
    }
}