package uk.gov.hmcts.ccd.bulk.user.management.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.gov.hmcts.ccd.bulk.user.management.domain.InputUser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputService {
    public List<InputUser> processInputFile(String csvPath) throws FileNotFoundException {
        List<InputUser> inputUsers = null;
        Pattern pattern = Pattern.compile(",");
        try (BufferedReader in = new BufferedReader(new FileReader(csvPath));) {
            inputUsers = in .lines() .skip(1) .map(line -> {
                String[] x = pattern.split(line, -1);
                for (String i : x) {
                    System.out.println(i);
                }
                return new InputUser(x[0], x[1], x[2], x[3], x[4], x[5], x[6]); }) .collect(Collectors.toList());
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.enable(SerializationFeature.INDENT_OUTPUT);
//            mapper.writeValue(System.out, inputUsers);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputUsers;
    }
}
