package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRespository extends JpaRepository<Post,Long> {

}
