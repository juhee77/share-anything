package laheezy.community.service;

import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.post.PostListResponse;
import laheezy.community.dto.post.PostModifyDto;
import laheezy.community.dto.post.PostResponseDto;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.repository.PostRepository;
import laheezy.community.repository.PostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostRepositoryImpl postRepositoryImpl;

    @Transactional
    public Post writePost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post modifyPost(Post post, PostModifyDto postModifyRequestForm) {
        post.modify(postModifyRequestForm);
        return post;
    }

    @Transactional
    public void deletePost(Post post) {
        postRepository.deleteById(post.getId());
    }

    public PostListResponse searchAll(Pageable pageable) {
        log.info("pageable : {} ", pageable);
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponseDto> postResponseDtos = getPostDtos(posts);
        return new PostListResponse(postResponseDtos, pageable, posts);
    }

    private List<PostResponseDto> getPostDtos(Page<Post> posts) {
        List<PostResponseDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.toPostResponseDto(post);
            dtos.add(postResponseDto);
        }
        return dtos;
    }

    public Post findById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_POST);
        }
        return post.get();
    }

    public List<Post> findFollowPost(Member member) {
        List<Following> following = member.getFollowing();
        List<Post> allPost = new ArrayList<>();
        for (Following following1 : following) {
            List<Post> posts = following1.getMemberB().getPosts();
            allPost.addAll(posts.stream().filter(Post::isOpen).toList()); //오픈된것만 받아온다.
        }
        return allPost;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<PostResponseDto> findAllPostWithHeartCnt() {
        return postRepositoryImpl.findAllPostWithHeartCnt();
    }

    public List<PostResponseDto> findAllOpenPostInBoard(String boardName) {
        return postRepositoryImpl.findEachBoardPostWithHeartCnt(boardName);
    }
}
