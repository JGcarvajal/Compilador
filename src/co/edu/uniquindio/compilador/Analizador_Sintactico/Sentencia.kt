package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

open class Sentencia {

    fun getArbolVisual(): TreeItem<String> {

        return TreeItem("Sentencia")
    }
}