package co.edu.uniquindio.compilador.Analizador_Lexico

class AnalizadorLexico (var codigoFuente:String) {
    var caracterActual =codigoFuente[0]
    var listaTokens=ArrayList<Token>()
    var posicionActual=0
    var finCodigo=0.toChar()
    var filaActual=0
    var columnaActual=0
    /**
     * Este metodo nos permite analizar el texto que se ha ingresado
     */
    fun analizar(){
        while(caracterActual !=finCodigo){
            if (caracterActual==' '||caracterActual=='\t'||caracterActual=='\n'){
                obtenerSiguienteCaracter()
                continue
            }
            if (esDecimal()) continue
            if (esEntero()) continue
            if (esComentarioBloque()) continue

            almacenarToken(""+caracterActual,Categoria.DESCONOCIDO,filaActual,columnaActual)
            obtenerSiguienteCaracter()
        }
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
}