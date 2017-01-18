/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.intersistemas.aws;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author willian
 */
public class Teste {

    public static void main(String[] args) throws Exception {
        String accessKey = "";
        String secretKey = "";
        String bucket = "";
        String name = "folder/to/my/file/dica_data_agendamento.txt";

        AWSS3Tool awsTool = new AWSS3Tool(bucket, accessKey, secretKey);

        //awsTool.createBucket("teste2");
//        String url = awsTool.url("folder/to/my/select tabelas usadas.txt");
//        System.out.println(url);
//        String torrent = awsTool.torrent("folder/to/my/select tabelas usadas.txt");
//        System.out.println(torrent);
        //awsTool.delete("folder/to/my/select tabelas usadas.txt");
//        String[] lista = new String[2];
//        lista[0] = "folder/select tabelas usadas.txt";
//        lista[1] = "folder/to/select tabelas usadas.txt";
//        awsTool.deleteMultiple(lista);
//        S3File details = awsTool.getDetails("select tabelas usadas.txt");
//        System.out.println(details.getSource().getCompleteMetadataMap());
//        System.out.println(details.getSource().getContentType());
//        System.out.println(details.getSource().getLastModifiedDate());
        //DateRangeCreator.fromHours(1);//TODO não ta funcionando
//        Date date = new Date(System.currentTimeMillis() + 100000);
//        System.out.println(date);
//        String publicUrl = awsTool.publicUrlFor(date, name);
//        System.out.println(publicUrl);
        // S3File file = awsTool.get("folder/select tabelas usadas.txt");
//        //File temp = File.createTempFile("inicio", "fim");
//        //File file2 = file.getSource().getDataInputFile();
//        InputStream file3 = file.getSource().getDataInputStream();
//        System.out.println(getStringFromInputStream(file3));
        //File docFile = new File("D:/willian/Desktop/index.gsp");
//        Map<String, Object> opts = new HashMap();
//        opts.put("acl", "private");
//        opts.put("storageClass", "STANDARD");
//        awsTool.setAcl("public");
//        awsTool.setStorageClass("STANDARD");
//        awsTool.setPath("folder/to/my");
        //awsTool.fileUpload(docFile, opts);
        //awsTool.fileUpload(docFile);
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                line = (new String(line.getBytes("iso-8859-1"), "UTF-8"));
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
