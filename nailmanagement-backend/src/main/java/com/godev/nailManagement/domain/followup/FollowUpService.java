package com.godev.nailManagement.domain.followup;

import com.godev.nailManagement.common.exceptions.BusinessException;
import com.godev.nailManagement.common.exceptions.ClientNotFoundException;
import com.godev.nailManagement.domain.client.Client;
import com.godev.nailManagement.domain.client.ClientRepository;
import com.godev.nailManagement.domain.client.queryDtos.MaxScheduleDateTimeQuery;
import com.godev.nailManagement.domain.client.queryDtos.MostDoneProcedures;
import com.godev.nailManagement.domain.client.queryDtos.OftenWeekdaysAndTime;
import com.godev.nailManagement.infra.ai.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FollowUpService {

    private final ClientRepository clientRepository;
    private final AIService aiService;
    private final JavaMailSender mailSender;

    @Value("${NAIL_STUDIO_PHONE_NUMBER}")
    private String phoneNumber;

    private final String whatsMessage = URLEncoder.encode(
            "Olá! Gostaria de agendar um horário! 😊",
            StandardCharsets.UTF_8
    );

    public FollowUpService(ClientRepository clientRepository,  AIService aiService,  JavaMailSender mailSender) {
        this.clientRepository = clientRepository;
        this.aiService = aiService;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 0 15 * * MON-FRI")
    public void sendFollowUps() {

        SimpleMailMessage message = new SimpleMailMessage();

        List<MaxScheduleDateTimeQuery> clientsLongTimeNoSee = clientRepository.selectLastScheduledDateByUser();

        //Iterated to verify for each client;
        for(MaxScheduleDateTimeQuery c : clientsLongTimeNoSee){

            OftenWeekdaysAndTime preferredDayAndTime = clientRepository.selectOftenWeekdaysAndTimeByUser(c.client_id());
            MostDoneProcedures preferredProcedure = clientRepository.selectMostDoneProceduresByUser(c.client_id());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            OffsetDateTime avaliableDay = nextAvaliableDay(c, preferredDayAndTime).orElse(null);

            String baseMessage;

            String url = "https://wa.me/" + phoneNumber + "?text=" + whatsMessage;

            //Verification
            if (avaliableDay != null) {
                baseMessage = """
                    Olá %s! Verificamos que você está há %d dias sem renovar suas unhas!
                    Temos disponibilidade para %s na data/hora: %s!
                    Favor entrar em contato via Whatsapp para realização do agendamento.
                    
                    Aguardamos seu contato via Whatsapp para marcar seu horário!
                    Basta clicar no link abaixo:
                    %s
                    
                    -Nail Studio.
                    """.formatted(c.username(), c.inactive_days(), preferredProcedure.p_name(), avaliableDay.format(formatter), url);
            } else {
                baseMessage = """
                    Olá %s! Verificamos que você está há %d dias sem renovar suas unhas!
                    Infelizmente não temos horários disponíveis na data/hora de sua preferência,
                    mas que tal experimentar um horário novo?
                    
                    Aguardamos seu contato via Whatsapp para marcar seu horário!
                    Basta clicar no link abaixo:
                    %s
                    
                    - Nail Studio.
                    """.formatted(c.username(), c.inactive_days(), url);
            }

            String body = aiService.rewriteMessage(baseMessage);

            Client client = clientRepository.findById(c.client_id()).orElseThrow(() -> new ClientNotFoundException("Client Not Found!"));

            message.setTo(client.getEmail());
            message.setSubject("Sentimos sua falta, %s!".formatted(c.username()));
            message.setText(body);

            mailSender.send(message);

            client.setLast_email_sent(OffsetDateTime.now());
        }
    }

    public Optional<OffsetDateTime> nextAvaliableDay(MaxScheduleDateTimeQuery c, OftenWeekdaysAndTime preferredDayAndTime) throws BusinessException {
        DayOfWeek preferredDay = DayOfWeek.of(preferredDayAndTime.weekday());
        LocalTime preferredTime = LocalTime.of(preferredDayAndTime.hour().getHour(), preferredDayAndTime.hour().getMinute());

        LocalDateTime response = (clientRepository.selectAvaliableDatesByUser(c.client_id(), preferredTime, preferredDay).avaliableDateTime());

        OffsetDateTime result = response.atOffset(ZoneOffset.of("-03:00"));

        return Optional.of(result);
    }

}
