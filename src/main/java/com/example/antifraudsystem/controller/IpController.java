package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.controller.dto.ip.AddIpRequest;
import com.example.antifraudsystem.controller.dto.ip.Ip;
import com.example.antifraudsystem.controller.dto.ip.IpDeleteResult;
import com.example.antifraudsystem.service.IpService;
import com.example.antifraudsystem.service.dto.ipDto.IpAddResultDto;
import com.example.antifraudsystem.service.dto.ipDto.IpDeleteResultDto;
import com.example.antifraudsystem.service.dto.ipDto.IpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.antifraudsystem.controller.utility.Constants.IP_PATTERN;
import static com.example.antifraudsystem.controller.utility.Converter.*;


@RestController
@RequestMapping("/api/antifraud")
public class IpController {
    @Autowired
    private IpService ipService;

    @PostMapping("/suspicious-ip")
    public ResponseEntity<Ip> addIp(@RequestBody AddIpRequest addIpRequest) {
        if (addIpRequest == null || !addIpRequest.getIp().matches(IP_PATTERN)) {
            return ResponseEntity.badRequest().build();
        }
        IpDto ipDto = convertIpDto(addIpRequest);
        IpAddResultDto ipAddResultDto = ipService.addSuspiciousIp(ipDto);

        if (!ipAddResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(convertIpResult(ipAddResultDto.getIpDto()), HttpStatus.OK);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<IpDeleteResult> deleteIp(@PathVariable String ip) {
        if (ip == null || !ip.matches(IP_PATTERN)) {
            return ResponseEntity.badRequest().build();
        }
        IpDeleteResultDto ipDeleteResultDto = ipService.deleteSuspiciousIp(ip);
        if (!ipDeleteResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String message = String.format("IP %s successfully removed!", ip);
        return new ResponseEntity<>(new IpDeleteResult(message), HttpStatus.OK);
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<Ip>> getIpList() {
        return new ResponseEntity<>(convertIpList(ipService.getIpList()), HttpStatus.OK);
    }
}
