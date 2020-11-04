package com.example.demo.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @Author thailandking
 * @Date 2019/8/22 16:20
 * @LastEditors thailandking
 * @LastEditTime 2019/8/22 16:20
 * @Description JWT工具类
 */
@Component
public class JwtUtil {
	@Value("${audience.clientId}")
	private String clientId;
	@Value("${audience.base64Secret}")
	private String base64Secret;
	@Value("${audience.name}")
	private String name;
	@Value("${audience.expiresSecond}")
	private Long expiresSecond;

	// 解析jwt
	public User parseJWT(String jsonWebToken) {
		try {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
					.parseClaimsJws(jsonWebToken).getBody();
			if (claims == null) {
				return null;
			}
			Long id =Long.valueOf(claims.get("id").toString() );
			String name = claims.get("name").toString();
			String realName= claims.get("realName").toString();
			String url= claims.get("url").toString();
			User user = new User();
			user.setId(id);
			user.setName(name);
			user.setRealName(realName);
			user.setUrl(url);
			return user;
		} catch (Exception e) {
			return null;
		} 
	}

	// 构建jwt
	public String createJWT(User user) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// 生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").claim("id", user.getId())
				.claim("name", user.getName())
				.claim("realName", user.getRealName())
				.claim("url", user.getUrl())
				.setIssuer(clientId).setAudience(name)
				.signWith(signatureAlgorithm, signingKey);
		// 添加Token过期时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		long expMillis = nowMillis + expiresSecond;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);
		// 生成JWT
		return builder.compact();
	}
}