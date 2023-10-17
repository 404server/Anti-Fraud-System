package com.example.antifraudsystem.service.dto.ipDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpAddResultDto {
    private IpDto ipDto;
    private boolean success;

    public IpAddResultDto(boolean success) {
        this.success = success;
    }
}
