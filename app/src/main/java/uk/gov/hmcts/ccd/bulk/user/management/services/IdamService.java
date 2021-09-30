package uk.gov.hmcts.ccd.bulk.user.management.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.gov.hmcts.ccd.bulk.user.management.domain.IdamRole;
import uk.gov.hmcts.ccd.bulk.user.management.domain.InputUser;
import uk.gov.hmcts.ccd.bulk.user.management.domain.OutputUser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IdamService {
    private String environment;
    private String idamUrl;
    private String accessToken;

    private static final String CLIENT_ID = "ccd-bulk-user-register";
    private static final String PROD_URL = "https://idam-api.platform.hmcts.net";
    private static final String LOCAL_URL = "http://localhost:5000";
    private static final String CUSTOM_URL = "https://idam-api.%s.platform.hmcts.net";
    private static final String TOKEN_REQUEST = "/o/token";
    private static final String REGISTRATION_REQUEST = "/user/registration";
    private static final String USER_BY_EMAIL_REQUEST = "/users?email=%s";
    private static final String USER_BY_ID_REQUEST = "/api/v1/users/%s";
    private static final String UPDATE_USER_ROLES_REQUEST = "/api/v1/users/%s/roles";
    private static final String IDAM_ROLE_REQUEST = "/roles/name/%s";
    private static final String INDIVIDUAL_USER_ROLE_REQUEST = "/users/%s1/roles/%s2";
    private static final String REDIRECT_URI = "https://create-bulk-user-test/oauth2redirect";
    private static final String SCOPES = "openid roles create-user manage-user";
    private static final String HTTP_PREFIX = ": HTTP-";


    public IdamService (String environment) {
        this.environment = environment.toLowerCase();
        switch (this.environment) {
            case "prod":
                this.idamUrl = PROD_URL;
                break;
            case "local":
                this.idamUrl = LOCAL_URL;
                break;
            default:
                this.idamUrl = String.format(CUSTOM_URL, this.environment);
        }
        System.out.println(this.idamUrl);
    }


    public void getIdamToken (String userName, String password, String clientSecret) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> data = new HashMap<>();
        data.put("client_id", CLIENT_ID);
        data.put("client_secret", clientSecret);
        data.put("grant_type", "password");
        data.put("username", userName);
        data.put("password", password);
        data.put("redirect_uri", REDIRECT_URI);
        data.put("scope", SCOPES);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(this.idamUrl + TOKEN_REQUEST))
                .header("accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        final ObjectNode node = new ObjectMapper().readValue(response.body(), ObjectNode.class);

        if (node.has("access_token")) {
            System.out.println("access_token: " + node.get("access_token"));
        }

        this.accessToken = node.get("access_token").textValue();


    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }


    public OutputUser registerUser(InputUser user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(user.getIdamUser())))
                .uri(URI.create(this.idamUrl + REGISTRATION_REQUEST))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            System.out.println(user.getEmail() + HTTP_PREFIX + response.statusCode());
        } else {
            System.out.println(user.getEmail() + HTTP_PREFIX + response.statusCode() + ":" + response.body());
        }

        return mapper.readValue(response.body(), OutputUser.class);
    }

    public String getUserId(String email) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(this.idamUrl + String.format(USER_BY_EMAIL_REQUEST, email)))
                .header("accept", "*/*")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        final ObjectNode node = new ObjectMapper().readValue(response.body(), ObjectNode.class);
        System.out.println(response.body());
        if (node.has("id") && response.statusCode() == 200) {
            return node.get("id").textValue();
        }

        return node.get("id").textValue();
    }

    public String getRoles(String userId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(this.idamUrl + String.format(USER_BY_ID_REQUEST, userId)))
                .header("accept", "*/*")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        final ObjectNode node = new ObjectMapper().readValue(response.body(), ObjectNode.class);
        System.out.println(response.body());
//        if (node.has("roles") && response.statusCode() == 200) {
//            System.out.println(node.get("roles").toString());
//            return new ArrayList<String>(Arrays.asList(node.get("roles").toString().split(",")));
//        }
//
//        return new ArrayList<String>(Arrays.asList(node.get("roles").toString().split(",")));

        if (node.has("roles") && response.statusCode() == 200) {
            System.out.println(node.get("roles").toString());
            return node.get("roles").toString();
        }

        return node.get("roles").toString();
    }

    public IdamRole getRole(String roleName) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(this.idamUrl + String.format(IDAM_ROLE_REQUEST, roleName)))
                .header("accept", "*/*")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        return mapper.readValue(response.body(), IdamRole.class);
    }

    public String updateUserRoles(String userId, List<String> rolesUpdate) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rolesUpdate)))
                .uri(URI.create(this.idamUrl + String.format(UPDATE_USER_ROLES_REQUEST, userId)))
                .header("accept", "*/*")
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer " + this.accessToken)
                .build();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rolesUpdate));
        System.out.println(this.idamUrl + String.format(UPDATE_USER_ROLES_REQUEST, userId));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.toString());
        return HTTP_PREFIX + response.statusCode();
    }

    public OutputUser addRole(String userId, String role) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(this.idamUrl + String.format(INDIVIDUAL_USER_ROLE_REQUEST, userId, role)))
                .header("accept", "application/json")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return mapper.readValue(response.body(), OutputUser.class);
    }

    public OutputUser removeRole(String userId, String role) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(this.idamUrl + String.format(INDIVIDUAL_USER_ROLE_REQUEST, userId, role)))
                .header("accept", "application/json")
                .header("authorization", "Bearer " + this.accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return mapper.readValue(response.body(), OutputUser.class);
    }
}
