package cat.siesta.stickee.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.persistence.NoteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class NoteDeletionService {

    @Autowired
    private StickeeConfig stickeeConfig;

    @Autowired
    private NoteRepository noteRepository;

    @Scheduled(fixedDelayString = "${notes-deletion-delay}", timeUnit = TimeUnit.SECONDS)
    @Transactional
    public long deleteExpiredNotes() {
        var expiredDate = LocalDateTime.now().minus(stickeeConfig.getNotesMaxAge());
        var deletedNotesCount = noteRepository.deleteAllByCreationTimestampBefore(expiredDate);
        if (deletedNotesCount > 0L) {
            log.info("{} notes were deleted", deletedNotesCount);
        }
        return deletedNotesCount;
    }
}
