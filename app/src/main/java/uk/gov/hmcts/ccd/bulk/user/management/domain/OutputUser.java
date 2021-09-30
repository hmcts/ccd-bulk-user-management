package uk.gov.hmcts.ccd.bulk.user.management.domain;

import java.util.List;

public class OutputUser {
    private boolean active;
    private String email;
    private String forename;
    private String id;
    private String lastModified;
    private boolean locked;
    private boolean pending;
    private List<String> roles;
    private String surname;
    private String status;

    public OutputUser(boolean active, String email, String forename, String id, String lastModified, boolean locked, boolean pending, List<String> roles, String surname, String status) {
        this.active = active;
        this.email = email;
        this.forename = forename;
        this.id = id;
        this.lastModified = lastModified;
        this.locked = locked;
        this.pending = pending;
        this.roles = roles;
        this.surname = surname;
        this.status = status;
    }

    public OutputUser () {
        super();
    }
}
