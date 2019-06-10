package me.delta2force.bigMemeMaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class bigMemeMaker {
	public ArrayList<PaletteItem> palette = new ArrayList<>();
	public BufferedImage editMe;
	public int num;
	
	public void receiveImages() {
		File imgs = new File("images");
				for(File f : new File("images").listFiles()) {
					BufferedImage img = null;
					try {
						img = ImageIO.read(f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					palette.add(new PaletteItem(f.getName(), Utils.averageColor(img, 0, 0, img.getWidth(), img.getHeight()), "temp"));
					img = null;
					System.gc();
					int percent = (num*100/(imgs.listFiles().length-1));
					System.out.println(f.getName() + " scanned (" + num + "/" + (imgs.listFiles().length-1) + " " + percent + "%)");
					num++;
				}
				makeMeme(editMe);
	}
	
	public void makeMeme(BufferedImage src) {
		System.gc();
		BigBufferedImage  bi = (BigBufferedImage) BigBufferedImage.create(src.getWidth()*Main.individualPixelSize, src.getHeight()*Main.individualPixelSize, BigBufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		ExecutorService executorService = Executors.newFixedThreadPool(80);
		for(int x = 0;x<src.getWidth();x++) {
			final int cx = x;
			for(int y = 0;y<src.getHeight();y++) {
				final int cy = y;
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					Color c = new Color(src.getRGB(cx, cy));
					int lowest = 9999999;
					int lowestindex = 9999999;
					for(int i = 0;i<palette.size();i++) {
						PaletteItem pi = palette.get(i);
						int df = Utils.difference(pi.col, c);
						int templ = lowest;
						if(lowest < 0) {
							templ = -lowest;
						}
						int tempd = df;
						if(df < 0) {
							tempd = -df;
						}
						if(tempd < templ) {
							lowest = df;
							lowestindex = i;
						}
					}
					try {
						BufferedImage td = ImageIO.read(new File("images/"+palette.get(lowestindex).name));//+".png"));
						g.drawImage(td, cx*Main.individualPixelSize, cy*Main.individualPixelSize, Main.individualPixelSize, Main.individualPixelSize, null);
						td=null;
						System.gc();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Difference from image to pixel: "+lowest+" | Processed pixel " + cx + ", " + cy);
					}
				});
			}
			}
		
		executorService.shutdown();
		
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			System.out.println("Done processing pixels! Saving image...");
			ImageIO.write(bi, "png", new File("enjoy"+System.currentTimeMillis()+".png"));
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processIt(BufferedImage src, Graphics2D g, int x, int y) {
		Color c = new Color(src.getRGB(x, y));
		int lowest = 9999999;
		int lowestindex = 9999999;
		for(int i = 0;i<palette.size();i++) {
			PaletteItem pi = palette.get(i);
			int df = Utils.difference(pi.col, c);
			int templ = lowest;
			if(lowest < 0) {
				templ = -lowest;
			}
			int tempd = df;
			if(df < 0) {
				tempd = -df;
			}
			if(tempd < templ) {
				lowest = df;
				lowestindex = i;
			}
		}
		try {
			BufferedImage td = ImageIO.read(new File("images/"+palette.get(lowestindex).name));//+".png"));
			g.drawImage(td, x*Main.individualPixelSize, y*Main.individualPixelSize, Main.individualPixelSize, Main.individualPixelSize, null);
			td=null;
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Difference from image to pixel: "+lowest+" | Processed pixel " + x + ", " + y);
	}
}
