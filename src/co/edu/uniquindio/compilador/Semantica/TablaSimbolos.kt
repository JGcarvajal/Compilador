package co.edu.uniquindio.compilador.Semantica

import co.edu.uniquindio.compilador.Miscelaneos.Error

class TablaSimbolos(var listaErrores:ArrayList<Error>) {

    var listaSimbolos:ArrayList<Simbolo> = ArrayList()

    /**
     * Permite guardar un simbolo de tipo variable, constante, parametro o arreglo
     */
    fun guradarSimboloValor(nombre:String, tipodato:String, modificable:Boolean ,ambito:String,fila:Int , columna:Int, acceso:String?){
        var simbolo= Simbolo(nombre,tipodato,modificable,ambito, fila, columna, acceso)

        var simb:Simbolo?=buscarSimboloValor(nombre,ambito)

        if (simb == null){
            listaSimbolos.add(simbolo)
        }else{
            listaErrores.add(Error("El campo con nombre $nombre, ya existe dentro del amito $ambito",fila,columna,""))
        }
    }

    /**
     * Permite guardar un simbolo ue representa una funcion o metodo
     */
    fun guardarSimboloFuncion(nombre:String, tipoRetorno:String, tipoParametros:ArrayList<String>,ambito:String,acceso:String,fila:Int , columna:Int){
        var simbolo=Simbolo(nombre, tipoRetorno, tipoParametros, ambito, acceso)

        var simb:Simbolo?=buscarSimboloFuncion(nombre,tipoParametros)

        if (simb == null){
            listaSimbolos.add(simbolo)
        }else{
            listaErrores.add(Error("La funcion con nombre $nombre, ya existe dentro del amito $ambito",fila+1,columna+1,""))
        }
    }

    /**
     * Permite buscar un valor dentro de la tabla de simbolos
     */
    fun buscarSimboloValor(nombre:String, ambito:String):Simbolo?{
        for(simb in listaSimbolos){
            if (simb.tipoParametros == null) {
                if (simb.nombre == nombre && simb.ambito == ambito) {
                    return simb
                }
            }
        }
        return null


    }

    /**
     * Permite buscar una funcion dentro de la tabla de simbolos
     */
    fun buscarSimboloFuncion(nombre:String, tipoParametros:ArrayList<String>):Simbolo?{
        for(simb in listaSimbolos) {
            if (simb.tipoParametros != null) {
                if (simb.nombre == nombre && simb.tipoParametros == tipoParametros) {
                    return simb
                }
            }
        }
        return null
    }

    override fun toString(): String {
        return "ListaSimbolos=$listaSimbolos)"
    }


}