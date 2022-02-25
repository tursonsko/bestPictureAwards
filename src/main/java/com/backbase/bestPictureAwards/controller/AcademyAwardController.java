package com.backbase.bestPictureAwards.controller;

import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.service.AcademyAwardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AcademyAwardController {

    private final AcademyAwardService academyAwardService;

    public AcademyAwardController(AcademyAwardService academyAwardService) {
        this.academyAwardService = academyAwardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOneBrokerSettingsById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(academyAwardService.findAcademyAwardById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bestPicAndAwarded")
    public ResponseEntity<List<AcademyAward>> findAllAwardedAndBestPictureCatagory() {
        try {
            return new ResponseEntity<>(academyAwardService.findAllAwardedAndBestPictureCatagory(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bestPicture")
    public ResponseEntity<List<AcademyAward>> findAllBestPictureCategoryMovies() {
        try {
            return new ResponseEntity<>(academyAwardService.findAllBestPictureCategoryMovies(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/fillUpBoxOfficeValue")
    public ResponseEntity<String> fillUpBestPicturesBoxOfficeValue(@RequestParam(name = "apiKey") String apiKey) {
        try {
            academyAwardService.fillUpBestPicturesBoxOfficeValue(apiKey);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> checkIfIsAwardedBestPicture() {
        try {
            academyAwardService.checkIfIsAwardedBestPicture();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
