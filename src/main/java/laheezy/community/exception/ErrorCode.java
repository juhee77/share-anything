package laheezy.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common Errors


    // Member Errors
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다.", "MEMBER_001"),
    ID_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 아이디 입니다.", "MEMBER_002"),
    NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다.", "MEMBER_003"),
    NICKNAME_SAME_BEFORE(HttpStatus.CONFLICT, "현재 닉네임과 같은 네임으로는 수정이 불가능 합니다", "MEMBER_004"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "Login failed", "MEMBER_005"),
    ALREADY_LOGOUT(HttpStatus.UNAUTHORIZED, "로그아웃 된 사용자입니다.", "MEMBER_006"),
    INVALID_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 아이디입니다.", "MEMBER_007"),
    INVALID_BEFORE_PASSWORD(HttpStatus.BAD_REQUEST, "직전 비밀번호가 잘못 입력되었습니다", "MEMBER_008"),

    //Post Errors
    INVALID_POST(HttpStatus.BAD_REQUEST, "유효하지 않은 포스트 아이디입니다.", "POST_001"),

    //MemberChatRoom Errors
    INVALID_SUBSCRIBE(HttpStatus.BAD_REQUEST, "해당 채팅방을 구독하고 있지 않습니다.", "MEMBER-CHATROOM_001"),

    //Board Errors
    DUPLICATION_BOARD_NAME(HttpStatus.BAD_REQUEST, "해당 이름을 가진 board는 이미 존재합니다.", "BOARD_001"),
    INVALID_BOARD_NAME(HttpStatus.BAD_REQUEST, "없거나 삭제된 board 아이디 입니다.", "BOARD_002"),

    //Chatroom Errors
    DUPLICATION_CHATROOM_NAME(HttpStatus.BAD_REQUEST, "중복된 이름의 채팅방이 이미 존재합니다.", "CHATROOM_001"),
    INVALID_CHATROOM_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 방 입니다", "CHATROOM_002"),

    //CommentHeart Errors
    INVALID_COMMENT_HEART(HttpStatus.BAD_REQUEST, "좋아요 하지 않은 댓글 입니다", "COMMENT-HEART_001"),
    ALREADY_COMMENT_HEART(HttpStatus.BAD_REQUEST, "이미 좋아요 된 댓글 입니다", "COMMENT-HEART_002"),

    //PostHeart Errors
    INVALID_POST_HEART(HttpStatus.BAD_REQUEST, "좋아요 하지 않은 포스트 입니다", "POST-HEART_001"),
    ALREADY_POST_HEART(HttpStatus.BAD_REQUEST, "이미 좋아요 된 포스트 입니다", "POST-HEART_002"),

    //Comment Errors
    INVALID_COMMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 댓글 입니다.", "COMMENT_001"),

    //FileService Errors
    INVALID_FILE_TO_URL(HttpStatus.INTERNAL_SERVER_ERROR, "URL 변환 중 에러가 발생했습니다.", "FILE-SERVICE_001"),
    INVALID_FILE_TO_TRANSFER(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer 중 에러가 발생했습니다.", "FILE-SERVICE_002"),

    //Following Errors
    INVALID_FOLLOWING(HttpStatus.BAD_REQUEST, "팔로우 되어 있지 않습니다.", "FOLLOW_001"),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 되어 있습니다", "FOLLOW_002"),


    //Token Errors
    INVALID_TOKEN_INFO(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다.", "REFRESH-TOKEN_001"),
    INVALID_TOKEN_WITH_USER(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 일치하지 않습니다.", "REFRESH-TOKEN_002"),
    ;
    private final HttpStatus status; //http상태
    private final String message;
    private final String code;
}

