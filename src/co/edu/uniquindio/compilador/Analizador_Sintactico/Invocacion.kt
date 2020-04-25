package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

class Invocacion(var nombre:Token, var listaArgumentos:ArrayList<Expresion>):Sentencia() {
}