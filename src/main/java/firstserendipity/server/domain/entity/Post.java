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
public class Post extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "postId")
    private List<Comment> comments;


//    //테스트 코드에서 사용
//    @Builder
//    public Post(String title, String content, String image) {
//        super();
//        this.title = title;
//        this.content = content;
//        this.image = image;
//    }
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = LocalDateTime.now();
//        modifiedAt = LocalDateTime.now();
//    }
}
