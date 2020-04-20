package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Categoria
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import java.lang.NullPointerException

class AnalizadorSintactico(var listaTokens:ArrayList<Token>) {
    var posicionActual=0
    var tokenActual=listaTokens[posicionActual]
    var listaErrores=ArrayList<Error>()

    fun obtenerSiguienteToken(){
        posicionActual++

        if(posicionActual<listaTokens.size){
            tokenActual=listaTokens[posicionActual]
        }
    }

    /**
     * <UnidadCompilacion> ::= <ModificadorAcceso>”Class”<Identificador>  “{”<ListaFunciones> “}”
     */
    fun esUnidadCompilacion (): UnidadCompilacion?{
        var modificadorAcceso=esModificadorAcceso()
        if (modificadorAcceso != null) {
            obtenerSiguienteToken()

            if (tokenActual.lexema.toLowerCase() == "class") {
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var nombreClase = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                        obtenerSiguienteToken()
                        var listaFinciones = esListaFunciones()
                        if (tokenActual.categoria == Categoria.LLAVE_DERECHA){
                            if (listaFinciones.size > 0) {
                              return UnidadCompilacion(listaFinciones)
                            }else{
                                reportarError("Falta la lista de funciones")
                            }
                        }else{
                            reportarError("Falta la llave de cierre de la funcion")
                        }
                    }else{
                        reportarError("Falta la llave de apertura de la funcion")
                    }
                }else{
                    reportarError("Falta un nombre de la clase valido")
                }
            }else{
                reportarError("Falta la palabra reservada 'Class' ")
            }
        }else{
            reportarError("No hay una unidad de compilacion definida")
        }
        return null
    }

    /**
     * <ListaFunciones> ::= <Funcion> [<ListaFunciones>]
      */
    fun esListaFunciones():ArrayList<Funcion>{
        var listaFunciones= ArrayList<Funcion>()
        var funcion=esFuncion()

        while (funcion != null){
            listaFunciones.add(funcion)
            funcion = esFuncion()
        }
        return listaFunciones
    }

    /**
     * <Funcion> ::= <modificadorAcceso> “function”  <Return> <Identificador> “(” ”)” “{“<ListaSentencias> “}”
     */
    fun esFuncion():Funcion? {
        if (esModificadorAcceso()!= null) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "function") {
                obtenerSiguienteToken()
                var tipoRetorno=esTipoRetorno()
                if(tipoRetorno != null){
                    obtenerSiguienteToken()

                    if(tokenActual.categoria==Categoria.IDENTIFICADOR){
                        var nombreFuncion=tokenActual
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                            obtenerSiguienteToken()

                            var listaParametros = esListaParametros()

                            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                    obtenerSiguienteToken()

                                    var listaSentencias= esListaSentencias()

                                    if (listaSentencias != null) {

                                        return Funcion(nombreFuncion ,tipoRetorno,listaParametros,listaSentencias)
                                    }else{
                                        reportarError("Falta La lista de sentencias de la funcion")
                                    }

                                }else{
                                    reportarError("Falta la llave izquierda de la funcion")
                                }

                                }else{
                                reportarError("Falta el parentesis derecho de la funcion")
                            }
                        }else{
                            reportarError("Falta el parentesis izquierdo de la funcion")
                        }
                    }else{
                        reportarError("Falta el identificador de la funcion")
                    }

                }else{
                    reportarError("Falta el tipo de retorno de la funcion")
                }

            }else{
            reportarError("Falta la palabra reservada 'function' de la funcion")
            }
        }
        return null
    }

    /**
     * <ListaSentencias>::=  <Sentecia> [<ListaSentencias> ]
     */
    fun esListaSentencias(): ArrayList<Sentencia>{

        return ArrayList()
    }

    /**
     * <ListaParametros> ::= <Parametro> ["," <ListaParametros>]
     */
    fun esListaParametros():ArrayList<Parametro>{
        var parametro=esParametro()
        var listaParametros =ArrayList<Parametro>()
        while (parametro !=null){
            listaParametros.add(parametro)

            if (tokenActual.categoria == Categoria.SEPARADOR_COMA){
                obtenerSiguienteToken()
                parametro=esParametro()
            }else{
                if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO && tokenActual.categoria != Categoria.PARENTESIS_IZQUIERDO){
                reportarError("Falta el separador en la lista de parametros")
                }
                break
            }
        }
        return listaParametros
    }

    /**
     * <Parametro> ::=<TipoDato> <Identificador>
     */
    fun esParametro():Parametro?{
        var tipoDato=esTipoParametro()
        if (tipoDato != null){
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombre = tokenActual
                obtenerSiguienteToken()

                return Parametro(tipoDato,nombre)
            }else{
                reportarError("Falta un identificador validao para el parametro")
            }
        }else{
            reportarError("Falta el tipo de dato del parametro")
        }
        return null
    }

    /**
     * <ModificadorAcceso> ::= “private” | “public” | “protected”
     */
    fun esModificadorAcceso ():Token?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if(tokenActual.lexema.toLowerCase() == "public" || tokenActual.lexema.toLowerCase() == "private"
                ||tokenActual.lexema.toLowerCase() == "protected"){
                return tokenActual
            }
        }
        return null
    }

    /**
     * <TipoRetorno> ::= “String” | “Integer” | “Decimal” | “Float” | “List” | Identificador | “notReturn”
     */
    fun esTipoRetorno(): Token?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if(tokenActual.lexema.toLowerCase() == "string" || tokenActual.lexema.toLowerCase() == "integer"
                ||tokenActual.lexema.toLowerCase() == "decimal"||tokenActual.lexema.toLowerCase() == "float"
                ||tokenActual.lexema.toLowerCase() == "list" ||tokenActual.lexema.toLowerCase() == "notreturn"){
                return tokenActual
            }
        } else{
            if(tokenActual.categoria==Categoria.IDENTIFICADOR){
               return tokenActual
            }
        }
        return null
    }

    /**
     * <TipoRetorno> ::= “String” | “Integer” | “Decimal” | “Float” | “List” | Identificador | “notReturn”
     */
    fun esTipoParametro(): Token?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if(tokenActual.lexema.toLowerCase() == "string" || tokenActual.lexema.toLowerCase() == "integer"
                ||tokenActual.lexema.toLowerCase() == "decimal"||tokenActual.lexema.toLowerCase() == "float"
                ||tokenActual.lexema.toLowerCase() == "list" ){
                return tokenActual
            }
        } else{
            if(tokenActual.categoria==Categoria.IDENTIFICADOR){
                return tokenActual
            }
        }
        return null
    }

    fun reportarError( mensaje:String){
       listaErrores.add( Error(mensaje,tokenActual.fila,tokenActual.columna))
    }
}