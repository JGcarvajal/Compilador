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

            almacenarToken(""+caracterActual,Categoria.DESCONOCIDO,filaActual,columnaActual)
            obtenerSiguienteCaracter()
        }
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

                lexema+=caracterActual
                almacenarToken(lexema, Categoria.DESCONOCIDO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true

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

            do{
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }while(caracterActual!='_')

            if (caracterActual=='.'){
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }

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