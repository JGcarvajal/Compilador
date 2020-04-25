package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import kotlin.math.exp

class ExpresionAritmetica():Expresion() {
    var expresionAritmetica1:ExpresionAritmetica?=null
    var expresionAritmetica2:ExpresionAritmetica?=null
    var operador:Token?=null
    var valorNumerico:ValorNumerico?=null

    constructor(expAritmetica1:ExpresionAritmetica, operador:Token,expAritmetica2: ExpresionAritmetica):this(){
        this.expresionAritmetica1=expresionAritmetica1
        this.operador=operador
        this.expresionAritmetica2=expresionAritmetica2
    }
    constructor(varloNumerico:ValorNumerico, operador:Token,expAritmetica2: ExpresionAritmetica):this(){
        this.valorNumerico=valorNumerico
        this.operador=operador
        this.expresionAritmetica2=expresionAritmetica2
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


}