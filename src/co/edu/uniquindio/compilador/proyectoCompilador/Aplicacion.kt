package co.edu.uniquindio.compilador.proyectoCompilador

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico


fun main(){
    var lexico=AnalizadorLexico("3.5 65465a casa \n 6546546 #Este es un comentario de boque#")
    //var lexico=AnalizadorLexico("#Este#")
    lexico.analizar()
    print(lexico.listaTokens)
}