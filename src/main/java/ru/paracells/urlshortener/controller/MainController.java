package ru.paracells.urlshortener.controller;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.paracells.urlshortener.model.UrlModel;
import ru.paracells.urlshortener.repository.UrlShortenerRepository;
import ru.paracells.urlshortener.service.UrlShortenerService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MainController {


    private UrlShortenerService service;


    @Autowired
    public MainController(UrlShortenerService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String loadMainPage(Model model) {
        return service.loadMainPage(model);
    }

    @GetMapping("/addurl")
    public String addNewUrl(Model model, @ModelAttribute("urlModel") UrlModel urlModel) {
        return service.addNewUrl(model, urlModel);
    }

    @GetMapping("/decode")
    public ResponseEntity<String> decodeUrl(HttpServletRequest request, @RequestParam String url) {
        return service.decodeUrl(request, url);
    }
}

