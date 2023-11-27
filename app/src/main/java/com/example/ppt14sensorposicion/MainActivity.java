package com.example.ppt14sensorposicion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText posicion; //EditText de Posicion
    private SensorManager sm; //Objeto de sensor Manager que se utiliza para gestionar los sensores del dispositivo
    private Sensor sa; //Objeto de tpo sensor que representa el sensor de acelerometro
    private SensorEventListener SEL; //Objeto tipo sensoreventlistener que se utiliza para escuchar los cambios del sensor
    private int latigo = 0; // Agregamos la variable latigo y la inicializamos a 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Se inicializan las variables posición sm y sa
        posicion = findViewById(R.id.et_posicion);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sa = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Se verifica que el dispositivo tiene un sensor de acelerometro
        //Si no lo posee muestra un Toast que informa que no hay sensor disponible
        //Si lo encuentra se crea un nuevo sensorevetlistener llamado SEL
        if(sa == null){
            Toast.makeText(this, "No hay Sensor", Toast.LENGTH_SHORT).show();
        }else{
            SEL = new SensorEventListener() { //Escuchará los cambios del sensor del acelerometro
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    posicion.setText(Float.toString(x));
                    //Cuando el valor del acelerometro es mayor que -5 y latigo es igual a 0 se cambia el color de fondo azul y aumenta el valor del latigo
                    if(x > -5 && latigo == 0){
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                        //Cuando el valor es mayor a 5 y latigo es igual a 1, se cambia el color de fondo a rojo y aumenta el latigo a 2
                    }else if (x > 5 && latigo ==1){
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }
                    //Cuando latigo es igual a 2 se llama el método sonido que reproduce un archivo de sonido
                    if(latigo == 2){
                        sonido();
                        latigo = 0;
                    }
                }


            };
        }
    }
    //Utilizzando el MediaPlayer se reproduce el sonido latigoSehldong
    private void sonido(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.latigosheldong);
        mp.start();
    }

    public void iniciar(){//Se utilizará para registrar el SEL para escuchar los eventos
        sm.registerListener(SEL, sa, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume(){//Se sobreescribe para iniciar la escucha del sensor cuando la actividad está en primer plano
        iniciar();
        super.onResume();
    }

    public void detener(){//Se utiliza para detener la escucha del sensor cuando la aplicación no está en primer plano
        sm.unregisterListener(SEL);
    }

    @Override
    protected void onStop(){//Se sobreescribe para detener la escucha del sensor cuando la actividad se detienen
        detener();
        super.onStop();
    }

}