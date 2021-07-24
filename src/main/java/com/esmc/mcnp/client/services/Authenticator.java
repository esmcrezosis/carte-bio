package com.esmc.mcnp.client.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.esmc.mcnp.client.dto.User;
import com.esmc.mcnp.client.runtime.SerializationTools;
import com.esmc.mcnp.client.util.Md5Utils;

public class Authenticator {

    private static User user;

    public Authenticator() {
    }

    public static User loadUserFromServer(String login, int integrateur) throws RejectedExecutionException, NullPointerException, UnsupportedEncodingException, InterruptedException, ExecutionException, IOException, TimeoutException {
        if (StringUtils.isNotBlank(login)) {
            try {
                CustomPair result = null;
                if (integrateur == 1) {
                    result = AsyncRestClient.executePost("carte/asso", login);
                } else {
                    result = AsyncRestClient.executePost("carte/user", login);
                }
                if (Objects.nonNull(result)) {
                    if (result.getKey() == 200) {
                        user = (User) SerializationTools.jsonDeserialise(result.getValue(), User.class);
                        if (Objects.nonNull(user)) {
                            return user;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                throw e;
            }
        }
        return null;
    }

    public static boolean validate(String login, String password, int integrateur) throws UnsupportedEncodingException, InterruptedException, ExecutionException, IOException, RejectedExecutionException, NullPointerException, TimeoutException {
        User userVal = loadUserFromServer(login, integrateur);
        if (Objects.nonNull(userVal)) {
            if (integrateur == 1) {
                return userVal.getPassword() != null && userVal.getPassword().equals(password);
            } else {
                if (StringUtils.isNotBlank(userVal.getPassword())) {
                    String hpassword = Md5Utils.hash(password);
                    return userVal.getPassword() != null
                            && hpassword.equals(userVal.getPassword());
                } else {
                    return userVal.getPassword() != null
                            && BCrypt.checkpw(password, userVal.getPasswordHash());
                }
            }
        } else {
            return false;
        }
    }

    public static User getUser() {
        return user;
    }

}
