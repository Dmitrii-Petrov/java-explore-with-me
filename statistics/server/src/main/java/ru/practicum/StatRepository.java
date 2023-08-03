package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

//    @Query("select new ru.practicum.item.ItemCountByUser(it.userId, count(it.id))" +
//            "from Item as it "+
//            "where it.url like ?1 "+
//            "group by it.userId "+
//            "order by count(it.id) desc")
//    List<ItemCountByUser> countItemsByUser(String urlPart);

    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(st.id))" +
            "from Stat as st " +
            "where (st.uri in ?1) and (st.timestamp between ?2 and ?3)" +
            "group by st.uri ")
    List<StatDTO> countHitsNotUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);

 //TODO добавить обработку с уникальными значениями

//    @Query("select new ru.practicum.StatDTO(st.app, st.uri, count(st.id))" +
//            "from Stat as st " +
//            "where (st.uri in ?1) and (st.timestamp between ?2 and ?3)" +
//            "group by st.uri ")
//    List<StatDTO> countHitsUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);
}
