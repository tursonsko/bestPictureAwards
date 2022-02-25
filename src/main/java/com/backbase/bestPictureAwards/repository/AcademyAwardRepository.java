package com.backbase.bestPictureAwards.repository;


import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademyAwardRepository extends JpaRepository<AcademyAward, Long> {

    @Query("FROM AcademyAward a WHERE a.awarded = ?1 AND a.category = ?2")
    List<AcademyAward> findAcademyAwardByAwardedAndCategory(AwardStatusEnum awardStatusEnum, String category);

    @Query("FROM AcademyAward a WHERE a.category = ?1")
    List<AcademyAward> findAcademyAwardByAwarded(String category);

    List<AcademyAward> findAcademyAwardByNomineeInAndCategory(List<String> movieTitles, String category);

    Optional<AcademyAward> findAcademyAwardByNomineeAndCategory(String movieTitle, String category);
}
