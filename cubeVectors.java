
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
 
import acm.graphics.*;
import acm.program.*;
 
 
public class CubeVectors extends GCanvas implements GraphicsContestConstants, ComponentListener{
     
    public CubeVectors(int colorInx) {
        main = new Cube4D();
        setColor(colorInx);
        addComponentListener(this);
    }
     
    public class Point {
        private double[] value;
         
        public Point (double[] input) {
            value = input;
        }
         
        //sets the temp value to zero then adds on each transformation
        private void transform (double[][] matrix) {
            double[] tempValue = new double[value.length];
            for (int i = 0; i < value.length; i++) {
                tempValue[i] = 0.0;
                for (int j = 0; j < value.length; j++) {
                    tempValue[i] += matrix[i][j]*value[j];
                }
            }
            value = tempValue;
        }
         
        private double[] getAllCoords() {
            return value;
        }
         
        private double getCoord(int num){
            return value[num];
        }
    }
     
    /* this is the circle class, a circle is defined by a point and is scaled by the z value*/
    public class Circle {
        private Point p;
        private int dimension;
        private double scale;
        GOval circle;
         
        public Circle(double[] values) {
            p = new Point(values);
            dimension = values.length;
            if (dimension >= 3) {
                scale = values[2]*CIRCLE_SCALAR;
            } else {
                scale = DEFAULT_CIRCLE_SCALE;
            }
            circle = new GOval(p.getCoord(0)*scalar+constantX-scale/2, p.getCoord(1)*scalar+constantY-scale/2 , scale, scale);
            circle.setColor(setColor(colorIndex));
            if (circlesVisible == true) {
                circle.setVisible(true);
            } else {
                circle.setVisible(false);
            }
            //circle.setFilled(true);
            add(circle);
        }
        public void update(double[][] matrix) {
            p.transform(matrix);
            circle.setLocation(p.getCoord(0)*scalar+constantX-scale/2, p.getCoord(1)*scalar+constantY-scale/2);
            if (dimension >= 3) {
                scale = p.getCoord(2)*CIRCLE_SCALAR;
            }
            if (scale < 0.1) {
                circle.setSize(0.1, 0.1);
                scale = 0.1;
            } else {
                circle.setSize(scale, scale);
            }
            if (circlesVisible == true) {
                circle.setVisible(true);
            } else {
                circle.setVisible(false);
            }
            circle.setColor(setColor(colorIndex));
        }
         
    }
     
    /*this is the line class, a line is defined as by two points which can be of any dimension*/
    public class Line {
        //instance vars
        private Point p1, p2;
        GLine line;
         
         
        //constructor
        public Line (double[] values1, double[] values2) {
            p1 = new Point(values1);
            p2 = new Point(values2);
            line = new GLine(p1.getCoord(0)*scalar+constantX, p1.getCoord(1)*scalar+constantY, p2.getCoord(0)*scalar+constantX, p2.getCoord(1)*scalar+constantY);
            //line = new GLine(p1.getCoord(0), p1.getCoord(1), p2.getCoord(0), p2.getCoord(1));
            //line.setColor(rgen.nextColor());
            line.setColor(setColor(colorIndex));
            if (linesVisible == true) {
                line.setVisible(true);
            } else {
                line.setVisible(false);
            }
            add(line);
        }
        public void update(double[][] matrix) {
            if (linesVisible == true) {
                line.setVisible(true);
            } else {
                line.setVisible(false);
            }
            p1.transform(matrix);
            p2.transform(matrix);
            line.setStartPoint(p1.getCoord(0)*scalar+constantX, p1.getCoord(1)*scalar+constantY);
            line.setEndPoint(p2.getCoord(0)*scalar+constantX, p2.getCoord(1)*scalar+constantY);
            line.setColor(setColor(colorIndex));
        }
    }
     
    public void setLinesVisible(Boolean b) {
        linesVisible = b;
    }
     
    public void setCirclesVisible(Boolean b) {
        circlesVisible = b;
    }
     
    //reads through the vectors text file and adds each word to the ArrayList 
    //vectors
    public void initializeVectors(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null && !line.equals("")) {
                addVector(line);
                line = br.readLine();
            }
            br.close();
        }
        catch (Exception e) {};
    }
     
    //will take in an input like "1 0 0 1 0; 10 1 1 0 1" and convert it to arrays of the
    //appropriate dimension, then to a line, then it puts the line into the arrayList of lines
    private void addVector(String s) {
        String point1string = (String) s.subSequence(0, s.indexOf(";"));
        String point2string = (String) s.substring(s.indexOf(";") + 1);
        double[] p1 = stringToArray(point1string);
        double[] p2 = stringToArray(point2string);
        Circle theCircle1 = new Circle(p1);
        Circle theCircle2 = new Circle(p2);
        Line theLine = new Line(p1, p2);
        allLines.add(theLine);
        if (!allCircles.contains(theCircle1)) {
            allCircles.add(theCircle1);
        }
        if (!allCircles.contains(theCircle2)) {
            allCircles.add(theCircle2);
        }
    }
 
     
    private double[] stringToArray(String s) {
        int dimension = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                dimension += 1;
            }
        }
        double[] array = new double[dimension];
        String temp = "";
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != ' ') {
                temp += s.charAt(i);
            } else {
                array[count] = Double.parseDouble(temp);
                count++;
                temp = "";
            }
        }
        return array;
    }
     
    public Color setColor(int index) {
        colorIndex = index;
        if (index == COLORS_NAME_ARRAY.length-1) {
            return rgen.nextColor();
        } else {
            return COLORS_ARRAY[index];
        }
    }
     
    public void scaleByFactor(double scale) {
        scalar *= scale;
    }
     
    public double getScalar() {
        return scalar;
    }
     
    public void setScale(double value) {
        scalar = value;
    }
     
    public ArrayList<Line> getAllLines() {
        return allLines;
    }
    public ArrayList<Circle> getAllCircles() {
        return allCircles;
    }
     
    public void setConstants(double x, double y, double scale) {
        constantX = x;
        constantY = y;
        scalar = scale;
    }
    //same as above method minus scale
    public void setConstants(double x, double y) {
        constantX = x;
        constantY = y;
    }
     
    public void refresh() {
        removeAll();
        allCircles.clear();
        allLines.clear();
    }
     
    //instance vars
    private Boolean circlesVisible = false;
    private Boolean linesVisible = true;
    private int colorIndex;
    private double[] clickCoordsInit = new double[] {0, 0};
    private Cube4D main;
    private ArrayList<Line> allLines = new ArrayList<Line>();
    private ArrayList<Circle> allCircles = new ArrayList<Circle>();
    private double scalar;
    private double constantX;
    private double constantY;
    private RandomGenerator rgen = new RandomGenerator();
    public void componentHidden(ComponentEvent arg0) {
        // TODO Auto-generated method stub
         
    }
 
    public void componentMoved(ComponentEvent arg0) {
        // TODO Auto-generated method stub
         
    }
 
    public void componentResized(ComponentEvent arg0) {
        setConstants(getWidth()/2, getHeight()/2);
         
    }
 
    public void componentShown(ComponentEvent arg0) {
        // TODO Auto-generated method stub
         
    }
}
    
