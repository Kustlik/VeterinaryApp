package pl.gr.veterinaryapp.service.impl.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gr.veterinaryapp.exception.IncorrectDataException;
import pl.gr.veterinaryapp.repository.VisitRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class VisitValidator {
    @Autowired
    Clock systemClock;
    @Autowired
    VisitRepository visitRepository;

    private static final int MINIMAL_TIME_TO_VISIT = 60;

    public void validateVisitDate(long vetId, OffsetDateTime startDateTime, Duration duration) {
        var nowZoned = OffsetDateTime.now(systemClock);

        if (startDateTime.isBefore(nowZoned)) {
            throw new IncorrectDataException("Visit start time need to be in future.");
        }

        if (Duration.between(nowZoned, startDateTime).toMinutes() < MINIMAL_TIME_TO_VISIT) {
            throw new IncorrectDataException("The time to your visit is too short.");
        }

        if (!visitRepository.findAllOverlapping(vetId, startDateTime, startDateTime.plus(duration)).isEmpty()) {
            throw new IncorrectDataException("This date is not available.");
        }
    }
}
