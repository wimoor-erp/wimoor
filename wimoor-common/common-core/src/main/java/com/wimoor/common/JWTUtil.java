package com.wimoor.common;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wimoor.common.result.ResultCode;

public class JWTUtil {
     public static final long TOKEN_EXPIRE_TIME = 7200 * 1000;
     private static final String ISSUER = "cheng";
 
     /**
      * 生成Token
      * @param username 用户标识（不一定是用户名，有可能是用户ID或者手机号什么的）
      * @param secretKey
      * @return
      */
     public static String generateToken(String username, String secretKey) {
         Algorithm algorithm = Algorithm.HMAC256(secretKey);
         Date now = new Date();
         Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
 
         String token = JWT.create()
                 .withIssuer(ISSUER)
                 .withIssuedAt(now)
                 .withExpiresAt(expireTime)
                 .withClaim("username", username)
                 .sign(algorithm);
         return token;
     }
 
     /**
      * 校验Token
      * @param token
      * @param secretKey
      * @return
     * @throws TokenAuthenticationException 
      */
     public static void verifyToken(String token, String secretKey) throws TokenAuthenticationException {
         try {
             Algorithm algorithm = Algorithm.HMAC256(secretKey);
             JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
             jwtVerifier.verify(token);
         } catch (JWTDecodeException jwtDecodeException) {
             throw new TokenAuthenticationException(ResultCode.TOKEN_INVALID.getCode(), ResultCode.TOKEN_INVALID.getMsg());
         } catch (SignatureVerificationException signatureVerificationException) {
             throw new TokenAuthenticationException(ResultCode.TOKEN_SIGNATURE_INVALID.getCode(), ResultCode.TOKEN_SIGNATURE_INVALID.getMsg());
         } catch (TokenExpiredException tokenExpiredException) {
             throw new TokenAuthenticationException(ResultCode.TOKEN_EXPIRED.getCode(), ResultCode.TOKEN_INVALID.getMsg());
         } catch (Exception ex) {
             throw new TokenAuthenticationException(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMsg());
         }
     }
 
     /**
      * 从Token中提取用户信息
      * @param token
      * @return
      */
     public static String getUserInfo(String token) {
         DecodedJWT decodedJWT = JWT.decode(token);
         String username = decodedJWT.getClaim("username").asString();
         return username;
     }
 
 }