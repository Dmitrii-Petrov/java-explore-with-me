package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.CompilationDto;
import ru.practicum.DTO.CompilationNewDto;
import ru.practicum.model.Compilation;

@Component
@AllArgsConstructor
public class CompilationMapper {

    public static CompilationDto compilationToDto(Compilation compilation) {

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation dtoToCompilation(CompilationNewDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        return compilation;
    }
}
