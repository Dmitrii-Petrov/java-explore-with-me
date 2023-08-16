package ru.practicum.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data

@AllArgsConstructor
@NoArgsConstructor

public class RequestUpdateDto {

    List<RequestShortDto> confirmedRequests = new ArrayList<>();

    List<RequestShortDto> rejectedRequests = new ArrayList<>();
}
