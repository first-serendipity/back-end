package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long postId);

}
