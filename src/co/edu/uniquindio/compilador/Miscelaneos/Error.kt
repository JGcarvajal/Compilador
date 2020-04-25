package co.edu.uniquindio.compilador.Miscelaneos

/**
 * La clase Error permite almacenar los errores que se presentan en todo el proyecto
 */
class Error(var error: String, var fila:Int, var columna:Int, var categoria:String) {
    override fun toString(): String {
        return "Error(error='$error', fila=$fila, columna=$columna, categoria=$categoria), \n"
    }
}