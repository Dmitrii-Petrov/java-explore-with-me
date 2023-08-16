package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.LocationDto;
import ru.practicum.model.Location;

@Component
@AllArgsConstructor
public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

}
