package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token

class ValorNumerico(var signo:Token, var numero:Token){
    override fun toString(): String {
        return "ValorNumerico(signo=$signo, numero=$numero)"
    }
}