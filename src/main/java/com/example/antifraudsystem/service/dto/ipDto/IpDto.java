package com.example.antifraudsystem.service.dto.ipDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IpDto {
    private Long id;
    private String ip;

}
