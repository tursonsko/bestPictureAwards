package com.backbase.bestPictureAwards.model.entity;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "academy_awards")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    //pola z .csv
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String year;
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String category;
    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String nominee;
    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String additionalInfo;
    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private AwardStatusEnum awarded;

    //todo pole do ocen filmow, logika -> zliczane i wyciagana srednia
    @Column(nullable = false, columnDefinition = "double default 0")
    private Double rating;
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long ratingTotalSum;
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long votesNumber;

//    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer boxOffice;


    /*taski
     1) Wniosek powinien wskazywać, czy film zdobył Oscara za „Najlepszy film”, biorąc pod uwagę
        tytuł filmu oparty na tym API i załączonym pliku CSV, który zawiera zwycięzców z 1927 do 2010.

     2) Powinno również umożliwiać użytkownikom ocenianie filmów

     3) dostarczanie ich listy 10 najwyżej ocenianych filmów uporządkowanych według wartości kasowej.
     */

//todo do dokumentacji sqlka
// todo najpierw odpalic ten skrypt -> potem csv import -> potem odpalic apke
/*    CREATE TABLE test_table (
        id bigint NOT NULL AUTO_INCREMENT,
        year varchar(20) NOT NULL,
        category varchar(255) NOT NULL,
        nominee varchar(1000) NOT NULL,
        additional_info varchar(1000) NOT NULL,
        awarded varchar(255) NOT NULL,
        rating double NOT NULL DEFAULT 0,
        votes_number bigint NOT NULL DEFAULT 0,
        box_office int NOT NULL DEFAULT 0,
        PRIMARY KEY (id)
      )
;*/

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
