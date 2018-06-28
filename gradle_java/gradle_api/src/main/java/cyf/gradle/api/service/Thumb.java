package cyf.gradle.api.service;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.merge.ImgMergeWrapper;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.ImgCell;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.TextCell;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 图片拼接
 * @author Cheng Yufei
 * @create 2018-06-27 10:20
 **/
public class Thumb {


    public static void main(String[] args) throws IOException {
        BufferedImage img1 = ImageIO.read(new File("D:/345x345.jpg"));
        BufferedImage img2 = ImageIO.read(new File("D:/750x375.jpg"));

        ImgCell imgCell = ImgCell.builder()
                .img(img1)
                .x(0)
                .y(0)
                .build();

       /* LineCell lineCell = LineCell.builder()
                .x1(img1.getWidth() / 3)
                .x2(img1.getWidth() * 7 / 6)
                .y1(img1.getHeight() + 4)
                .y2(img1.getHeight() + 4)
                . color(Color.LIGHT_GRAY)
                .dashed(true)
                .build();*/

        ImgCell imgCell2 = ImgCell.builder()
                .img(img2)
                .x(img1.getWidth() / 2)
                .y(img1.getHeight() + 4)
                .build();

        TextCell textCell = new TextCell();
        textCell.setFont(new Font("宋体", Font.BOLD, 22));
        textCell.setColor(Color.GREEN);
        textCell.setStartX(180);
        textCell.setStartY(140);
        textCell.setEndX(220);
        textCell.setEndY(180);
        textCell.addText("文本");
        textCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);

        BufferedImage ans = ImgMergeWrapper.merge(Arrays.asList(imgCell, imgCell2,textCell),
                img1.getWidth() / 2 + img2.getWidth(),
                img1.getHeight() + 4 + img2.getHeight(),
                Color.WHITE);
        ImageIO.write(ans, "jpg", new File("D:/DD.jpg"));
    }
}
