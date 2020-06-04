package co.edu.uniquindio.compilador.Semantica

import co.edu.uniquindio.compilador.Analizador_Sintactico.UnidadCompilacion
import co.edu.uniquindio.compilador.Miscelaneos.Error

class AnalizadorSemantico(var unidadCompilacion: UnidadCompilacion) {
    var erroresSemanticos: ArrayList<Error> = ArrayList()
    var tablaSimbolos:TablaSimbolos = TablaSimbolos(erroresSemanticos)

    fun llenarTablaSimbolos (){
        unidadCompilacion.llenarTAblaSimbolos(tablaSimbolos,erroresSemanticos)
    }

    fun analizarSentica(){
        unidadCompilacion.analizarSentica(tablaSimbolos,erroresSemanticos)
    }
}