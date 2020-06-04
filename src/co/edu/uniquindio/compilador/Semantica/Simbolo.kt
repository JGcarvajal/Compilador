package co.edu.uniquindio.compilador.Semantica

class Simbolo() {
    var nombre:String=""
    var tipo:String=""
    var modificable:Boolean=false
    var ambito:String=""
    var acceso:String?=""
    var fila:Int=0
    var columna:Int=0
    var tipoParametros:ArrayList<String>? = null

    /**
     * Contructor para crear un simbolo de tipo valor
     */
    constructor( nombre:String, tipodato:String, modificable:Boolean ,ambito:String,fila:Int , columna:Int, acceso:String?):this(){
        this.nombre=nombre
        this.tipo=tipodato
        this.modificable=modificable
        this.ambito=ambito
        this.fila=fila
        this.columna=columna
        this.acceso=acceso
    }

    /**
     * Contructor para crear un simbolo de tipo metodo
     */
    constructor( nombre:String, tipoRetorno:String, tipoParametros:ArrayList<String>,ambito:String,acceso:String):this(){
        this.nombre=nombre
        this.tipo=tipoRetorno
        this.tipoParametros=tipoParametros
        this.ambito=ambito
        this.acceso=acceso
    }

    override fun toString(): String {

        return if (tipoParametros == null) {
             "Simbolo(nombre='$nombre', tipo='$tipo', modificable=$modificable, ambito='$ambito', acceso='$acceso', fila=$fila, columna=$columna),\n"
        } else {
             "Simbolo(nombre='$nombre', tipo='$tipo', modificable=$modificable, acceso='$acceso', tipoParametros=$tipoParametros),\n"
        }
    }


}