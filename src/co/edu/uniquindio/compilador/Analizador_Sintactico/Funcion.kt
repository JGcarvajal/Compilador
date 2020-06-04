package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

/**
 * Esta es la clase que nos permite representar uan funcion dentro del lenguaje propuesto
 */
class Funcion (var modAcceso: Token,var nombreFuncion:Token, var tipoRetorno: Token?, var listaParametros: ArrayList<Parametro>?,
               var listaSentencias: ArrayList<Sentencia>?, var retorno:Expresion?) {
    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros," +
                " listaSentencias=$listaSentencias, Retorno=$retorno), \n"
    }

    fun llenarTablaSimbolos (tablaSimbolos: TablaSimbolos, erroresSemanticos:ArrayList<Error>, ambito:String){
        tablaSimbolos.guardarSimboloFuncion(nombreFuncion.lexema,tipoRetorno!!.lexema,obtenerTipoParametro(),ambito,
            modAcceso.lexema,nombreFuncion.fila,nombreFuncion.columna)
        if (listaParametros != null){
        for (p in listaParametros!!){
            tablaSimbolos.guradarSimboloValor(p.nombre.lexema,p.tipoDato.lexema,true,nombreFuncion.lexema,p.nombre.fila,
                p.nombre.columna,modAcceso.lexema)
        }}

        if (listaSentencias != null) {
            for (sent in listaSentencias!!) {
                sent.llenarTablaSimbolos(tablaSimbolos,erroresSemanticos,nombreFuncion.lexema)
            }
        }
    }

    fun obtenerTipoParametro():ArrayList<String>{
        var lista =ArrayList<String>()
        if (listaParametros != null) {
            for (p in listaParametros!!) {
                lista.add(p.tipoDato.lexema)
            }
        }
        return lista
    }

    fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Funcion")

        raiz.children.add(TreeItem("Nombre: "+nombreFuncion.lexema))
        if(tipoRetorno != null) {
            raiz.children.add(TreeItem("Tipo Retorno: " + tipoRetorno!!.lexema))
        }

        if (listaParametros != null) {
            var raizParametros = TreeItem("Parametros")
            for (p in listaParametros!!) {
                raizParametros.children.add(p.getArbolVisual())
            }
            raiz.children.add(raizParametros)
        }

        var raizSentencias= TreeItem("Sentencias")
        for(s in listaSentencias!!){
            raizSentencias.children.add(s.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        if (retorno != null){
            var raizRetorno= TreeItem<String>("Retorno")
            raizRetorno.children.add(retorno!!.getArbolVisual())
            raiz.children.add(raizRetorno)
        }

        return raiz
    }

    fun analizarSemantica (tablaSimbolos:TablaSimbolos,erroresSemanticos:ArrayList<Error>){
        if (listaSentencias != null) {
            for (sent in listaSentencias!!) {
                sent.analizarSemantica(tablaSimbolos,erroresSemanticos,nombreFuncion.lexema)
            }
        }
    }

    fun getJavaCode():String {
    var codigo=""
//* Principal (Main)
        if (nombreFuncion.getJavaCode().toLowerCase() =="principal"){
            codigo ="public static void main (String[] args){ \n"
        }else {
            codigo = "\t"
            if (modAcceso != null) {
                codigo += modAcceso.getJavaCode()
            }
            codigo += " static "
            if (tipoRetorno != null) {
                codigo +=  tipoRetorno!!.getJavaCode()
            } else {
                codigo += "void"
            }
            codigo += " " + nombreFuncion.getJavaCode() + "("
                if (listaParametros != null) {
                    if (listaParametros.isNullOrEmpty() && listaParametros!!.size>0) {

                        for (par in listaParametros!!) {
                            codigo += par.getJavaCode() + ", "
                        }
                        codigo = codigo.substring(0, codigo.length - 2)
                    }
                }

            codigo += ") { \n"


        }
        if (listaSentencias != null) {
            for (sent in listaSentencias!!) {
                codigo += sent.getJavaCode()
            }
        }
        codigo += "\t } \n"
    return codigo
    }
}