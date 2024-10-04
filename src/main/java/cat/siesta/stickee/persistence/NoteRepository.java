package cat.siesta.stickee.persistence;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, String> {

    public long deleteAllByCreationTimestampBefore(Instant timestamp);

    public long deleteAllByExpirationTimestampBefore(Instant timestamp);
}
