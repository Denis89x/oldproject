package com.example.project1.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DropboxService {

    private final DbxClientV2 client;

    private final Environment environment;

    public DropboxService(@Value("${dropbox.accessToken}") String accessToken, Environment environment) {
        client = new DbxClientV2(new DbxRequestConfig("dropbox/java-tutorial"), accessToken);
        this.environment = environment;
    }

    public String saveImage(MultipartFile file, String name) throws DbxException, IOException {
        String imageName = name + ".jpg";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream()).size(800, 600).outputFormat("jpg").toOutputStream(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        client.files().uploadBuilder("/" + imageName).uploadAndFinish(inputStream);
        return imageName;
    }

    public byte[] getImage(String imageName) throws DbxException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        client.files().downloadBuilder("/" + imageName).download(outputStream);
        return outputStream.toByteArray();
    }

    public String getImageUrl(String imageName) throws DbxException {
        String accessToken = environment.getProperty("dropbox.accessToken");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, accessToken);

        SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings("/" + imageName);
        String sharedUrl = sharedLinkMetadata.getUrl();

        return sharedUrl.replace("www.dropbox.com", "dl.dropboxusercontent.com");
    }

    public List<String> listImages() throws DbxException {
        List<String> result = new ArrayList<>();
        ListFolderResult listFolderResult = client.files().listFolder("");
        for (Metadata metadata : listFolderResult.getEntries()) {
            if (metadata instanceof FileMetadata) {
                result.add(metadata.getName());
            }
        }
        return result;
    }
}
