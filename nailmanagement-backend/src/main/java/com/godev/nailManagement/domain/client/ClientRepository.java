package com.godev.nailManagement.domain.client;

import com.godev.nailManagement.domain.client.queryDtos.AvaliableDates;
import com.godev.nailManagement.domain.client.queryDtos.MaxScheduleDateTimeQuery;
import com.godev.nailManagement.domain.client.queryDtos.MostDoneProcedures;
import com.godev.nailManagement.domain.client.queryDtos.OftenWeekdaysAndTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByUsername(String username);

    @Query(value = """
            SELECT (DATE(current_date) - DATE(max(a.schedule)))::int inactive_days,
                        c.id client_id,
                        c.username,
                        max(a.schedule)::timestamp last_schedule
            			FROM client c
                        JOIN agenda a ON c.id = a.client_id
                        GROUP BY c.id
                        HAVING DATE(current_date) - DATE(max(a.schedule)) > 30
                        AND DATE(current_date) - DATE(c.last_email_sent) >= 7
            """, nativeQuery = true)
    List<MaxScheduleDateTimeQuery> selectLastScheduledDateByUser();

    @Query(value = """
            SELECT EXTRACT(DOW FROM a.schedule)::int AS weekday,
                    TO_CHAR(a.schedule, 'HH24:MI')::time AS hour,
                    COUNT(*) AS total
                    FROM agenda a
                    WHERE a.client_id = :client_id
                    GROUP BY weekday, hour
                    ORDER BY total DESC
                    LIMIT 1;
            """, nativeQuery = true)
    OftenWeekdaysAndTime selectOftenWeekdaysAndTimeByUser(@Param("client_id") UUID  client_id);

    @Query(value = """
            SELECT count(a.procedure_id)::int p_count,
                p.name p_name,
                p.id p_id,
                c.username,
                c.id c_id
                FROM procedure p
                JOIN agenda a ON p.id = a.procedure_id
                JOIN client c ON c.id = a.client_id
                WHERE c.id = :client_id
                GROUP BY p.name, c.id, p.id
                ORDER BY p_count DESC
                LIMIT 1;
            """, nativeQuery = true)
    MostDoneProcedures selectMostDoneProceduresByUser(@Param("client_id") UUID  client_id);

    @Query(value = """
            WITH candidate_dates AS (
                SELECT
                    (date_trunc('day', now()) + (gs * interval '1 day'))::date AS candidate_date
                FROM generate_series(0, 30) gs
            )
            SELECT
                (candidate_date + CAST(:time as time)) AS available_datetime
            FROM candidate_dates cd
            WHERE
                -- Dia da semana ISO (1 = segunda, 7 = domingo)
                EXTRACT(ISODOW FROM candidate_date) = :weekday
            
                -- Data/hora precisa ser futura
                AND (candidate_date + CAST(:time as time)) > now()
            
                -- Não pode existir agenda para o cliente nesse horário
                AND NOT EXISTS (
                    SELECT 1
                    FROM agenda a
                    WHERE a.client_id = :client_id
                      AND a.schedule = (candidate_date + CAST(:time as time))
                )
                AND candidate_date NOT IN (
                	SELECT holiday_date FROM brazil_holidays
                )
            ORDER BY candidate_date
            LIMIT 1;
            """, nativeQuery = true)
    AvaliableDates selectAvaliableDatesByUser(@Param("client_id") UUID  client_id, @Param("time") LocalTime time, @Param("weekday") DayOfWeek weekday);
}