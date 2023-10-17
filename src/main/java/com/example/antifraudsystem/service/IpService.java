package com.example.antifraudsystem.service;

import com.example.antifraudsystem.repository.IpRepository;
import com.example.antifraudsystem.repository.domain.IpModel;
import com.example.antifraudsystem.service.dto.ipDto.IpAddResultDto;
import com.example.antifraudsystem.service.dto.ipDto.IpDeleteResultDto;
import com.example.antifraudsystem.service.dto.ipDto.IpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IpService {

    @Autowired
    private IpRepository ipRepository;

    public IpAddResultDto addSuspiciousIp(IpDto ip) {
        if (ipRepository.existsByIp(ip.getIp())) {
            return new IpAddResultDto(false);
        }
        ipRepository.save(new IpModel(ip.getIp()));
        IpModel ipModel = ipRepository.findByIp(ip.getIp());
        IpDto ipDto = new IpDto().setId(ipModel.getId()).setIp(ipModel.getIp());

        return new IpAddResultDto(ipDto, true);
    }

    public IpDeleteResultDto deleteSuspiciousIp(String ip) {
        if (!ipRepository.existsByIp(ip)) {
            return new IpDeleteResultDto(false);
        }
        IpModel ipModel = ipRepository.findByIp(ip);
        ipRepository.delete(ipModel);
        return new IpDeleteResultDto(true);
    }

    public List<IpDto> getIpList() {
        List<IpDto> ipDtoList = new ArrayList<>();
        for (IpModel ipModel : ipRepository.findByOrderByIdAsc()) {
            ipDtoList.add(new IpDto().setId(ipModel.getId()).setIp(ipModel.getIp()));
        }
        return ipDtoList;
    }


}
