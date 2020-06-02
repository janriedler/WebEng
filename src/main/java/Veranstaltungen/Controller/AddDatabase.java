package Veranstaltungen.Controller;

import Veranstaltungen.Start;
import Veranstaltungen.VeraJdbcRepository;
import Veranstaltungen.Veranstaltung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddDatabase {

    private String name;
    private String ort;
    private String beschreibung;
    private String datum;
    private String art;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "addVer")
    @ResponseBody
    public String getQuery (@RequestParam String name, @RequestParam String ort, @RequestParam String beschreibung,
                            @RequestParam String art, @RequestParam String datum) {
        this.name = name;
        this.ort = ort;
        this.beschreibung = beschreibung;
        this.art = art;
        this.datum = datum;
        insert();
        return "<div>\n" +
                "    <a href=\"http://localhost:8080\">Startseite</a> <br><br><br>\n" +
                "</div>" +
                "Die Veranstaltung wurde erfolgreich hinzugefügt";
    }

    @Autowired
    VeraJdbcRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

    private void insert() {
        Veranstaltung neu = new Veranstaltung(name, ort, datum, art, beschreibung);
        repository.insert(neu);
    }




}
