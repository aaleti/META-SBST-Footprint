package footprints.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

public class Plotting extends JFrame{
    private static final Shape circle = new Ellipse2D.Double(-3, -3, 6, 6);
    private static final Color line = Color.gray;
    private Shape[] shapes;
    
    private JFreeChart chart;
    private final Font font18=new Font("Dialog", Font.PLAIN,22);
    private final Font font15=new Font("Dialog", Font.PLAIN,20);
    private final Paint[] palette =new Paint[] {new Color(0xDC143C),new Color(0x4682B4), new Color(0xFF8C00), new Color(0x3CB371)};
    
    private static int[] intArray(double a, double b, double c) {
         return new int[] {(int) a, (int) b, (int) c};
     }
    private static int[] intArray(double a, double b, double c, double d) {
      return new int[] {(int) a, (int) b, (int) c, (int) d};
     }
    private Shape[] createShapes(){
        Shape[] result = new Shape[12];
        double size = 10.0;
        double delta = size / 2.0;
        int[] xpoints = null;
        int[] ypoints = null;
 
         // square
         result[0] = new Rectangle2D.Double(-delta, -delta, size, size);
        // circle
         result[1] = new Ellipse2D.Double(-delta, -delta, size, size);

         // up-pointing triangle
         xpoints = intArray(0.0, delta, -delta);
         ypoints = intArray(-delta, delta, delta);
         result[2] = new Polygon(xpoints, ypoints, 3);
 
         // diamond
         xpoints = intArray(0.0, delta, 0.0, -delta);
         ypoints = intArray(-delta, 0.0, delta, 0.0);
         result[3] = new Polygon(xpoints, ypoints, 4);
 
        // horizontal rectangle
        result[4] = ShapeUtilities.createDiagonalCross(4, 1);
        //Rectangle2D.Double(-delta, -delta / 2, size, size / 2);
 
        // down-pointing triangle
        xpoints = intArray(-delta, +delta, 0.0);
       ypoints = intArray(-delta, -delta, delta);
       
       result[5] = new Polygon(xpoints, ypoints, 3);

       // horizontal ellipse
       result[6] = ShapeUtilities.createRegularCross(4, 1);
       //new Ellipse2D.Double(-delta, -delta / 2, size, size / 2);

     // right-pointing triangle
      xpoints = intArray(-delta, delta, -delta);
      ypoints = intArray(-delta, 0.0, delta);
      result[7] = new Polygon(xpoints, ypoints, 3);
 
        // vertical rectangle
      result[8] = new Rectangle2D.Double(-delta / 2, -delta, size / 2, size);

         // left-pointing triangle
       xpoints = intArray(-delta, delta, delta);
        ypoints = intArray(0.0, -delta, +delta);
        result[9] = new Polygon(xpoints, ypoints, 3);

         return result;
    }
    
    public Plotting(DefaultXYDataset dataset, String xLabel, String yLabel){
        shapes=createShapes();
        chart = createChart(dataset, xLabel, yLabel);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        this.add(chartPanel, BorderLayout.CENTER);
    }

    public Plotting(DefaultXYZDataset data,String title, String xLabel, String yLabel){
        shapes=createShapes();
         chart=createChart(data, title, xLabel, yLabel);       
         ChartPanel chartPanel = new ChartPanel(chart, false);
         chartPanel.setPreferredSize(new Dimension(500, 400));
         this.add(chartPanel, BorderLayout.CENTER);
    }
    
    private JFreeChart createChart(DefaultXYZDataset dataset, String title, String xLabel, String yLabel) {
        
        NumberAxis xAxis = new NumberAxis(xLabel);
        NumberAxis yAxis = new NumberAxis(yLabel);
    
         // create a paint-scale and a legend showing it

        LookupPaintScale paintScale = new LookupPaintScale(0, 1, Color.black);
        

        paintScale.add(0.9, new Color(122,1,119));
        paintScale.add(0.8, new Color(197,27,138));
        paintScale.add(0.7, new Color(247,104,161));
        
        paintScale.add(0.6, new Color(251,180,185));
        paintScale.add(0.5, new Color(254,235,226));
        
        paintScale.add(0.4, new Color(255,255,204));
        paintScale.add(0.3, new Color(161,218,180));
       
        paintScale.add(0.2, new Color(65,182,196));
        paintScale.add(0.1, new Color(44,127,184));
        paintScale.add(0.0, new Color(37,52,148));
        
        NumberAxis nx = new NumberAxis();
        //nx.setTickMarkPaint(Color.white);
        nx.setTickUnit(new NumberTickUnit(0.1));
        nx.setTickLabelFont(new Font("Dialog", Font.PLAIN, 16));
        PaintScaleLegend psl = new PaintScaleLegend(paintScale, nx);
        psl.setPosition(RectangleEdge.RIGHT);
        psl.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
        psl.setMargin(50.0, 20.0, 80.0, 0.0);


 
        XYBlockRenderer renderer= new XYBlockRenderer();
        renderer.setPaintScale(paintScale);
        renderer.setBlockHeight(0.15);
        renderer.setBlockWidth(0.15);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

      
        chart = new JFreeChart(plot);
        chart.setBackgroundPaint(Color.GRAY);
        chart.addSubtitle(psl);
        chart.removeLegend();
        plot.getRangeAxis().setLabelFont(font18);
        plot.getRangeAxis().setTickLabelFont(font15);
        plot.getDomainAxis().setLabelFont(font18);
        plot.getDomainAxis().setTickLabelFont(font15);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        chart.setBackgroundPaint(Color.WHITE);
        return chart;
    }

    private JFreeChart createChart(DefaultXYDataset dataset, String xLabel, String yLabel) {
  
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        renderer.setSeriesShape(0, shapes[0]);
        renderer.setSeriesPaint(0, new Color(117,112,179));
        renderer.setSeriesShape(1, shapes[1]);
        renderer.setSeriesPaint(1, new Color(27,158,119));
        renderer.setSeriesShape(2, shapes[2]);
        renderer.setSeriesPaint(2, new Color(252, 154, 79));
        renderer.setSeriesShape(3, shapes[3]);
        renderer.setSeriesPaint(3, new Color(207, 23, 118));
        renderer.setSeriesShape(4, shapes[4]);
        renderer.setSeriesPaint(4, new Color(102,166,30));
        renderer.setSeriesShape(6, shapes[6]);
        renderer.setSeriesPaint(6, new Color(31,120,180));
        renderer.setSeriesShape(5, shapes[9]);
        renderer.setSeriesPaint(5, new Color(251,154,153));
        renderer.setSeriesShape(7, shapes[7]);
        renderer.setSeriesPaint(7, new Color(166,206,227));
        renderer.setSeriesShape(8, shapes[0]);
        renderer.setSeriesShape(9, shapes[1]);
        renderer.setSeriesShape(10, shapes[3]);
        renderer.setSeriesPaint(10, new Color(166,206,0));
        renderer.setSeriesShape(11, shapes[4]);
        
        NumberAxis xAxis = new NumberAxis(xLabel);
        NumberAxis yAxis = new NumberAxis(yLabel);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setDrawingSupplier(new DefaultDrawingSupplier(palette, 
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, 
                    DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, 
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, 
                    DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE)); 
        plot.getRangeAxis().setLabelFont(font18);
        plot.getRangeAxis().setTickLabelFont(font15);
        plot.getDomainAxis().setLabelFont(font18);
        plot.getDomainAxis().setTickLabelFont(font15);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        chart = new JFreeChart(plot);
        chart.setBackgroundPaint(Color.WHITE);

        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setItemFont(new Font("Dialog", Font.PLAIN, 16));
        return chart;
    }

    public void saveChartAsPng(String path, String filename){
	File directory = new File(path);
    	if(directory.exists() == false){
            directory.mkdirs();
    	}
    	
    	File file = new File(path + filename);
    	try{
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
    	}catch(IOException e){
            System.out.println("The image could not be saved");
    	}
    }
   
}