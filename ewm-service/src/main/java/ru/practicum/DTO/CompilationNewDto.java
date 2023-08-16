package ru.practicum.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationNewDto {

    @NotNull
    private List<Long> events = new ArrayList<>();

    @NotNull
    private Boolean pinned = false;

    @NotNull
    @NotBlank
    @Size(max = 50, min = 1)
    private String title;

}
