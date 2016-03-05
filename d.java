import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;


public class d {
    static int[] data ;
    static String[] lines;
    static ImageMNIST[] ges;
    static String output;
    static String input;
    static int lineNum;
	public static void main(String... args) throws Exception {
		input="C:/Users/Anastassis/Desktop/arxeia sxolhs/A.I/Assign3/train7.txt";
		output="image1.png";
		lineNum=6265;
	    lines=new String[6742];
	    ges=new ImageMNIST[6742];
	    loadDigits(input);
	   
	    int sz = 28;
	    byte[] buffer = new byte[sz * sz];

	    for (int i = 0; i < sz; i++) {
	        for (int j = 0; j < sz; j++) {
	            buffer[(i * sz) + j] =fromIntToByte(ges[lineNum].pic[i][j]);
	        }
	    }
	    ImageIO.write(BMP.getGrayscale(28, buffer), "PNG", new File(output));
	}
	
	
	 public static byte fromIntToByte(int value) throws Exception {
	        String stringByte = "";
	        if (value < 0 && value > 255) {
	            throw new Exception("Must be from 0<=value<=255");
	        }
	        if (value <= 127) {
	            for (int i = 0; i < 8; i++) {
	                stringByte = String.valueOf(value % 2) + stringByte;
	                value = value / 2;
	            }
	        } else {
	            value = value / 2;
	            for (int i = 0; i < 7; i++) {
	                stringByte = String.valueOf(value % 2) + stringByte;
	                value = value / 2;
	            }
	            stringByte = "-" + stringByte;
	        }
	        byte b = Byte.parseByte(stringByte, 2);
	        return b;
	    }
	 
	public static void loadDigits(String path) throws IOException{
	
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		int coutner=0;
		while ((line = br.readLine()) != null) {
			lines[coutner]=line;
			String[] t = line.split(" ");
			Integer[] intline = new Integer[784];
			for(int kk=0;kk<t.length;kk++)
				intline[kk]=Integer.valueOf(t[kk]);
			ImageMNIST im=new ImageMNIST(intline);
			ges[coutner]=im;
			coutner++;
		}
		br.close();
		
	}
}
