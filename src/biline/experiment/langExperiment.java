package biline.experiment;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class langExperiment {

	public langExperiment() {
		
	}

	public static void main(String[] args) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new URL(
			        "https://upload.wikimedia.org/wikipedia/en/2/24/Lenna.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		    Graphics g = image.getGraphics();
		    g.setFont(g.getFont().deriveFont(30f));
		    g.drawString("Hello World!", 100, 100);
		    g.dispose();

		    try {
				ImageIO.write(image, "png", new File("test.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
