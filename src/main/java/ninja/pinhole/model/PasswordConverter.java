package ninja.pinhole.model;

import javax.persistence.AttributeConverter;

/**
 * Converter for a password.
 * Makes stored password in db unreadable
 */
public class PasswordConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String s) {
        return xorPassWord(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return xorPassWord(s);
    }

    /**
     * XOR a string with a secret key. Wooo..
     */
    private String  xorPassWord(String s){
        // Changing the secret will invalidate any existing password!
        String secret = "Zjkl5d@~!";
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); i++) {
            // sb.append((char) (s.charAt(i) ^ (char) 255));
            sb.append((char) (s.charAt(i) ^ secret.charAt(i % secret.length())));
        }

        return sb.toString();
    }

}
