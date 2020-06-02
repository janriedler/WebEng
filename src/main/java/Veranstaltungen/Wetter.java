package Veranstaltungen;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Wetter {
    static public List<Veranstaltung> calc(List<Veranstaltung> vera) throws IOException {
        for (int i = 0; i < vera.size(); i++) {
            if (checkTime(vera.get(i).getDatum())) {
                String id = JsonFormat.getWoeid(vera.get(i).getOrt());
                if (!id.equals("noloc")) {
                    vera.get(i).setWetter(JsonFormat.getWetter(id, vera.get(i).getDatum()));
                }
            }

        }
        return vera;
    }

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

    public static void main(String[] args) {
        System.out.println(checkTime("06/03/2020"));
    }
}
