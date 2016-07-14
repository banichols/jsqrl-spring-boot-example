package org.jsqrl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jsqrl.model.MyUser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@Service
public class InMemoryUserService implements SqrlUserService<MyUser> {

    private Map<String, MyUser> userTable;

    public InMemoryUserService(){
        userTable = new HashMap<>();
    }

    @Override
    public MyUser getUserBySqrlKey(String publicKey) {
        return findUserBySqrlIdentity(publicKey);
    }

    @Override
    public Boolean updateIdentityKey(String previousIdentityKey, String identityKey) {
        MyUser user = findUserBySqrlIdentity(previousIdentityKey);

        if(user != null) {
            user.setIdentityKey(identityKey);
            return true;
        }

        return false;
    }

    @Override
    public MyUser registerSqrlUser(String identityKey, String serverUnlockKey, String verifyUnlockKey) {
        String userId = UUID.randomUUID().toString();
        MyUser newUser = new MyUser(userId, identityKey, serverUnlockKey, verifyUnlockKey, true, null, null);
        userTable.put(userId, newUser);
        return newUser;
    }

    @Override
    public Boolean disableSqrlUser(String identityKey) {
        MyUser user = getUserBySqrlKey(identityKey);
        if(user != null){
            user.setSqrlEnabled(false);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Boolean enableSqrlUser(String identityKey) {
        MyUser user = getUserBySqrlKey(identityKey);
        if(user != null){
            user.setSqrlEnabled(true);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Boolean removeSqrlUser(String identityKey) {

        MyUser user = getUserBySqrlKey(identityKey);

        if(user != null) {
            userTable.remove(user.getId());
            return true;
        }
        return false;
    }

    public Boolean updateUserById(String id, String firstName, String email){
        MyUser user = userTable.get(id);
        user.setFirstName(firstName);
        user.setEmail(email);
        return true;
    }

    private MyUser findUserBySqrlIdentity(String publicKey){

        return userTable.entrySet().stream()
                .filter(e -> publicKey.equals(e.getValue().getIdentityKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }



}
