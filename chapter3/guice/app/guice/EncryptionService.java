package guice;

public interface EncryptionService {
	public String encrypt(String clearText);
	public String decrypt(String cipherText);
}
