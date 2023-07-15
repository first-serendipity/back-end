package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMemberId(Long memberId);

}
