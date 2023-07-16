package firstserendipity.server.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
