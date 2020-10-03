package ru.paracells.urlshortener.service;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.paracells.urlshortener.model.DatabaseSequence;
import ru.paracells.urlshortener.model.UrlModel;
import ru.paracells.urlshortener.repository.UrlShortenerRepository;
import ru.paracells.urlshortener.util.CheckUrl;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class UrlShortenerService {

    private UrlShortenerRepository repository;
    private MongoOperations operations;

    private static final Hashids codeEncode = new Hashids();


    @Autowired
    public UrlShortenerService(UrlShortenerRepository repository, MongoOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    public String loadMainPage(Model model) {
        UrlModel urlModel = new UrlModel();

        model.addAttribute("urlModel", urlModel);
        return "index.html";
    }


    public ResponseEntity<String> decodeUrl(HttpServletRequest request, @RequestParam String url) {
        UrlModel modelurl = repository.findUrlModelByEncodedUrl(url);
        String realUrl = modelurl.getRealUrl();
        if (realUrl.indexOf("http") != 0 || realUrl.indexOf("https") != 0) {
            realUrl = "http://" + realUrl;
        }
        try {
            URI uri = new URI(realUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("BAD url", HttpStatus.BAD_REQUEST);

    }

    public String addNewUrl(Model model, @ModelAttribute("urlModel") UrlModel urlModel) {
        boolean resultChecking = CheckUrl.checkString(urlModel.getRealUrl());
        if (resultChecking) {
            long currentID = generateSequence(UrlModel.SEQUENCE_NAME);
            urlModel.setId(currentID);
            String encode = codeEncode.encode(currentID);
            urlModel.setEncodedUrl(encode);
            repository.save(urlModel);
            String url = urlModel.getEncodedUrl();
            model.addAttribute("encodedurl", url);
            return "result";
        }
        return "redirect:/";
    }

    public long generateSequence(String seqName) {
        DatabaseSequence counter = operations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
