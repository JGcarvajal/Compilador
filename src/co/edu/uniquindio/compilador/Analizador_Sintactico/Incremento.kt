package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Incremento(var nombre:Token,var operador:Token):Sentencia() {

    override fun toString(): String {
        return "Incremento(nombre=$nombre,operador=$operador)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Operacion de Incremento")
        raiz.children.add(TreeItem("nombre: ${nombre.lexema}"))
        return raiz
    }

    override fun getJavaCode(): String {
        return nombre.getJavaCode()+"++;"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        var simb=tablaSimbolos.buscarSimboloValor(nombre.lexema, ambito)

        if (simb == null){
            erroresSemanticos.add(Error("La variable ${nombre.lexema} no existe dentro del ambito $ambito", nombre.fila, nombre.columna,""))
        }
    }
}