package br.com.finalcraft.unesp.java.pdi.javafx.histogram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;
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

public class BrightHistogramViwer {

    private static final int BINS = 255;
    private ImgWrapper imgWrapper;
    private BufferedImage image;
    private HistogramDataset dataset;
    private XYBarRenderer renderer;

    public BrightHistogramViwer(ImgWrapper imgWrapper) {
        this.imgWrapper = imgWrapper.clone();
        image = ImageHelper.convertToBufferedImage(this.imgWrapper);
    }

    private ChartPanel createChartPanel() {
        dataset = new HistogramDataset();

        if (imgWrapper.isBlackAndWhite()){
            addBrightnessToDataset(dataset,"Preto/Branco", imgWrapper.getRed());
        }else {
            addBrightnessToDataset(dataset,"Vermelho", imgWrapper.getRed());
            addBrightnessToDataset(dataset,"Verde", imgWrapper.getRed());
            addBrightnessToDataset(dataset,"Azul", imgWrapper.getRed());
        }

        // chart
        JFreeChart chart = ChartFactory.createHistogram(
                "Histograma",
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

    private void addBrightnessToDataset(HistogramDataset dataset, String label, ImgMatrix imgMatrix){
        int[] intarray = imgMatrix.getAllPixelsInOrder();
        double[] doubleArray = new double[intarray.length];
        for (int i = 0; i < intarray.length; i++) {
            doubleArray[i] = intarray[i];
        }
        dataset.addSeries(label, doubleArray, BINS);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new VisibleAction(this,0)));
        if (!imgWrapper.isBlackAndWhite()){
            panel.add(new JCheckBox(new VisibleAction(this,1)));
            panel.add(new JCheckBox(new VisibleAction(this,2)));
        }
        return panel;
    }

    private class VisibleAction extends AbstractAction {
        private final BrightHistogramViwer bhv;
        private final int i;

        public VisibleAction(BrightHistogramViwer bhv, int i) {
            this.bhv = bhv;
            this.i = i;
            this.putValue(NAME, (String) bhv.dataset.getSeriesKey(i));
            this.putValue(SELECTED_KEY, true);
            renderer.setSeriesVisible(i, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            renderer.setSeriesVisible(i, !bhv.renderer.getSeriesVisible(i));
        }
    }

    private List<Component> componentList = new ArrayList();

    public void update(ImgWrapper imgWrapper){
        this.imgWrapper = imgWrapper.clone();
        image = ImageHelper.convertToBufferedImage(this.imgWrapper);
        jLabel.setIcon(new ImageIcon(image));
        for (Component component : componentList) {
            frame.remove(component);
        }
        frame.add(createChartPanel());
        frame.pack();
    }

    private JFrame frame;

    public boolean isVisible(){
        return frame != null && frame.isVisible();
    }

    private JLabel jLabel;
    public void display() {
        frame = new JFrame("Histograma");
        componentList.add(frame.add(createChartPanel()));
        frame.add(createControlPanel(), BorderLayout.SOUTH);
        frame.add(jLabel = new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
