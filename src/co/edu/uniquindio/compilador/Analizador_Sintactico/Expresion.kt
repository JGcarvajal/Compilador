package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.scene.control.TreeItem

open class Expresion {

    open fun getArbolVisual():TreeItem<String>{
        return TreeItem("Expsesion")
    }

}