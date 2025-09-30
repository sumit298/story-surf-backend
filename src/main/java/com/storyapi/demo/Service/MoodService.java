package com.storyapi.demo.Service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.storyapi.demo.Entity.Mood;
import com.storyapi.demo.Repository.MoodRepository;
import com.storyapi.demo.dto.MoodDTO;
import com.storyapi.demo.mapper.DTOMapper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MoodService {
    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private DTOMapper mapper;

    @Transactional(readOnly = true)
    public MoodDTO findByName(String name) throws ResourceNotFoundException {
        Mood mood = moodRepository.findByMoodName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Mood not found: " + name));
        return mapper.toMoodDTO(mood);
    }

    public MoodDTO createMood(String name, String colorCode) {

        // Validate mood doesn't already exist
        if (moodRepository.existsByMoodName(name)) {
            throw new IllegalArgumentException("Mood with name '" + name + " already exists");
        }

        Mood mood = new Mood();
        mood.setMoodName(name);
        mood.setColorCode(colorCode);
        Mood savedMood = moodRepository.save(mood);

        return mapper.toMoodDTO(savedMood);
    }

    public MoodDTO updateMood(Long id, String description, String colorCode) throws ResourceNotFoundException {
        Mood mood = moodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mood not found with id: " + id));
        if (colorCode != null)
            mood.setColorCode(colorCode);

        Mood updatedMood = moodRepository.save(mood);
        return mapper.toMoodDTO(updatedMood);
    }

    // @Transactional(readOnly = true)
    // public Map<MoodCategory, List<MoodDTO>> getMoodsGroupedByCategory() {
    // List<Mood> allMoods =
    // moodRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

    // return allMoods.stream()
    // .collect(Collectors.groupingBy(
    // Mood::getCategory,
    // Collectors.mapping(
    // mapper::toMoodDTO,
    // Collectors.toList()
    // )
    // ));
    // }

}
