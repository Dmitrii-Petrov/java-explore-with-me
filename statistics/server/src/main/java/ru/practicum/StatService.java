package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class StatService {

    private final StatRepository statRepository;
}
