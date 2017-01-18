/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.intersistemas.aws;

import org.jets3t.service.model.S3Object;

/**
 *
 * @author willian
 */
public class S3File {

    private S3Object source;

    public S3File(S3Object _source) {
        source = _source;
    }

    public S3Object getSource() {
        return source;
    }

    public void setSource(S3Object source) {
        this.source = source;
    }
}
