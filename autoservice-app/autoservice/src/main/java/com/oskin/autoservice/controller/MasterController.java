package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.service.MasterService;
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
@RequestMapping("/autoservice")
public class MasterController {
    private final MasterService masterService;

    @Autowired
    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }
    @GetMapping("/masters")
    public List<MasterDto> findAll() {
        return masterService.getListOfMastersDto(SortTypeMaster.ID);
    }
    @GetMapping("/masters/sort_by_busy")
    public List<MasterDto> findAllSortByBusy() {
        return masterService.getListOfMastersDto(SortTypeMaster.BUSYNESS);
    }
    @GetMapping("/masters/sort_by_name")
    public List<MasterDto> findAllSortByName() {
        return masterService.getListOfMastersDto(SortTypeMaster.NAME);
    }
    @GetMapping("/masters/{id}")
    public MasterDto findById(@PathVariable("id") int id) {
        return masterService.findMaster(id);
    }
    @PostMapping("/masters")
    public void save(@RequestBody MasterRequest masterRequest) {
        masterService.addMaster(masterRequest);
    }
    @PutMapping("/masters/{id}")
    public void update(@PathVariable("id") int id, @RequestBody MasterRequest masterRequest) {
        masterService.updateMaster(id, masterRequest);
    }

    @DeleteMapping("/masters/{id}")
    public void delete(@PathVariable("id") int id) {
        masterService.deleteMaster(id);
    }
}
