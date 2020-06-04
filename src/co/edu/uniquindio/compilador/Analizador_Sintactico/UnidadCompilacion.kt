package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

/**
 * Esta es la clase que nos permite representar la unidad de compilacion dentro del lenguaje propuesto
 */
class UnidadCompilacion(var nombreClass:Token, var listaFunciones:ArrayList<Funcion>) {
    var erroresSemanticos:ArrayList<Error> = ArrayList()
    override fun toString(): String {
        return "UnidadCompilacion(listaFunciones=$listaFunciones), \n"
    }

    fun getArbolVisual():TreeItem<String>{
        var raiz=TreeItem<String>("Clase: ${nombreClass.lexema}")

        for(f in listaFunciones){

            raiz.children.add(f.getArbolVisual())
        }
        return raiz
    }

    fun llenarTAblaSimbolos (tablaSimbolos:TablaSimbolos,erroresSemanticos:ArrayList<Error>){
        for (funcion in listaFunciones) {
        funcion.llenarTablaSimbolos(tablaSimbolos,erroresSemanticos,"Unidad de Compilacion")
            this.erroresSemanticos=erroresSemanticos
    }
    }

    fun analizarSentica(tablaSimbolos:TablaSimbolos,erroresSemanticos:ArrayList<Error>){
        if (listaFunciones != null ){
            for (funcion in listaFunciones) {
                funcion.analizarSemantica(tablaSimbolos,erroresSemanticos)
            }
        }
    }

    fun getJavaCod():String{

        var codigo= "public class ${nombreClass.getJavaCode()} { \n"


        for (f in listaFunciones) {

            codigo += f.getJavaCode()
        }

        codigo += "}"

        var principal:Boolean=false

        for ( func in listaFunciones){
            if (func.nombreFuncion.lexema.toLowerCase() == "principal"){
                var principal=true
            }
        }

        if (!principal){
            erroresSemanticos.add(Error("No no se encontro la clase principal",0,0,""))
        }
        return codigo
    }
}