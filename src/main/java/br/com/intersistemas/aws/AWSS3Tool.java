/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.intersistemas.aws;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.MultipleDeleteResult;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.utils.Mimetypes;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Created by willian on 16/01/2017.
 *
 * @author willian
 */
public class AWSS3Tool {

    private String acl;
    private String storageClass;
    private Boolean useEncryption;
    private String bucketLocation;
    private String bucket;
    private String path;
    private final AWSCredentialsHolder credentialsHolder;
    private RestS3Service s3Service;

    public AWSS3Tool(String _bucket, String properties) throws Exception {
        credentialsHolder = new AWSCredentialsHolder(properties);
        init(_bucket);
    }

    public AWSS3Tool(String _bucket, String accessKey, String secretKey) throws Exception {
        credentialsHolder = new AWSCredentialsHolder(accessKey, secretKey);
        init(_bucket);
    }

    private void init(String _bucket) throws Exception {
        s3Service = s3Service != null ? s3Service : new RestS3Service(credentialsHolder.buildJetS3tCredentials());
        try {
            bucket = bucket != null ? bucket : createBucket(_bucket).getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        validateBucketName();
    }

    //upload method for inputstreams
    public S3File inputStreamUpload(InputStream inputStream, String name) throws S3ServiceException {
        return inputStreamUpload(inputStream, name, null);
    }
    
    public S3File inputStreamUpload(InputStream inputStream, String name, Map<String, Object> cls) throws S3ServiceException {
        //System.out.println("attempting to upload file from inputStream");
        //s3 object
        S3Object s3Object = buildS3Object(new S3Object(), name, cls);
        s3Object.setDataInputStream(inputStream);
        s3Object.setContentType(Mimetypes.getInstance().getMimetype(name));
        //upload
        return new S3File(s3Service.putObject(bucket, s3Object));
    }

    public S3File fileUpload(File file) throws Exception {
        return fileUpload(file, null);
    }

    public S3File fileUpload(File file, Map<String, Object> cls) throws Exception {
        //System.out.println("attempting to upload file from plain file object");
        //s3 object
        S3Object s3Object = buildS3Object(new S3Object(file), file.getName(), cls);
        //upload
        return new S3File(s3Service.putObject(bucket, s3Object));
    }

    //delete the file
    public void delete(String name) throws Exception {
        delete(name, path);
    }

    public void delete(String name, String path) throws Exception {
        String objectKey = buildObjectKey(name, path);
        s3Service.deleteObject(new S3Bucket(bucket), objectKey);
    }

    //delete multiple files in one request
    public void deleteMultiple(String[] objectKeys) throws Exception {
        MultipleDeleteResult result = s3Service.deleteMultipleObjects(bucket, objectKeys);
        if (result.hasErrors()) {
            System.out.println("deleteMultipleObjects had errors: " + result.getErrorResults());
        }
    }

    // list all files
    public List<S3File> list() throws Exception {
        List<S3File> retorno = new ArrayList<>();
        S3Object[] s3ObjectList = s3Service.listObjects(bucket);
        for (S3Object it : s3ObjectList) {
            retorno.add(new S3File(it));
        }
        return retorno;
    }

    //delete all files in specified path
    public void deleteAll() throws Exception {
        S3Bucket s3Bucket = new S3Bucket(bucket);
        S3Object[] s3ObjectList = s3Service.listObjects(bucket);
        for (S3Object s3Object : s3ObjectList) {
            s3Service.deleteObject(s3Bucket, s3Object.getKey());
        }
    }

    //get the file
    public S3File get(String name) throws Exception {
        return get(name, path);
    }

    public S3File get(String name, String path) throws Exception {
        String objectKey = buildObjectKey(name, path);
        S3Object s3Object = s3Service.getObject(bucket, objectKey);
        return new S3File(s3Object);
    }

    //get the file details. metadata without the file content
    public S3File getDetails(String name) throws Exception {
        return getDetails(name, path);
    }

    public S3File getDetails(String name, String path) throws Exception {
        String objectKey = buildObjectKey(name, path);
        S3Object s3Object = (S3Object) s3Service.getObjectDetails(bucket, objectKey);
        return new S3File(s3Object);
    }

    //build the URL to retrieve the file
    public String url(String name) {
        return url(name, path);
    }

    public String url(String name, String path) {
        String objectKey = buildObjectKey(name, path);
        return "http://" + bucket + ".s3.amazonaws.com/" + objectKey;
    }

    //creates a torrent file for seeding S3 hosted files
    public String torrent(String name) throws Exception {
        return torrent(name, path);
    }

    public String torrent(String name, String path) throws Exception {
        String objectKey = buildObjectKey(name, path);
        return s3Service.createTorrentUrl(bucket, objectKey);
    }

    //creates a signed URL for retrieving private files
    public String publicUrlFor(Date expiryDate, String name) throws Exception {
        return publicUrlFor(expiryDate, name, path);
    }

    public String publicUrlFor(Date expiryDate, String name, String path) throws Exception {
        String objectKey = buildObjectKey(name, path);
        return s3Service.createSignedGetUrl(bucket, objectKey, expiryDate);
    }

    public S3Bucket createBucket(String bucketName) throws S3ServiceException {
        return s3Service.getOrCreateBucket(bucketName, bucketLocation);
    }

    public Map<String, Object> copyObject(String sourceObjectKey, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        return copyObject(bucket, sourceObjectKey, bucket, destinationObject, replaceMetadata);
    }

    public Map<String, Object> copyObject(String sourceObjectKey, String destinationBucketName, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        return copyObject(bucket, sourceObjectKey, destinationBucketName, destinationObject, replaceMetadata);
    }

    public Map<String, Object> copyObject(String sourceBucketName, String sourceObjectKey, String destinationBucketName, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        return s3Service.copyObject(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObject, replaceMetadata);
    }

    public Map<String, Object> moveObject(String sourceObjectKey, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        return moveObject(bucket, sourceObjectKey, bucket, destinationObject, replaceMetadata);
    }

    public Map<String, Object> moveObject(String sourceObjectKey, String destinationBucketName, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        return moveObject(bucket, sourceObjectKey, destinationBucketName, destinationObject, replaceMetadata);
    }

    public Map<String, Object> moveObject(String sourceBucketName, String sourceObjectKey, String destinationBucketName, StorageObject destinationObject, boolean replaceMetadata) throws ServiceException {
        Map<String, Object> copyResult = this.copyObject(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObject, replaceMetadata);

        try {
            this.delete(sourceBucketName, sourceObjectKey);
        } catch (Exception e) {
            copyResult.put("DeleteException", e);
        }

        return copyResult;
    }

    public Map<String, Object> renameObject(String sourceObjectKey, StorageObject destinationObject) throws ServiceException {
        return this.moveObject(bucket, sourceObjectKey, bucket, destinationObject, false);
    }

    public Map<String, Object> renameObject(String bucketName, String sourceObjectKey, StorageObject destinationObject) throws ServiceException {
        return this.moveObject(bucketName, sourceObjectKey, bucketName, destinationObject, false);
    }

    //method to build the correct key for file (composed with name and path)
    private String buildObjectKey(String name, String path) {
        if (path != null) {
            if (!path.endsWith("/")) {
                path = path.concat("/");
            }
            path = path.concat(name);
            return path;
        }
        return name;
    }

    //build s3 object
    private S3Object buildS3Object(S3Object s3Object, String name, Map<String, Object> cls) {
        //System.out.println(cls);
        s3Object.setKey(buildObjectKey(name, path));
        s3Object.setBucketName(bucket);
//		metadata.each { metaKey, metaValue ->
//			s3Object.addMetadata(metaKey, metaValue.toString());
//		}

        String aclMap = cls != null ? (String) cls.get("acl") : this.acl;
        aclMap = aclMap != null ? aclMap.toLowerCase() : "";
        switch (aclMap) {
            case "public":
                s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
                break;
            case "private":
                s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE);
                break;
            case "public_read_write":
                s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ_WRITE);
                break;
            case "authenticated_read":
                s3Object.setAcl(AccessControlList.REST_CANNED_AUTHENTICATED_READ);
                break;
            default:
                break;
        }

        String storageClassMap = cls != null ? (String) cls.get("storageClass") : this.storageClass;
        storageClassMap = storageClassMap != null ? storageClassMap.toUpperCase() : "";
        switch (storageClassMap) {
            case "REDUCED_REDUNDANCY":
                s3Object.setStorageClass(S3Object.STORAGE_CLASS_REDUCED_REDUNDANCY);
                break;
            case "STANDARD":
                s3Object.setStorageClass(S3Object.STORAGE_CLASS_STANDARD);
                break;
            case "GLACIER":
                s3Object.setStorageClass(S3Object.STORAGE_CLASS_GLACIER);
                break;
            default:
                break;
        }

        String useEncryptionMap = cls != null ? (String) cls.get("useEncryption") : (this.useEncryption != null ? this.useEncryption.toString() : null);
        if (Boolean.valueOf(useEncryptionMap)) {
            s3Object.setServerSideEncryptionAlgorithm(S3Object.SERVER_SIDE_ENCRYPTION__AES256);
        }

        return s3Object;
    }

    private void validateBucketName() throws Exception {
        if (bucket == null) {
            throw new Exception("Invalid upload attemp, do not forget to set your bucket");
        }
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public Boolean getUseEncryption() {
        return useEncryption;
    }

    public void setUseEncryption(Boolean useEncryption) {
        this.useEncryption = useEncryption;
    }

    public String getBucketLocation() {
        return bucketLocation;
    }

    public void setBucketLocation(String bucketLocation) {
        this.bucketLocation = bucketLocation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void deleteBucket(String bucketName) throws ServiceException {
        s3Service.deleteBucket(bucketName);
    }

//    public PutObjectResult createFolder(String bucketName, String folderName) {
//        // create meta-data for your folder and set content-length to 0
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(0);
//        // create empty content
//        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
//        // create a PutObjectRequest passing the folder name suffixed by /
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "suffix" /*SUFFIX*/, emptyContent, metadata);
//        // send request to S3 to create folder
//        return client.putObject(putObjectRequest);
//    }
    public void deleteFolder(String folderName) throws Exception {
        S3Object[] fileList = s3Service.listObjects(bucket, folderName, null);
        for (S3Object file : fileList) {
            delete(file.getKey());
        }
        delete(folderName);
    }
}
