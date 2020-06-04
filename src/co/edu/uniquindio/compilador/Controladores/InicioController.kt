package co.edu.uniquindio.compilador.Controladores

import co.edu.uniquindio.compilador.Analizador_Lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.Analizador_Lexico.Token
import co.edu.uniquindio.compilador.Analizador_Sintactico.AnalizadorSintactico
import co.edu.uniquindio.compilador.Analizador_Sintactico.UnidadCompilacion
import co.edu.uniquindio.compilador.Miscelaneos.Error
import co.edu.uniquindio.compilador.Semantica.AnalizadorSemantico
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

    //Tbala tokens
    @FXML lateinit var tblTokens :TableView<Token>
    @FXML lateinit var clmLexema:TableColumn<Token,String>
    @FXML lateinit var clmCategoria:TableColumn<Token,String>
    @FXML lateinit var clmFila:TableColumn<Token,Int>
    @FXML lateinit var clmColumna:TableColumn<Token,Int>

    //Tabla Errores Sintacticos
    @FXML lateinit var tblErroresSintacticos :TableView<Error>
    @FXML lateinit var clMensajeES:TableColumn<Error,String>
    @FXML lateinit var clFilaES:TableColumn<Error,Int>
    @FXML lateinit var clColumnaES:TableColumn<Error,Int>
    @FXML lateinit var clCategoriaES:TableColumn<Error,String>

    //Errores Lexicos
    @FXML lateinit var tblErroresLexicos :TableView<Error>
    @FXML lateinit var clMensajeEL:TableColumn<Error,String>
    @FXML lateinit var clFilaEL:TableColumn<Error,Int>
    @FXML lateinit var clColumnaEL:TableColumn<Error,Int>
    @FXML lateinit var clCategoriaEL:TableColumn<Error,String>
    var unidadCompilacion:UnidadCompilacion?=null


    // Arbol Sintactico
        @FXML lateinit var tvArbol:TreeView<String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        clmLexema.cellValueFactory=PropertyValueFactory("lexema")
        clmCategoria.cellValueFactory=PropertyValueFactory("categoria")
        clmFila.cellValueFactory=PropertyValueFactory("fila")
        clmColumna.cellValueFactory=PropertyValueFactory("columna")

        clMensajeES.cellValueFactory=PropertyValueFactory("error")
        clFilaES.cellValueFactory=PropertyValueFactory("fila")
        clColumnaES.cellValueFactory=PropertyValueFactory("columna")
        clCategoriaES.cellValueFactory=PropertyValueFactory("categoria")

        clMensajeEL.cellValueFactory=PropertyValueFactory("error")
        clFilaEL.cellValueFactory=PropertyValueFactory("fila")
        clColumnaEL.cellValueFactory=PropertyValueFactory("columna")
        clCategoriaEL.cellValueFactory=PropertyValueFactory("categoria")

        leerDatos();
    }

    /**
     * Este metodo permite limpiar los campos y la tabla de la lista de tokens
     */
    @FXML
    fun Limpiar(){
        taCodigoFuente.text=""
        tblTokens.items=FXCollections.observableArrayList(ArrayList<Token>())
        tblErroresLexicos.items=FXCollections.observableArrayList(ArrayList<Error>())
        tblErroresSintacticos.items=FXCollections.observableArrayList(ArrayList<Error>())
        tvArbol.root=TreeItem()
    }

    /**
     * En este metodo se eenvia el codigo ingresado a analizar
     */
    @FXML
    fun AnalizarCodigo(e:ActionEvent){
        var codFuente=taCodigoFuente.text
        if (codFuente.length>0) {
            var lexico=AnalizadorLexico(codFuente)
            lexico.Analizar()

            tblTokens.items=FXCollections.observableArrayList(lexico.listaTokens)
            tblErroresLexicos.items=FXCollections.observableArrayList(lexico.listaErrores)

            if (lexico.listaErrores.isEmpty()) {
                var sintaxis = AnalizadorSintactico(lexico.listaTokens)
                unidadCompilacion = sintaxis.esUnidadCompilacion()

                tblErroresSintacticos.items=FXCollections.observableArrayList(sintaxis.listaErrores)

                if (unidadCompilacion != null) {
                    tvArbol.root = unidadCompilacion!!.getArbolVisual()

                    var semantica=AnalizadorSemantico(unidadCompilacion!!)
                    semantica.llenarTablaSimbolos()
                    semantica.analizarSentica()
                    println(semantica.tablaSimbolos)
                    println(semantica.erroresSemanticos)
                }
            }else{
                var alerta=Alert(Alert.AlertType.WARNING)

                alerta.headerText="Atención"
                alerta.contentText = "Hay errores lexicos en el codigo fuente"
            }
        }
        escribirDatos(codFuente)
    }

    @FXML
    fun TraducirCodigo(e:ActionEvent){
        if (unidadCompilacion != null) {
           var codigoTraducido= unidadCompilacion!!.getJavaCod()
            print(codigoTraducido)
        }

    }

    /**
     * Este metodo nos permite cargar datos que se hayan almacenado anteriormente
     */
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

    /**
     * Este metodo permite almacenar lo que se ha ingresado en la entrada de codigo
     */
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