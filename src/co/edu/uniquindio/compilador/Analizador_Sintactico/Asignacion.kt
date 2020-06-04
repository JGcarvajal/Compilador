package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.Simbolo
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.exp

class Asignacion():Sentencia() {
    var nombre:Token?=null
    var operador:Token?=null
    var expresion:Expresion?=null
    var invocacion:Sentencia?=null

    constructor( nombre:Token, operador:Token?,  expresion: Expresion?):this(){
        this.nombre=nombre
        this.operador=operador
        this.expresion=expresion
    }

    constructor( nombre:Token, operador:Token?,  invocacion: Sentencia):this(){
        this.nombre=nombre
        this.operador=operador
        this.invocacion=invocacion
    }

    override fun toString(): String {
        return "Asignacion(nombre=$nombre, operador=$operador expresion=$expresion)"
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Asignacion")
        if(operador != null) {
            raiz.children.add(TreeItem("Nombre: ${nombre!!.lexema}"))
        }
        if(operador != null) {
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }

        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        }else{
            raiz.children.add(invocacion!!.getArbolVisual())
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        var simb:Simbolo?=null
        if (nombre != null) {
            simb = tablaSimbolos.buscarSimboloValor(nombre!!.lexema, ambito)
        }
        if (simb == null){

            erroresSemanticos.add(Error("El campo ${nombre!!.lexema} no existe en el ambito $ambito",nombre!!.fila, nombre!!.columna,""))

        }else{
            var tipo=simb.tipo

            if (expresion != null){
                expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
                var tipoExp =expresion!!.obtenerTipo(tablaSimbolos, ambito, erroresSemanticos)

                if (tipoExp != tipo){
                    erroresSemanticos.add(Error("El tipo de dato de la expresion ${tipoExp} no coincide con el tipo de dato del campo ${nombre!!.lexema} que es de tipo $tipo",
                        nombre!!.fila,nombre!!.columna,""))
                }
                //Todo falta validar el tipo de retorno dela invocacion
            }else if (invocacion != null){
                invocacion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            }

        }
    }

    override fun getJavaCode(): String {
        var codigo=nombre!!.getJavaCode() + operador!!.getJavaCode()

        if (expresion != null){
            codigo += expresion!!.getJavaCode()
        }else if (invocacion != null){
            codigo += invocacion!!.getJavaCode()
        }
        codigo += "; \n"
        return codigo
    }
}