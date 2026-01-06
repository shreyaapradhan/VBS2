package com.virtual.demo.dto;

import jakarta.persistence.Entity;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    int id;
    String key;
    String value;
}
