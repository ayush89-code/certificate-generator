
package com.example.certgen;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

@Service
public class ImageService {

    // Coordinates chosen for template size 2048x1582
    private static final int NAME_Y = 520;
    private static final int SKILL_Y = 660;
    private static final int CENTER_X = 2048/2;

    public byte[] createCertificate(String name, String skill) {
        try {
            BufferedImage template = ImageIO.read(new ClassPathResource("sciencekedeewane_certificate.png").getInputStream());
            Graphics2D g = template.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Name - red color, larger font
            g.setColor(new Color(178,34,34)); // firebrick
            Font nameFont = new Font("Serif", Font.BOLD, 64);
            g.setFont(nameFont);
            drawCenteredString(g, name, CENTER_X+90, NAME_Y+180, nameFont);

            // Skill - green color
            int maxWidth = 1590; // max space available
            // Skill - green color, centered auto-wrap
            g.setColor(new Color(26, 120, 26, 236));
            Font skillFont = new Font("Calibri", Font.BOLD, 35);
            g.setFont(skillFont);
            drawWrappedCenteredText(g, skill, CENTER_X+80, SKILL_Y+160, maxWidth, 40);

            //  drawWrappedText(g, skill, 200, 820, maxWidth, 40);



            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(template, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void drawCenteredString(Graphics2D g, String text, int centerX, int y, Font font) {
        if (text==null) return;
        FontRenderContext frc = g.getFontRenderContext();
        double textWidth = font.getStringBounds(text, frc).getWidth();
        int x = (int)(centerX - textWidth/2);
        g.drawString(text, x, y);
    }
    private void drawWrappedCenteredText(Graphics2D g, String text, int centerX, int startY, int maxWidth, int lineHeight) {
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        int y = startY;
        for (String l : lines) {
            int textWidth = fm.stringWidth(l);
            int x = centerX - textWidth / 2;
            g.drawString(l, x, y);
            y += lineHeight;
        }
    }

}
