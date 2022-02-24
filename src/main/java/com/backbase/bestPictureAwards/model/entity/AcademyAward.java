package com.backbase.bestPictureAwards.model.entity;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "academy_awards_temp")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //pola z .csv
    @Column
    private String year;
    @Column
    private String category;
    @Column
    private String nominee;
    @Column
    private String additionalInfo;
    @Column
    @Enumerated(EnumType.STRING)
    private AwardStatusEnum awarded;

    //todo pole do ocen filmow, logika -> zliczane i wyciagana srednia
    @Column(nullable = false)
    private Double rating;
    @Column(nullable = false)
    private Long votesNumber;

//    @Column(nullable = false)
//    private Integer boxValue;
    //todo to pole bedzie dzialac ale najpierw parser stringa na inta, narazie wylaczone


    /*taski
     1) Wniosek powinien wskazywać, czy film zdobył Oscara za „Najlepszy film”, biorąc pod uwagę
        tytuł filmu oparty na tym API i załączonym pliku CSV, który zawiera zwycięzców z 1927 do 2010.

     2) Powinno również umożliwiać użytkownikom ocenianie filmów

     3) dostarczanie ich listy 10 najwyżej ocenianych filmów uporządkowanych według wartości kasowej.
     */

//todo do dokumentacji sqlka
/*    CREATE TABLE academy_awards (
            id bigint NOT NULL AUTO_INCREMENT,
            year VARCHAR(20) NOT NULL,
    category VARCHAR(255) NOT NULL,
    nominee VARCHAR(1000) NOT NULL,
    additional_info VARCHAR(1000) NOT NULL,
    awarded VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);*/

//response z omdb api
/*    {
        "Title": "Kill Bill: Vol. 1",
            "Year": "2003",
            "Rated": "R",
            "Released": "10 Oct 2003",
            "Runtime": "111 min",
            "Genre": "Action, Crime, Drama",
            "Director": "Quentin Tarantino",
            "Writer": "Quentin Tarantino, Uma Thurman",
            "Actors": "Uma Thurman, David Carradine, Daryl Hannah",
            "Plot": "After awakening from a four-year coma, a former assassin wreaks vengeance on the team of assassins who betrayed her.",
            "Language": "English, Japanese, French",
            "Country": "United States",
            "Awards": "Nominated for 5 BAFTA Film 29 wins & 103 nominations total",
            "Poster": "https://m.media-amazon.com/images/M/MV5BNzM3NDFhYTAtYmU5Mi00NGRmLTljYjgtMDkyODQ4MjNkMGY2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            "Ratings": [
        {
            "Source": "Internet Movie Database",
                "Value": "8.1/10"
        },
        {
            "Source": "Rotten Tomatoes",
                "Value": "85%"
        },
        {
            "Source": "Metacritic",
                "Value": "69/100"
        }
    ],
        "Metascore": "69",
            "imdbRating": "8.1",
            "imdbVotes": "1,070,459",
            "imdbID": "tt0266697",
            "Type": "movie",
            "DVD": "13 Apr 2004",
            "BoxOffice": "$70,099,045",
            "Production": "N/A",
            "Website": "N/A",
            "Response": "True"
    }*/
}
