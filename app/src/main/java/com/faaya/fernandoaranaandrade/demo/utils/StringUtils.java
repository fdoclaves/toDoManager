package com.faaya.fernandoaranaandrade.demo.utils;

import android.content.Context;

import com.faaya.fernandoaranaandrade.demo.R;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public  static String fecha_final_ = "fecha_final_";
    public  static String faltan_ = "faltan_";
    public  static String semanas_ = "semanas_";
    public  static String faltan_una_semana_ = "faltan_una_semana_";
    public  static String dias_ = "dias_";
    public  static String falta_un_dia_ = "falta_un_dia_";
    public  static String meses_ = "meses_";
    public  static String fata_un_mes_ = "fata_un_mes_";
    public static String tiempo_finalizado = "tiempo_finalizado";

    public static final String SEMANAS = "semanas";
    public static final String DIAS = "días";
    public static final String MESES = "meses";

    public static Map<String, String> getMap(Context context){
        Map<String, String> map = new HashMap<>();
        map.put(falta_un_dia_, context.getString(R.string.falta_un_dia_));
        map.put(fecha_final_, context.getString(R.string.fecha_final));
        map.put(faltan_, context.getString(R.string.faltan_));
        map.put(faltan_una_semana_, context.getString(R.string.faltan_una_semana_));
        map.put(semanas_, context.getString(R.string.semanas_));
        map.put(dias_, context.getString(R.string.dias_));
        map.put(meses_, context.getString(R.string.meses_));
        map.put(fata_un_mes_, context.getString(R.string.fata_un_mes_));
        map.put(tiempo_finalizado, context.getString(R.string.tiempo_finalizado));

        map.put(SEMANAS, context.getString(R.string.SEMANAS));
        map.put(DIAS, context.getString(R.string.DIAS));
        map.put(MESES, context.getString(R.string.MESES));
        return map;
    }
}
