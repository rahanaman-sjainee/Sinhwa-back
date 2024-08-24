package com.example.backend.Firebase;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.auth.FirebaseAuthException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseService {
    private static String TEST_COLLECTION_NAME = "test";

    @Value("${firbase.firebase-bucket}")
    private String firebaseBucket;

    public String uploadFiles(MultipartFile file, String path,String fileName) throws IOException {
        //파이어베이스 storage 버켓 호출. storage 관리 클래스를 싱글톤으로 관리하는 듯
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        //IO Exception // file.getbyte -> byte[]로 데이터 읽어오기
        InputStream content = new ByteArrayInputStream(file.getBytes());

        //경로는 filename 앞에 붙이면 됨
        Blob blob = bucket.create(path + fileName,content,file.getContentType());
        System.out.println(blob.getMediaLink());
        return blob.getMediaLink();
    }


}
