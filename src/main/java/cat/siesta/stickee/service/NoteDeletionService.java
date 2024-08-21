package cat.siesta.stickee.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.persistence.NoteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteDeletionService {

    @Autowired
    private StickeeConfig stickeeConfig;

    @Autowired
    private NoteRepository noteRepository;

    @Transactional
    public long deleteExpiredNotes() {
        var expiredDate = LocalDateTime.now().minus(stickeeConfig.getNoteMaxAge());
        var deletedNotesCount = noteRepository.deleteAllByCreationTimestampBefore(expiredDate);
        log.info("{} notes were deleted", deletedNotesCount);
        return deletedNotesCount;
    }
}
