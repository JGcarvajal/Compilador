package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem
import kotlin.math.exp

class Asignacion():Sentencia() {
    var nombre:Token?=null
    var operador:Token?=null
    var expresion:Expresion?=null
    var invocacion:Sentencia?=null

    constructor( nombre:Token, operador:Token,  expresion: Expresion):this(){
        this.nombre=nombre
        this.operador=operador
        this.expresion=expresion
    }

    constructor( nombre:Token, operador:Token,  invocacion: Sentencia):this(){
        this.nombre=nombre
        this.operador=operador
        this.invocacion=invocacion
    }

    override fun toString(): String {
        return "Asignacion(nombre=$nombre, operador=$operador expresion=$expresion)"
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Asignacion")
        raiz.children.add(TreeItem("Nombre: ${nombre!!.lexema}"))
        raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))

        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        }else{
            raiz.children.add(invocacion!!.getArbolVisual())
        }

        return raiz
    }
}