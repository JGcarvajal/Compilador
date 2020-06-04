package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Invocacion(var nombre:Token, var listaArgumentos:ArrayList<Expresion>):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Invocacion Funcion")
        raiz.children.add(TreeItem("Nombre Funcion: ${nombre.lexema}"))
        var raizSentencias= TreeItem("Argumentos")
        for(arg in listaArgumentos){
            raizSentencias.children.add(arg.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        var listaTipoArgs=obtenerTiposArgumentos(tablaSimbolos,ambito,erroresSemanticos)
        var s =tablaSimbolos.buscarSimboloFuncion(nombre.lexema,listaTipoArgs)

        if (s== null){
            erroresSemanticos.add(Error("La funcion ${nombre.lexema} no existe dentrro del ambito $ambito", nombre.fila,nombre.columna,""))
        }
    }

    fun obtenerTiposArgumentos(tablaSimbolos: TablaSimbolos, ambito: String, erroresSemanticos: ArrayList<Error>): ArrayList<String>{
        var listaArgs =ArrayList<String>()

        for ( arg in listaArgumentos){
            listaArgs.add(arg.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos ))
        }
        return listaArgs
    }

    override fun getJavaCode(): String {
        var codigo ="\t \t"+ nombre.getJavaCode()+" ("

        if (listaArgumentos.isNotEmpty()) {
            for (arg in listaArgumentos) {
                codigo += arg.getJavaCode() + ", "
            }
            codigo = codigo.substring(0, codigo.length - 2)
        }
        codigo+= "); \n"
        return codigo
    }
}