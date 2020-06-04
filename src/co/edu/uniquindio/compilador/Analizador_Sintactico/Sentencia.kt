package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compilador.Miscelaneos.Error

open class Sentencia {

   open fun getArbolVisual(): TreeItem<String> {

        return TreeItem("Sentencia")
    }

    open fun llenarTablaSimbolos (tablaSimbolos: TablaSimbolos, erroresSemanticos:ArrayList<Error>, ambito:String) {

    }

    open fun analizarSemantica (tablaSimbolos:TablaSimbolos,erroresSemanticos:ArrayList<Error>, ambito:String){

    }

   open fun getJavaCode():String{

        return ""
    }
}