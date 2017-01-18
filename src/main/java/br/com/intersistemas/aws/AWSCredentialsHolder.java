/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.intersistemas.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import java.io.File;
import org.jets3t.service.security.AWSCredentials;

/**
 *
 * @author willian
 */
public class AWSCredentialsHolder {

    /**
     * AWS access key
     */
    private String accessKey;
    /**
     * AWS secret key
     */
    private String secretKey;
    /**
     * Location of a properties file
     */
    private String properties;

    /**
     * @return BasicAWSCredentials which are compatible with the AWS SDK
     */
    public BasicAWSCredentials buildAwsSdkCredentials() throws Exception {
        if (properties != null) {
            PropertiesCredentials propertiesCredentials = new PropertiesCredentials(new File(properties));
            accessKey = propertiesCredentials.getAWSAccessKeyId();
            secretKey = propertiesCredentials.getAWSSecretKey();
            //System.out.println("building AWS SDK credentials (from a property file)");
        } else {
            //System.out.println("building AWS SDK credentials from plain credentials");
        }

        if (!asBoolean(accessKey) || !asBoolean(secretKey)) {
            throw new Exception("Please check user guide to see how you should configure AWS credentials");
        }
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    /**
     * @return JetS3tCredentials which are compatible with JetS3t
     */
    public AWSCredentials buildJetS3tCredentials() throws Exception {
        if (properties != null) {
            //System.out.println(new File(properties).getAbsolutePath());
            PropertiesCredentials propertiesCredentials = new PropertiesCredentials(new File(properties));
            accessKey = propertiesCredentials.getAWSAccessKeyId();
            secretKey = propertiesCredentials.getAWSSecretKey();
            //System.out.println("building JetS3t AWS credentials (from a property file)");
        } else {
            //System.out.println("building JetS3t AWS credentials from plain credentials");
        }

        if (!asBoolean(accessKey) || !asBoolean(secretKey)) {
            throw new Exception("Please check user guide to see how you should configure AWS credentials");
        }

        return new AWSCredentials(accessKey, secretKey);
    }

    public AWSCredentialsHolder(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public AWSCredentialsHolder(String properties) {
        this.properties = properties;
    }

    private boolean asBoolean(String string) {
        return (string != null && string.length() > 0);
    }

}
