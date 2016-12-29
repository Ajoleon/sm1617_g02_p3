package git.ujaen.es.practica2;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pablo on 29/12/2016.
 */

public class Configuracion extends android.support.v4.app.Fragment {
    //Inicialización de los parámetros del bundle
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String direccion = "";
    private String puerto = "";

    private EditText mdireccion = null;
    private EditText mpuerto = null;


    public static Configuracion newInstance(String dir, String num){
        Configuracion fragment = new Configuracion();

        //Llamo a la clase Bundle
        Bundle args = new Bundle();

        //Establezco los parámetros del Bundle en usuario y contraseña
        args.putString(ARG_PARAM1, dir);
        args.putString(ARG_PARAM2, num);

        //Establezco el usuario y contraseña en el fragmento
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Si no hay parámetros
        if(savedInstanceState==null) {
            //Si no hay parámetros en el fragmento
            if (getArguments() != null) {

                //Establezco los valores predeterminados en los edittext
                direccion = getArguments().getString(ARG_PARAM1);
                puerto = getArguments().getString(ARG_PARAM2);
                //Establezco los valores predeterminados en el objeto de la clase autentication


            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Si hay valores en el Bundle
        if(savedInstanceState!=null){
            //Se establecen en la clase autentication. Permite guardar parámetros en un cambio de configuración
            direccion = savedInstanceState.getString(ARG_PARAM1);
            puerto = savedInstanceState.getString(ARG_PARAM2);
        }

        //Infla  el contenedor con el fragmento de explicación
        final View fragmento = inflater.inflate(R.layout.fragment_configuration, container, false);

        redibuja(fragmento);

        //Botón con el método al que se llama al pulsar.
        Button boton = (Button) fragmento.findViewById(R.id.test_button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtiene los valores que hay en los campos de EditText
                mdireccion = (EditText) fragmento.findViewById(R.id.ip);
                mpuerto = (EditText) fragmento.findViewById(R.id.puerto);

                //Los establece en la clase autentication
                direccion = mdireccion.getEditableText().toString();
                puerto = mpuerto.getEditableText().toString();

                //Los redibuja en el fragmento
                mdireccion.setText(direccion);
                mpuerto.setText(puerto);

                String texto = direccion +" "+ puerto;
                System.out.println(texto);

                try
                {
                    OutputStreamWriter fout=
                            new OutputStreamWriter(
                                    getContext().openFileOutput("configuracion", MODE_PRIVATE));
                    fout.write(texto);
                    fout.close();
                    Toast.makeText(getActivity(), "Configuración cambiada", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    System.out.println("Error al escribir fichero a memoria interna");
                }
            }
        });
        //Devuelvo la vista
        return fragmento;
    }

    private void redibuja(View fragmento){
        //Obtiene los valores que hay en los campos de EditText
        mdireccion = (EditText) fragmento.findViewById(R.id.ip);
        mpuerto = (EditText) fragmento.findViewById(R.id.puerto);

        //Cuando pierde el foco, actualiza la variable
        mdireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                direccion = mdireccion.getEditableText().toString();
            }
        });
        mpuerto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                puerto = mpuerto.getEditableText().toString();
            }
        });

        //Los redibuja en el fragmento
        mdireccion.setText(direccion);
        mpuerto.setText(puerto);

    }


}
