package co.edu.uniquindio.compilador.Analizador_Sintactico

import javafx.scene.control.TreeItem

class Decision(var expresionLogina:ExpresionLogica, var listaSentecias: ArrayList<Sentencia>,var listaSenteciasElse: ArrayList<Sentencia>?):Sentencia() {
    override fun toString(): String {
        return "Decision(expresionLogina=$expresionLogina, listaSentecias=$listaSentecias, listaSenteciasElse=$listaSenteciasElse)"
    }

    override fun getArbolVisual(): TreeItem<String> {

        var raiz= TreeItem<String>("Expresion Logica")
        var condicion= TreeItem<String>("Condiciones")

        condicion.children.add(expresionLogina.getArbolVisual())
        raiz.children.addAll(condicion)

        var raizTrue= TreeItem<String>("Sentencias Verdaderas")

        for (s in listaSentecias){
            raizTrue.children.add(s.getArbolVisual())
        }
        raiz.children.add(raizTrue)

        if (listaSenteciasElse != null) {
            var raizElse = TreeItem<String>("Sentencias Falsas")
            for (s in listaSenteciasElse!!){
                raizElse.children.add(s.getArbolVisual())
            }
            raiz.children.add(raizElse)
        }
        return raiz
    }
}