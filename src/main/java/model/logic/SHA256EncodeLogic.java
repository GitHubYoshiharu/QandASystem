package model.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class SHA256EncodeLogic {
	public String execute(String targetString) {
        String encodedString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(targetString.getBytes());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String tmp = Integer.toHexString(digest[i] & 0xff);
                if (tmp.length() == 1) {
                    builder.append('0').append(tmp);
                } else {
                    builder.append(tmp);
                }
            }
            encodedString = builder.toString();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHA256EncodeLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encodedString;
    }
}
