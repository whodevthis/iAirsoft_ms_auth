package com.msAuth.application.port.out;

public interface UserSecurityPort {
    String loginAndCreateToken(String username, String password, String ip);
    String logOut(Long userId,String ip );
}
