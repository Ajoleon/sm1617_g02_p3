package git.ujaen.es.practica2;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

/**
 * Tarea asíncrona que se ancarga de activar un socket mediante una url
 *
 * Created by Antonio on 10/11/2016.
 */
public  class Autenticar extends AsyncTask<Autentication,Void,Sesion> {

    /**
     * Método que se encarga de iniciar datos antes de ejecutar la tarea asíncrona
     */
    @Override
    protected void onPreExecute() {
        Autentication datos = null;
        super.onPreExecute();
    }

    /**
     * Método que abre una hebra asíncrona dentrás de la interfaz de usuario
     *
     * @param params  Objeto de la clase Autentication
     * @return Objeto de la clase Sesion
     */
    @Override
    protected Sesion doInBackground(Autentication ... params) {
        //Iniciamos un objeto de la clase Sesion mediante su constructor
        Sesion sesion = new Sesion("","");

        try {
            //Usuario y contraseña de Autentication
            String mUser = params[0].mUser;
            String mPass = params[0].mPass;

            //URL del servidor
            URL url = new URL("http://www4.ujaen.es");

            //Inicio el socket
            Socket socket = new Socket(url.getHost(), 80);

            //Inicio de las clases especializadas para leer y escribir
            OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader is = new InputStreamReader(socket.getInputStream());

            //Escribo para enviar al socket
            os.write(new String("GET /~jccuevas/ssmm/autentica.php?user=" + mUser + "&pass=" + mPass + " HTTP/1.1\r\nhost:www4.ujaen.es\r\n\r\n"));
            os.flush();

            //Clase para almacenar en buffer
            BufferedReader in = new BufferedReader(is);

            //Inicio variables que voy a utilizar
            String inputline; //Línea de entrada
            String [] linea = null; //Línea donde almaceno la entrada en cadenas, separadas por &
            String [] sessionid =  null; //Id de sesion
            String [] expires = null; //Fecha en la que expira la sesion

            //Mientras que cada línea de entrada sea distinta de null
            while ((inputline = in.readLine()) != null) {
                //Separo las dos partes de la línea que estaban separadas por &
                linea = inputline.split("&");
            }

            //Establezco el id de sesion quitando la cabecera, que no me interesa
            sessionid = linea[0].split("SESION-ID=");
            //Establezco la fecha quitando la cabecera, que no me interesa
            expires = linea[1].split("EXPIRES=");

            //Los introduzco en el objeto de la clase sesion
            sesion.setmSessionId(sessionid[1]);
            sesion.setmExpires(expires[1]);

            //Cierro clases para escribir y leer
            os.close();
            in.close();

            //Cierro socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Devuelve el objeto de la clase sesión
        return sesion;
    }


}