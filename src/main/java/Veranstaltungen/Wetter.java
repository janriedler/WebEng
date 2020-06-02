package Veranstaltungen;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Wetter {

    /**
     *
     */
    static public List<Veranstaltung> calc(List<Veranstaltung> vera) throws IOException {
        for (int i = 0; i < vera.size(); i++) {
            if (checkTime(vera.get(i).getDatum())) { //wenn Zeit inner nächster Woche
                String id = JsonFormat.getWoeid(vera.get(i).getOrt()); //mit Klasse JsonFormat wird die ID von einem Ort bestimmt
                if (!id.equals("noloc")) { //loloc wird von JsonFormat zurückgegen wenn der Ort nicht gefunden wurde
                    vera.get(i).setWetter(JsonFormat.getWetter(id, vera.get(i).getDatum())); //mit Klasse JsonFormat wird mittels ID und Datum das Wetter bestimmt
                }
            }

        }
        return vera;
    }


    //prüft ob Datum innerhelb der nächten Woche liegt (Kann noch verbessert werden)
    static boolean checkTime(String datum){
        String inline = "";
        String[] zahl = datum.split("-");
        datum = zahl[1] + "/" + zahl[2] + "/" + zahl[0];
        System.out.println(datum);
        return ZonedDateTime
                .now()                           // Captures current moment as seen by the wall-clock time of the JVM’s current default time zone. Better to pass the optional `ZoneId` argument to specify explicitly the desired/expected time zone.
                .minusWeeks( 1 )
                .isBefore(
                        LocalDateTime
                                .parse(
                                        datum + " 00:01 PM" ,
                                        DateTimeFormatter.ofPattern( "MM/dd/uuuu hh:mm a" , Locale.US )
                                )
                                .atZone(
                                        ZoneId.systemDefault()   // Better to pass explicitly the time zone known to have been intended for this input. See discussion below.
                                )
                );
    }

}
