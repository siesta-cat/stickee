package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {

    public long deleteAllByCreationTimestampBefore(LocalDateTime timestamp);
}
