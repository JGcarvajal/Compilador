package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

/**
 * Esta es la clase que nos permite representar la unidad de compilacion dentro del lenguaje propuesto
 */
class UnidadCompilacion( var listaFunciones:ArrayList<Funcion>) {
    override fun toString(): String {
        return "UnidadCompilacion(listaFunciones=$listaFunciones), \n"
    }

    fun getArbolVisual():TreeItem<String>{
        var raiz=TreeItem<String>("Unidad de Compilacion")

        for(f in listaFunciones){

            raiz.children.add(f.getArbolVisual())
        }
        return raiz
    }
}