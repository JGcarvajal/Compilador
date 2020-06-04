package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.exp

class CicloWhile(var expRelacional: ExpresionRelacional?, var listaSentencias: ArrayList<Sentencia>?):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Ciclo While")

        if (expRelacional != null){
        raiz.children.add(expRelacional!!.getArbolVisual())
        }


        var raizSentencias= TreeItem("Sentencias")

        if (listaSentencias != null) {
            for (s in listaSentencias!!) {
                raizSentencias.children.add(s.getArbolVisual())
            }
        }

        raiz.children.add(raizSentencias)
        return raiz
    }

    override fun llenarTablaSimbolos(
        tablaSimbolos: TablaSimbolos,
        erroresSemanticos: ArrayList<Error>,
        ambito: String
    ) {
        if (listaSentencias != null) {
            for (sent in listaSentencias!!){
                sent.llenarTablaSimbolos(tablaSimbolos,erroresSemanticos,ambito)
            }

        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (listaSentencias != null) {
            for (sent in listaSentencias!!){
                sent.analizarSemantica(tablaSimbolos,erroresSemanticos,ambito)
            }

        }

        if (expRelacional != null){
            expRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = "while ("

        if (expRelacional != null){
            codigo += expRelacional!!.getJavaCode()
        }
        codigo += ") { \n"

        if (listaSentencias != null){
            for (sent in listaSentencias!!){
                codigo +=sent.getJavaCode()
            }
        }
        codigo += "} \n"
        return codigo
    }
}