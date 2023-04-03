package laheezy.community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String name;

    @CreationTimestamp
    private LocalDateTime dateTime;

    @UpdateTimestamp
    private LocalDateTime lastmodified;

    private long maker; //해당 게시판 생성한 사람의 아이디

    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board",orphanRemoval = true )
    @Builder.Default
    List<Post> posts = new ArrayList<>();
}
