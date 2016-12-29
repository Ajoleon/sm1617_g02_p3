package git.ujaen.es.practica2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Created by Pablo on 24/11/2016.
 */

public class PasoParam extends Fragment {

    private Socket socket;
    private static final int SERVERPORT = 6000;
    private static final String SERVER_IP = "192.168.1.108";
    private static Boolean existesocket = false;

    private EditText medida = null;
    private String valormedida = "";
    private String valorfranja ="";

    Handler uiHandler;

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

        redibuja(fragmento);

        Button boton = (Button) fragmento.findViewById(R.id.test_button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                uiHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch(msg.what){
                        case -2:
                            Toast.makeText(getActivity(), "No ha sido posible conectar con el servidor", Toast.LENGTH_SHORT).show();
                            break;
                        case -1:
                            Toast.makeText(getActivity(), "Ya existe medida para esa franja y día", Toast.LENGTH_SHORT).show();
                            break;
                        case 0:
                            Toast.makeText(getActivity(), "Medida aceptada", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Medida dentro de límites", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Controlarse con la comida", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Tomar medicación", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(getActivity(), "Ir al médico", Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            Toast.makeText(getActivity(), "Inyectar insulina", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
                valormedida = medida.getText().toString();
                new Thread(new ClientThread()).start();
            }
        });

        Spinner spinner = (Spinner) fragmento.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.franjas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position){
                    case 0:
                        valorfranja = "Ayuno";
                        break;
                    case 1:
                        valorfranja = "Desayuno";
                        break;
                    case 2:
                        valorfranja = "Almuerzo";
                        break;
                    case 3:
                        valorfranja = "Merienda";
                        break;
                    case 4:
                        valorfranja = "Cena";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return fragmento;
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {

                String texto = leerArchivo(getContext());
                String[] line;
                line = texto.split(" ");

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
                    System.out.println("Existe socket");
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    SharedPreferences settings = getActivity().getSharedPreferences("sesion", 0);

                    String user = settings.getString("USER", "");
                    String pass = settings.getString("PASS", "");

                    System.out.println("Entrada1: " + inputStream.readLine());
                    Datos data = new Datos(Integer.parseInt(user), pass);
                    Mensaje m = new Mensaje(1, data);
                    outputStream.write(m.toByteArray());

                    System.out.println("Entrada2: " + inputStream.readLine());

                    System.out.println(valormedida + " " + valorfranja);
                    int i = -1;
                    try {
                        i = Integer.parseInt(valormedida);
                    } catch (NumberFormatException e) {
                    }

                    data = new Datos(i, valorfranja);
                    m = new Mensaje(2, data);
                    outputStream.write(m.toByteArray());

                    String inputline; //Línea de entrada
                    String[] linea; //Línea donde almaceno la entrada en cadenas, separadas por &

                    inputline = inputStream.readLine();
                    System.out.println(inputline);
                    linea = inputline.split(" ");
                    System.out.println(linea[1]);
                    //SI la medida ha sido aceptada
                    if (Objects.equals(linea[1], "201")) {
                        Message msg = new Message();
                        msg.what = 0;
                        uiHandler.sendMessageDelayed(msg, 0);

                        switch (linea[2]) {
                            case "1":
                                msg = new Message();
                                msg.what = 1;
                                uiHandler.sendMessageDelayed(msg, 0);
                                break;
                            case "2":
                                msg = new Message();
                                msg.what = 2;
                                uiHandler.sendMessageDelayed(msg, 0);
                                break;
                            case "3":
                                msg = new Message();
                                msg.what = 3;
                                uiHandler.sendMessageDelayed(msg, 0);
                                break;
                            case "4":
                                msg = new Message();
                                msg.what = 4;
                                uiHandler.sendMessageDelayed(msg, 0);
                                break;
                            case "5":
                                msg = new Message();
                                msg.what = 5;
                                uiHandler.sendMessageDelayed(msg, 0);
                                break;
                        }
                    } else if (Objects.equals(linea[1], "401")) {
                        Message msg = new Message();
                        msg.what = -1;
                        uiHandler.sendMessageDelayed(msg, 0);
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

            } catch (IOException e1) {
                existesocket = false;
                Message msg = new Message();
                msg.what = -2;
                uiHandler.sendMessageDelayed(msg, 0);
            }
        }
    }

    private void redibuja(View fragmento){
        //Obtiene los valores que hay en los campos de EditText
        medida = (EditText) fragmento.findViewById(R.id.auth_edit1);

        medida.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("Has focus");
                valormedida = medida.getText().toString();
            }
        });
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
