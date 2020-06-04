package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import java.util.*
import kotlin.collections.ArrayList

class CicloForeach(var lista:Token,var item:Token,  var tipoDato:Token,var listaSentencias:ArrayList<Sentencia>?): Sentencia() {
    override fun toString(): String {
        return "CicloForeach(Lista=$lista, item=$item, tipoDato=$tipoDato, Sentencias$listaSentencias)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Ciclo Foreach")

        raiz.children.add(TreeItem("Lista: ${lista.lexema}"))
        raiz.children.add(TreeItem("Item: ${item.lexema}"))
        if(tipoDato != null) {
            raiz.children.add(TreeItem("Tipo: ${tipoDato!!.lexema}"))
        }

        var raizSentencias= TreeItem("Sentencias")

        if(listaSentencias != null) {
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
        tablaSimbolos.guradarSimboloValor(item.lexema,tipoDato.lexema,false,ambito,item.fila,item.columna,"Private")
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
    }

    override fun getJavaCode(): String {
        var codigo="for ("+tipoDato.getJavaCode() +" "+item.getJavaCode()+": "+lista.getJavaCode() +"){"

        if (listaSentencias != null){
            for (sent in listaSentencias!!){
                codigo+= sent.getJavaCode()
            }
        }

        codigo+="} \n"



        return codigo
    }
}