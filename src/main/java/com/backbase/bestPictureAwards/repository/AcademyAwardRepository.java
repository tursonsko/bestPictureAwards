package com.backbase.bestPictureAwards.repository;


import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademyAwardRepository extends JpaRepository<AcademyAward, Long> {
}
