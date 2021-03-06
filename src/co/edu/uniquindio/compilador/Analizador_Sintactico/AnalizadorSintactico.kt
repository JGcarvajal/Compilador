package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Categoria
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import java.io.Console
import java.io.PrintWriter


class AnalizadorSintactico(var listaTokens:ArrayList<Token>) {
    var posicionActual=0
    var tokenActual=listaTokens[posicionActual]
    var listaErrores=ArrayList<Error>()

    fun obtenerSiguienteToken(){
        posicionActual++

        if(posicionActual<listaTokens.size){
            tokenActual=listaTokens[posicionActual]
        }else{

            tokenActual= Token("",Categoria.FIN_CODIGO,tokenActual.fila,tokenActual.columna)
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
                    } else {
                        reportarError("Falta la llave de apertura de la clase", "Unidad de Compilacion")
                    }
                    var listaFunciones = esListaFunciones()
                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                    if (tokenActual.categoria != Categoria.LLAVE_DERECHA) {

                        reportarError(
                            "Falta la  llave de cierre de la undad de compilacion",
                            "Unidad de Compilacion")
                    }
                        if (listaFunciones.size > 0) {
                            return UnidadCompilacion(nombreClase,listaFunciones)
                        } else {
                            reportarError("Falta la lista de funciones", "Unidad de Compilacion")
                        }
                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()



                }else{
                    reportarError("Falta un nombre de la clase valido","Unidad de Compilacion")
                }
            }else{
                reportarError("Falta la palabra reservada 'Class' ","Unidad de Compilacion")
            }
        }else{
            reportarError("No hay una unidad de compilacion definida","Unidad de Compilacion")
        }
        return null
    }

    /**
     * * BNF para reresentar una lista funciones
     * <ListaFunciones> ::= <Funcion> [<ListaFunciones>]
     */
    fun esListaFunciones():ArrayList<Funcion>{
        var listaFunciones= ArrayList<Funcion>()
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        var funcion=esFuncion()

        while (funcion != null){
            listaFunciones.add(funcion)
            obtenerSiguienteToken()
            funcion = esFuncion()
        }
        return listaFunciones
    }

    /**
     * BNF para reresentar una funcion
     * <Funcion> ::= <modificadorAcceso> “function”  <Return> <Identificador> “(” ”)” “{“<ListaSentencias> [ “return” <Expresion>]  “}”
     */
    fun esFuncion():Funcion? {
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        var modAcceso=esModificadorAcceso()
        if (modAcceso!= null) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "function") {
                obtenerSiguienteToken()
            }else{
                reportarError("Falta la palabra reservada 'function' de la funcion","Funciones")
            }
            var tipoRetorno=esTipoRetorno()
            if(tipoRetorno != null){
                obtenerSiguienteToken()
            }else{
                reportarError("Falta el tipo de retorno de la funcion","Funciones")
            }

            var nombreFuncion=tokenActual
            var listaParametros:ArrayList<Parametro>?=null

            if(tokenActual.categoria!=Categoria.IDENTIFICADOR){
                reportarError("Falta el identificador de la funcion","Funciones")
                modoPanico(Categoria.LLAVE_IZQUIERDA)
                nombreFuncion= Token("Sin Nombre",Categoria.ERROR,tokenActual.fila,tokenActual.columna)

            }else{
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                    obtenerSiguienteToken()
                }else{
                    reportarError("Falta el parentesis izquierdo de la funcion","Funciones")
                }
                listaParametros = esListaParametros()

                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                }else{
                    reportarError("Falta el parentesis derecho de la funcion","Funciones")
                }
            }


            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                obtenerSiguienteToken()
            }else{
                reportarError("Falta la llave izquierda de la funcion","Funciones")
            }
            var listaSentencias= esListaSentencias()
            //obtenerSiguienteToken()
            var retorno:Expresion?=null
            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "return" ){
                obtenerSiguienteToken()
                retorno = esExpresion()

                if (retorno == null){
                    reportarError("Falta la expresion de retorno de la funcion","Funciones")

                }
                if (tokenActual.categoria != Categoria.FIN_SENTENCIA) {
                    reportarError("Falta el fin de linea de del retorno de la funcion","Funciones")

                }
            }



            while (tokenActual.categoria == Categoria.FIN_SENTENCIA && tokenActual.categoria!= Categoria.FIN_CODIGO ) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {

                return Funcion(modAcceso,nombreFuncion ,tipoRetorno,listaParametros,listaSentencias,retorno)
            }else{
                reportarError("Falta la llave derecha de la funcion","Funciones")
            }
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()


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
                    reportarError("Falta el separador en la lista de parametros","Lista de Parametros")
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
                reportarError("Falta un identificador valido para el parametro","Parametros")
            }
        }else{
            if (listaTokens[posicionActual+1].categoria == Categoria.IDENTIFICADOR){
                reportarError("Falta el tipo de dato del parametro","Parametros")}
        }
        return null
    }

    /**
     ** BNF para reresentar un modificador de acceso
     * <ModificadorAcceso> ::= “private” | “public” | “protected”
     */
    fun esModificadorAcceso ():Token?{
        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

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
                ||tokenActual.lexema.toLowerCase() == "list" ||tokenActual.lexema.toLowerCase() == "notreturn"
                ||tokenActual.lexema.toLowerCase() == "int"){
                return tokenActual
            }
        } else{
            if(tokenActual.categoria==Categoria.IDENTIFICADOR){
                return tokenActual
            }
        }
        return null
    }

    fun esRetorno():Retorno?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "return"){
            obtenerSiguienteToken()

            var exp=esExpresion()
            if (exp == null){
                reportarError("Falta la expresion de retorno de la fincuon","Retorno funcion")
            }

            return Retorno(exp)
        }
        return null
    }

    /**
     ** BNF para reresentar un tipo de parametro
     * <TipoRetorno> ::= “String” | “Integer” | “Decimal” | “Float” | “List” | Identificador | “notReturn”
     */
    fun esTipoParametro(): Token?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            //obtenerSiguienteToken()
            if(tokenActual.lexema.toLowerCase() == "string" || tokenActual.lexema.toLowerCase() == "integer"
                ||tokenActual.lexema.toLowerCase() == "decimal"||tokenActual.lexema.toLowerCase() == "float"
                ||tokenActual.lexema.toLowerCase() == "list" ||tokenActual.lexema.toLowerCase() == "int" ){
                return tokenActual
            }
        } else{
            if(tokenActual.categoria==Categoria.IDENTIFICADOR && listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO){
                return tokenActual
            }
        }
        return null
    }

    /**
     * Este metodo permite agregar un error a la lista de errores
     */
    fun reportarError( mensaje:String, categoria:String){
        listaErrores.add( Error(mensaje,tokenActual.fila,tokenActual.columna, categoria))
    }

    /**
     * <InvocacionFuncion> ::= <Indentificador> “(“ [<ListaArgumentos>] “)”
     */
    fun esInvocacionFuncion(): Invocacion? {
        // while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_ASIGNACION  && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO
                && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO){


                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    val nombreFuncion = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta paréntesis izquierdo","Invocacion Funcion")
                    }
                    val listaArgumentos: ArrayList<Expresion> = esListaArgumentos()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta paréntesis derecho","Invocacion Funcion")
                    }
                    if (tokenActual.categoria != Categoria.FIN_SENTENCIA) {
                        reportarError("Falta fin de sentencia","Invocacion Funcion")
                    }
                    return Invocacion(nombreFuncion, listaArgumentos)

                }
            }
        }catch (ex:Exception){

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
                    reportarError("Falta el separador en la lista de expresiones de la invocacion de la funcion","Lista de Argumentos")
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
            reportarError("Falta un identificador validao para el argumento","Argumentos")
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

        expresion=esExpresionCadena()
        if (expresion!= null) return expresion

        return null
    }

    fun esExpresionCadena():Expresion?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria== Categoria.CADENA ){
            var cadena=tokenActual
            obtenerSiguienteToken()
            var expresion:Expresion?= null

            if (tokenActual.lexema == "+"){
                obtenerSiguienteToken()
                expresion=esExpresion()
            }
            return ExpresionCadena(cadena,expresion)
        }else {
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var variable = tokenActual
                obtenerSiguienteToken()
                return ExpresionCadena(variable,null)
            }
        }
        return null
    }

    /***
     * <ExpresionRelacional>::= <ExpresionAritmetica> <OperadorRelacional> <ExpresionAritmetica>
     */
    fun esExpresionRelacional():ExpresionRelacional?{
        try{
            var expresion1=esExpresionAritmetica()
            if (expresion1 != null){
                //obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                    var operadorRelacional=tokenActual
                    obtenerSiguienteToken()
                    var expresion2=esExpresionAritmetica()

                    if (expresion2 != null) {
                        return ExpresionRelacional(expresion1,operadorRelacional,expresion2)
                    }else{
                        reportarError("Falta la operacion 2 de la expresion relacional","Expresion Relacional")
                    }
                }else{
                    reportarError("Falta el operador relacional","Expresion Relacional")
                }
            }
        }
        catch (ex:java.lang.Exception){
            print("Exception Metodo de creacion de Expresion Relacional")
        }
        return null
    }
    /**
     * <ExpresionAritmetica> ::=  “(“<ExpresionAritmetica> “)” [<Z>] | <ValorNumerico>[<Z>]
     *  <Z> ::= <OperadorMatematico> <ExpresionAritmetica>[<Z>]
     */
    fun esExpresionAritmetica():ExpresionAritmetica?{

        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            obtenerSiguienteToken()
            val expAritmetica: ExpresionAritmetica? = esExpresionAritmetica()

            if (expAritmetica != null) {
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                }else{
                    reportarError("Falta el parentesis derecho de la expresion Aritmetica","Expresion Aritmetica")
                }

                if (tokenActual.categoria == Categoria.OPERADOR_MATEMATICO){
                    var operador =tokenActual
                    obtenerSiguienteToken()

                    val expAritmetica2: ExpresionAritmetica? = esExpresionAritmetica()

                    if (expAritmetica2 != null){
                        return ExpresionAritmetica(expAritmetica, operador,expAritmetica2)
                    }
                }else{
                    return ExpresionAritmetica(expAritmetica)
                }

            }
        }else{
            var numerico=esValorNumerico()

            if (numerico != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_MATEMATICO) {
                    var operador = tokenActual
                    obtenerSiguienteToken()

                    val expAritmetica2: ExpresionAritmetica? = esExpresionAritmetica()

                    if (expAritmetica2 == null) {
                        reportarError("Falta la expresion 2 de la Expresion Aritmetica","Expresion Aritmetica")

                    }
                    return ExpresionAritmetica(numerico, operador, expAritmetica2!!)

                } else {
                    return ExpresionAritmetica(numerico)
                }
            }
        }
        return null
    }

    /**
     * <ValorNumerico> ::= [<Signo>] real | [<Signo>] entero | [<Signo>] identificador
     */
    fun esValorNumerico():ValorNumerico?{
        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        var  signo=""
        if (tokenActual.lexema == "+" || tokenActual.lexema == "-") {
            signo =tokenActual.lexema
            obtenerSiguienteToken()
        }
        if (signo =="") signo="+"

        var tSigno= Token(signo,Categoria.SIGNO,tokenActual.fila,tokenActual.columna)


        if (tokenActual.categoria == Categoria.ENTREO || tokenActual.categoria == Categoria.DECIMAL ||
            (tokenActual.categoria == Categoria.IDENTIFICADOR && listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO)){
            return ValorNumerico(tSigno,tokenActual)
        }
        return null
    }

    /**
     * * BNF para reresentar una sentencia
     * <ListaSentencias>::=  <Sentecia> [<ListaSentencias> ]
     */
    fun esListaSentencias():ArrayList<Sentencia>?{
        var sentecia=esSentencia()
        var listaSentencias =ArrayList<Sentencia>()

        while (sentecia !=null){
            listaSentencias.add(sentecia)
            obtenerSiguienteToken()
            sentecia=esSentencia()
        }
        return listaSentencias
    }

    /**
     * <Sentencia> ::= <Ciclo> | <DesicionSimple> | <Declaracion> | <Asignacion> | TipoRetorno   | <Imprimir> | <Leer> | <Incremento> | <Decremento> | <InvocacionFuncion>
     */
    fun esSentencia():Sentencia?{
        var sentencia:Sentencia?=esCicloForEach()
        if (sentencia != null) return sentencia

        sentencia=esCicloWhile()
        if (sentencia != null) return sentencia

        sentencia=esAsignacion()
        if (sentencia != null) return sentencia

        sentencia=esDecicionSimple()
        if (sentencia != null) return sentencia

        sentencia=esDeclaracion()
        if (sentencia != null) return sentencia

        sentencia=esInvocacionFuncion()
        if (sentencia != null) return sentencia

        sentencia=esIncremento()
        if (sentencia != null) return sentencia

        sentencia=esDecremento()
        if (sentencia != null) return sentencia

        sentencia=esTRyCatch()
        if (sentencia != null) return sentencia

        sentencia=esInterrupcion()
        if (sentencia != null) return sentencia

        sentencia=esRetorno()
        if (sentencia != null) return sentencia

        sentencia=esImpresion()
        if (sentencia != null) return sentencia

        sentencia=esLeer()
        if (sentencia != null) return sentencia

        return null
    }

    fun esLeer():Sentencia?{

        if (tokenActual.categoria== Categoria.PALABRA_RESERVADA && tokenActual.lexema== "Leer") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var variable = tokenActual
                obtenerSiguienteToken()

                return Leer(variable)
            }
        }
        return null
    }
    fun esImpresion():Sentencia?{
        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase()=="print"){
            obtenerSiguienteToken()
            var imprimir=esExpresion()
            if (imprimir != null){

                return Imprimir(imprimir)
            }
        }
        return null
    }

    fun esInterrupcion():Sentencia?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase()=="break"){
            var interrupcion=tokenActual
            obtenerSiguienteToken()
            return Interrupcion(interrupcion)
        }
        return null
    }
    /***
     * <TryCatch>::= “try” “{“ [<ListaSentencias> ]  “}”  <catch> [<finally>]
    <catch>::= “catch””(“ <Parametro> “)”  “{“ [<ListaSentencias> ] “}” [<catch>]
    <finally>::= “finally” “{“ [<ListaSentencias> ] “}”
     */
    fun esTRyCatch():TryCatch?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "try"){
            obtenerSiguienteToken()
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA){
                obtenerSiguienteToken()

                var sentencias=esListaSentencias()
                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()


                if (tokenActual.categoria == Categoria.LLAVE_DERECHA){
                    obtenerSiguienteToken()
                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "catch"){
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                            obtenerSiguienteToken()

                            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase().contains("exception")){
                                var tipoExcepcion=tokenActual
                                obtenerSiguienteToken()

                                if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                                    var nombreExcepcion=tokenActual
                                    obtenerSiguienteToken()

                                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                                        obtenerSiguienteToken()
                                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA){
                                            obtenerSiguienteToken()

                                            var sentenciasCatch=esListaSentencias()
                                            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                            if (tokenActual.categoria == Categoria.LLAVE_DERECHA){
                                                obtenerSiguienteToken()
                                                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
                                                if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "finally"){
                                                    obtenerSiguienteToken()
                                                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                                    if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                                        obtenerSiguienteToken()
                                                        var sentenciasFinally = esListaSentencias()
                                                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                                        if (tokenActual.categoria == Categoria.LLAVE_DERECHA){
                                                            return TryCatch(sentencias,tipoExcepcion,nombreExcepcion,sentenciasCatch,sentenciasFinally)
                                                        }else{
                                                            reportarError("Falta la llave de cierre del Finally","TryCatch")
                                                        }
                                                    }else{
                                                        reportarError("Falta la llave de apertura del Finally","TryCatch")
                                                    }
                                                }else{
                                                    do{
                                                        posicionActual--
                                                        tokenActual=listaTokens[posicionActual]
                                                    }while (tokenActual.categoria == Categoria.FIN_SENTENCIA)
                                                    return TryCatch(sentencias,tipoExcepcion,nombreExcepcion,sentenciasCatch,null)
                                                }
                                            }else{

                                                reportarError("Falta la llave de cierre del Catch","TryCatch")
                                            }
                                        }else{
                                            reportarError("Falta la llave de apertura del Catch","TryCatch")
                                        }
                                    }else{
                                        reportarError("Falta el parentesis de cierre del Catch","TryCatch")
                                    }
                                }else{
                                    reportarError("Falta el identificador del Catch","TryCatch")
                                }
                            }else{
                                reportarError("Falta el tipo de Excepcion del Catch","TryCatch")
                            }
                        }else{
                            reportarError("Falta parentesis de apertura del Catch","TryCatch")
                        }
                    }else{
                        reportarError("Falta la sentencia 'Catch' del Try","TryCatch")
                    }
                }else{
                    reportarError("Falta la llave de cierre del Try","TryCatch")
                }
            }else{
                reportarError("Falta la llave de apertura del Try","TryCatch")
            }
        }
        return null
    }
    /***
     * <esIncremento> ::= "Identidicador" "--" <Expresion>
     */
    fun esIncremento():Incremento?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO
                && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO){

                if (tokenActual.categoria== Categoria.IDENTIFICADOR){
                    var nombre=tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria== Categoria.OPERADOR_INCREMENTO){
                        var operador = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            return Incremento(nombre,operador)
                        }else{
                            reportarError("Falta el fin de sentencia en la operacion de Incremento","Sentencia de Incremento")
                        }
                    }
                }

            }
        }catch (ex:Exception){

        }
        return null
    }

    /***
     * <esDecremento> ::= "Identidicador" "--" <Expresion>
     */
    fun esDecremento():Decremento?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO
                && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO){

                if (tokenActual.categoria== Categoria.IDENTIFICADOR){
                    var nombre=tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria== Categoria.OPERADOR_DECREMENTO){
                        var operador = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            return Decremento(nombre,operador)
                        }else{
                            reportarError("Falta el fin de sentencia de la expresion de decremente","Sentencia de Decremento")
                        }
                    }
                }

            }
        }catch (ex:Exception){

        }
        return null
    }
    /***
     *<Declaracion> ::= [<ModificadorAcceso>] <TipoDato> <Identificador> [<OperadorAsignacion> <TipoAsignacion>]
     */
    fun esDeclaracion():Declaracion?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO && tokenActual.lexema.toLowerCase() != "return"
                && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO) {

                var modAcceso = esModificadorAcceso()
                if (modAcceso != null) obtenerSiguienteToken()

                var tipoDato = esTipoParametro()

                if (tipoDato != null) {
                    obtenerSiguienteToken()

                    if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                        var nombre = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                            var opAsignacion = tokenActual
                            obtenerSiguienteToken()
                            var expresion = esExpresion()
                            var invocacion = esInvocacionFuncion()
                            var asignacion:Asignacion


                            if (expresion != null || invocacion != null) {
                                if (expresion!= null) {
                                    asignacion = Asignacion(nombre, opAsignacion, expresion)
                                }else{
                                    asignacion = Asignacion(nombre, opAsignacion, invocacion!!)
                                }
                                return Declaracion(modAcceso, tipoDato, nombre, asignacion)
                            } else {
                                reportarError("Falta la expresion de asignacion de la declaracion", "Declaracion")
                                asignacion = Asignacion(nombre, opAsignacion, null)
                                return Declaracion(modAcceso, tipoDato, nombre, asignacion)
                            }

                        } else {
                            return Declaracion(modAcceso, tipoDato, nombre, null)
                        }

                    } else {
                        reportarError("Falta el identificador de la declaracion", "Declaracion")
                    }

                }else{
                    //reportarError("Falta el tipo de dato de la declaracion", "Declaracion")
                    // return null
                    /*while(tokenActual.categoria== Categoria.FIN_SENTENCIA){
                        obtenerSiguienteToken()}
                    return Declaracion()*/
                }
            }
        }catch (ex:Exception){

        }
        return null
    }
    /**
     * <CicloForEach>::= “Foreach” “(“ <Identificador> “in” <Identificador> “as” <TipoDato> “)” “{”<ListaSentencias> “}”
     */
    fun esCicloForEach():CicloForeach?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "foreach" ){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
            }
            else{
                reportarError("Falta parentesis izquierdo en el Foreach","Ciclo Foreach")
                obtenerSiguienteToken()
            }
            var lista:Token?=null
            if (tokenActual.categoria != Categoria.IDENTIFICADOR) {
                reportarError("Lista no valida en el Foreach","Ciclo Foreach")
                lista = Token("Sin Lista",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
                modoPanico(Categoria.PARENTESIS_DERECHO)

            }else{
                lista = tokenActual
                obtenerSiguienteToken()
            }

            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "in") {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta la palabra reservada 'in' en el Foreach","Ciclo Foreach")
                obtenerSiguienteToken()
            }
            var item:Token?=null
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                item =tokenActual
                obtenerSiguienteToken()
            } else {
                reportarError("Identificador del item invalido en el Foreach","Ciclo Foreach")
                modoPanico(Categoria.PARENTESIS_DERECHO)
                item= Token("Sin Item",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
            }
            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "as") {
                obtenerSiguienteToken()
            }else {
                reportarError("Falta la palabra reservada 'as' en el Foreach ","Ciclo Foreach")
                obtenerSiguienteToken()
            }
            var tipoDato = esTipoParametro()

            if (tipoDato != null) {
                obtenerSiguienteToken()
            } else {
                reportarError("Tipo de dato invalido en el Foreach", "Ciclo Foreach")
                tipoDato= Token("Error",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
                obtenerSiguienteToken()
            }

            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta parentesis derecho en el Foreach", "Ciclo Foreach")
                obtenerSiguienteToken()
            }
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta llave izquierda en el Foreach", "Ciclo Foreach")
                obtenerSiguienteToken()
            }
            var listaSentencias = esListaSentencias()
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                return CicloForeach( lista,item, tipoDato,  listaSentencias )

            } else {
                reportarError("Falta llave derecha en el Foreach", "Ciclo Foreach")
                obtenerSiguienteToken()
            }








        }
        return null
    }

    /***
     * <CicloWhile> ::= “MeanTime” “(“ <ExpresionRelacional> “)” “{”<ListaSentencias> “}”
     */
    fun esCicloWhile():CicloWhile?{
        try {
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "meantime") {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()

                    var expRelacional = esExpresionRelacional()

                    if (expRelacional == null) {

                        reportarError("Falta la expresion relacional del ciclo while", "Ciclo While")
                        modoPanico(Categoria.PARENTESIS_DERECHO)
                    }
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                            obtenerSiguienteToken()

                            var sentencias = esListaSentencias()

                            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                                obtenerSiguienteToken()
                                return CicloWhile(expRelacional, sentencias)

                            } else {
                                reportarError("Falta la llave derecha del ciclo while", "Ciclo While")
                            }
                        } else {
                            reportarError("Falta la llave izquierda del ciclo while", "Ciclo While")
                        }
                    } else {
                        reportarError("Falta el parentesis derecho del ciclo while", "Ciclo While")
                    }

                } else {
                    reportarError("Falta el parentesis izquierdo del ciclo while", "Ciclo While")
                }
            }

        }
        catch (ex:java.lang.Exception){
            print("Exception Metodo de creacion de ciclo While")
        }
        return null
    }

    /**
     * <Asignacion> ::= <Identificador> <OperadorAsignacion> <Expreresion> “\n”
     */
    fun esAsignacion():Sentencia?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try {
            if (listaTokens[posicionActual + 1].categoria != Categoria.PARENTESIS_IZQUIERDO && listaTokens[posicionActual + 1].categoria != Categoria.OPERADOR_DECREMENTO
                && listaTokens[posicionActual + 1].categoria != Categoria.OPERADOR_INCREMENTO
            ) {

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var nombre = tokenActual
                    obtenerSiguienteToken()

                    var opAsignacion:Token?=null

                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                         opAsignacion = tokenActual
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta el operador de asinacion de la asignacion", "Asignacion")
                        obtenerSiguienteToken()
                    }
                        var expresion = esExpresion()
                        var invocacion: Sentencia? = esInvocacionFuncion()

                        if (expresion != null || invocacion != null) {

                            //obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                                //obtenerSiguienteToken()
                                if (expresion != null) {
                                    return Asignacion(nombre, opAsignacion, expresion)
                                } else {
                                    return Asignacion(nombre, opAsignacion, invocacion!!)
                                }
                            } else {
                                reportarError("Falta el fin de sentencia de la asignacion", "Asignacion")
                            }
                        } else {
                            reportarError("Falta la expresion de la asignacion", "Asignacion")
                            modoPanico(Categoria.FIN_SENTENCIA)
                            return Asignacion(nombre, opAsignacion,null)
                        }


                }
            }
        }catch ( ex:java.lang.Exception){

        }
        return null
    }

    /**
     * <DesicionSimple> ::= “If” “(“ <EspresionLogica> “)” “{”<ListaSentencias> “}” [<Sino>]
    <Sino> ::= “else”  “{”<ListaSentencias> “}”
     */
    fun esDecicionSimple():Decision?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "if"){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                obtenerSiguienteToken()
            }else{
                reportarError("Falta el parentesis izquierdo del if","Decision")
                obtenerSiguienteToken()
            }
                var expLogica: ExpresionLogica?=esExpresionLogica()

                if (expLogica != null){
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                            obtenerSiguienteToken()
                            var sentencias=esListaSentencias()
                            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()


                            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                                obtenerSiguienteToken()

                                if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "else") {
                                    obtenerSiguienteToken()
                                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                    if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                        obtenerSiguienteToken()
                                        var sentenciasElse = esListaSentencias()
                                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                        if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {

                                            if (sentenciasElse != null) {
                                                obtenerSiguienteToken()
                                                return Decision(expLogica, sentencias, sentenciasElse)
                                            }else{
                                                reportarError("Falta lista de sentencias del else","Decision")
                                            }
                                        }else{
                                            reportarError("Falta el parentesis derecho del else","Decision")
                                        }
                                    }else{
                                        reportarError("Falta el parentesis izquierdo del else","Decision")
                                    }
                                } else{
                                    return Decision(expLogica, sentencias, null)
                                }

                            }else{
                                reportarError("Falta la llave derecha del if","Decision")
                            }
                        }else{
                            reportarError("Falta la lleve izquierda del if","Decision")
                        }
                    }else{
                        reportarError("Falta el parentesis derecho del if","Decision")
                    }
                }else{
                    reportarError("Falta la expresion logica del if","Decision")
                }

        }

        return null
    }

    /**
     * <ExpresionLogica> <ExpresionRelacional>  [<OperadorLogico><ExpresionLogica>]
     */
    fun esExpresionLogica():ExpresionLogica?{
        var operadorUnario:Token?=null

        if (tokenActual.categoria == Categoria.OPERADOR_LOGICO_UNARIO) {
            operadorUnario=tokenActual
            obtenerSiguienteToken()
        }

        var expRelacional:ExpresionRelacional?=esExpresionRelacional()

        if (expRelacional != null){
            //obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_LOGICO_BINARIO){
                var operadorBinario=tokenActual
                obtenerSiguienteToken()
                var expresionLogica=esExpresionLogica()

                if (expresionLogica != null){
                    return ExpresionLogica(expRelacional,operadorBinario, expresionLogica)
                }else{
                    reportarError("Falta la expresion logica despues del operador logico","Expresion Logica")
                }
            }else{
                return ExpresionLogica(operadorUnario,expRelacional)
            }

        }else{
            reportarError("Falta la expresion relacional de la expresion logica","Expresion Logica")
        }
        return null
    }

    fun modoPanico( parada:Categoria){
        while (tokenActual.categoria != parada && tokenActual.categoria!= Categoria.FIN_CODIGO){

            obtenerSiguienteToken()
        }
    }
}