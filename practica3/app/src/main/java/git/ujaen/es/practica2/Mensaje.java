package git.ujaen.es.practica2;

/**
 * @author Pablo Castillo Segura - Antonio José León Sánchez
 * @version: 15/11/2016
 *
 * La clase Mensaje define los mensajes que se van a utilizar en el protocolo
 * Tendrán una cabecera y un campo de datos, que se forma por un entero y una cadena de caracteres.
 * Se implementarán también los métodos toByteArray y toArrayByte que se utilizarán para montar el mensaje
 * y para leerlo, respectivamente.
 */

public class Mensaje {
    /**AUTH, POST y GRPH son las cabeceras que se van a enviar en el mensaje
     *
     * AUTH: (Authentification) Cabecera para el mensaje de autentificación que envía el cliente
     * POST: (Post measure) Cabecera para el mensaje de añadir una nueva medida
     * GRPH: (Graphic) Cabecera para pedir al servidor que devuelva una lista
     * RTRN: (Return) Cabecera para enviar datos del servidor al cliente
     * CRLF: Final de línea y retorno de carro
     */
    public static final String AUTH = "AUTH";
    public static final String POST = "POST";
    public static final String GRPH = "GRPH";
    public static final String RTRN = "RTRN";
    public static final String CRLF = "\r\n";


    //Se inicializan las variables cabecera y datos, atributos del mensaje
    String header = "";
    Datos data = null;

    /**Constructor de la clase Mensaje
     *
     * @param opcion es un entero que se obtiene de la interfaz y se utiliza para añadir la cabecera al mensaje
     * @param data es un campo de datos, definido en la clase Datos
     * @see git.ujaen.es.practica2.Datos Referencia a la clase Datos, que se utiliza en el mensaje
     */
    public Mensaje (int opcion, Datos data){
        //En función de la opción que marque el cliente en su interfaz, se añadirá una cabecera u otra al mensaje
        switch (opcion){
            case 1:
                this.header = AUTH;
                break;
            case 2:
                this.header= POST;
                break;
            case 3:
                this.header = GRPH;
                break;
            case 4:
                this.header = RTRN;
                break;
        }
        this.data=data;
    }

    /**Método para convertir la cadena de caracteres del mensaje en los bytes que se van a enviar
     *
     * @return   los bytes que se envían como mensaje
     */
    public byte[] toByteArray(){
        String m = header+" "+data.toString()+CRLF;
        return m.getBytes();
    }

    /**Método para convertir los bytes del mesaje a cadena de caracteres
     *
     * @param bytes son los bytes del mensaje que se transformarán
     * @return   la cadena de caracteres que se recibe
     */
    public String toArrayByte(Byte[] bytes){
        String m = bytes.toString();
        return m;
    }

}
