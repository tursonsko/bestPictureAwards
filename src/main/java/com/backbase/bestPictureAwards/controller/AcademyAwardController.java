package com.backbase.bestPictureAwards.controller;

import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
import com.backbase.bestPictureAwards.model.dto.request.AwardedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.request.RatedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.response.AwardedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.RatedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.TopTenMoviesResponseDto;
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

    /**task 0
     * GET /api/v1/fillUpBoxOfficeValue?apikey=
     * Controller to fill up database with Box Office values from Omdb Api
     * @param apiKey reqired
     * @return status code 200 if all went good or 401 if no api key is provided
     * @throws AcademyAwardNotFoundException
     */
    @GetMapping("/fillUpBoxOfficeValue")
    public ResponseEntity<Void> fillUpBestPicturesBoxOfficeValue(@RequestParam(name = "apiKey") String apiKey)
            throws AcademyAwardNotFoundException {
        fillUpDatabaseService.fillUpBestPicturesBoxOfficeValue(apiKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**task 1
     * POST /api/v1/checkIfMovieIsAwarded
     * Controller to request database o get info if movie was awarded in "Best Picture" category
     * @param dto AwardedMovieResponseDto
     * @return Staus code 200 if all went good or 400 if body is missing
     * @throws AcademyAwardNotFoundException
     */
    @PostMapping("/checkIfMovieIsAwarded")
    public ResponseEntity<AwardedMovieResponseDto> checkIfIsAwardedBestPicture(@RequestBody AwardedMovieRequestDto dto)
            throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.checkIfIsAwardedBestPicture(dto), HttpStatus.OK);
    }

    /**task 2
     * POST /api/v1/rateMovie
     * Controller to post rate for selecteed movie in "Best Picture" category
     * @param dto RatedMovieResponseDto
     * @return status code 200 or 400 if msiing request body
     * @throws AcademyAwardNotFoundException
     * @throws WrongRateException
     */
    @PostMapping("/rateMovie")
    public ResponseEntity<RatedMovieResponseDto> giveRateForNomineeToBestPictureMovie(@RequestBody RatedMovieRequestDto dto)
            throws AcademyAwardNotFoundException, WrongRateException {
        return new ResponseEntity<>(academyAwardService.giveRateForNomineeToBestPictureMovie(dto), HttpStatus.OK);
    }

    /**task 3
     * GET /api/v1/topTenMoviesSortedByBoxOfficeValue
     * Controller to get list of top 10 rated movies from database ordered by "box office" value
     * @return status code 200 or 400 caused by AcademyAwardNotFoundException
     * @throws AcademyAwardNotFoundException
     */
    @GetMapping("/topTenMoviesSortedByBoxOfficeValue")
    public ResponseEntity<List<TopTenMoviesResponseDto>> findTenTopRatedMoviesSortedByBoxOfficeValue()
            throws AcademyAwardNotFoundException {
        return new ResponseEntity<>(academyAwardService.findTenTopRatedMoviesSortedByBoxOfficeValue(), HttpStatus.OK);
    }
}