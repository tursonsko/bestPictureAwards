package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.dto.OmdbApiResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AcademyAwardService {

    private final AcademyAwardRepository academyAwardRepository;
    private final ConfigProperties configProperties;

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(60_000))
            .setReadTimeout(Duration.ofMillis(60_000))
            .build();

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    public AcademyAwardService(AcademyAwardRepository academyAwardRepository, ConfigProperties configProperties) {
        this.academyAwardRepository = academyAwardRepository;
        this.configProperties = configProperties;
    }

    public AcademyAward findAcademyAwardById(Long id) {
        AcademyAward foundRecord =  academyAwardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("record not found"));
        log.info(foundRecord.toString());
        return foundRecord;
    }

    public List<AcademyAward> findAllAwardedAndBestPictureCatagory() {
        List<AcademyAward> academyAwards = academyAwardRepository.findAcademyAwardByAwardedAndCategory(AwardStatusEnum.valueOf(configProperties.getAwardedTypeYes()), configProperties.getCategoryBestPicture());
        return academyAwards;
    }

    public List<AcademyAward> findAllBestPictureCategoryMovies() {
        List<AcademyAward> academyAwards = academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
        return academyAwards;
    }

    //todo implementacja
    //    metoda sprawdzajaca baze i omdb (
    //      potrzebuje uderzyc do bazy za pomocą tytułu (potestować jak baza czyta male/duze litery i jakies apostrofy, kropki itd.)
    //     jesli tytul filmu a bazie && w api && jest awarded:YES w bazie to zwrocic że jest (jakiś model najlepiej dto response)

    //testoa - 4 rekordy - 2 z boxOffice, 2 z wartoscia N/A
    public List<AcademyAward> findAllAwardedTestAndBestPictureCatagory() {
        List<AcademyAward> academyAwards = academyAwardRepository.findAcademyAwardByAwardedAndCategory(AwardStatusEnum.valueOf(configProperties.getAwardedTypeTest()), configProperties.getCategoryBestPicture());
        return academyAwards;
    }



    //todo zmienic na findAllBestPictureCategoryMovies() = academyAwards

//    @PostConstruct
    public void addBoxOfficeValuesToBestPictureMovis() {
        List<AcademyAward> academyAwards = findAllAwardedTestAndBestPictureCatagory();
        if (academyAwards != null && academyAwards.size() > 0) {
            List<String> bestPicturesTitles = academyAwards.stream().map(AcademyAward::getNominee)
                    .collect(Collectors.toList());
            log.info(String.valueOf(bestPicturesTitles));
//            fillUpBestPicturesBoxOfficeValue();
        }

    }

    public void fillUpBestPicturesBoxOfficeValue(String apiKey) {

        List<String> bestPicturesTitles = findAllAwardedTestAndBestPictureCatagory().stream()
                .map(AcademyAward::getNominee)
                .collect(Collectors.toList());

        List<OmdbApiResponseDto> apiResponseList = new ArrayList<>();
        Map<String, Integer> ombdResponseDtoMap = new HashMap<>();

        bestPicturesTitles.forEach(movieTitle -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.ALL));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            String urlTemplate = UriComponentsBuilder.fromHttpUrl(configProperties.getOmdbApiUrl())
                    .queryParam(configProperties.getOmdbApiTitleParamName(),
                            "{" + configProperties.getOmdbApiTitleParamName() + "}")
                    .queryParam(configProperties.getOmdbApiKeyParamName(),
                            "{" + configProperties.getOmdbApiKeyParamName() + "}")
                    .build()
                    .toUriString();
            Map<String, String> params = new HashMap<>();
            params.put(configProperties.getOmdbApiTitleParamName(), movieTitle);
            params.put(configProperties.getOmdbApiKeyParamName(), apiKey);
            ResponseEntity<OmdbApiResponseDto> response = null;
            try {
                response = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, OmdbApiResponseDto.class, params);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException();
            }
            log.info(gson.toJson(response.getBody()));

            String movieTitleToUpdate = response.getBody().getTitle();
            String boxOfficeValueToParse = response.getBody().getBoxOffice();
            Integer boxOfficeValueToFill = Utils.parseValueToNumeric(boxOfficeValueToParse);

            apiResponseList.add(response.getBody());
            ombdResponseDtoMap.put(movieTitleToUpdate, boxOfficeValueToFill);
//            AcademyAward academyAwardToUpdate = academyAwardRepository.findAcademyAwardByNominee(movieTitleToUpdate)
//                    .orElseThrow(() -> new RuntimeException("Movie not found"));

        });
        log.info(String.valueOf(ombdResponseDtoMap));

        for (Map.Entry<String, Integer> entry : ombdResponseDtoMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        List<String> movieTitles = new ArrayList<>(ombdResponseDtoMap.keySet());
        List<Integer> movieBoxOffice = new ArrayList<>(ombdResponseDtoMap.values());
        log.info(String.valueOf(movieTitles));
        log.info(String.valueOf(movieBoxOffice));

        List<AcademyAward> foundMoviesByTitlesFromOmdbApi = academyAwardRepository.findAcademyAwardByNomineeInAndCategory(movieTitles, configProperties.getCategoryBestPicture());
        foundMoviesByTitlesFromOmdbApi.forEach(movie -> {
            String movieTitle = movie.getNominee();
            movie.setBoxOffice(ombdResponseDtoMap.get(movieTitle));
        });

        academyAwardRepository.saveAll(foundMoviesByTitlesFromOmdbApi);

        foundMoviesByTitlesFromOmdbApi.forEach(System.out::println);
        log.info("koniec");



    }

    //todo 1 zadanie - sprawdzenie czy film po tytule wygral oscara - tylko z bazy zapytanie, bez API
    // TUTAJ ZROBIC Z RESTEM

    public void checkIfIsAwardedBestPicture() {
        AcademyAward movie = academyAwardRepository.findAcademyAwardByNomineeAndCategory("Black Swan", "Best Picture")
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        log.info(movie.getAwarded().name());
        if(movie.getAwarded().equals(AwardStatusEnum.YES)) {
            log.info("film wygral oscara");
        } else {
            log.info("film NIEEEEEE wygral oscara");

        }
    }


    //todo 2 zadanie metoda zwracajaca liste top10 ocenionych filmow
    // (mozna zreobić osobną kolumnę z ocenami z imdb np,
    // zebym mial sortowanie po ocenach moich i imdb osobno(?))
    public void findTenTopRatedMoviesSortedByBoxOfficeValue() {
    }

    //todo 3 zadanie - ocena filmu 1-10 skala double i
    // srednia z 1 miejscem po przecinku (wszystkie Best Picture filmy - sami zwyciezcy z bazy)
    public void giveRateForNomineeToBestPictureMovie() {
    }
}
