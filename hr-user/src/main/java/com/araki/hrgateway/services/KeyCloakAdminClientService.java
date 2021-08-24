package com.araki.hrgateway.services;

import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araki.hrgateway.config.KeyCloakConfig;
import com.araki.hrgateway.entities.User;

@Service
public class KeyCloakAdminClientService {

	@Autowired
	KeyCloakConfig keyCloakConfig;

	@SuppressWarnings("unused")
	public void addUser(User user) {

		Keycloak keycloak = keyCloakConfig.getBuilder();
        RealmResource realmResource = keycloak.realm(keyCloakConfig.getRealm());
		UsersResource usersResource = realmResource.users();
		CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

		UserRepresentation kcUser = new UserRepresentation();
		kcUser.setUsername(user.getEmail());
		kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
		kcUser.setFirstName(user.getFirstName());
		kcUser.setLastName(user.getLastName());
		kcUser.setEmail(user.getEmail());
		kcUser.setEnabled(true);
		kcUser.setEmailVerified(false);
		
		Response response = usersResource.create(kcUser);
		System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
        System.out.println(response.getLocation());
        String userId = CreatedResponseUtil.getCreatedId(response);
        
        UserResource userResource = usersResource.get(userId);
        RoleRepresentation userRole = realmResource.roles().get("app-user").toRepresentation();
        
        userResource.roles().realmLevel()
        	.add(Arrays.asList(userRole));
        
	}

	private static CredentialRepresentation createPasswordCredentials(String password) {
		CredentialRepresentation passwordCredentials = new CredentialRepresentation();
		passwordCredentials.setTemporary(false);
		passwordCredentials.setType(CredentialRepresentation.PASSWORD);
		passwordCredentials.setValue(password);
		return passwordCredentials;
	}
}
