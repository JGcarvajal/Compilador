package co.edu.uniquindio.compilador.Controladores

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*

class InicioController : Initializable{
    @FXML lateinit var taCodigoFuente :TextArea
    @FXML lateinit var tblTokens :TableView<Token>
    @FXML lateinit var clmLexema:TableColumn<Token,String>
    @FXML lateinit var clmCategoria:TableColumn<Token,String>
    @FXML lateinit var clmFila:TableColumn<Token,Int>
    @FXML lateinit var clmColumna:TableColumn<Token,Int>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        clmLexema.cellValueFactory=PropertyValueFactory("lexema")
        clmCategoria.cellValueFactory=PropertyValueFactory("categoria")
        clmFila.cellValueFactory=PropertyValueFactory("fila")
        clmColumna.cellValueFactory=PropertyValueFactory("columna")
    }

    @FXML
    fun AnalizarCodigo(e:ActionEvent){
        var codFuente=taCodigoFuente.text
        if (codFuente.length>0) {
            var lexico=AnalizadorLexico(codFuente)
            lexico.Analizar()

            tblTokens.items=FXCollections.observableArrayList(lexico.listaTokens)
        }
    }


}