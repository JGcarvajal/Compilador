package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

class CicloForeach(var Lista:ArrayList<Sentencia>,var item:Token,  var tipoDato:Token): Sentencia() {
    override fun toString(): String {
        return "CicloForeach(Lista=$Lista, item=$item, tipoDato=$tipoDato)"
    }
}