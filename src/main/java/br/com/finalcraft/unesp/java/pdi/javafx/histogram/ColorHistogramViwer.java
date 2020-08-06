package br.com.finalcraft.unesp.java.pdi.javafx.histogram;

import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ColorHistogramViwer {

    private static final int BINS = 255;
    private final ImgWrapper imgWrapper;
    private final BufferedImage image;
    private HistogramDataset dataset;
    private XYBarRenderer renderer;

    public ColorHistogramViwer(ImgWrapper imgWrapper) {
        this.imgWrapper = imgWrapper.clone();
        image = ImageHelper.convertToBufferedImage(this.imgWrapper);
    }

    private ChartPanel createChartPanel() {
        dataset = new HistogramDataset();
        Raster raster = image.getRaster();

        if (imgWrapper.isBlackAndWhite()){
            addColorToDataset(dataset,"Preto/Branco", raster, image , 0);//Vermelho
        }else {
            addColorToDataset(dataset,"Vermelho", raster, image, 0);
            addColorToDataset(dataset,"Verde", raster, image, 1);
            addColorToDataset(dataset,"Azul", raster, image, 2);
        }

        // chart
        JFreeChart chart = ChartFactory.createHistogram(
                "Histograma de Cores",
                "Valor",
                "Escala",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        XYPlot plot = (XYPlot) chart.getPlot();
        renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());

        Paint[] paintArray;
        if (imgWrapper.isBlackAndWhite()){
           paintArray = new Paint[]{
                   new Color(0x80080431, true)
           };

        }else {
            paintArray = new Paint[]{
                    new Color(0x80ff0000, true),
                    new Color(0x8000ff00, true),
                    new Color(0x800000ff, true)
            };
        }

        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    private void addColorToDataset(HistogramDataset dataset, String label, Raster raster, BufferedImage image, int colorBand){
        final int w = imgWrapper.getWidth();
        final int h = imgWrapper.getHeight();
        double[] r = new double[w * h];
        r = raster.getSamples(0, 0, w, h, colorBand, r);
        dataset.addSeries(label, r, BINS);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new VisibleAction(0)));
        if (!imgWrapper.isBlackAndWhite()){
            panel.add(new JCheckBox(new VisibleAction(1)));
            panel.add(new JCheckBox(new VisibleAction(2)));
        }
        return panel;
    }

    private class VisibleAction extends AbstractAction {
        private final int i;

        public VisibleAction(int i) {
            this.i = i;
            this.putValue(NAME, (String) dataset.getSeriesKey(i));
            this.putValue(SELECTED_KEY, true);
            renderer.setSeriesVisible(i, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
        }
    }

    public void display() {
        JFrame f = new JFrame("Histograma de Cores");
        f.add(createChartPanel());
        f.add(createControlPanel(), BorderLayout.SOUTH);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
