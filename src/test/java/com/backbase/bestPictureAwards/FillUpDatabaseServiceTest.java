package com.backbase.bestPictureAwards;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.service.FillUpDatabaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FillUpDatabaseServiceTest {

    @Mock
    private AcademyAwardRepository academyAwardRepository;

    private AcademyAward academyAward = new AcademyAward();

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    private FillUpDatabaseService fillUpDatabaseService;

    @Before
    public void setUp() {
        academyAward = new AcademyAward();
    }

    @After
    public void tearDown() {
        academyAwardRepository.deleteAll();
    }

    @Test
    public void testFindAllBestPictureCategoryMoviesThrowAcademyAwardNotFoundException() {

        assertThatExceptionOfType(AcademyAwardNotFoundException.class)
                .isThrownBy(() -> fillUpDatabaseService.findAllBestPictureCategoryMovies());

    }

    @Test
    public void testFindAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {

        List<AcademyAward> academyAwards = new ArrayList<>();
        academyAwards.add(academyAward);

        when(academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture()))
                .thenReturn(academyAwards);
        fillUpDatabaseService.findAllBestPictureCategoryMovies();

        assertNotEquals(0, academyAwards.size());
    }

    @Test
    public void testChangeAllBoxOfficeNullValues() throws AcademyAwardNotFoundException {
        AcademyAward academyAward1 = new AcademyAward();
        academyAward1.setNominee("Titanic");
        academyAward1.setRating(1.0);
        academyAward1.setBoxOffice(null);

        AcademyAward academyAward2 = new AcademyAward();
        academyAward2.setNominee("Black Swan");
        academyAward2.setRating(2.0);
        academyAward2.setBoxOffice(3000);

        List<AcademyAward> movies = new ArrayList<>();
        movies.add(academyAward1);
        movies.add(academyAward2);

        when(academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture()))
                .thenReturn(movies);

        fillUpDatabaseService.changeAllBoxOfficeNullValues();
        assertNotNull(movies.get(0).getBoxOffice());
    }

    @Test
    public void testUpdateAllBoxOfficeValue() throws AcademyAwardNotFoundException {
        List<String> movieTitles = List.of("Titanic", "Black Swan");

        AcademyAward academyAward1 = new AcademyAward();
        academyAward1.setNominee("Titanic");
        academyAward1.setRating(1.0);

        AcademyAward academyAward2 = new AcademyAward();
        academyAward2.setNominee("Black Swan");
        academyAward2.setRating(2.0);

        List<AcademyAward> movies = new ArrayList<>();
        movies.add(academyAward1);
        movies.add(academyAward2);

        Map<String, Integer> ombdResponseDtoMap = new HashMap<>();
        ombdResponseDtoMap.put("Titanic", 777666);
        ombdResponseDtoMap.put("Black Swan", 432111);

        when(academyAwardRepository.findAcademyAwardByNomineeInAndCategory(movieTitles, configProperties.getCategoryBestPicture()))
                .thenReturn(movies);

        when(academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture()))
                .thenReturn(movies);

        fillUpDatabaseService.updateAllBoxOfficeValue(movieTitles, ombdResponseDtoMap);
        fillUpDatabaseService.changeAllBoxOfficeNullValues();

        assertNotNull(movies.get(0).getBoxOffice());
        assertNotNull(movies.get(1).getBoxOffice());

    }

    @Test
    public void testRemoveNullKeysFromOmdbResponseMap() {
        Map<String, Integer> ombdResponseDtoMap = new HashMap<>();
        ombdResponseDtoMap.put(null, 1111);
        ombdResponseDtoMap.put(null, 1111);
        fillUpDatabaseService.removeNullKeysFromOmdbResponseMap(ombdResponseDtoMap);
        assertTrue(ombdResponseDtoMap.isEmpty());
    }
}