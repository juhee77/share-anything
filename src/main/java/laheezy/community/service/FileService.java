package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.file.File;
import laheezy.community.domain.file.FileType;
import laheezy.community.domain.file.Postfile;
import laheezy.community.domain.file.Profile;
import laheezy.community.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public File storeProFile(MultipartFile multipartFile, String caption, Member nowLogin, FileType profile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null; //TODO: error 처리
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
        File file = new Profile().makeProfile(nowLogin, caption, originalFilename, storeFileName);

        fileRepository.save(file);
        log.info("파일 업로드 성공:{}", file.getOriginName());
        return file;
    }


    public File storePostFile(MultipartFile multipartFile, String caption, Post post, FileType profile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null; //TODO: error 처리
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
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
}
