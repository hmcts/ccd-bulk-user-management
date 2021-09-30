package uk.gov.hmcts.ccd.bulk.user.management.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IdamRole {
    private List<String> assignableRoles;
    private List<String> conflictingRoles;
    private String description;
    private String id;
//    private List<String> linkedRoles;
    private String name;

//    @JsonCreator
//    public IdamRole(@JsonProperty("assignableRoles") List<String> assignableRoles,
//                    @JsonProperty("conflictingRoles") List<String> conflictingRoles,
//                    @JsonProperty("description") String description,
//                    @JsonProperty("id") String id,
//                    @JsonProperty("name") String name) {
    public IdamRole () {
        super();
    }

    public IdamRole(List<String> assignableRoles, List<String> conflictingRoles, String description, String id, String name) {
        this.assignableRoles = assignableRoles;
        this.conflictingRoles = conflictingRoles;
        this.description = description;
        this.id = id;
//        this.linkedRoles = linkedRoles;
        this.name = name;
    }

    public List<String> getAssignableRoles() {
        return assignableRoles;
    }

    public void setAssignableRoles(List<String> assignableRoles) {
        this.assignableRoles = assignableRoles;
    }

    public List<String> getConflictingRoles() {
        return conflictingRoles;
    }

    public void setConflictingRoles(List<String> conflictingRoles) {
        this.conflictingRoles = conflictingRoles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public List<String> getLinkedRoles() {
//        return linkedRoles;
//    }
//
//    public void setLinkedRoles(List<String> linkedRoles) {
//        this.linkedRoles = linkedRoles;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
