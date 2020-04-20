package co.edu.uniquindio.compilador.Analizador_Lexico

class AnalizadorLexico (var codigoFuente:String) {
    var caracterActual =codigoFuente[0]
    var listaTokens=ArrayList<Token>()
    var posicionActual=0
    var finCodigo=0.toChar()
    var filaActual=0
    var columnaActual=0
    val operadoresArit = ArrayList<Char>()
    /**
     * Este metodo nos permite analizar el texto que se ha ingresado
     */
    fun Analizar(){

        operadoresArit.add('+')
        operadoresArit.add('-')
        operadoresArit.add('*')
        operadoresArit.add('/')

        while(caracterActual !=finCodigo){
            if (caracterActual==' '||caracterActual=='\t'){
                obtenerSiguienteCaracter()
                continue
            }
            if (esDecimal()) continue
            if (esEntero()) continue
            if (esComentarioBloque()) continue
            if (esComentarioLinea()) continue
            if (esIdentificador()) continue
            if (finLinea()) continue
            if (esOperadorMatematico()) continue
            if (esOperadorLogico()) continue
            if (esOperadorRelacional()) continue
            if (esOperadorAsignacion()) continue
            if (esCadena()) continue
            if (esSeparador()) continue
            if (esPalabraReservada()) continue
            if (esAgrupador()) continue
            if (esOperadorIncremento()) continue
            if (esCaracter()) continue

            almacenarToken(""+caracterActual,Categoria.DESCONOCIDO,filaActual,columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    fun esCaracter():Boolean{
        if(caracterActual+""=="'"){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual+""=="'"){
                lexema+=caracterActual
                obtenerSiguienteCaracter()

                if(caracterActual+""=="'") {
                    hacerBT(posicionInicial,filaInicial,columnaInicial)
                    return false
                }

                lexema+=caracterActual
                obtenerSiguienteCaracter()

                if(caracterActual+""=="'") {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    if(caracterActual+""=="'") {
                        lexema += caracterActual
                        almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial);
                        obtenerSiguienteCaracter()
                        return true
                    }else{
                    hacerBT(posicionInicial,filaInicial,columnaInicial)
                    return false
                }
                }else{
                    hacerBT(posicionInicial,filaInicial,columnaInicial)
                    return false
                }
            }else {
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }
        }
        return false
    }
    fun esOperadorIncremento():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (caracterActual=='+' || caracterActual=='-'){
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual=='+'&& lexema=="+"){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_INCREMENTO, filaInicial, columnaInicial);
                obtenerSiguienteCaracter()
                return true
            } else if(caracterActual=='-'&& lexema=="-"){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_DECREMENTO, filaInicial, columnaInicial);
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }

        }
        return false
    }
    fun esAgrupador():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual
        if (caracterActual=='{'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.LLAVE_IZQUIERDA, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='}'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.LLAVE_DERECHA, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='('){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.PARENTESIS_IZQUIERDO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual==')'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.PARENTESIS_DERECHO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='['){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.CORCHETE_IZQUIERDO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual==']'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.CORCHETE_DERECHO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esPalabraReservada():Boolean{
        var palabrasRes=ArrayList<String>()
        palabrasRes.add("where")
        palabrasRes.add("is")
        palabrasRes.add("equalString")
        palabrasRes.add("select")
        palabrasRes.add("group")
        palabrasRes.add("by")
        palabrasRes.add("his")
        palabrasRes.add("and")
        palabrasRes.add("notReturn")
        palabrasRes.add("not")
        palabrasRes.add("enter")
        palabrasRes.add("delete")
        palabrasRes.add("all")
        palabrasRes.add("data")
        palabrasRes.add("update")
        palabrasRes.add("in")
        palabrasRes.add("ad")
        palabrasRes.add("subtract")
        palabrasRes.add("falling")
        palabrasRes.add("upward")
        palabrasRes.add("mthod")
        palabrasRes.add("class")
        palabrasRes.add("list")
        palabrasRes.add("string")
        palabrasRes.add("integer")
        palabrasRes.add("decimal")
        palabrasRes.add("float")
        palabrasRes.add("function")
        palabrasRes.add("public")
        palabrasRes.add("private")
        palabrasRes.add("protected")


        if(caracterActual.isLetter()){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual.isLetter()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()

                if(palabrasRes.contains(lexema.toLowerCase()) ){
                    almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial);
                    return true
                }
            }

            hacerBT(posicionInicial,filaInicial,columnaInicial)
            return false

        }
        return false
    }
    fun esSeparador():Boolean{

        if(caracterActual==','){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.SEPARADOR_COMA, filaInicial, columnaInicial);
            return true
        }

        return false
    }
    fun esCadena():Boolean{
        if(caracterActual=='"'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual!='"'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual=='"') {
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.CADENA, filaInicial, columnaInicial);
                return true

            }

        }
        return false
    }
    fun esOperadorMatematico():Boolean{

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresArit.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (operadoresArit.contains(caracterActual)){

                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false

            }
            almacenarToken(lexema, Categoria.OPERADOR_MATEMATICO, filaActual, columnaActual)

            return true
        }

        return false
    }

    fun esOperadorAsignacion():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresArit.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual=='=') {
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true

            } else{
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }

        }else if(caracterActual=='='){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaActual, columnaActual)
            obtenerSiguienteCaracter()
            return true
        }

        return false
    }

    fun esOperadorLogico():Boolean{
        val operadoresLog = ArrayList<Char>()
        operadoresLog.add('&')
        operadoresLog.add('|')
        operadoresLog.add('!')

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresLog.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (operadoresLog.contains(caracterActual)){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.DESCONOCIDO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
            almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaActual, columnaActual)

            return true
        }

        return false
    }

    fun esOperadorRelacional():Boolean{
        val operadoresRelac = ArrayList<Char>()
        operadoresRelac.add('<')
        operadoresRelac.add('>')
        operadoresRelac.add('=')

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresRelac.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual=='=') {
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
            if(operadoresRelac.contains(caracterActual)){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.DESCONOCIDO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
            if(lexema=="="){
                hacerBT(posicionInicial,filaInicial,columnaInicial)
            }else {
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaActual, columnaActual)
                return true
            }
        }

        return false
    }
    fun finLinea():Boolean{
        if(caracterActual=='\n'){
            almacenarToken(""+caracterActual,Categoria.TERMINAL_LINEA,filaActual,columnaActual)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }
    fun esComentarioLinea():Boolean{
        if (caracterActual=='%'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual=='%'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()

                while (caracterActual!='\n'){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }

                almacenarToken(lexema,Categoria.COMENTARIO_LINEA,filaInicial,columnaInicial);
                obtenerSiguienteCaracter()
                return true
            }
        }
        return false
    }
    /**
     * Este metodo nos permite determinar si un token es un entero
     */
    fun esEntero():Boolean {
        if (caracterActual.isDigit()){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            while(caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }

            if (caracterActual=='.'){
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }
            almacenarToken(lexema,Categoria.ENTREO,filaInicial,columnaInicial);
            return true
        }
        return false
    }

    fun esIdentificador():Boolean {
        if (caracterActual=='_'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual.isLetter()) {
                do {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                } while (caracterActual != '_')

            }else{
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema,Categoria.DESCONOCIDO,filaInicial,columnaInicial);
                return true
            }
            var cadena:String="jlsdjkl5646*/*+++.}{+{+:][]*¡?=?=))()(&%#!|"
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema,Categoria.IDENTIFICADOR,filaInicial,columnaInicial);

            return true
        }
        return false
    }

    /**
     * Ente metodo permite determinar si un token es comentario de bloque
     */
    fun esComentarioBloque():Boolean{
        if(caracterActual=='#'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual!='#'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual=='#') {
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial);
                return true
            }

        }
        return false
    }

    /**
     * Este metodo nos permite determinar si un token es un decimal
     */
    fun esDecimal():Boolean{
        if (caracterActual.isDigit()|| caracterActual=='.'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            if (caracterActual=='.'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()

                if(caracterActual.isDigit()){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
            }else{
                lexema+=caracterActual
                obtenerSiguienteCaracter()

                while(caracterActual.isDigit()){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }

                if (caracterActual=='.') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }else{
                    hacerBT(posicionInicial,filaInicial,columnaInicial)
                    return false
                }
            }
            while(caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema,Categoria.DECIMAL,filaInicial,columnaInicial)
            return true
        }

        return false
    }

    /**
     * Este metodo nos permite hacer backtraking cuando ocurre un error con un token
     */
    fun hacerBT(posicionInicial:Int,filainicial:Int,columnaInicial:Int){
        posicionActual=posicionInicial
        filaActual=filainicial
        columnaActual=columnaInicial

        caracterActual=codigoFuente[posicionActual]
    }

    /**
     * Este metodo permite almacenar un token despues de haberse analizado
     */
    fun almacenarToken(lexema:String, categoria:Categoria, fila:Int, columna:Int) =listaTokens.add(Token(lexema,categoria,fila, columna))

    /**
     * Este metodo permite consumir el caracter actual y avanzar en el analisis
     */
    fun obtenerSiguienteCaracter(){
        if(posicionActual==codigoFuente.length-1){
            caracterActual=finCodigo
        }else{
            if(caracterActual=='\n'){
                filaActual++
                columnaActual=0
            }else{
                columnaActual++
            }
            posicionActual++
            caracterActual=codigoFuente[posicionActual]
        }
    }

    fun error(){

    }
}