package git.ujaen.es.practica2;

/**
 * @author Pablo Castillo Segura - Antonio José León Sánchez
 * @version: 15/11/2016
 *
 * Interfaz de Cliente en la que se declaran los métodos a implementar
 *
 */

public interface  Cliente {

    /**En esta clase, se trabaja con los atributos campo1 y campo2 y se devuelve un entero, que será la respuesta del servidor
     *
     * @param dni el DNI de la persona a autentificar
     * @param clave la contraseña de esa persona
     * @return    respuesta que, en función del valor, significará OK, ERROR en usuario, ERROR en clave
     */
    public int Autenticacion (int dni, String clave);

    /**En esta clase no se utilizarán los campos, simplemente, se utiliza para liberar la conexión
     *
     * @return    respuesta que, en función del valor, significará OK o ERROR
     */
    public Boolean Liberacion();

    /**Aquí se debe implementar código para añadir la medida tomada a una nueva entrada de la base de datos
     * Devuelve código de OK ,ERROR, o alguna opción más si fuese necesaria
     *
     * @param medida es la toma de los niveles de azúcar, que es un entero
     * @param franja es el intervalo horario donde se deben anotar los niveles
     * @return    respuesta que, en función del valor, significará OK o ERROR
     */
    public int Tomarmuestra(int medida, String franja);

    /**Método en el que se trabaja con los campos para devolver una lista de las medidas
     *
     * @param numerotomas es la cantidad de medidas que quieres que devuelva el servidor
     * @param franja es el intervalo horario donde se deben anotar los niveles
     * @return    una lista de enteros con las medidas para la franja indicada en el mensaje
     */
    public String[] Listar(int numerotomas, String franja);

}
