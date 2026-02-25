package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasterService {
    Config config;
    MasterRepository masterRepository;
    OrderMasterService orderMasterService;
    OrderMasterRepository orderMasterRepository;
    OrderRepository orderRepository;
    MapperToDto mapperToDto;

    @Autowired
    public MasterService(Config config, MasterRepository masterRepository,
                         OrderMasterService orderMasterService, OrderMasterRepository orderMasterRepository,
                         OrderRepository orderRepository, MapperToDto mapperToDto) {
        this.config = config;
        this.masterRepository = masterRepository;
        this.orderMasterService = orderMasterService;
        this.orderMasterRepository = orderMasterRepository;
        this.orderRepository = orderRepository;
        this.mapperToDto = mapperToDto;
    }

    @Transactional
    public void addMaster(int id, String name) {
        Master master = new Master(id, name);
        masterRepository.create(master);
    }

    @Transactional
    public void addMaster(MasterRequest masterRequest) {
        Master master = new Master(masterRequest.getName());
        masterRepository.create(master);
    }

    @Transactional
    public boolean deleteMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            orderMasterService.deleteByMaster(master.getId());
            masterRepository.delete(master.getId());
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteMaster(int id) {
        Master master = masterRepository.find(id);
        if (master != null) {
            orderMasterService.deleteByMaster(id);
            masterRepository.delete(id);
        }
    }

    public MasterDto findMaster(int id) {
        return mapperToDto.mapToMasterDto(masterRepository.find(id));
    }

    @Transactional
    public void updateMaster(Master master) {
        masterRepository.update(master);
    }
    @Transactional
    public void updateMaster(int id, MasterRequest masterRequest) {
        Master master = new Master(id, masterRequest.getName());
        masterRepository.update(master);
    }

    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        return masterRepository.findAll(sortType);
    }
    public List<MasterDto> getListOfMastersDto(SortTypeMaster sortType) {
        return masterRepository.findAll(sortType).stream().map(
                master -> mapperToDto.mapToMasterDto(master)).toList();
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        Order order = orderRepository.find(name);
        if (order != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getMastersByOrderInDB(order.getId());
            return orderMasterService.getMasterFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
}
