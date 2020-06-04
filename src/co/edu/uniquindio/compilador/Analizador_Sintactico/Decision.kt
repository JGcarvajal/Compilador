package co.edu.uniquindio.compilador.Analizador_Sintactico

import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Decision(var expresionLogica:ExpresionLogica, var listaSentecias: ArrayList<Sentencia>?,var listaSenteciasElse: ArrayList<Sentencia>?):Sentencia() {
    override fun toString(): String {
        return "Decision(expresionLogina=$expresionLogica, listaSentecias=$listaSentecias, listaSenteciasElse=$listaSenteciasElse)"
    }

    override fun getArbolVisual(): TreeItem<String> {

        var raiz= TreeItem<String>("Decision Simple")
        var condicion= TreeItem<String>("Condiciones")

        condicion.children.add(expresionLogica.getArbolVisual())
        raiz.children.addAll(condicion)

        var raizTrue= TreeItem<String>("Sentencias Verdaderas")

        for (s in listaSentecias!!){
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

    override fun llenarTablaSimbolos(
        tablaSimbolos: TablaSimbolos,
        erroresSemanticos: ArrayList<Error>,
        ambito: String
    ) {
        if (listaSentecias!=null){
            for (sent in listaSentecias!!){
                sent.llenarTablaSimbolos(tablaSimbolos,erroresSemanticos,ambito)
            }
        }

        if (listaSenteciasElse!=null){
            for (sentElse in listaSenteciasElse!!){
                sentElse.llenarTablaSimbolos(tablaSimbolos,erroresSemanticos,ambito)
            }
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (listaSentecias!=null){
            for (sent in listaSentecias!!){
                sent.analizarSemantica(tablaSimbolos,erroresSemanticos,ambito)
            }
        }

        if (listaSenteciasElse!=null){
            for (sentElse in listaSenteciasElse!!){
                sentElse.analizarSemantica(tablaSimbolos,erroresSemanticos,ambito)
            }
        }

        if (expresionLogica != null){
            expresionLogica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = "if ("

        if (expresionLogica != null){
            codigo += expresionLogica!!.getJavaCode()
        }
        codigo += ") { \n"

        if (listaSentecias != null){
            for (sent in listaSentecias!!){
                codigo +=sent.getJavaCode()
            }
        }
        codigo +="} \n"

        if (listaSenteciasElse != null){
            codigo += "else { \n"
            if (listaSenteciasElse != null){
                for (sent in listaSenteciasElse!!){
                    codigo +=sent.getJavaCode()
                }
            }

            codigo +="} \n"
        }

        return codigo
    }
}