package uk.gov.hmcts.ccd.bulk.user.management.services;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileService {
    String timestamp;

    public FileService (String timestamp){
        this.timestamp = timestamp;
    }
    public void backupInputFile (String csvPath) throws IOException {
        File source = new File(csvPath);
        File dest = new File(String.format("%s1%s2_INPUT.csv", csvPath.replace("csv", ""), this.timestamp));

        FileUtils.copyFile(source, dest);



    }
}
