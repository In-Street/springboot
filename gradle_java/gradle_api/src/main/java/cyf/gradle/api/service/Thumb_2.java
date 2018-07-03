package cyf.gradle.api.service;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.merge.ImgMergeWrapper;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.IMergeCell;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.ImgCell;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.TextCell;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 图片拼接
 *
 * @author Cheng Yufei
 * @create 2018-06-27 10:20
 **/
public class Thumb_2 {


    public static void main(String[] args) throws IOException {
        int width = 800;
        int gap = 50;
        String pre = "D:/YUFEI/work/daishu/文档/等等/图片/图片/";
        String endImg = ".png";

        BufferedImage logo = ImageIO.read(new File(pre + "头部背景" + endImg));
        BufferedImage end = ImageIO.read(new File(pre + "攻略" + endImg));
        BufferedImage avatar = ImageIO.read(new File(pre + "头像" + endImg));
        BufferedImage headerBorder = ImageIO.read(new File(pre + "头部边框" + endImg));
        BufferedImage bottomBorder = ImageIO.read(new File(pre + "底部边框" + endImg));
        BufferedImage leftBorder = ImageIO.read(new File(pre + "左侧边框" + endImg));
        BufferedImage rightBorder = ImageIO.read(new File(pre + "右侧边框" + endImg));

        List<BufferedImage> first = Lists.newArrayList(ImageIO.read(new File(pre + "小标签1" + endImg)), ImageIO.read(new File(pre + "小标签2" + endImg)), ImageIO.read(new File(pre + "小标签3" + endImg)));

        List<BufferedImage> content = Lists.newArrayList(ImageIO.read(new File(pre + "标签1" + endImg)), ImageIO.read(new File(pre + "标签2" + endImg)), ImageIO.read(new File(pre + "标签3" + endImg)));

        List<IMergeCell> list = new ArrayList<>();

//        first.add(0, headerBorder);
        first.add(0, logo);
        first.add(1, avatar);
//        final int[] y = {logo.getHeight() + avatar.getHeight() + 10};
        final int[] y = {0};
        List<ImgCell> firstImgCells = IntStream.rangeClosed(0, first.size() - 1).mapToObj(i -> {
            BufferedImage image = first.get(i);
            ImgCell cell;
            if (i == 0) {
                cell = ImgCell.builder()
                        .img(image)
                        .x((800 - image.getWidth()) / 2)
                        .y(y[0])
                        .build();
                y[0] += image.getHeight() + gap;
            } else {
                cell = ImgCell.builder()
                        .img(image)
                        .x((800 - image.getWidth()) / 2)
                        .y(y[0])
                        .build();
                y[0] += image.getHeight() + gap;
            }
            return cell;
        }).collect(Collectors.toList());

        list.addAll(firstImgCells);

        TextCell textCell = new TextCell();
        textCell.setFont(new Font("宋体", Font.BOLD, 22));
        textCell.setColor(Color.BLACK);
        textCell.setStartX((width - headerBorder.getWidth()) / 2);
        textCell.setStartY(y[0] + gap);
        textCell.setEndX((width - headerBorder.getWidth()) / 2 + headerBorder.getWidth());
        textCell.setEndY(y[0] + gap + 150);
        textCell.addText("文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本");
        textCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);

        list.add(textCell);


        TextCell text = new TextCell();
        text.setFont(new Font("宋体", Font.BOLD, 22));
        text.setColor(Color.BLACK);
        text.setStartX((width - headerBorder.getWidth()) / 2);
        text.setStartY(textCell.getEndY() + gap);
        text.setEndX((width - headerBorder.getWidth()) / 2 + headerBorder.getWidth());
        text.setEndY(textCell.getEndY() + 150);
        text.addText("文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本文本");
        text.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        text.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);

        list.add(text);


        final int[] y_c = {text.getEndY() + 80};
        content.add(end);
//        content.add(bottomBorder);
        List<ImgCell> contentImgCells = IntStream.rangeClosed(0, content.size() - 1).mapToObj(i -> {
            BufferedImage image = content.get(i);
            ImgCell cell;
            if (i == 0) {
                cell = ImgCell.builder()
                        .img(image)
                        .x((800 - image.getWidth()) / 2)
                        .y(y_c[0])
                        .build();
                y_c[0] += image.getHeight() + gap;
            } else {
                cell = ImgCell.builder()
                        .img(image)
                        .x((800 - image.getWidth()) / 2)
                        .y(y_c[0])
                        .build();
                y_c[0] += image.getHeight() + gap;
            }
            return cell;
        }).collect(Collectors.toList());
        list.addAll(contentImgCells);

        int hight = y_c[0];
      /*  BufferedImage ans = ImgMergeWrapper.merge(list,
                width,
                hight, Color.white);
        ImageIO.write(ans, "png", new File("D:/EE.png"));*/

        BufferedImage bg = new BufferedImage(width, hight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bg);
        g2d.setColor(Color.orange);
        g2d.fillRect(0, 0, width, hight);

        list.stream().forEach(s -> s.draw(g2d));
        ImageIO.write(bg, "png", new File("D:/FF.png"));
    }
}
