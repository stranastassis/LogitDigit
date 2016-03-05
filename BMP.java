import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.ImageIO;


public class BMP {
    /**
     * (Testing only)
     */
    public static void main(String[] args) throws Exception {
        BMP.makeMeCross(args[0]);
    }

    /**
     *
     * @param filename The file name for the 'crosshair' image
     *
     * @throws IOException If the file could not be written to by ImageIO
     */
    public static void makeMeCross(String filename) throws IOException {
        int sz = 101;
        byte[] buffer = new byte[sz * sz];

        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                // Make a 'crosshair' pattern
                buffer[(i * sz) + j] = ((j == 50) || (i == 50)) ? (byte) 255 : 0;
            }
        }

        ImageIO.write(BMP.getGrayscale(101, buffer), "PNG", new File(filename));
    }

    /**
     *
     * @param width The image width (height derived from buffer length)
     * @param buffer The buffer containing raw grayscale pixel data
     *
     * @return THe grayscale image
     */
    public static BufferedImage getGrayscale(int width, byte[] buffer) {
        int height = buffer.length / width;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        int[] nBits = { 8 };
        ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }
}