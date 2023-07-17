package firstserendipity.server.repository;

import firstserendipity.server.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepository {

    private final EntityManager em;

    public List<Post> findPostsByMemberId(Long memberId) {
        String query = "select p from Post p join Like l on l.postId = p.id where l.memberId = :memberId";
        TypedQuery<Post> result = em.createQuery(query, Post.class)
                .setParameter("memberId", memberId);

        return result.getResultList();
    }

}
