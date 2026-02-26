package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/autoservice/places")
public class PlaceController {
    private final PlaceService placeService;
    private final DateService dateService;

    @Autowired
    public PlaceController(PlaceService placeService, DateService dateService) {
        this.placeService = placeService;
        this.dateService = dateService;
    }

    @GetMapping
    public List<PlaceDto> findAll() {
        return placeService.getListOfPlaceDto();
    }

    @GetMapping("/{id}")
    public PlaceDto findById(@PathVariable("id") int id) {
        return placeService.findPlace(id);
    }

    @GetMapping("/free")
    public List<PlaceDto> getFreePlace(@RequestBody DateDto date) {
        return dateService.getFreePlaceDto(date.getDate());
    }

    @GetMapping("/free/count")
    public int getCountFreePlace(@RequestBody DateDto date) {
        return dateService.getCountFreePlaceInDate(date.getDate());
    }

    @PostMapping
    public void save(@RequestBody PlaceRequest placeRequest) {
        placeService.addPlace(placeRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody PlaceRequest placeRequest) {
        placeService.updatePlace(id, placeRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        placeService.deletePlace(id);
    }
}
