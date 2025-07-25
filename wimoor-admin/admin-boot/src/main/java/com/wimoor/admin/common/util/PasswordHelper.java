package com.wimoor.admin.common.util;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import lombok.Data;
 
/**
 * <p>User:  wimoor
 * <p>Date: 18-6-28
 * <p>Version: 1.0
 */
@Data
public class PasswordHelper {


    private static String algorithmName = "md5";
    private static int hashIterations = 2;
 

   public static String initRoundomSalt() {
	   RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	   return randomNumberGenerator.nextBytes().toHex();
   }
   
    public static String encryptPassword(String account,String password,String salt) {
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes( account + salt),
                hashIterations).toHex();
        return newPassword;
    }

 
    
    
}
