/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package uk.gov.hmcts.ccd.bulk.user.management.app;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.*;
import uk.gov.hmcts.ccd.bulk.user.management.domain.InputUser;
import uk.gov.hmcts.ccd.bulk.user.management.domain.OutputUser;
import uk.gov.hmcts.ccd.bulk.user.management.services.FileService;
import uk.gov.hmcts.ccd.bulk.user.management.services.IdamService;
import uk.gov.hmcts.ccd.bulk.user.management.services.InputService;
import uk.gov.hmcts.ccd.bulk.user.management.services.RoleService;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.sql.Timestamp;

public class App {

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String strTimeStamp = new String(timestamp.toString()).replace(" ","");
        System.out.println(strTimeStamp);
        FileService fileService = new FileService(strTimeStamp);
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }

        String csvPath = console.readLine("Please enter csv file path: ");
        String idamUserName = console.readLine("Please enter ccd idam-admin username: ");
        char[] idamUserPassword = console.readPassword("Please enter ccd idam-admin password: ");
        char[] clientSecret = console.readPassword("Please enter idam oauth2 secret for ccd-bulk-user-register client: ");
        System.out.println(String.format("Password entered was: %s", new String(idamUserPassword)));
        System.out.println(String.format("Password entered was: %s", new String(clientSecret)));
        String environment = console.readLine("Please enter environment default [prod]: ");
        if (environment.isEmpty()){environment = "prod";}
        System.out.println(String.format("Environment was: '%s'", environment));
        InputService inputService = new InputService();
        IdamService idamService = new IdamService(environment);
        RoleService roleService = new RoleService(idamService);
        try {
            List<InputUser> inputUsers =  inputService.processInputFile(csvPath);
            fileService.backupInputFile(csvPath);
            idamService.getIdamToken(idamUserName, new String(idamUserPassword), new String(clientSecret));
            List<OutputUser> outputUsers = new ArrayList<>();

            for (InputUser user : inputUsers) {
                if (!user.getInviteStatus().equalsIgnoreCase("SUCESS")){
                    if (user.getOperation().equalsIgnoreCase("add")) {
                        outputUsers.add(idamService.registerUser(user));
                    } else {
                        String userId = idamService.getUserId(user.getEmail());
                        List<String> rolesUpdate = new ObjectMapper().readValue(idamService.getRoles(userId), new TypeReference<List<String>>() {});
                        System.out.println("Initial from main " + rolesUpdate);
                        System.out.println(rolesUpdate);
                        rolesUpdate.addAll(user.getRolesToAdd());
                        System.out.println(rolesUpdate);
                        rolesUpdate.removeAll(user.getRolesToRemove());
                        System.out.println(rolesUpdate);
                        try {
                            if (!idamService.updateUserRoles(userId,  rolesUpdate).equals("HTTP-200")) throw new Exception();
                        } catch (Exception e) {
                            if (!roleService.updateRoles(userId, user.getRolesToAdd(), user.getRolesToRemove()).equals("HTTP-200")) throw new Exception();
                        }
                        //outputUsers.add(idamService.updateUserRoles(roleService));
                    }
                } else {
                    System.out.println("Skipped " + user.getEmail());
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
