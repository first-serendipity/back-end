package firstserendipity.server.repository;

import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepository {

    private final EntityManager em;

    public List<Post> findPostsByMemberId(Long memberId) {
        String query = "select p from Post p join Like l on l.post.id = p.id where l.memberId = :memberId";
        TypedQuery<Post> result = em.createQuery(query, Post.class)
                .setParameter("memberId", memberId);

        return result.getResultList();
    }

    public List<Post> findPostsByLikeCountDesc() { // 인기게시글 조회
        return em.createQuery("select p from Post p order by size(p.likes) DESC").getResultList();
    }


    public Post findPostWithComments (Long id) { // 선택 게시글 조회 시 댓글 + 좋아요 수
        String query = "select p from Post p " +
                "left join fetch p.comments " +
                "where p.id = :id";

        return (Post) em.createQuery(query).setParameter("id", id).getSingleResult();


    }

    public Long findLikeCount(Long id) {
        String query = "select count(l) " +
                "from Post p " +
                "left join p.likes l " +
                "where p.id = :id";
        try {
            return (Long) em.createQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }

    }

    public List<Post> findAllPostsCreatedAtDesc() { //전체 게시글 조회
        String query = "select p from Post p " +
                "left join fetch p.likes " +
                "order by p.createdAt desc";
        return em.createQuery(query).getResultList();
    }

}