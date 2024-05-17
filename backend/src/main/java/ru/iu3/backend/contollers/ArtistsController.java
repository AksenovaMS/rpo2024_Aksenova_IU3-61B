package ru.iu3.backend.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artists;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.ArtistsRepository;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.tools.DataValidationException;


import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")

public class ArtistsController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ArtistsRepository artistsRepository;

    @GetMapping("/artists")
    public Page getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistsRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity getArtist(@PathVariable(value = "id") Long artistId)
            throws DataValidationException
    {
        Artists artist = artistsRepository.findById(artistId).orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/artists/{id}/paintings")
    public ResponseEntity<List<Painting>> getArtistPaintings(@PathVariable(value="id") Long artistId) {
        Optional<Artists> cc = artistsRepository.findById(artistId);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().paintings);
        }
        return ResponseEntity.ok(new ArrayList<Painting>());
    }

    @PostMapping("/deleteartists")
    public ResponseEntity deleteArtists(@Validated @RequestBody List artists) {
        artistsRepository.deleteAll(artists);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artists artists)
            throws Exception {
        try {
            Optional<Country>
                    cc = countryRepository.findById(artists.country.id);
            if (cc.isPresent()) {
                artists.country = cc.get();
            }
            Artists nc = artistsRepository.save(artists);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                error = "artistalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }


    @PutMapping("/artists/{id}")
    public ResponseEntity<Artists> updateArtist(@PathVariable(value = "id") Long artistId, @Validated @RequestBody Artists artistDetails)
            throws DataValidationException {
        try {
            Artists artist = artistsRepository.findById(artistId).orElseThrow(() -> new DataValidationException("Художник с таким индексом не найден"));
            artist.name = artistDetails.name;
            artist.country = (Country) countryRepository.findByName(artistDetails.country.name).orElseThrow(() -> new DataValidationException("Страна с таким именем не найдена"));
            artist.century = artistDetails.century;
            artistsRepository.save(artist);
            return ResponseEntity.ok(artist);
        } catch (Exception ex) {
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                throw new DataValidationException("Этот художник уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }
    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtists(@PathVariable(value = "id") Long artistId) {
        Optional<Artists>
                artists = artistsRepository.findById(artistId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (artists.isPresent()) {
            artistsRepository.delete(artists.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

}
