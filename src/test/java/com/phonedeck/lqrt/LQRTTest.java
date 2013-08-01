package com.phonedeck.lqrt;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class LQRTTest {

    private static final String TEXT = "The quick brown fox jumps over the lazy dog";
    
    @Test
    public void text() throws WriterException, IOException, NotFoundException {
        QRCode code = LQRT.qr(TEXT, ErrorCorrectionLevel.L, null);
            
        ImageQRPainter iqrp = new ImageQRPainter();
        iqrp.paint(code);                
    
        BinaryBitmap bb = imageToBitmap(iqrp.getImage());
        MultiFormatReader mfr = new MultiFormatReader();
                
        Result res = mfr.decode(bb);        
        assertEquals(TEXT, res.getText());
        assertEquals(BarcodeFormat.QR_CODE, res.getBarcodeFormat());        
    }
    
    private static BinaryBitmap imageToBitmap(BufferedImage image) {
        return new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(image.getWidth(), image.getHeight(), imageToRGB(image))));
    }
    
    private static int[] imageToRGB(BufferedImage image) {
        int[] arr = new int[image.getWidth() * image.getHeight()];
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                arr[j * image.getWidth() + i] = image.getRGB(i, j);
            }
        }
        return arr;
    }
    
    private static class ImageQRPainter extends QRPainter {
        
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics g = image.getGraphics();
        
        public ImageQRPainter() {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());            
            g.setColor(Color.BLACK);
        }
        
        @Override
        protected void fillRect(int x, int y, int width, int height) {
            g.fillRect(x, y, width, height);                
        }        
        
        public void paint(QRCode qr) {
            paint(image.getWidth(), image.getHeight(), 1, qr);
        }
        
        public BufferedImage getImage() {
            return image;
        }
    }

}

