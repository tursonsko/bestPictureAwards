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
    private AcademyAward academyAward = new AcademyAward();

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    private AcademyAwardService academyAwardService;

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
    public void testCheckIfIsAwardedBestPictureReturnNewAwardedMovie() throws AcademyAwardNotFoundException {

        when(academyAward.getAwarded()).thenReturn(AwardStatusEnum.YES);
        when(academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(any(), any(), any()))
                .thenReturn(Optional.of(academyAward));

        academyAwardService.checkIfIsAwardedBestPicture(movieRequestDto);
        //awarded nie jest przypisane na stale
        //assertEquals("An Oscar-winning movie", awardedMovieResponseDto.getAwarded());

        verify(academyAward, times(1)).getNominee();
        verify(academyAward, times(1)).getYear();
        verify(academyAward, times(1)).getCategory();
        verify(academyAward, times(1)).getAwarded();
    }

    @Test
    public void testFindTenTopRatedMoviesSortedByBoxOfficeValueThrowsException() {

        assertThatExceptionOfType(AcademyAwardNotFoundException.class)
                .isThrownBy(() -> academyAwardService.findTenTopRatedMoviesSortedByBoxOfficeValue());
    }

    @Test
    public void testFindTenTopRatedMoviesSortedByBoxOfficeValueReturnCorrectList() throws AcademyAwardNotFoundException {
        //do pelnego przetestowania stworzyc 11 obiektow z wartosciami i porownac result list czy usunelo poprawny element

        AcademyAward academyAward1 = new AcademyAward();
        academyAward1.setRating(1.0);
        academyAward1.setBoxOffice(100);

        AcademyAward academyAward2 = new AcademyAward();
        academyAward2.setRating(2.0);
        academyAward2.setBoxOffice(200);

        AcademyAward academyAward3 = new AcademyAward();
        academyAward3.setRating(2.0);
        academyAward3.setBoxOffice(300);

        AcademyAward academyAward4 = new AcademyAward();
        academyAward4.setRating(4.0);
        academyAward4.setBoxOffice(400);

        List<AcademyAward> allBestGivenMovies = new ArrayList<>();
        List<AcademyAward> mockList = new ArrayList<>(List.of(
                academyAward, academyAward, academyAward, academyAward, academyAward, academyAward, academyAward)
        );
        allBestGivenMovies.add(academyAward1);
        allBestGivenMovies.add(academyAward2);
        allBestGivenMovies.add(academyAward3);
        allBestGivenMovies.add(academyAward4);
        allBestGivenMovies.addAll(mockList);

        when(academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture()))
                .thenReturn(allBestGivenMovies);

        List<TopTenMoviesResponseDto> expectedLimitedSortedList = academyAwardService
                .findTenTopRatedMoviesSortedByBoxOfficeValue();

        assertNotEquals(allBestGivenMovies, expectedLimitedSortedList);
        assertTrue(expectedLimitedSortedList.size() < 11);
        assertTrue(expectedLimitedSortedList.get(0).getBoxOffice() > expectedLimitedSortedList.get(1).getBoxOffice());

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

    @Test
    public void testGiveRateForNomineeToBestPictureMovie() throws AcademyAwardNotFoundException, WrongRateException {

        Long ratingTotalSum = 72L;
        Integer providedRate = 8;
        Long votesNumber = 9L;

        when(academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(any(), any(), any()))
                .thenReturn(Optional.of(academyAward));

        when(academyAward.getRatingTotalSum()).thenReturn(ratingTotalSum);
        when(movieRequestDto.getRate()).thenReturn(providedRate);
        when(academyAward.getRatingTotalSum()).thenReturn(votesNumber);

        RatedMovieResponseDto resultRateMovieResponseDto = academyAwardService
                .giveRateForNomineeToBestPictureMovie(movieRequestDto);

        verify(academyAwardRepository,times(1)).save(academyAward);
        assertNotNull(resultRateMovieResponseDto);

    }
}
