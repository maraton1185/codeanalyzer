package ebook.auth.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import ebook.auth.interfaces.ICrypt;
import ebook.core.exceptions.CryptException;

public class Crypt implements ICrypt {

	public static final String CRYPT_PREFIX = "TnhTI5Az983Akms~AyHtHctE$|F546DLAUuN?RpXm4fLutW*G61Bj#fFw#DDDAnn";
	private String iv = "fedcba9876543210";// Dummy iv (CHANGE IT!)
	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;

	private String SecretKey = "0123456789abcdef";// Dummy secretKey (CHANGE
													// IT!)
	public static final String characterEncoding = "UTF-8";

	public Crypt() {

		try {
			ivspec = new IvParameterSpec(getKeyBytes(iv));

			keyspec = new SecretKeySpec(getKeyBytes(SecretKey), "AES");

			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
		byte[] keyBytes = new byte[16];
		byte[] parameterKeyBytes = key.getBytes(characterEncoding);
		System.arraycopy(parameterKeyBytes, 0, keyBytes, 0,
				Math.min(parameterKeyBytes.length, keyBytes.length));
		return keyBytes;
	}

	public byte[] encrypt(byte[] plainText) throws Exception {

		byte[] encrypted = null;

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

		encrypted = cipher.doFinal(plainText);

		return encrypted;
	}

	public byte[] decrypt(byte[] cipherText) throws Exception {

		byte[] decrypted = null;

		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

		decrypted = cipher.doFinal(cipherText);

		return decrypted;
	}

	@Override
	public byte[] Encrypt(String plainText) throws CryptException {
		try {
			return encrypt(plainText.getBytes(characterEncoding));
		} catch (Exception e) {
			throw new CryptException();
		}
	}

	@Override
	public String toString(byte[] cipheredBytes)
			throws UnsupportedEncodingException {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encodeBuffer(cipheredBytes);
	}

	@Override
	public byte[] toByteArray(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	@Override
	public String Decrypt(byte[] cipheredBytes) throws CryptException {
		try {
			return new String(decrypt(cipheredBytes), characterEncoding);
		} catch (Exception e) {
			throw new CryptException();
		}
	}

	@Override
	public byte[] toByteArray(String input) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(input);
	}
}