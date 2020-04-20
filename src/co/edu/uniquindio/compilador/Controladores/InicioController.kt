package co.edu.uniquindio.compilador.Controladores

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.internal.impl.types.checker.NewCapturedType

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
        leerDatos();
    }

    @FXML
    fun Limpiar(){
        taCodigoFuente.text=""
        tblTokens.items=FXCollections.observableArrayList(ArrayList<Token>())
    }
    @FXML
    fun AnalizarCodigo(e:ActionEvent){
        var codFuente=taCodigoFuente.text
        if (codFuente.length>0) {
            var lexico=AnalizadorLexico(codFuente)
            lexico.Analizar()

            tblTokens.items=FXCollections.observableArrayList(lexico.listaTokens)
        }
        escribirDatos(codFuente)
    }

    fun leerDatos(){
        try {


            var path:String=System.getProperty("java.io.tmpdir")+File.separator+"codFuente.txt"
            var archivo: File = File(path);

            if (archivo.exists()) {

            var scanner: Scanner = Scanner(archivo);
            var linea: String = "";
            while (scanner.hasNextLine()) {
                linea += scanner.nextLine();

            }
            taCodigoFuente.text = linea
                scanner.close()
            }
        }
        catch (e:Exception){
            taCodigoFuente.text="";
        }
    }

    fun escribirDatos(datos:String){
        var fichero:FileWriter?=null;
        try{
           // var ruta = "E:/archivo.txt";
            var path:String=System.getProperty("java.io.tmpdir")+File.separator+"codFuente.txt"
            var archivo: File = File(path);
             fichero= FileWriter(path);

            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            var pw:PrintWriter= PrintWriter(fichero)
            pw.println(datos)


    }catch (e:Exception){
            print("No se ha podido almacenar los datos")
     }finally {

            if (fichero != null){
                fichero.close()
            }
     }
    }
}