package co.edu.uniquindio.compilador.proyectoCompilador

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico


fun main(){
    //var lexico=AnalizadorLexico("3.5 65465a _casa_ \n 6546546 #Este es un comentario de boque# %%Comentario de linea\n")
    var lexico=AnalizadorLexico("_a_=6")
    lexico.Analizar()
    print(lexico.listaTokens)
}