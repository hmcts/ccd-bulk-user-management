package uk.gov.hmcts.ccd.bulk.user.management.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputUser {
    private String operation;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private List<String> rolesToAdd;
    private List<String> rolesToRemove;
    private String inviteStatus;
    private IdamUser idamUser;

    public InputUser(String operation, String email, String firstName, String lastName, String roles, String rolesToAdd, String rolesToRemove, String inviteStatus) {
        this.operation = operation;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = new ArrayList<String>(Arrays.asList(roles.split("\\|")));
        this.rolesToAdd = new ArrayList<String>(Arrays.asList(rolesToAdd.split("\\|")));
        this.rolesToRemove = new ArrayList<String>(Arrays.asList(rolesToRemove.split("\\|")));
        this.inviteStatus = inviteStatus;
        this.idamUser = new IdamUser(this.email, this.firstName, this.lastName, this.roles);
    }

    public InputUser (String operation, String email, String firstName, String lastName, String roles, String rolesToAdd, String rolesToRemove) {
        this.operation = operation;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = new ArrayList<String>(Arrays.asList(roles.split("\\|")));
        this.rolesToAdd = new ArrayList<String>(Arrays.asList(rolesToAdd.split("\\|")));
        this.rolesToRemove = new ArrayList<String>(Arrays.asList(rolesToRemove.split("\\|")));
        this.inviteStatus = "";
        this.idamUser = new IdamUser(this.email, this.firstName, this.lastName, this.roles);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRolesToAdd() {
        return rolesToAdd;
    }

    public void setRolesToAdd(List<String> rolesToAdd) {
        this.rolesToAdd = rolesToAdd;
    }

    public List<String> getRolesToRemove() {
        return rolesToRemove;
    }

    public void setRolesToRemove(List<String> rolesToRemove) {
        this.rolesToRemove = rolesToRemove;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public IdamUser getIdamUser() {
        return idamUser;
    }

    public void setIdamUser(IdamUser idamUser) {
        this.idamUser = idamUser;
    }
}
