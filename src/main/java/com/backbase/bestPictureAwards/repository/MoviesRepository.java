package com.backbase.bestPictureAwards.repository;

import com.backbase.bestPictureAwards.model.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviesRepository extends JpaRepository<Long, Movies> {
}
