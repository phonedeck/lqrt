package com.phonedeck.lqrt;

public abstract class QRPainter {

    private int inputWidth;

    private int inputHeight;

    private int qrWidth;

    private int qrHeight;

    private int outputWidth;

    private int outputHeight;

    private int leftPadding;

    private int topPadding;

    private int multiple;

    private QRCode qr;

    protected void paint(int width, int height, int quietZone, QRCode qr) {
        this.qr = qr;
        ByteMatrix input = qr.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        inputWidth = input.getWidth();
        inputHeight = input.getHeight();
        qrWidth = inputWidth + (quietZone << 1);
        qrHeight = inputHeight + (quietZone << 1);
        outputWidth = Math.max(width, qrWidth);
        outputHeight = Math.max(height, qrHeight);

        multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        // Padding includes both the quiet zone and the extra white pixels to
        // accommodate the requested
        // dimensions. For example, if input is 25x25 the QR will be 33x33
        // including the quiet zone.
        // If the requested size is 200x160, the multiple will be 4, for a QR of
        // 132x132. These will
        // handle all the padding from 100x100 (the actual QR) up to 200x160.
        leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        
        onBeforePaint();
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    fillRect(outputX, outputY, multiple, multiple);
                }
            }
        }
        onAfterPaint();
    }

    protected void onBeforePaint() {
    }

    protected abstract void fillRect(int x, int y, int width, int height);

    protected void onAfterPaint() {
    }

    protected QRCode getQr() {
        return qr;
    }
    
    protected int getOuterLeft() {
        return (outputWidth - (qrWidth * multiple)) / 2;
    }
    
    protected int getOuterTop() {
        return (outputHeight - (qrHeight * multiple)) / 2;
    }
        
    protected int getOuterWidth() {
        return qrWidth * multiple;
    }
    
    protected int getOuterHeight() {
        return qrHeight * multiple;
    }
    
    protected int getInnerLeft() {
        return leftPadding;
    }
    
    protected int getInnerTop() {
        return topPadding;
    }
    
    protected int getInnerWidth() {
        return inputWidth * multiple;        
    }
    
    protected int getInnerHeight() {
        return inputHeight * multiple;
    }
    
}
