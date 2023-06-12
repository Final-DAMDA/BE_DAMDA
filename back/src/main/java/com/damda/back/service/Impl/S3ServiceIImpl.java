package com.damda.back.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceIImpl implements S3Service {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	private final AmazonS3Client amazonS3Client;

	/***
	 * @apiNote 이미지 업로드
	 * @param multipartFiles
	 * @return 업로드된 이미지 반환
	 */
	@Override
	public List<String> uploadFile(List<MultipartFile> multipartFiles,String folderName){
		List<String> fileNameList = new ArrayList<>();

		// forEach 구문을 통해 multipartFiles 리스트로 넘어온 파일들을 순차적으로 fileNameList 에 추가
		multipartFiles.forEach(file -> {
			String fileName = createFileName(file.getOriginalFilename()); //파일이름
			String folderPath = folderName+"/"+fileName;
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			try(InputStream inputStream = file.getInputStream()){
				amazonS3.putObject(new PutObjectRequest(bucket, folderPath, inputStream, objectMetadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
				fileNameList.add(fileName);
			} catch (IOException e){
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
			}


		});

		return fileNameList;
	}

	/**
	 * @ApiNote 이미지 url , 반환
	 * @param fileNameList
	 * @return
	 */

	@Override
	public List<String> uploadFileUrl(List<String> fileNameList, String folderName){
		String bucketFolder = bucket+"/"+folderName;
		List<String> fileUrlList = new ArrayList<>();
		fileNameList.forEach(fileName ->{
			String fileUri = URLDecoder.decode(amazonS3Client.getUrl(bucketFolder, fileName).toString(), StandardCharsets.UTF_8);
			fileUrlList.add(fileUri);
		});
		return fileUrlList;
	}


	// 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
	@Override
	public String createFileName(String fileName){
		return UUID.randomUUID().toString()+getFileExtension(fileName);
	}


	// file 형식이 잘못된 경우를 확인(jpg, png형식 파일만 업로드 가능)
	@Override
	public String getFileExtension(String fileName){
		if (fileName.length() == 0) {
			throw new CommonException(ErrorCode.BAD_REQUEST,"잘못된 형식의 파일입니다.");
		}
		ArrayList<String> fileValidate = new ArrayList<>();
		fileValidate.add(".jpg");
		fileValidate.add(".jpeg");
		fileValidate.add(".png");
		fileValidate.add(".JPG");
		fileValidate.add(".JPEG");
		fileValidate.add(".PNG");
		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!fileValidate.contains(idxFileName)) {
			throw new CommonException(ErrorCode.BAD_REQUEST,"이미지 형식의 파일(jpg, png)만 업로드 가능합니다!");
		}
		return "_"+fileName;

	}


	@Override
	public void deleteFile(String fileName){
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
		System.out.println(bucket);
	}
}
