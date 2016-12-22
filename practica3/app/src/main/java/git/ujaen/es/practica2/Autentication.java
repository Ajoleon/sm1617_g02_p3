package git.ujaen.es.practica2;

/**Clase para recoger los datos
 *
 * Created by Antonio on 29/09/2016.
 */
public class Autentication {

	//Al declararse protected no se pueden usar fuera de la clase, solo pueden usarlo las clases que heredan
    //Inicializo atributos de la clase
    protected String mUser="user";
    protected String mPass ="";
	
	//Constructor de la clase Autentication
    public Autentication(String User, String Pass) {
        this.mUser = User;
        this.mPass = Pass;
    }

    //Métodos para establecer o devolver el usuario y la contraseña
     public String getmUser() {
         return mUser;
     }

     public void setmUser(String mUser) {
         this.mUser = mUser;
     }

     public String getmPass() {
         return mPass;
     }

     public void setmPass(String mPass) {
         this.mPass = mPass;
     }
 }
