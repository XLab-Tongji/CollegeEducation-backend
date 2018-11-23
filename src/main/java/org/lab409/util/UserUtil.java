package org.lab409.util;

import org.lab409.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserUtil {
    UserEntity getCurrentUser();
    String getCurrentPassword();
    UserDetails getUserHelperInfo();

}
