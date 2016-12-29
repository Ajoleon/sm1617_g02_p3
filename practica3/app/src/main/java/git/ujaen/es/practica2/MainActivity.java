package git.ujaen.es.practica2;


import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Obtengo las preferencias
        SharedPreferences settings = getSharedPreferences("sesion", 0);

        String sesionid = settings.getString("SESION-ID", "");
        String expires = settings.getString("EXPIRES", "0000-00-00-00-00-00");
        System.out.println(expires);

        //Defino el formato de fecha y la clase Fecha para obtener la actual
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        //Inicializo variables para la fecha
        Date fecha = null;
        Date fechaactual = new Date();

        //Convierto la fecha en la que expira al formato buscado
        try {
            fecha = dateFormat.parse(expires);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Convierto la fecha actual al formato buscado
        try {
            fechaactual = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Fecha actual "+fechaactual+ " Fecha "+fecha);

        //Si la fecha actual a la fecha en la que expira la sesión
        if (fechaactual.after(fecha)) {

            //Inicio gestor de fragmentos
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = fm.findFragmentById(R.id.main_frame);

            //Si no hay ningún fragmento
            if (f == null) {
                //Añado a la vista el fragmento de Autenticación
                AuthFragment au = AuthFragment.newInstance("","");
                ft.add(R.id.main_frame, au);
                ft.addToBackStack(null);
                ft.commit();
            }

        } else {//Si todavía se mantiene la sesión
            //Paso a la segunda actividad
            Intent intent= new Intent(this,Main2Activity.class);
            startActivity(intent);
        }

        //Listview de la actividad 1, con explicación, autenticación, e historial de usuarios
        listview();
    }

    public void listview(){
        //String para listview con los títulos de los fragmentos
        final String[] opciones = { "Explicación", "Autenticación", "Historial de usuarios"};

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
                        //Se inicializa una nueva instancia del fragmento de explicación
                        Explanation e = Explanation.newInstance();
                        //Se añade fragmento de explicación
                        ft.replace(R.id.main_frame, e);
                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();
                        break;

                    //Caso de posición 1, que es el fragmento de autenticación
                    case 1:
                        //Creamos una nueva instancia del fragmento de autenticación, donde se inician los parámetros
                        AuthFragment au = AuthFragment.newInstance("", "");
                        //Reemplazamos el fragmento ya existente por el de autenticación
                        ft.replace(R.id.main_frame, au);

                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();
                        break;

                    //Caso de posición 2, que es el fragmento del historial de login
                    case 2:
                        Historial h = Historial.newInstance();
                        //Reemplazamos el fragmento ya existente por el del historial
                        ft.replace(R.id.main_frame, h);
                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();
                        break;
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        boolean result = super.onCreateOptionsMenu(menu);

        return result;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Llamamos al Gestor de fragmentos
        FragmentManager fm = getSupportFragmentManager();
        //Comenzamos la transacción de fragmentos
        FragmentTransaction ft = fm.beginTransaction();
        //Encontramos el fragmento principal de la aplicación
        Fragment f = fm.findFragmentById(R.id.main_frame);

        if (id == R.id.main_menu_help) {
            //Se inicializa una nueva instancia del fragmento de explicación
            Explanation e = Explanation.newInstance();
            //Se añade fragmento de explicación
            ft.replace(R.id.main_frame, e);
            //Añadimos null a la pila hacia atrás
            ft.addToBackStack(null);
            ft.commit();
            //Ejecuta la transacción de fragmentos
            return true;
        }else if(id == R.id.main_menu_configuration){

            String texto = leerArchivo();
            System.out.println("Texto antes del fragmento:"+texto+".");

            String[] linea;
            linea = texto.split(" ");

            Configuracion con;

            if(!Objects.equals(texto, "")) {
                System.out.println("Del archivo");
                con = Configuracion.newInstance(linea[0],linea[1]);
            }else{
                System.out.println("De lo predeterminado");
                //Se inicializa una nueva instancia del fragmento de configuracion
                con = Configuracion.newInstance("192.168.1.108","5000");
            }

            //Se añade fragmento de explicación
            ft.replace(R.id.main_frame, con);

            //Añadimos null a la pila hacia atrás
            ft.addToBackStack(null);
            ft.commit();
            //Ejecuta la transacción de fragmentos
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String leerArchivo(){
        String texto="";
        try{
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("configuracion")));

            texto = fin.readLine();
            fin.close();
        }catch (Exception ex){
            System.out.println("Error al leer fichero desde memoria interna");
        }

        return texto;
    }

}




