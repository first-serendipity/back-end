package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> existsByMemberIdAndPostId( Long memberId, Long postId);
    Long countByPostId(Long postId);
}