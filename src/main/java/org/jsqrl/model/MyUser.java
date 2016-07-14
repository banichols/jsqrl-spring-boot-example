package org.jsqrl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@Getter
@Setter
@AllArgsConstructor
public class MyUser implements SqrlUser{

    private String id;

    private String identityKey;
    private String serverUnlockKey;
    private String verifyUnlockKey;
    private Boolean sqrlEnabled;

    private String firstName;
    private String email;

    @Override
    public Boolean sqrlEnabled() {
        return sqrlEnabled;
    }
}
