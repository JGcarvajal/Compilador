package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Categoria
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error

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
     * BNF para reresentar la unidad de compilacion
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
     * * BNF para reresentar una lista funciones
     * <ListaFunciones> ::= <Funcion> [<ListaFunciones>]
      */
    fun esListaFunciones():ArrayList<Funcion>{
        var listaFunciones= ArrayList<Funcion>()
        var funcion=esFuncion()
        obtenerSiguienteToken()
        while (funcion != null){
            listaFunciones.add(funcion)
            funcion = esFuncion()
        }
        return listaFunciones
    }

    /**
     * BNF para reresentar una funcion
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
     ** BNF para reresentar una lista de parametros
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
     ** BNF para reresentar un parametro
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
     ** BNF para reresentar un modificador de acceso
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
     * BNF para reresentar un tio de retorno
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
     ** BNF para reresentar un tipo de parametro
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

    /**
     * Este metodo permite agregar un error a la lista de errores
     */
    fun reportarError( mensaje:String){
       listaErrores.add( Error(mensaje,tokenActual.fila,tokenActual.columna))
    }

    /**
     * <InvocacionFuncion> ::= <Indentificador> “(“ [<ListaArgumentos>] “)”
     */
    fun esInvocacionFuncion(): Invocacion? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val nombreFuncion = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val listaArgumentos: ArrayList<Expresion> = esListaArgumentos()
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                        obtenerSiguienteToken()
                        return Invocacion(nombreFuncion, listaArgumentos)
                    } else {
                        reportarError("Falta fin de sentencia")
                    }
                } else {
                    reportarError("Falta paréntesis derecho")
                }
            } else {
                reportarError("Falta paréntesis izquierdo")
            }
        }
        return null
    }

    /**
     * <ListaArgumentos> ::= <Expresion> [“,” <ListaArgumentos>]
     */
    fun esListaArgumentos():ArrayList<Expresion>{
        var expresion=esExpresion()
        var listaExpresiones =ArrayList<Expresion>()
        while (expresion !=null){
            listaExpresiones.add(expresion)

            if (tokenActual.categoria == Categoria.SEPARADOR_COMA){
                obtenerSiguienteToken()
                expresion=esExpresion()
            }else{
                if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO && tokenActual.categoria != Categoria.PARENTESIS_IZQUIERDO){
                    reportarError("Falta el separador en la lista de expresiones de la invocacion de la funcion")
                }
                break
            }
        }
        return listaExpresiones
    }

    /**
     * <Argumento ::= <Identificador>>
      */
    fun esArgumento():Argumento?{
        //var tipoDato=esTipoParametro()


            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombre = tokenActual
                obtenerSiguienteToken()

                return Argumento(nombre)
            }else{
                reportarError("Falta un identificador validao para el argumento")
            }

        return null
    }

    /**
     * <Expresion> ::= <ExpresionLogica> | <ExpresionRelacional> | <ExpresionAritmetica> | <ExpresionCadena>
     */
    fun esExpresion():Expresion?{
        var expresion:Expresion?=esExpresionAritmetica()

        if (expresion!= null) return expresion

        expresion=esExpresionRelacional()

        if (expresion!= null) return expresion

        return null
    }

    fun esExpresionRelacional():Expresion?{
        return null
    }

    fun esExpresionAritmetica():Expresion?{
        return null
    }

    /**
     * * BNF para reresentar una sentencia
     * <ListaSentencias>::=  <Sentecia> [<ListaSentencias> ]
     */
    fun esListaSentencias():ArrayList<Sentencia>{
        var sentecia=esSentencia()
        var listaSentencias =ArrayList<Sentencia>()

        while (sentecia !=null){
            listaSentencias.add(sentecia)
        }
        return listaSentencias
    }

    /**
     * <Sentencia> ::= <Ciclo> | <DesicionSimple> | <Declaracion> | <Asignacion> | TipoRetorno   | <Imprimir> | <Leer> | <Incremento> | <Decremento> | <InvocacionFuncion>
     */
    fun esSentencia():Sentencia?{
        var sentencia:Sentencia?=esCicloForEach()

        if (sentencia != null) return sentencia

        return null
    }

    /**
     * <CicloForEach>::= “Foreach” “(“ <Identificador> “in” <Identificador> “as” <TipoDato> “)” “{”<ListaSentencias> “}”
     */
    fun esCicloForEach():CicloForeach?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "foreach" ){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var lista = tokenActual.lexema
                    obtenerSiguienteToken()

                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "in") {
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                            var item =tokenActual
                            obtenerSiguienteToken()

                            var tipoDato=esTipoParametro()

                            if (tipoDato != null) {

                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()

                                    if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                        obtenerSiguienteToken()
                                        var listaSentencias = esListaSentencias()

                                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                            return CicloForeach(listaSentencias, item, tipoDato)
                                        } else {
                                            reportarError("Falta llave derecha en el Foreach")
                                        }
                                    } else {
                                        reportarError("Falta llave izquierda en el Foreach")
                                    }
                                } else {
                                    reportarError("Falta parentesis derecho en el Foreach")
                                }
                            }else{
                                reportarError("Tipo de dato invalido en el Foreach")
                            }
                        } else {
                            reportarError("Identificador del item invalido en el Foreach")
                        }
                    } else {
                        reportarError("Falta la palabra reservada 'As' en el Foreach")
                    }
                } else {
                    reportarError("Lista no valida en el Foreach")
                }
            }else{
                reportarError("Falta parentesis izquierdo en el Foreach")
            }
        }
        return null
    }
}