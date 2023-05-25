package com.mms.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

public class JcodecThumbnail {
	
	private int frameNumber = 1;
	private Picture picture;

	public String getThumbnail(String vedio, String path) {
		
		try {
			picture = FrameGrab.getFrameFromFile(new File(path+File.separator+vedio), frameNumber);
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			File file = new File(path+File.separator+vedio+".png");
			ImageIO.write(bufferedImage, "png", file);
			
			return file.getName();
			
		} catch (IOException | JCodecException e) {
			e.printStackTrace();
			return null;
		}

	}

}
