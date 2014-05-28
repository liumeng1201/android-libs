package com.lm.lib.http_net_lib.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IOUtilities {
	
	public static String readFromFile(String path) {
		ByteArrayOutputStream out = null;
		FileInputStream in = null;
		try {
			File f = new File(path);
			in = new FileInputStream(f);
			out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1000];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			out.flush();
			return new String(out.toByteArray());
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
