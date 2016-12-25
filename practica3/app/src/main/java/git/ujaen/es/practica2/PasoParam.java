package git.ujaen.es.practica2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Pablo on 24/11/2016.
 */

public class PasoParam extends Fragment {

    private Socket socket;
    private static final int SERVERPORT = 6000;
    private static final String SERVER_IP = "192.168.1.108";

    /**Método para crear una nueva instancia del fragmento de explicación
     *
     * @return fragmento de explicación
     */
    public static PasoParam newInstance(){
        PasoParam fragment = new PasoParam();
        return fragment;
    }

    /**Método al que se llama al crear la vista
     *
     * @param inflater Necesario para inflar el fragmento con la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia de los parámetros guardados tras un recreado del fragmento (no lo vamos a utilizar)
     * @return la vista
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Infla  el contenedor con el fragmento de explicación
        final View fragmento = inflater.inflate(R.layout.fragment_paso, container, false);
            TextView tv1 = (TextView)fragmento.findViewById(R.id.textView1);
            tv1.setText("Aquí estará la toma de datos para su envío");

        Button boton = (Button) fragmento.findViewById(R.id.test_button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new Thread(new ClientThread()).start();
            }
        });
        return fragmento;
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
                try {
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    Datos data = new Datos(77777778,"pablo");
                    Mensaje m = new Mensaje(1,data);
                    outputStream.write(m.toByteArray());

                    System.out.println("Entrada1: "+ inputStream.readLine());

                    data = new Datos(155,"Desayuno");
                    m = new Mensaje(2,data);
                    outputStream.write(m.toByteArray());

                    System.out.println("Entrada2: "+ inputStream.readLine());

                    inputStream.close();
                    outputStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


}
