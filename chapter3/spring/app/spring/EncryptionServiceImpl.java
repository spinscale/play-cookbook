package spring;

import org.apache.commons.lang.StringUtils;

//@Component
public class EncryptionServiceImpl implements EncryptionService {

	@Override
	public String decrypt(String cipherText) {
		return "D" + StringUtils.reverse(cipherText);
	}

	@Override
	public String encrypt(String clearText) {
		return "E" + StringUtils.reverse(clearText);
	}
}
