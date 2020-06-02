package Veranstaltungen.Controller;

import Veranstaltungen.Start;
import Veranstaltungen.VeraJdbcRepository;
import Veranstaltungen.Veranstaltung;
import Veranstaltungen.Wetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class Main {
    /**
     * Der Listener fängt die Startseite "http://localhost:8080" ab und leitet weiter an resources -> templates -> verlist.html (Tymeleaf)
     * d.h es wird die Startseite angezeigt
     * Dazu wird eine Liste mit allen Veranstaltungen in der Zukunft erstellt, auf 20 Einträge reduziert und die dann der
     * Startseite mitübergeben
     *
     * Dazu wird Wetter.calc gestartet, wodurch das Wetter zu jedem Ort berechnet wird
     */
    @GetMapping()
    public String showAll(Model model) throws IOException, ParseException {
        List<Veranstaltung> ver = new ArrayList<>(repository.findAll());
        List<Veranstaltung> future = new ArrayList<>();
        for (Veranstaltung veranstaltung : ver) {
            if (checkFuture(veranstaltung.getDatum())) {
                future.add(veranstaltung);
            }
        }
        while (ver.size() > 20) {
            ver.remove(0);
        }
        model.addAttribute("veranstaltungen", Wetter.calc(future));
        return "verlist";
    }


    /**
     * Der Listener fängt "http://localhost:8080/add" ab und leitet weiter an resources -> templates -> add.html (Tymeleaf)
     * d.h es wird die add.html Seite angezeigt
     */
    @GetMapping("add")
    public String form() {
        return "add";
    }

    /**
     * holt die selbstgeschrieben  VeraJdbcRepository Klasse, wodurch die dortigen Methoden für Datenbanken abfragen
     * verwendet werden können
     */
    @Autowired
    VeraJdbcRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }



    /**
     * die Startseite (verlist.html) hat post request hier hin gesendet. Dieser beinhalten die gewünschte Art
     * nach der die Veranstaltung sotiert werden soll. Die wird dann hier genommen, eine entsprechende Liste erstellt
     * und dann wieder an die Startseite gesendet --> Jetzt wird dort nur noch Veranstaltungen von einer Art gezeigt
     */
    @RequestMapping("sort")
    public String showAll2(Model model, @RequestParam String sort) throws IOException {
        List<Veranstaltung> ver = new ArrayList<>();
        if (sort.equals("Alle (auch Vergangenheit)")) {
            ver.addAll(repository.findAll());
            while (ver.size() > 20) {
                ver.remove(0);
            }
            model.addAttribute("veranstaltungen", Wetter.calc(ver));
            return "verlist";
        }
        ver.addAll(repository.findType(sort));
        while (ver.size() > 20) {
            ver.remove(0);
        }
        model.addAttribute("veranstaltungen", Wetter.calc(ver));
        return "verlist";
    }


    /**
     * die Startseite (verlist.html) hat post request hier hin gesendet. Dieser beinhalten eine Suche
     * Die wird dann hier genommen, alle Einträge in DB (Name,Ort) überprüft ob diese die Suche entahlten und
     * dann eine entsprechende Liste erstellt
     * Dies wird dann wieder an die Startseite gesendet --> Jetzt wird dort nur noch Veranstaltungen gezeigt die die
     * Suche entahlten
     */
    @RequestMapping("suche")
    public String showAll3(Model model, @RequestParam String entry) throws IOException {
        List<Veranstaltung> verg= new ArrayList<>(repository.findAll());
        List<Veranstaltung> su = new ArrayList<>();
        for (int i = 0; i < verg.size(); i++) {
            if (verg.get(i).getVer_name().contains(entry) || verg.get(i).getOrt().contains(entry)) {
                su.add(verg.get(i));
            }
        }

        model.addAttribute("veranstaltungen", Wetter.calc(su));
        return "verlist";
    }


    //prüft ob Datum in Zukunft liegt
    public static boolean checkFuture(String pDateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(pDateString);
        return new Date().before(date);
    }




}
