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
import java.util.ArrayList;
import java.util.List;

@Controller
public class Main {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "test")
    @ResponseBody
    public String getQuery (@RequestParam String end) {
        return "hallo " + end;
    }

    @GetMapping("add")
    public String form() {
        return "add";
    }


    @Autowired
    VeraJdbcRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }


    @GetMapping()
    public String showAll2(Model model) throws IOException {
        List<Veranstaltung> ver = new ArrayList<>();
        ver.addAll(repository.findAll());
        while (ver.size() > 20) {
            ver.remove(0);
        }
        model.addAttribute("veranstaltungen", Wetter.calc(ver));
        return "verlist";
    }


}
