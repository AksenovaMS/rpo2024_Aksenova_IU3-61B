package ru.iu3.backend.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artists;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.ArtistsRepository;
import ru.iu3.backend.repositories.CountryRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")

public class ArtistsController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ArtistsRepository artistsRepository;

    @GetMapping("/artists")
    public List
    getAllArtists() {
        return artistsRepository.findAll();
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
    public ResponseEntity<Artists> updateArtists(@PathVariable(value = "id") Long artistId,
                                                 @RequestBody Artists artistDetails) {
        Artists artists = null;
        Optional<Artists>
                cc = artistsRepository.findById(artistId);
        if (cc.isPresent()) {
            artists = cc.get();
            artists.name = artistDetails.name;
            artistsRepository.save(artists);
            return ResponseEntity.ok(artists);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
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
