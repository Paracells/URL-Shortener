package ru.paracells.urlshortener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.paracells.urlshortener.model.UrlModel;

@Repository
public interface UrlShortenerRepository extends MongoRepository<UrlModel, Integer> {

    UrlModel findUrlModelByEncodedUrl(String encode);

}
