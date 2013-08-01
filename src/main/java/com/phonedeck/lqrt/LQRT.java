package com.phonedeck.lqrt;

/**
 * Main access point of the LQRT library.
 * @author egergo@gmail.com (Gergo Ertli)
 */
public class LQRT {

    public static QRCode qr(String content, ErrorCorrectionLevel ecLevel, String encoding) throws WriterException {
        return Encoder.encode(content, ecLevel, encoding);
    }
    
}
