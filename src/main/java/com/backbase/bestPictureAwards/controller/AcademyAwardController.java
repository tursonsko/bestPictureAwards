package com.backbase.bestPictureAwards.controller;

import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
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

    //usunac
    @GetMapping("/{id}")
    public ResponseEntity<?> findOneBrokerSettingsById(@PathVariable Long id) throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.findAcademyAwardById(id), HttpStatus.OK);
    }

    //usunac
    @GetMapping("/bestPictureAndAwarded")
    public ResponseEntity<List<AcademyAward>> findAllAwardedAndBestPictureCatagory() throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.findAllAwardedAndBestPictureCatagory(), HttpStatus.OK);
    }

    //usunac
    @GetMapping("/bestPicture")
    public ResponseEntity<List<AcademyAward>> findAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.findAllBestPictureCategoryMovies(), HttpStatus.OK);
    }

    //task 0
    @GetMapping("/fillUpBoxOfficeValue")
    public ResponseEntity<Void> fillUpBestPicturesBoxOfficeValue(@RequestParam(name = "apiKey") String apiKey)
            throws AcademyAwardNotFoundException {
        fillUpDatabaseService.fillUpBestPicturesBoxOfficeValue(apiKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //task 1
    @PostMapping("/checkIfMovieIsAwarded")
    public ResponseEntity<AwardedMovieResponseDto> checkIfIsAwardedBestPicture(@RequestBody AwardedMovieRequestDto dto)
            throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.checkIfIsAwardedBestPicture(dto), HttpStatus.OK);
    }

    //task 2
    @PostMapping("/rateMovie")
    public ResponseEntity<RatedMovieResponseDto> giveRateForNomineeToBestPictureMovie(@RequestBody RatedMovieRequestDto dto)
            throws AcademyAwardNotFoundException, WrongRateException {
        return new ResponseEntity<>(academyAwardService.giveRateForNomineeToBestPictureMovie(dto), HttpStatus.OK);
    }

    //task 3
    @GetMapping("/topTenMoviesSortedByBoxOfficeValue")
    public ResponseEntity<List<TopTenMoviesResponseDto>> findTenTopRatedMoviesSortedByBoxOfficeValue()
            throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.findTenTopRatedMoviesSortedByBoxOfficeValue(), HttpStatus.OK);
    }
}