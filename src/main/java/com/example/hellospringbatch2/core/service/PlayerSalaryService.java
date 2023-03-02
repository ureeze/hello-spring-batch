package com.example.hellospringbatch2.core.service;

import com.example.hellospringbatch2.core.dto.PlayerDto;
import com.example.hellospringbatch2.core.dto.PlayerSalaryDto;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class PlayerSalaryService {
    public PlayerSalaryDto calSalary(PlayerDto player) {
        int salary = (Year.now().getValue() - player.getBirthYear()) * 1000000;
        return PlayerSalaryDto.of(player, salary);
    }
}
