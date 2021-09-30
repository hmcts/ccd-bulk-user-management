package uk.gov.hmcts.ccd.bulk.user.management.services;

import uk.gov.hmcts.ccd.bulk.user.management.domain.IdamRole;

import java.io.IOException;
import java.util.*;

public class RoleService {
    private List<IdamRole> idamRoles;
    private IdamService idamService;

    public RoleService(IdamService idamService) {
        this.idamRoles = new ArrayList<>();
        this.idamService = idamService;
    }

    public String getRole (String roleName) throws IOException, InterruptedException {
        if (!this.idamRoles.stream().anyMatch(o -> o.getName().equals(roleName))){
            this.idamRoles.add(idamService.getRole(roleName));
        }
        Optional<IdamRole> returnedRole = idamRoles.stream().filter(r -> r.getName().equals(roleName)).findAny();
        return returnedRole.get().getId();
    }

    public String updateRoles(String userId, List<String> rolesToAdd, List<String> rolesToRemove) throws IOException, InterruptedException {
        addRoles(userId, rolesToAdd);
        removeRoles(userId, rolesToRemove);

        return null;
    }

    private boolean removeRoles(String userId, List<String> rolesToRemove) throws IOException, InterruptedException {
        for (String role : rolesToRemove ) {
            this.idamService.removeRole(userId, getRole(role));

        }
        return true;
    }

    private boolean addRoles(String userId, List<String> rolesToAdd) throws IOException, InterruptedException {
        for (String role : rolesToAdd ) {
            this.idamService.addRole(userId, getRole(role));

        }
        return true;
    }



}
