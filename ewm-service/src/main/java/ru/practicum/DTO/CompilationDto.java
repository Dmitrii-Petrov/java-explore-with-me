package ru.practicum.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;

    private List<EventShortDto> events = new ArrayList<>();

    private Boolean pinned;

    private String title;
}
