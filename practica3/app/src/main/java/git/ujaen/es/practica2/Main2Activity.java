package git.ujaen.es.practica2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
             * @see AuthFragment.newInstance() Método donde se crea la nueva instancia del fragmento de autenticación
             */
            PasoParam p = PasoParam.newInstance();
            //Añadimos el fragmento al main_frame
            ft.add(R.id.main_frame, p);
            //Añadimos null a la pila hacia atrás
            ft.addToBackStack(null);
            //Ejecuta la transacción de fragmentos
            ft.commit();

        }
    }
}
