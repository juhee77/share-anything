package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.file.File;
import laheezy.community.domain.file.Postfile;
import laheezy.community.domain.file.Profile;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import static laheezy.community.exception.ErrorCode.INVALID_FILE_TO_TRANSFER;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final MemberService memberService;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }


    //프로필 이미지 업로드기능(기존에 있는 경우 드랍후 새로운 이미지로 교체)
    @Transactional
    public Member storeProFile(MultipartFile profileImg, Member member) throws IOException {
        log.info("파일 업로드 시도");

        //기존이미지 드랍(1:1);
        if (member.getProfileImage() != null) {
            dropProfileImage(member.getProfileImage().getId());
            memberService.dropProfileImage(member);
        }

        //새로운 이미지 업로드
        //이미지 업로드 하지 않은 경우
        if (profileImg.isEmpty()) {
            return member;
        }

        //이미지 업로드를 한 경우에마 다시 업로드
        String originalFilename = profileImg.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        profileImg.transferTo(new java.io.File(getFullPath(storeFileName)));
        File file = new Profile().makeProfile(member, originalFilename, storeFileName);

        fileRepository.save(file);
        log.info("파일 업로드 성공:{}", file.getOriginName());
        return member;
    }

    @Transactional
    public void dropProfileImage(Long id) {
        fileRepository.deleteById(id);
    }


    @Transactional
    public File storePostFile(MultipartFile multipartFile, String caption, Post post) {
        if (multipartFile.isEmpty()) {
            return null; //처리할 파일이 없음
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        try {
            multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new CustomException(INVALID_FILE_TO_TRANSFER);
        }
        File file = new Postfile().makePostfile(post, caption, originalFilename, storeFileName);

        fileRepository.save(file);
        log.info("파일 업로드 성공:{}", file.getOriginName());
        return file;
    }

    private String createStoreFileName(String originalFilename) {
        //사진의 저장이름을 따로 지정하여 저장(같은 이름으로 파일이 올라올 수 있어서)
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public UrlResource convertToUrlResource(File file) {
        String storeFileName = file.getStoreName();
        try {
            return new UrlResource("file:" + getFullPath(storeFileName));
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.INVALID_FILE_TO_URL);
        }
    }

}
