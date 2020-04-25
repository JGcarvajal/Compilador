package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

class Argumento (var nombre: Token){
    override fun toString(): String {
        return "Argumento(nombre=$nombre)"
    }
}