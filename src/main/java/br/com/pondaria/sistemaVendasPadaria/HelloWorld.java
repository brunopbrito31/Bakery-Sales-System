package br.com.pondaria.sistemaVendasPadaria;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloWorld {

    @GetMapping("/seila")
    public static String getSeila() {
        return "seila";
    }


}
