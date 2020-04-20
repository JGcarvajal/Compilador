package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

class Funcion (var nombreFuncion:Token, var tipoRetorno: Token, var listaParametros: ArrayList<Parametro>, var listaSentencias: ArrayList<Sentencia>) {
    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros, listaSentencias=$listaSentencias), \n"
    }
}