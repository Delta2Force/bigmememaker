package me.delta2force.bigMemeMaker;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {
	public static String editMeName;
	public static int individualPixelSize;
	
	public static void main(String[] args) {
		if(args.length <2) {
			System.out.println("java -jar bigmememaker <Name of the Original Pic> <Size of the images in the collage>");
			System.exit(0);
		}else {
			editMeName = args[0];
			individualPixelSize = Integer.parseInt(args[1]);
		}
		bigMemeMaker bmm = new bigMemeMaker();
		try {
			bmm.editMe = ImageIO.read(new File(editMeName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmm.receiveImages();
	}
}
