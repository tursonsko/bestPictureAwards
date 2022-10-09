package com.backbase.bestPictureAwards;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
import com.backbase.bestPictureAwards.model.dto.request.MovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.response.RatedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.TopTenMoviesResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.service.AcademyAwardService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcademyAwardServiceTest {

    @Mock
    private AcademyAwardRepository academyAwardRepository;

    @Mock
    private MovieRequestDto movieRequestDto;

    @Mock
    private AcademyAward academyAward;

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    private AcademyAwardService academyAwardService;

    @Before
    public void setUp() {
        academyAward = new AcademyAward();
    }

    @After
    public void tearDown() {
        academyAwardRepository.deleteAll();
    }



    @Test
    public void testFindAcademyAwardByNomineeAndYearLikeAndCategoryReturnAcademyAward() throws AcademyAwardNotFoundException {

        when(academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(any(), any(), any()))
                .thenReturn(Optional.of(academyAward));

        academyAwardService.findAcademyAwardByNomineeAndYearLikeAndCategory(movieRequestDto);

        verify(movieRequestDto, times(1)).getMovieTitle();
    }

    @Test
    public void testFindAcademyAwardByNomineeAndYearLikeAndCategoryThrowsAcademyAwardNotFoundException() {

        assertThatExceptionOfType(AcademyAwardNotFoundException.class).isThrownBy(() -> academyAwardService
                .findAcademyAwardByNomineeAndYearLikeAndCategory(movieRequestDto));
    }

    @Test
    public void testFindAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {

        //given
        List<AcademyAward> allBestPicturesMovies = new ArrayList<>();
        allBestPicturesMovies.add(academyAward);

        //when
        when(academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture()))
                .thenReturn(allBestPicturesMovies);

        academyAwardService.findAllBestPictureCategoryMovies();
        //then
        assertNotEquals(0, allBestPicturesMovies.size());
    }

    @Test
    public void testFindAllBestPictureCategoryMoviesThrowsAcademyAwardNotFoundException() {

        assertThatExceptionOfType(AcademyAwardNotFoundException.class)
                .isThrownBy(() -> academyAwardService.findAllBestPictureCategoryMovies());
    }


    @Test
    public void testFindTenTopRatedMoviesSortedByBoxOfficeValueThrowsException() {

        assertThatExceptionOfType(AcademyAwardNotFoundException.class)
                .isThrownBy(() -> academyAwardService.findTenTopRatedMoviesSortedByBoxOfficeValue());
    }

    @Test
    public void testCalculateMovieRatingReturnsCorrectRatingValue() throws WrongRateException {

        Long ratingTotalSum = 72L;
        Integer providedRate = 8;
        Long votesNumber = 9L;

        Double result = academyAwardService.calculateMovieRating(ratingTotalSum, providedRate, votesNumber);
        assertEquals(8D, result, 0.0);
    }

    @Test
    public void testCalculateMovieRatingThrowsWrongRateExceptionWhenVotesNumberLessThanZero() {
        Long ratingTotalSum = 72L;
        Integer providedRate = 8;
        Long votesNumber = -3L;

        assertThatExceptionOfType(WrongRateException.class).isThrownBy(() -> academyAwardService
                .calculateMovieRating(ratingTotalSum, providedRate, votesNumber));
    }

    @Test
    public void testCalculateMovieRatingThrowsWrongRateExceptionWhenProvidedRateEqualsZero() {
        Long ratingTotalSum = 72L;
        Integer providedRate = 0;
        Long votesNumber = 9L;

        assertThatExceptionOfType(WrongRateException.class).isThrownBy(() -> academyAwardService
                .calculateMovieRating(ratingTotalSum, providedRate, votesNumber));
    }

    @Test
    public void testCalculateMovieRatingThrowsWrongRateExceptionWhenProvidedRateGreatrThanTen() {
        Long ratingTotalSum = 72L;
        Integer providedRate = 11;
        Long votesNumber = 9L;

        assertThatExceptionOfType(WrongRateException.class).isThrownBy(() -> academyAwardService
                .calculateMovieRating(ratingTotalSum, providedRate, votesNumber));
    }

    @Test
    public void testCalculateMovieRatingReturnsProvidedRateValueAsRatingAverage() throws WrongRateException {
        Long ratingTotalSum = 0L;
        Integer providedRate = 8;
        Long votesNumber = 0L;
        Double result = academyAwardService.calculateMovieRating(ratingTotalSum, providedRate, votesNumber);
        assertEquals(8D, result, 0.0);
    }

    @Test
    public void testGiveRateForNomineeToBestPictureMovieThrownAcademyAwardNotFoundException() {
        assertThatExceptionOfType(AcademyAwardNotFoundException.class).isThrownBy(() -> academyAwardService
                .giveRateForNomineeToBestPictureMovie(movieRequestDto));
    }

    @Test
    public void testGiveRateForNomineeToBestPictureMovieThrownWrongRateException() {
        when(academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(any(), any(), any()))
                .thenReturn(Optional.of(academyAward));

        assertThatExceptionOfType(WrongRateException.class).isThrownBy(() -> academyAwardService
                .giveRateForNomineeToBestPictureMovie(movieRequestDto));
    }

}
