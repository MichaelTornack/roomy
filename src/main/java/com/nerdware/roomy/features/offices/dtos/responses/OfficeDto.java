package com.nerdware.roomy.features.offices.dtos.responses;

import com.nerdware.roomy.domain.entities.Office;

public record OfficeDto(Integer id, String name, String location, Integer capacity)
{
    public static OfficeDto toDto(Office office) {
        return new OfficeDto(office.getId(), office.getName(), office.getLocation(), office.getCapacity());
    }
}
