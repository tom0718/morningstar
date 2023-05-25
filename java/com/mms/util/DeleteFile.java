package com.mms.util;

import java.io.File;

public class DeleteFile {

	public static void delFile(String path, String filename){
		File file = new File(path, filename);
		if(file.exists()) file.delete();
	}
}
