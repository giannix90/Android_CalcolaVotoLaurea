package com.example.gianni.calcolavotolaurea;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gianni on 16/07/16.
 */
class materia{

    String nome_materia;
    int CFU;
    int voto;
}

public class Voti {
    List<materia> Voti;
    double MediaPesata;
    int VotoLaurea;

    Voti(List<materia> list){

        /*Constructor*/
        Voti=list;
        MediaPesata=0;
        VotoLaurea=0;

    }

    public double getMediaPesata(){

        /*Return the media pesata*/

        int temp=0;
        double medp=0;
        int p=0;

        try {

            for (int i=0;i<Voti.size();i++){

                temp += Voti.get(i).CFU * Voti.get(i).voto;
                p += Voti.get(i).CFU;
                Log.e("Voti_class","temp: "+temp);

                Log.e("Voti_class","p: "+p);
            }
        }catch(java.util.NoSuchElementException e){
            //We have no more elements
            Log.d("Voti_class","No more elements in the iterators");
        }

        this.MediaPesata=medp=(double)temp/(double)p;

        return medp;
    }

    public int getVotoLaurea(int C,int R){
        /*
        * C is the commission rating
        * R is the relator rating
        *
        * */

        //Apply the algorithm

        Log.e("Voti_class","Media pesata: "+getMediaPesata());

        if(Voti.size()>0)
            return (int) (3*getMediaPesata()+22+C+R);
        else
            return 0;
    }

    public void addVoto(String name,int voto,int cfu){
        materia m=new materia();
        m.nome_materia=name;
        m.voto=voto;
        m.CFU=cfu;

        this.Voti.add(m);
    }

}
