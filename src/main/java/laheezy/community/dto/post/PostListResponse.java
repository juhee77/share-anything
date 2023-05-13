package laheezy.community.dto.post;

import laheezy.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private List<PostResponseDto> postList;
    private Pageable pageable;
    private boolean last;
    private int totalPages;
    private Long totalElements;
    private Sort sort;
    private boolean first;
    private boolean empty;

    public PostListResponse(List<PostResponseDto> content, Pageable pageable, Page<Post> posts) {
        this.postList = content;
        this.pageable = pageable;
        this.last = posts.isLast();
        this.totalPages = posts.getTotalPages();
        this.totalElements = posts.getTotalElements();
        this.first = posts.isFirst();
        this.empty = posts.isEmpty();
        this.sort = posts.getSort().ascending();
    }
}
