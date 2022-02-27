package com.backbase.bestPictureAwards.controller;

import com.backbase.bestPictureAwards.model.dto.request.AwardedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.request.RatedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.response.AwardedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.RatedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.TopTenMoviesResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.service.AcademyAwardService;
import com.backbase.bestPictureAwards.service.FillUpDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AcademyAwardController {

    private final AcademyAwardService academyAwardService;
    private final FillUpDatabaseService fillUpDatabaseService;

    public AcademyAwardController(AcademyAwardService academyAwardService, FillUpDatabaseService fillUpDatabaseService) {
        this.academyAwardService = academyAwardService;
        this.fillUpDatabaseService = fillUpDatabaseService;
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

    @GetMapping("/bestPictureAndAwarded")
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

    //todo task 0
    @GetMapping("/fillUpBoxOfficeValue")
    public ResponseEntity<String> fillUpBestPicturesBoxOfficeValue(@RequestParam(name = "apiKey") String apiKey) {
        try {
            fillUpDatabaseService.fillUpBestPicturesBoxOfficeValue(apiKey);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //task 1
    @PostMapping("/checkIfMovieIsAwarded")
    public ResponseEntity<AwardedMovieResponseDto> checkIfIsAwardedBestPicture(@RequestBody AwardedMovieRequestDto dto) {
        try {
            return new ResponseEntity<>(academyAwardService.checkIfIsAwardedBestPicture(dto), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //task 2
    @PostMapping("/rateMovie")
    public ResponseEntity<RatedMovieResponseDto> giveRateForNomineeToBestPictureMovie(@RequestBody RatedMovieRequestDto dto) {
        try {
            return new ResponseEntity<>(academyAwardService.giveRateForNomineeToBestPictureMovie(dto), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //task 3
    @GetMapping("/topTenMoviesSortedByBoxOfficeValue")
    public ResponseEntity<List<TopTenMoviesResponseDto>> findTenTopRatedMoviesSortedByBoxOfficeValue() {
        try {
            return new ResponseEntity<>(academyAwardService.findTenTopRatedMoviesSortedByBoxOfficeValue(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}