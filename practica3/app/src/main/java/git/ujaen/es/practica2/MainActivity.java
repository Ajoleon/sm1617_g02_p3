package git.ujaen.es.practica2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences("sesion2", 0);
        String sesionid = settings.getString("SESION-ID", "");
        String expires = settings.getString("EXPIRES", "0000-00-00-00-00-00");
        System.out.println(expires);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date fecha = null;
        Date fechaactual = new Date();
        System.out.println("aqui");
        try {
            System.out.println("entro");
            fecha = dateFormat.parse(expires);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("entro2");
            fechaactual = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fechaactual.after(fecha)) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment f = fm.findFragmentById(R.id.main_frame);
            if (f == null) {//Si no es null es que había un fragmento
                AuthFragment au = AuthFragment.newInstance("","");
                ft.add(R.id.main_frame, au);
                ft.addToBackStack(null);
                ft.commit();

            }
        } else {
            Intent intent= new Intent(this,Main2Activity.class);
            startActivity(intent);
        }
        listview();
    }
    public void listview(){
        //String para listview con los títulos de los fragmentos
        final String[] opciones = { "Explicación", "Autenticación"};

        //Creamos el adaptador
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones);

        //Encontramos ListView del fragmento
        final ListView listView = (ListView) findViewById(R.id.list_view);
        //Establecemos adaptador para ese ListView
        listView.setAdapter(adaptador);

        //Establecemos un escuchador de click en los items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**Método al hacer click en un item de la lista
             *
             * @param a     Adaptador del ListView
             * @param v     Vista
             * @param position  Posición del item clickado
             * @param id    id del item
             */
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id){
                //Llamamos al Gestor de fragmentos
                FragmentManager fm = getSupportFragmentManager();
                //Comenzamos la transacción de fragmentos
                FragmentTransaction ft = fm.beginTransaction();
                //Encontramos el fragmento principal de la aplicación
                Fragment f = fm.findFragmentById(R.id.main_frame);

                //Switch para los distintos casos de los items del ListView. La entrada es la posición del item clickado
                switch (position) {

                    //Caso de posición 0, que es del fragmento de explicación
                    case 0:
                        //if(autenticado) {
                        //Se inicializa una nueva instancia del fragmento de explicación
                        Explanation e = Explanation.newInstance();

                        //Se añade fragmento de explicación
                        ft.replace(R.id.main_frame, e);

                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();

                        //Establecemos autenticado a false
                        //    autenticado = false;
                        //}

                        break;

                    //Caso de posición 1,
                    case 1:
                        //Si no se ha accedido al fragmento de autenticado, para que no haya
                        //un bug en el recreado del fragmento el cambio a la otra vista lo realice
                        //if(!autenticado){
                        //Creamos una nueva instancia del fragmento de autenticación, donde se inician los parámetros
                        AuthFragment au = AuthFragment.newInstance("", "");
                        //Reemplazamos el fragmento ya existente por el de autenticación
                        ft.replace(R.id.main_frame, au);

                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();

                        //Establecemos autenticado a true porque ha entrado en el fragmento de autenticación
                        //    autenticado = true;
                        //}

                        break;
                }

                //Obtenemos texto del item en la posición clickada
                //String texto = String.valueOf(a.getItemAtPosition(position));

                //Mostramos tostada con el texto y la posición
                //Toast.makeText(MainActivity.this, texto +", con posicion: "+ position, Toast.LENGTH_SHORT).show();

            }
        });
    }



}




