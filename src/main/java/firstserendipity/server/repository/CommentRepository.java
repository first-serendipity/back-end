package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMemberId(Long id);

    Optional<Comment> findByCommentId(Long id);

    void deleteAllByPostId(Long id);
}
