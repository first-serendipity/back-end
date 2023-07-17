package firstserendipity.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public void updatePost(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

}
