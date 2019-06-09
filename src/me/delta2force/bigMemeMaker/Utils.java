package me.delta2force.bigMemeMaker;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Utils {
	public static Color averageColor(BufferedImage bi, int x0, int y0, int w,
	        int h) {
	    int x1 = x0 + w;
	    int y1 = y0 + h;
	    long sumr = 0, sumg = 0, sumb = 0;
	    for (int x = x0; x < x1; x++) {
	        for (int y = y0; y < y1; y++) {
	            Color pixel = new Color(bi.getRGB(x, y));
	            sumr += pixel.getRed();
	            sumg += pixel.getGreen();
	            sumb += pixel.getBlue();
	        }
	    }
	    int num = w * h;
	    return new Color((int)(sumr / num), (int)(sumg / num), (int)(sumb / num));
	}
	
	public static int difference(Color from, Color to) {
		int fr = from.getRed();
		int fg = from.getGreen();
		int fb = from.getBlue();
		
		int tr = to.getRed();
		int tg = to.getGreen();
		int tb = to.getBlue();
		
		int dr = tr-fr;
		int dg = tg-fg;
		int db = tb-fb;
		
		return (int) (Math.pow(dr, 2)+Math.pow(dg, 2)+Math.pow(db, 2));
	}
}
