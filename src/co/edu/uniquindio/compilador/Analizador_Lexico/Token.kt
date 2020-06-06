package co.edu.uniquindio.compilador.Analizador_Lexico

/**
 * Esta es la clase Token que contiene la definicion de la esructura de los tokens del analizador lexico
 */
class Token (var lexema:String, var categoria: Categoria, var fila:Int, var columna: Int) {
    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna), \n"
    }

    fun getJavaCode():String{
        if (categoria == Categoria.PALABRA_RESERVADA){
            if (lexema.toLowerCase() == "notreturn"){
                return "void"
            }else if (lexema.toLowerCase() == "bool"){
                return "boolean"
            }else if (lexema.toLowerCase() == "equalstring"){
                return "equalString"
            }else if (lexema.toLowerCase() == "meantime"){
                return "while"
            }else if (lexema.toLowerCase() == "string"){
                return "String"
            }else if (lexema.toLowerCase() == "decimal"){
                return "double"
            }else if (lexema.toLowerCase() == "exception"){
                return "Exception"
            }
        }

        if (categoria == Categoria.OPERADOR_RELACIONAL){

        }
        if (categoria == Categoria.CADENA){
            return lexema.replace("%","\"")
        }

        if (categoria == Categoria.IDENTIFICADOR){
            return lexema.replace("_","")
        }

        if (categoria == Categoria.OPERADOR_LOGICO_BINARIO){
            return "&&"
        }


        return lexema
    }
}