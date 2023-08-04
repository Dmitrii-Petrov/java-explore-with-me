package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(st.ip))" +
            "from Stat as st " +
            "where " +
            "(st.uri in ?1) and " +
            "(st.timestamp between ?2 and ?3) " +
            "group by st.app,st.uri " +
            "order by count(st.ip) desc")
    List<StatDTO> countHitsNotUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(st.ip))" +
            "from Stat as st " +
            "where " +
            "(st.timestamp between ?1 and ?2) " +
            "group by st.app,st.uri " +
            "order by count(st.ip) desc")
    List<StatDTO> countHitsNotUniqueIpNullUris(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(distinct st.ip))" +
            "from Stat as st " +
            "where " +
            "(st.uri in ?1) and " +
            "(st.timestamp between ?2 and ?3) " +
            "group by st.app,st.uri " +
            "order by count(distinct st.ip) desc")
    List<StatDTO> countHitsUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(distinct st.ip))" +
            "from Stat as st " +
            "where " +
            "(st.timestamp between ?1 and ?2) " +
            "group by st.app,st.uri " +
            "order by count(distinct st.ip) desc")
    List<StatDTO> countHitsUniqueIpNullUris(LocalDateTime start, LocalDateTime end);
}
