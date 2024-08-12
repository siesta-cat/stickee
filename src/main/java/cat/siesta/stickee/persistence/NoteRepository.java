package cat.siesta.stickee.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
    public Optional<Note> findByResourceLocator(String resourceLocator);
    public boolean existsByResourceLocator(String resourceLocator);
}