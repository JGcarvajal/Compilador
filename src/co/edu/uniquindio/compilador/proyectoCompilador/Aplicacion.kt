package co.edu.uniquindio.compilador.proyectoCompilador

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.Analizador_Sintactico.AnalizadorSintactico


fun main(){
    //var lexico=AnalizadorLexico("3.5 65465a _casa_ \n 6546546 #Este es un comentario de boque# %%Comentario de linea\n")
    var lexico=AnalizadorLexico("private class _calculadora_ { private function String _sumar_ (String _cosa_) {} }")
    lexico.Analizar()
    //print(lexico.listaTokens)
    var sintactico=AnalizadorSintactico(lexico.listaTokens)

    print(sintactico.esUnidadCompilacion())
    print(sintactico.listaErrores)
}