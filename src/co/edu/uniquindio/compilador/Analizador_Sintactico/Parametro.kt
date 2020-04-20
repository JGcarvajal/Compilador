package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

/**
 ** Esta es la clase que nos permite representar un parametro dentro del lenguaje propuesto
 */
class Parametro (var tipoDato:Token, var nombre:Token) {
    override fun toString(): String {
        return "Parametro(tipoDato=$tipoDato, nombre=$nombre), \n"
    }
}