package com.ownxile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyDirectory {

	public static void copyDirectory(File srcPath, File dstPath)
			throws IOException {
		if (dstPath.exists()) {
			System.out
					.println("A backup has already been created in the last 24 hours.");
			return;
		}
		if (srcPath.isDirectory()) {
			dstPath.mkdirs();

			final String files[] = srcPath.list();
			for (String file : files) {
				copyDirectory(new File(srcPath, file), new File(dstPath, file));
			}
		} else {
			if (!srcPath.exists()) {
				System.out.println("File or directory does not exist.");
				System.exit(0);
			} else {
				final InputStream in = new FileInputStream(srcPath);
				final OutputStream out = new FileOutputStream(dstPath);
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
	}
}