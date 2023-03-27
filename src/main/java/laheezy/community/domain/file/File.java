package laheezy.community.domain.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //단일 테이블로
@DiscriminatorColumn(name = "FileType", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class File {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption; //대체 문자
    private String storeName;    //사진을 전달받아서 서버의 특정 폴더에 저장할 것이므로 사진이 저장된 경로를 저장
    private String originName;

    @CreationTimestamp
    private LocalDateTime create_date;

}