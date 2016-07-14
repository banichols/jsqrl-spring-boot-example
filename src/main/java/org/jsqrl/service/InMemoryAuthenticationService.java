package org.jsqrl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brent on 7/10/2016.
 */
@Service
public class InMemoryAuthenticationService implements SqrlAuthenticationService {

    private Map<String, Authorization> authTable;
    private Map<String, String> nutRelationTable;

    public InMemoryAuthenticationService(){
        this.authTable = new HashMap<>();
        this.nutRelationTable = new HashMap<>();
    }

    @Override
    public Boolean createAuthenticationRequest(String originalNut, String ipAddress) {
        Authorization authorization = new Authorization(ipAddress, null, null, false);
        authTable.put(originalNut, authorization);
        return true;
    }

    @Override
    public Boolean linkNut(String oldNut, String newNut) {
        Authorization auth = authTable.get(oldNut);
        if(auth != null) {
            //This is an original authorization nut
            nutRelationTable.put(newNut, oldNut);
        } else{
            String authNut = nutRelationTable.get(oldNut);
            nutRelationTable.remove(oldNut);
            nutRelationTable.put(newNut, authNut);
        }
        return true;
    }

    @Override
    public Boolean authenticateNut(String nut, String identityKey) {
        String authNut = nutRelationTable.get(nut);
        Authorization auth = authTable.get(authNut);
        auth.setIdentityKey(identityKey);
        auth.setAuthorized(true);
        return true;
    }

    @Override
    public String getAuthenticatedSqrlIdentityKey(String nut) {

        Authorization auth = authTable.get(nut);

        if(auth != null && auth.getAuthorized()){
            return auth.getIdentityKey();
        }

        return null;
    }


    @AllArgsConstructor
    @Getter
    private class Authorization{
        private String ipAddress;
        @Setter
        private String identityKey;
        @Setter
        private String correlatedNut;
        @Setter
        private Boolean authorized;
    }

}
