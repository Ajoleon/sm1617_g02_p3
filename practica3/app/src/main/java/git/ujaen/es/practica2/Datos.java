package git.ujaen.es.practica2;

/**
 * @author Pablo Castillo Segura - Antonio José León Sánchez
 * @version: 15/11/2016
 *
 * La clase datos especifica los campos que se almacenarán en el mensaje
 * Tendrá un campo entero y otro de cadena de caracteres, que se utilizarán siempre en los mensajes
 *
 * Como modo alternativo, se podría montar un mensaje con el campo de datos con un string,
 * y en función de la cabecera, el servidor trata los campos como entero-entero, cadena-entero, cadena-cadena, etc
 */

public class Datos {
    /**Estos serán los atributos que utilizarán los datos
     * campo1: Campo que almacenará un entero
     * campo2: Campo que almacenará una cadena de caracteres
     *
     * Las restricciones de estos valores, se harán cuando se capturen en la interfaz,
     * ya que cada uno tiene unos limites diferentes
     */

    //Se inicializan los atributos
    int campo1 = 0;
    String campo2 = "";

    /**Constructor de la clase Datos
     *
     * @param c1 primer campo de los datos
     * @param c2 segundo campo de los datos
     */
    public Datos (int c1, String c2){
        this.campo1=c1;
        this.campo2=c2;
    }

    /**
     * Método para convertir los datos en cadena de caracteres
     *
     * @exception NumberFormatException para evitar error al pasar de entero a String en el campo 1
     * @return    los campos en una cadena de caracteres separados por un espacio
     */
    public String toString(){
        String c1="";
        String c2="";

        try{
            c1 = String.valueOf(campo1);
        }catch(NumberFormatException e){
            System.out.println("Error en los datos");
        }
        c2 = campo2;

        return c1+" "+c2;
    }

}
