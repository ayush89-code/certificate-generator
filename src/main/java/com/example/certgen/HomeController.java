
package com.example.certgen;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@Controller
public class HomeController {

    private final ExcelService excelService;
    private final ImageService imageService;

    public HomeController(ExcelService excelService, ImageService imageService) {
        this.excelService = excelService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Map<String,String> map = excelService.mapNameToDesc();
        model.addAttribute("names", map.keySet());
        return "index";
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generate(@RequestParam("name") String name) {
        Map<String,String> map = excelService.mapNameToDesc();
        String skill = map.getOrDefault(name, "");
        byte[] png = imageService.createCertificate(name, skill);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate_" + name.replaceAll("\\s+", "_") + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

}
