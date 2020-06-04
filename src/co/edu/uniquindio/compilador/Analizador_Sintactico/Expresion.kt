package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

open class Expresion {

    open fun getArbolVisual():TreeItem<String>{
        return TreeItem("Expsesion")
    }

    open fun obtenerTipo(tablaSimbolos: TablaSimbolos,ambito:String, erroresSemanticos:ArrayList<Error>):String{
    return ""
    }

    open fun analizarSemantica (tablaSimbolos:TablaSimbolos,erroresSemanticos:ArrayList<Error>, ambito:String){
    }

    open fun getJavaCode():String{

        return ""
    }
}