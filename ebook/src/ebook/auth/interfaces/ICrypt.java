package ebook.auth.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ebook.core.exceptions.CryptException;

public interface ICrypt {

	String CRYPT_PREFIX = "TnhTI5Az983Akms~AyHtHctE$|F546DLAUuN?RpXm4fLutW*G61Bj#fFw#DDDAnn";

	byte[] Encrypt(String activationString) throws CryptException;

	String toString(byte[] encrypt) throws UnsupportedEncodingException;

	byte[] toByteArray(InputStream inputStream) throws IOException;

	String Decrypt(byte[] cipheredBytes) throws CryptException;

	byte[] toByteArray(String activationString) throws IOException;

}
