package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Categoria
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.Simbolo
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.exp

class ExpresionAritmetica():Expresion() {
    var expresionAritmetica1:ExpresionAritmetica?=null
    var expresionAritmetica2:ExpresionAritmetica?=null
    var operador:Token?=null
    var valorNumerico:ValorNumerico?=null

    constructor(expAritmetica1:ExpresionAritmetica, operador:Token,expAritmetica2: ExpresionAritmetica):this(){
        this.expresionAritmetica1=expAritmetica1
        this.operador=operador
        this.expresionAritmetica2=expAritmetica2
    }
    constructor(valorNumerico:ValorNumerico, operador:Token,expAritmetica2: ExpresionAritmetica):this(){
        this.valorNumerico=valorNumerico
        this.operador=operador
        this.expresionAritmetica2=expAritmetica2
    }
    constructor(expAritmetica: ExpresionAritmetica):this(){
        this.expresionAritmetica1= expAritmetica
    }
    constructor(valorNumerico:ValorNumerico?):this(){
        this.valorNumerico=valorNumerico

    }

    override fun toString(): String {
        return "ExpresionAritmetica(expresionAritmetica1=$expresionAritmetica1, expresionAritmetica2=$expresionAritmetica2, operador=$operador, valorNumerico=$valorNumerico)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Expresion Aritmetica")

        if (expresionAritmetica1 != null && expresionAritmetica2 != null ){
            raiz.children.add(expresionAritmetica1!!.getArbolVisual())
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
            raiz.children.add(expresionAritmetica2!!.getArbolVisual())
        }else{
            if (valorNumerico != null && expresionAritmetica2 != null ){

                raiz.children.add(valorNumerico!!.getArbolVisual())
                raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
                raiz.children.add(expresionAritmetica2!!.getArbolVisual())

            }else{
                if(expresionAritmetica1 != null){
                    raiz.children.add(expresionAritmetica1!!.getArbolVisual())
                }else {
                    if (valorNumerico != null) {
                        raiz.children.add(valorNumerico!!.getArbolVisual())
                    }
                }
            }
        }


        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, erroresSemanticos:ArrayList<Error>): String {
       if ( expresionAritmetica1 != null && expresionAritmetica2 != null){
           var tipo1 =expresionAritmetica1!!.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos)
           var tipo2 =expresionAritmetica2!!.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos)

           if (tipo1 == "decimal" || tipo2 == "decimal"){
               return "decimal"
           }else{
               return "int"
           }

       }else if (valorNumerico != null && expresionAritmetica2 != null){
           var tipo1 =obtenerTipoCampo(tablaSimbolos, ambito, erroresSemanticos)

           var tipo2 =expresionAritmetica2!!.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos)

           if ( tipo1 == "decimal" || tipo2 == "decimal"){
               return "decimal"
           }else{
               return "int"
           }
       } else if (expresionAritmetica1 != null){
           return expresionAritmetica1!!.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos)
       }else if (valorNumerico != null){
            return obtenerTipoCampo(tablaSimbolos, ambito, erroresSemanticos)
       }
        return ""
    }

    fun obtenerTipoCampo(tablaSimbolos: TablaSimbolos,ambito: String,erroresSemanticos: ArrayList<Error>):String{
            if (valorNumerico!!.numero.categoria == Categoria.ENTREO){
            return "int"
        }else if (valorNumerico!!.numero.categoria == Categoria.DECIMAL){
            return "decimal"
        }else{
            var simbolo = tablaSimbolos.buscarSimboloValor(valorNumerico!!.numero.lexema,ambito)

            if (simbolo != null){
                return simbolo.tipo
            }else{
                erroresSemanticos.add(Error("El campo ${valorNumerico!!.numero.lexema} no existe en el ambito $ambito",valorNumerico!!.numero.fila, valorNumerico!!.numero.columna,""))
            }
        }
        return ""
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (valorNumerico != null){
            if (valorNumerico!!.numero.categoria ==Categoria.IDENTIFICADOR){

                    var simb:Simbolo? = tablaSimbolos.buscarSimboloValor(valorNumerico!!.numero.lexema, ambito)

                    if (simb == null) {

                        erroresSemanticos.add(
                                Error(
                                    "El campo ${valorNumerico!!.numero.lexema} no existe dontro del ambito $ambito",
                                    valorNumerico!!.numero.fila, valorNumerico!!.numero.columna, ""
                                )
                                )

                    }else if (simb!!.tipo != "int" && simb!!.tipo != "decimal") {

                        erroresSemanticos.add(
                            Error(
                                "El campo ${valorNumerico!!.numero.lexema} no no representa un valor numerico en la expresion aritmetica",
                                valorNumerico!!.numero.fila, valorNumerico!!.numero.columna, ""
                            )
                        )
                    }


            }
        }
        if (expresionAritmetica1!= null){
            expresionAritmetica1!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }

        if (expresionAritmetica2!= null){
            expresionAritmetica2!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo=""
        if (valorNumerico != null){
            codigo=valorNumerico!!.getJavaCode()
        }
        if (expresionAritmetica1!= null){
            expresionAritmetica1!!.getJavaCode()
        }

        if (expresionAritmetica2!= null){
            expresionAritmetica2!!.getJavaCode()
        }

        return codigo
    }
}