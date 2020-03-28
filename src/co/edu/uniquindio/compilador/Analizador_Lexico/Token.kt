package co.edu.uniquindio.compilador.Analizador_Lexico

/**
 * Esta es la clase Token que contiene la definicion de la esructura de los tokens del analizador lexico
 */
class Token (var lexema:String, var categoria: Categoria, var fila:Int, var columna: Int) {
    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna), \n"
    }
}