package co.edu.uniquindio.compilador.Analizador_Sintactico

/**
 * Esta es la clase que nos permite representar la unidad de compilacion dentro del lenguaje propuesto
 */
class UnidadCompilacion( var listaFunciones:ArrayList<Funcion>) {

    override fun toString(): String {
        return "UnidadCompilacion(listaFunciones=$listaFunciones), \n"
    }
}