package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.dto.request.AwardedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.request.RatedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.response.OmdbApiResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.AwardedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.RatedMovieResponseDto;
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
//        log.info(String.valueOf(academyAwards.size()));
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

        List<String> bestPicturesTitles = findAllBestPictureCategoryMovies().stream()
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
//            log.info(gson.toJson(response.getBody()));

            String movieTitleToUpdate = response.getBody().getTitle();
            String boxOfficeValueToParse = response.getBody().getBoxOffice();
            Integer boxOfficeValueToFill = Utils.parseValueToNumeric(boxOfficeValueToParse);
            ombdResponseDtoMap.put(movieTitleToUpdate, boxOfficeValueToFill);
//            ombdResponseDtoMap.entrySet().removeIf(curr -> curr.getKey() == null);

            apiResponseList.add(response.getBody());

        });
        apiResponseList.removeIf(omdbApiResponseDto -> omdbApiResponseDto.getTitle() == null);


//        Map<String, Integer> apiResponseMap = apiResponseList.stream()
//                .collect(Collectors.toMap(OmdbApiResponseDto::getTitle.concat(OmdbApiResponseDto::getYear),
//                        omdbApiResponseDto -> Utils.parseValueToNumeric(omdbApiResponseDto.getBoxOffice()),
//                        (key1, key2) -> key1));

        /*
        * lista obiektow (title, year, box office) -> foreach -> obiekt.title i obiekt.year i tym uderzam do bazy -> jesli znajdzie to w tym filmie zapisac boxOffice, zebym mogl z bazy wyszukac liste po title i year a potem zebym mogl zapisac w bazie boxOffice tego filmu ktory znajde w jsonie?
        *
        *
        * Cleopatra z 1934 (N/A) i 1963(57,777,778) - czyli trzeba zapytac api na podstawie tytlu + yar
        *
        *
        *
        * sortowanie top 10 filmow
        * then comparing
        * */
                
                
//        apiResponseList.forEach(System.out::println);
//        for (Map.Entry<String, Integer> entry : apiResponseMap.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }

        List<OmdbApiResponseDto> list = apiResponseList.stream().filter(omdbApiResponseDto ->
            omdbApiResponseDto.getTitle().equals("Cleopatra")).collect(Collectors.toList());


        list.sort(
                Comparator.comparing((OmdbApiResponseDto b) -> b.getTitle())
                        .thenComparing((OmdbApiResponseDto b) -> b.getBoxOffice(), Comparator.reverseOrder()));
        System.out.println(list);

        /*
        jesli chce porywnaw obiekty za pomoca Comparator to musze zaimplementoweac Comparator i nadpisac metode compare(a,b) -> a - b - ascending (b - a descending)
        Comparable ma metode compareTo a.compareTo(b)
        * */

//        log.info(String.valueOf(ombdResponseDtoMap));

        //printowanie mapy responsow wszystkich
//        for (Map.Entry<String, Integer> entry : ombdResponseDtoMap.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
        List<String> movieTitles = new ArrayList<>(ombdResponseDtoMap.keySet()).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        movieTitles.forEach(System.out::println);
//        List<Integer> movieBoxOffice = new ArrayList<>(ombdResponseDtoMap.values());
//        log.info(String.valueOf(movieTitles));
//        log.info(String.valueOf(movieBoxOffice));

        List<AcademyAward> foundMoviesByTitlesFromOmdbApi = academyAwardRepository.findAcademyAwardByNomineeInAndCategory(movieTitles, configProperties.getCategoryBestPicture());
        foundMoviesByTitlesFromOmdbApi.forEach(movie -> {
            String movieTitle = movie.getNominee();
            movie.setBoxOffice(ombdResponseDtoMap.get(movieTitle));
        });

        academyAwardRepository.saveAll(foundMoviesByTitlesFromOmdbApi);

//        foundMoviesByTitlesFromOmdbApi.forEach(System.out::println);
        log.info("koniec");

        System.out.println("-------------------------");
        //printy do sprawdzenia czego brakuje - 5 pol
        //mapa 479 sztuk
//        for (Map.Entry<String, Integer> entry : apiResponseMap.entrySet()) {
//            System.out.println(entry.getKey());
//        }
        System.out.println("-------------------------");
        System.out.println("-------------------------");

        apiResponseList.forEach(omdbApiResponseDto -> {
            System.out.println(omdbApiResponseDto.getTitle());
        });

        System.out.println("-------------------------");




    }



    //sprawdzanie czy wygrakl czy nie oscara - done
    public AwardedMovieResponseDto checkIfIsAwardedBestPicture(AwardedMovieRequestDto dto) {
        AcademyAward movie = academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(dto.getMovieTitle(), String.valueOf(dto.getYear()), configProperties.getCategoryBestPicture())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        log.info(movie.getNominee() + " -> " + movie.getAwarded().name());
        return new AwardedMovieResponseDto(movie);
    }


    //todo 2 zadanie metoda zwracajaca liste top10 ocenionych filmow
    // (mozna zreobić osobną kolumnę z ocenami z imdb np,
    // zebym mial sortowanie po ocenach moich i imdb osobno(?))
    public void findTenTopRatedMoviesSortedByBoxOfficeValue() {
    }

    //srednia - done
    public RatedMovieResponseDto giveRateForNomineeToBestPictureMovie(RatedMovieRequestDto dto) {
        AcademyAward movie = academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(dto.getMovieTitle(), String.valueOf(dto.getYear()), configProperties.getCategoryBestPicture())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Long ratingTotalSum = movie.getRatingTotalSum();
        Integer providedRate = dto.getRate();
        Long votesNumber = movie.getVotesNumber();
        Double newRating = Utils.round(calculateMovieRating(ratingTotalSum, providedRate, votesNumber), 1);
        movie.setRating(newRating);
        movie.setRatingTotalSum(ratingTotalSum + providedRate);
        movie.setVotesNumber(votesNumber + 1L);
        academyAwardRepository.save(movie);
        return new RatedMovieResponseDto(movie);
    }

    private Double calculateMovieRating(Long ratingTotalSum, Integer providedRate, Long votesNumber) {
        if (providedRate <= 10 && providedRate >= 1) {
            if (votesNumber < 0) {
                log.info("nie moze byc liczba ocen mmiejsza niz 0");
                throw new RuntimeException("nie moze byc liczba ocen mmiejsza niz 0");
            }
            return votesNumber == 0 ? Double.valueOf(providedRate) : (double)(ratingTotalSum + providedRate) / (votesNumber + 1L);
        } else {
            log.info("wartosci tylko pomiedzy 1 a 10");
            throw new RuntimeException("wartosci tylko pomiedzy 1 a 10");
        }
    }
}
// 9 8 6 4 5 3 1 9 = 45 / 8 = 5.625 (5.6 powinno byc)
