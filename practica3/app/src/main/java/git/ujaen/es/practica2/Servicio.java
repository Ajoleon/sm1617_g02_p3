package git.ujaen.es.practica2;

/**
 * @author Pablo Castillo Segura - Antonio José León Sánchez
 * @version: 15/11/2016
 *
 * Servicio es una clase abstracta que se implementará en el servidor, y de la cual heredarán las demás
 * Se utiliza para declarar los distintos métodos, que más tarde se implementarán
 *
 */

public abstract class  Servicio {

    //Estos son los campos del mensaje, que utilizarán los métodos para trabajar
    protected int campo1 = 0;
    protected String campo2 = "";

    /**Constructor de la clase Servicio que permite a los métodos utilizar los campos
     *
     * @param c1 primer campo del mensaje
     * @param c2 segundo campo del mensaje
     */
    public Servicio(int c1, String c2){
        this.campo1 = c1;
        this.campo2=c2;
    }

    /**En esta clase, se trabaja con los atributos campo1 y campo2 y se devuelve un entero, que será la respuesta del servidor
     *
     * @return    respuesta que, en función del valor, significará OK, ERROR en usuario, ERROR en clave
     */
    public abstract int Autenticacion();

    /**En esta clase no se utilizarán los campos, simplemente, se utiliza para liberar la conexión
     *
     * @return    respuesta que, en función del valor, significará OK o ERROR
     */
    public abstract Boolean Liberacion();

    /**Aquí se debe implementar código para añadir la medida tomada a una nueva entrada de la base de datos
     * Devuelve código de OK ,ERROR, o alguna opción más si fuese necesaria
     *
     * @return respuesta que, en función del valor, significará OK o ERROR
     */
    public abstract int tomarMuestra();

    /**Método en el que se trabaja con los campos para devolver una lista de las medidas
     *
     * @return   una lista de enteros con las medidas para la franja indicada en el mensaje
     */
    public abstract int[] Lista();

}
