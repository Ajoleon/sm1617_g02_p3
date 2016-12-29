package git.ujaen.es.practica2;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


/**
 * Clase que extiende de la clase fragmento.
 * Sirve para manejar el fragmento de autenticación.
 * @seee git.ujaen.es.practica2.R.layout.fragment_auth Fragmento de autenticación
 *
 */
public class AuthFragment extends Fragment {
    //Inicialización de los parámetros del bundle
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Inicialización de los parámetros de usuario y contraseña
    private String mUser = "";
    private String mPass = "";
    //Inicialización del objeto de la clase Autentication
    private Autentication mAutentica = new Autentication("","");
    //Inicialización del objeto de la clase Sesion
    private Sesion sesion = new Sesion("","");

    //Inicialización de las casillas de usuario y contraseña
    private EditText mEditUser = null;
    private EditText mEditPass = null;

    public AuthFragment() {
        // Required empty public constructor
    }

    /**
     * Método para crear una instancia del fragmento
     *
     * @param user Usuario
     * @param pass Contraseña
     * @return Nueva instancia del fragmento de autenticación
     */
    public static AuthFragment newInstance(String user, String pass) {
        //Nuevo fragmento
        AuthFragment fragment = new AuthFragment();

        //Llamo a la clase Bundle
        Bundle args = new Bundle();

        //Establezco los parámetros del Bundle en usuario y contraseña
        args.putString(ARG_PARAM1, user);
        args.putString(ARG_PARAM2, pass);
        //Establezco el usuario y contraseña en el fragmento
        fragment.setArguments(args);

        //Devuelvo el fragmento
        return fragment;
    }

    /**
     * Método al que se llama al crear el fragmento
     *
     * @param savedInstanceState Objeto de la clase Bundle que almacena los parámetros a colocar en la vista
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Si no hay parámetros
        if(savedInstanceState==null) {
            //Si no hay parámetros en el fragmento
            if (getArguments() != null) {
                //Establezco los valores predeterminados en los edittext
                mUser = getArguments().getString(ARG_PARAM1);
                mPass = getArguments().getString(ARG_PARAM2);
                //Establezco los valores predeterminados en el objeto de la clase autentication
                mAutentica.setmUser(mUser);
                mAutentica.setmPass(mPass);
            }
        }
    }

    /**
     * Método al que se llama al crear o recrear la vista
     *
     * @param inflater Objeto de la clase LayoutInflater que permite inflar la vista
     * @param container Objeto de la clase viewgroup que contiene toda la vista
     * @param savedInstanceState Objeto de la clase Bundle que almacena los parámetros a colocar en la vista
     * @return La vista creada
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Si hay valores en el Bundle
        if(savedInstanceState!=null){
            //Se establecen en la clase autentication. Permite guardar parámetros en un cambio de configuración
            mAutentica.setmUser(savedInstanceState.getString(ARG_PARAM1));
            mAutentica.setmPass(savedInstanceState.getString(ARG_PARAM2));
        }

        //Infla la vista con el fragmento de autenticación
        final View fragmento = inflater.inflate(R.layout.fragment_auth, container, false);

        //Va redibujando el fragmento a medida que se va escribiendo en los campos
        redibuja(fragmento);

        //Botón con el método al que se llama al pulsar.
        Button boton = (Button) fragmento.findViewById(R.id.auth_button_send);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtiene los valores que hay en los campos de EditText
                mEditUser = (EditText) fragmento.findViewById(R.id.auth_edit_user);
                mEditPass = (EditText) fragmento.findViewById(R.id.auth_edit_pass);

                //Los establece en la clase autentication
                mAutentica.setmUser(mEditUser.getEditableText().toString());
                mAutentica.setmPass(mEditPass.getEditableText().toString());


                //Los redibuja en el fragmento
                mEditUser.setText(mAutentica.getmUser());
                mEditPass.setText(mAutentica.getmPass());

                //Método que comprueba si el dispositivo dispone de conexión a Internet
                Boolean online = isOnline();

                //Si está online
                if(online) {
                    //Llamo a la tarea asíncrona para el socket
                    Autenticar aut = new Autenticar(getContext());

                    try {
                        //Almaceno los parámetros en un objeto de la clase autentication
                        Autentication a = new Autentication(mAutentica.getmUser(), mAutentica.getmPass());
                        //Método para iniciar la tarea asíncrona con el objeto de la clase Autentication, devolviendo objeto de la clase sesion
                        sesion = aut.execute(a).get();

                        //Si la sesión es 401 es porque el usuario o la clave eran erróneos, y si es null es porque no se ha iniciado el socket
                        if(!Objects.equals(sesion.getmSessionId(), "401")&& sesion.getmSessionId()!=null) {
                            //Llamo al método para establecer preferencias compartidas
                            SharedPreferences settings = getActivity().getSharedPreferences("sesion", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            //Almaceno en el editor el identificador de sesion y la fecha en la que expira
                            editor.putString("SESION-ID", sesion.getmSessionId());
                            editor.putString("EXPIRES", sesion.getmExpires());
                            editor.putString("USER",mAutentica.getmUser());
                            editor.putString("PASS",mAutentica.getmPass());
                            //Almaceno las preferencias compartidas
                            editor.commit();

                            //Muestra por consola
                            System.out.println("SESION-ID: " + sesion.getmSessionId());
                            System.out.println("EXPIRES: " + sesion.getmExpires());
                            System.out.println("USER: " + mAutentica.getmUser());
                            System.out.println("PASS: " + mAutentica.getmPass());

                            //Llamo a la actividad 2 y la inicio
                            Intent intent = new Intent(getActivity(), Main2Activity.class);
                            startActivity(intent);

                            String texto = "";//Inicializo texto donde guardaremos lo que contiene el archivo

                            texto = leerArchivo();//Recogemos lo del archivo

                            try{
                                //Clase para escribir en archivo
                                OutputStreamWriter osw = new OutputStreamWriter(getContext().openFileOutput("historial", MODE_PRIVATE));

                                //Escribimos los leído
                                osw.write(texto);
                                //Escribimos Usuario del objeto de la clase Autentication
                                osw.write(mAutentica.getmUser());
                                //Separamos de contraseña mediante espacio
                                osw.write(" ");
                                //Escribimos Contraseña del objeto de la clase Autentication
                                osw.write(mAutentica.getmPass());
                                //Finalizamos con &&, que utilizaremos para separar las tuplas
                                osw.write("&&");

                                //Cerramos la clase
                                osw.close();
                            }catch(IOException e){ }

                        }else if(Objects.equals(sesion.getmSessionId(), "401")){
                            Toast.makeText(getActivity(), "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "No es posible conectarse al servidor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Interrupted exception");
                    } catch (ExecutionException e) {
                        System.out.println("Execution exception");
                        e.printStackTrace();
                    }

                }else{
                    //Pido que active la conexión a internet si no dispone de ella
                    Toast.makeText(getActivity(), "Active la conexión a Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Devuelvo la vista
        return fragmento;
    }

    /**
     * Método que redibuja los campos cada vez que se cambia de foco
     *
     * @param fragmento Vista del fragmento de autenticación
     */
    private void redibuja(View fragmento){
        //Obtiene los valores que hay en los campos de EditText
        mEditUser = (EditText) fragmento.findViewById(R.id.auth_edit_user);
        mEditPass = (EditText) fragmento.findViewById(R.id.auth_edit_pass);

        //Cuando pierde el foco, actualiza la variable
        mEditUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mAutentica.setmUser(mEditUser.getEditableText().toString());
            }
        });
        mEditPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mAutentica.setmPass(mEditPass.getEditableText().toString());
            }
        });

        //Los redibuja en el fragmento
        mEditUser.setText(mAutentica.getmUser());
        mEditPass.setText(mAutentica.getmPass());

    }

    /**
     * Método para guardar las variable tras un recreado de la actividad
     *
     * @param outState Objeto de la clase Bundle que almacena los valores
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Almacenamos en el Bundle los valores del objeto de la clase Autentication
        outState.putString(ARG_PARAM1, mAutentica.getmUser());
        outState.putString(ARG_PARAM2, mAutentica.getmPass());

    }

    /**
     * Método para saber si el dispositivo está online
     *
     * @return Si está o no online
     */
    public boolean isOnline() {
        //Clase que gestiona la conectividad
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Clase para obtener información de la red
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //Si la red está activa o conectándose
        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){
            //Devuelve true
            return true;
        }else{
            //Devuelve false
            return false;
        }
    }

    /**
     * Método para leer de un archivo
     *
     * @return Cadena de caracteres con la información del archivo
     */
    public String leerArchivo(){
        String texto="";
        //Inicio clase para leer
        InputStreamReader archivo = null;
        try {
            //Si encuentra el archivo historial, leo
            archivo = new InputStreamReader(getContext().openFileInput("historial"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            // Creamos un objeto buffer, en el que iremos almacenando el contenido del archivo
            BufferedReader br = new BufferedReader(archivo);
            texto = "";//Inicializamos texto
            try {
                while(br.ready()){//Miestras que haya en el buffer
                    texto = texto + br.readLine();//Voy concatenando con texto
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(NullPointerException e){}

        return texto;
    }
}
