package git.ujaen.es.practica2;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Tarea asíncrona que se ancarga de activar un socket mediante una url
 *
 * Created by Antonio on 10/11/2016.
 */
public  class Autenticar extends AsyncTask<Autentication,Void,Sesion> {

    private Socket socket;
    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "192.168.1.108";
    private Context mContext;


    public Autenticar (Context context){
        mContext = context;
    }
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
     * @param params Objeto de la clase Autentication
     * @return Objeto de la clase Sesion
     */
    @Override
    protected Sesion doInBackground(Autentication... params) {
        //Iniciamos un objeto de la clase Sesion mediante su constructor
        Sesion sesion = new Sesion("", "");

        String texto = leerArchivo(mContext);
        String[] line;
        line = texto.split(" ");

        try {
            SocketAddress sockaddr;
            if(Objects.equals(texto, "")) {
                System.out.println("Predeterminados");
                sockaddr = new InetSocketAddress(SERVER_IP, SERVERPORT);
            }else{
                System.out.println("Los del archivo");
                sockaddr = new InetSocketAddress(line[0], Integer.parseInt(line[1]));
            }
            socket = new Socket();
            int timeoutMs = 2000;   // 2 segundos para conectarse al servidor
            socket.connect(sockaddr, timeoutMs);

                try {
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    //Inicio variables que voy a utilizar
                    String inputline; //Línea de entrada
                    String[] linea = null; //Línea donde almaceno la entrada en cadenas, separadas por &
                    String[] sessionid = null; //Id de sesion
                    String[] expires = null; //Fecha en la que expira la sesion

                    inputline = inputStream.readLine();
                    System.out.println(inputline);
                    linea = inputline.split(" ");
                    System.out.println(linea[1]);

                    if (Objects.equals(linea[1], "200")) {//Mensaje de bienvenida porque ha conseguido conectar

                        Datos data = new Datos(Integer.parseInt(params[0].getmUser()), params[0].getmPass());
                        Mensaje m = new Mensaje(1, data);
                        outputStream.write(m.toByteArray());
                        outputStream.flush();


                        inputline = inputStream.readLine();
                        System.out.println(inputline);
                        linea = inputline.split(" ");
                        System.out.println(linea[1]);
                        if (Objects.equals(linea[1], "200")) {
                            linea = linea[2].split("&");

                            //Establezco el id de sesion quitando la cabecera, que no me interesa
                            sessionid = linea[0].split("SESION-ID=");
                            //Establezco la fecha quitando la cabecera, que no me interesa
                            expires = linea[1].split("EXPIRES=");

                            //Los introduzco en el objeto de la clase sesion
                            sesion.setmSessionId(sessionid[1]);
                            sesion.setmExpires(expires[1]);

                        } else if (Objects.equals(linea[1], "401")) {//Si son incorrectos, se pone el id de sesion a 401 para que lo sepa
                            System.out.println("Usuario o clave incorrectos");
                            sesion.setmSessionId("401");
                        }
                    }
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                } finally {
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        } catch (UnknownHostException e) {
            System.out.println("Host desconocido");
            e.printStackTrace();
        } catch (IOException e) {
            sesion.setmSessionId(null);
            System.out.println("IO exception");
            e.printStackTrace();
        }

        //Devuelve el objeto de la clase sesión
        return sesion;

/*Por si se quiere enviar la respuesta a un textview
    @Override
    protected void onPostExecute(Void result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }*/

    }
    public String leerArchivo(Context c){
        String texto="";
        try{
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    c.openFileInput("configuracion")));

            texto = fin.readLine();
            fin.close();
        }catch (Exception ex){
            System.out.println("Error al leer fichero desde memoria interna");
        }

        return texto;
    }

}
