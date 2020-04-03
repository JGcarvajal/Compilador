package co.edu.uniquindio.compilador.Analizador_Lexico

/**
 *ESta clase representa todas las categorias de los tokens validos del lenguaje
 */
enum class Categoria {
    ENTREO,
    DECIMAL,
    IDENTIFICADOR,
    OPERADOR_MATEMATICO,
    OPERADOR_LOGICO,
    OPERADOR_RELACIONAL,
    OPERADOR_ASIGNACION,
    DESCONOCIDO,
    COMENTARIO_BLOQUE,
    COMENTARIO_LINEA,
    TERMINAL_LINEA,
    ERROR
}