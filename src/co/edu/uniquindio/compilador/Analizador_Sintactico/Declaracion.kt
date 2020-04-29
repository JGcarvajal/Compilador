package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

class Declaracion(var modAcceso:Token?, var tipoDato:Token, var nombre:Token, var asignacion:Asignacion?):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Declaracion")

        if (modAcceso != null){
            raiz.children.add(TreeItem("Mod Acceso: ${modAcceso!!.lexema}"))
        }
        raiz.children.add(TreeItem("Tipo Dato: ${tipoDato.lexema}"))
        raiz.children.add(TreeItem("Nombre: ${nombre.lexema}"))

        if (asignacion != null){
            raiz.children.add(asignacion!!.getArbolVisual())
        }

        return raiz
    }
}