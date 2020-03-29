package co.edu.uniquindio.compilador.Analizador_Lexico

/**
 *ESta clase representa todas las categorias de los tokens validos del lenguaje
 */
enum class Categoria {
    ENTREO,
    DECIMAL,
    IDENTIFICADOR,
    OPERADOR_ARITMETICO,
    OPERADOR_LOGICO,
    DESCONOCIDO,
    COMENTARIO_BLOQUE,
    COMENTARIO_LINEA
}