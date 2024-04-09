package com.agentdid127.resourcepack.bedrock.utilities;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter extends com.agentdid127.resourcepack.library.utilities.ImageConverter {
	public ImageConverter(Integer defaultWIn, Integer defaultHIn, Path locationIn) throws IOException {
		super(defaultWIn, defaultHIn, locationIn);
	}

	public void newImage(int newWidth, int newHeight, int type) {
		// 2 = BufferedImage.TYPE_INT_ARGB
		this.newImage = new BufferedImage(Math.round((float) ((double) newWidth * this.getWidthMultiplier())),
				Math.round((float) ((double) newHeight * this.getHeightMultiplier())), type);
		this.g2d = (Graphics2D) this.newImage.getGraphics();
	}

	public void colorizeClipped(Color color) {
		this.newImage(this.getWidth(), this.getHeight());
		this.g2d.drawImage(this.image, 0, 0, null);
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				int imageRGBA = newImage.getRGB(x, y);
				int alpha = (imageRGBA >> 24) & 0xFF;
				if (alpha == 0)
					continue;
				int grayscaleValue = (imageRGBA >> 16) & 0xFF;
				int red = (grayscaleValue * color.getRed()) / 255;
				int green = (grayscaleValue * color.getGreen()) / 255;
				int blue = (grayscaleValue * color.getBlue()) / 255;
				this.newImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
	}

	private void copyPixels(BufferedImage inputImage, BufferedImage outImage, Integer alphaNew)
			throws IOException {
		BufferedImage image = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(inputImage, 0, 0, null);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgba = image.getRGB(x, y);
				int alpha = (rgba >> 24) & 0xFF;
				int red = (rgba >> 16) & 0xFF;
				int green = (rgba >> 8) & 0xFF;
				int blue = rgba & 0xFF;
				if ((red + blue + green + alpha) == 0)
					continue;
				if (alphaNew != null)
					alpha = alphaNew;
				outImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
	}

	private void copyPixels(BufferedImage inputImage, BufferedImage outImage) throws IOException {
		copyPixels(inputImage, outImage, null);
	}

	public void backgroundImage(Path background, int alpha) throws IOException {
		this.newImage(defaultW, defaultH);
		BufferedImage backgroundImage = ImageIO.read(background.toFile());
		this.copyPixels(backgroundImage, newImage, alpha);
		this.copyPixels(image, newImage);
	}
}