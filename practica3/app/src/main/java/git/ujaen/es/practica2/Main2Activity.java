package git.ujaen.es.practica2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SharedPreferences settings = getSharedPreferences("sesion", 0);

        String sesionid = settings.getString("SESION-ID", "");
        String expires = settings.getString("EXPIRES", "0000-00-00-00-00-00");

        //Llamamos al Gestor de fragmentos
        FragmentManager fm = getSupportFragmentManager();
        //Comenzamos la transacción de fragmentos
        FragmentTransaction ft = fm.beginTransaction();
        //Encontramos el fragmento principal de la aplicación
        Fragment f = fm.findFragmentById(R.id.main_frame);

        //Si antes no había ningún fragmento
        if(f==null){
            /**Creamos una nueva instancia del fragmento de autenticación, donde se inician los parámetros
             *
             * @see PasoParam.newInstance() Método donde se crea la nueva instancia del fragmento de autenticación
             */
            PasoParam p = PasoParam.newInstance();
            //Añadimos el fragmento al main_frame
            ft.add(R.id.main_frame, p);
            //Añadimos null a la pila hacia atrás
            ft.addToBackStack(null);
            //Ejecuta la transacción de fragmentos
            ft.commit();

        }

        //ListView de actividad 2, con explicación, toma de medidas, desconexión e historial de usuarios
        listview1();

    }

    public void listview1(){
        //String para listview con los títulos de los fragmentos
        final String[] opciones = { "Explicación", "Toma de medidas", "Desconexión", "Historial de usuarios"};

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

                    //Caso de posición 1, que es el fragmento de toma de medidas
                    case 1:
                        //Creamos una nueva instancia del fragmento de autenticación, donde se inician los parámetros
                        PasoParam p = PasoParam.newInstance();
                        //Reemplazamos el fragmento al main_frame
                        ft.replace(R.id.main_frame, p);
                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();
                        break;

                    //Caso de posición 2, que es el fragmento de desconexión
                    case 2:
                        //Limpiamos las preferencias compartidas
                        SharedPreferences settings = getSharedPreferences("sesion", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.commit();

                        //Mostramos el fragmento de autenticación
                        AuthFragment au = AuthFragment.newInstance("", "");
                        //Reemplazamos el fragmento ya existente por el de autenticación
                        ft.replace(R.id.main_frame, au);
                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();

                        //Para el desplegable de actividad 1 (en caso de abrirlo tras desconectar sin cerrar la aplicación)
                        listview2();
                        break;

                    //Caso de posición 3, que es el fragmento de historial
                    case 3:
                        Historial h = Historial.newInstance();
                        //Reemplazamos el fragmento ya existente por el del historial
                        ft.replace(R.id.main_frame, h);
                        //Añadimos null a la pila hacia atrás
                        ft.addToBackStack(null);
                        //Ejecuta la transacción de fragmentos
                        ft.commit();

                        //Para el desplegable de actividad 2
                        listview1();
                        break;
                }

            }
        });
    }

    //Para el desplegable de la actividad 1 en caso de abrirlo tras desconectar sin cerrar la aplicación
    public void listview2(){
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
            //Ejecuta la transacción de fragmentos
            ft.commit();
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
