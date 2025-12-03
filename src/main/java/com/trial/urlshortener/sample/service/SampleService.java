package com.trial.urlshortener.sample.service;

import com.trial.urlshortener.sample.entity.Sample;
import com.trial.urlshortener.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository repository;

    @Transactional
    public Sample create(String message) {
        return repository.save(Sample.from(message));
    }

    public List<Sample> findAll() {
        return repository.findAll();
    }

    public Sample findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
    }

}
