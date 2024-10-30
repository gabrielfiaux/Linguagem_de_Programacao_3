package br.com.FuriniSolutions.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {

    public static Date dataAtual() {
        // Obtém a data atual
        Date dataAtual = Calendar.getInstance().getTime();
        // Converte java.util.Date para java.sql.Date, pois se não da erro
        return new java.sql.Date(dataAtual.getTime());
    }
    
    public static String formatarData(Date data) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(data);
    }

}
