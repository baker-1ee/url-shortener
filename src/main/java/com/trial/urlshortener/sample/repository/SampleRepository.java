package com.trial.urlshortener.sample.repository;

import com.trial.urlshortener.sample.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
