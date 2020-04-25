package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

class ExpresionLogica:Expresion() {
    override fun getArbolVisual(): TreeItem<String> {

        return TreeItem("Expresion Logica")
    }
}