package com.ewe.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Zxing方式 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的，可以将该类直接拷贝到源码中使用
 * 
 * @author luckyhua 2015年10月28日
 */
public class MatrixToImageWriter {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private static final int Width = 200; // 二维码图片宽度
	private static final int Height = 200; // 二维码图片高度
	
	private static final String Img_Format_Jpg = "jpg"; //二维码输出图片格式
	private static final String Img_Format_Png = "png";
	private static final String Img_Format_Gif = "gif";
	
	private MatrixToImageWriter() {
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "
					+ format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "
					+ format);
		}
	}
	
	/**
	 * 生成二维码
	 * @param content	二维码内容
	 * @return
	 * @throws Exception
	 */
	public static String genQrcode(String content, String fileName) throws Exception{
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, Width, Height, hints);
		
		// 生成二维码
		fileName = fileName + "." + Img_Format_Jpg;
		File outputFile = new File(FileUtils.getFilePath(FileUtils.File_Qrcode) + File.separator + fileName);
		MatrixToImageWriter.writeToFile(bitMatrix, Img_Format_Jpg, outputFile);
		return FileUtils.getFileUrl(fileName, FileUtils.File_Qrcode);
	}
	
	/**
	 * 生成二维码
	 * @param content	二维码内容
	 * @return 文件存储路径
	 * @throws Exception
	 */
	public static String genQrcode(String filePath,String content, String fileName) throws Exception{
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, Width, Height, hints);
		
		// 生成二维码
		fileName = fileName + "." + Img_Format_Jpg;
		File f = new File(filePath);
		if(!f.exists())
			f.mkdirs();
		File outputFile = new File(filePath + File.separator + fileName);
		MatrixToImageWriter.writeToFile(bitMatrix, Img_Format_Jpg, outputFile);
		return filePath+fileName;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(genQrcode("http://www.qq.com", "11"));
	}
	
}
