/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaversion;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SGD;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.AdditiveRegression;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.meta.RandomCommittee;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.M5Rules;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.supervised.attribute.TSLagMaker;

/**
 *
 * @author user
 */
public class MainFrame extends javax.swing.JFrame {

    Instances RealDemands;

    MyClassifier algorithms[];
    Result results[];
    Result sortedResults[];

    XYSeries estimatedDataset;
    XYSeries realDataset;
    JFreeChart chart;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Open file");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("run");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.GridLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new java.io.File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV file", "csv");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            StringBuilder dataInString = new StringBuilder();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
                String line = reader.readLine();
                dataInString.append("Year,Month,Demand");
                dataInString.append(System.lineSeparator());
                line = reader.readLine();//Skip first line
                String refYear = "";
                while (line != null) {
//                    System.out.println(line);
                    String row[] = line.split(",");
                    String year = row[0];

                    if (year.length() > 0) {
                        refYear = year;
                    } else {
                        year = refYear;
                    }
                    String month = row[1];
                    String demand = row[2];
                    dataInString.append(year);
                    dataInString.append(",");
                    dataInString.append(month);
                    dataInString.append(",");
                    dataInString.append(demand);
                    dataInString.append(System.lineSeparator());
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(dataInString.toString());

            CSVLoader csvLoader = new CSVLoader();
            try {
                InputStream stream = new ByteArrayInputStream(dataInString.toString().getBytes(StandardCharsets.UTF_8));
                csvLoader.setSource(stream);
                RealDemands = csvLoader.getDataSet();

                estimatedDataset = new XYSeries("Estimated values");
                realDataset = new XYSeries("Real values");
                for (int i = 0; i < RealDemands.numInstances(); i++) {
                    realDataset.add(i + 1, RealDemands.get(i).value(2));
                }
                XYSeriesCollection xYDataset = new XYSeriesCollection();
                xYDataset.addSeries(estimatedDataset);
                xYDataset.addSeries(realDataset);
                chart = ChartFactory.createXYLineChart("Demand", "Month", "Value", xYDataset);
                ChartPanel panel = new ChartPanel(chart);
                panel.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
                jPanel1.add(panel);
                
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        runAllAlgorithms();

////\/\/\/ LAST MONTH
//                month = 120;
////\/\/\/ BEGINGIN OF MONTH
//        if InventoryLevel(month) <= RealDemands(month, 3) + BackOrder(month) + 73
//    
//            BackOrder(month + 1) = (RealDemands(month, 3) + BackOrder(month)) - InventoryLevel(month);
//        InventoryLevel(month) = 0;
//        OrderAmount(month) = BackOrder(month + 1) + 73 - InventoryLevel(month);
//        elseif InventoryLevel(month)>RealDemands(month, 3) + BackOrder(month) + 73
//        InventoryLevel(month) = InventoryLevel(month) - (RealDemands(month, 3) + BackOrder(month) + 73);
//        BackOrder(month + 1) = 0;
//        OrderAmount(month) = BackOrder(month + 1) + 73 - InventoryLevel(month);
//        end //^^^ BEGINGIN OF MONTH
//                //\/\/\/ END OF MONTH
//
//        if InventoryLevel(month) < 90
//    
//            TotalCost = TotalCost + InventoryLevel(month) * 1;
//        else {
//            TotalCost = TotalCost + InventoryLevel(month) * 2;
//        }
//        end TotalCost = TotalCost + BackOrder(month) * 3;
//        InventoryLevel(month + 1) = OrderAmount(month);
//^^^ END OF MONTH
//^^^ LAST MONTH
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        if(jList1.getSelectedIndex()>-1)
        {
            int selectedAlgorithm = Integer.valueOf(jList1.getSelectedValue().split(" ")[0]);
            String string = "Algorithm name: " + results[selectedAlgorithm].algorithmName + System.lineSeparator();
            string = string + "Total cost: " + results[selectedAlgorithm].totalCost + System.lineSeparator();
            jTextArea1.setText(string);
            refreshGUI();
        }
    }//GEN-LAST:event_jList1ValueChanged

    public void runAllAlgorithms() {
        setupForcastingAlgorithms();
        results = new Result[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            results[i] = new Result();
            results[i].algorithmName = algorithms[i].name + "Not done ";
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < algorithms.length; i++) {
                    results[i] = testOneAlgoritm(algorithms[i]);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            refreshGUI();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void refreshGUI() {
        estimatedDataset.clear();
        if (jList1.getSelectedIndex() > -1) {
            int selectedAlgorithm = Integer.valueOf(jList1.getSelectedValue().split(" ")[0]);
            if (results[selectedAlgorithm].EstimatedDemands != null) {
                for (int i = 0; i < results[selectedAlgorithm].EstimatedDemands.length; i++) {
                    estimatedDataset.add(i + 1, results[selectedAlgorithm].EstimatedDemands[i]);
                    chart.fireChartChanged();
                }
            }
        }

        sortedResults=deepClone(results);
        Arrays.sort(sortedResults);
        jList1.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                return sortedResults.length;
            }

            @Override
            public Object getElementAt(int index) {
                return sortedResults[index].algorithmName;
            }
        });
    }

    public Result testOneAlgoritm(MyClassifier classifier) {
        double EstimatedDemands[] = new double[RealDemands.numInstances()];
        double InventoryLevel[] = new double[RealDemands.numInstances()];
        double InventoryCost[] = new double[RealDemands.numInstances()];
        double OrderAmount[] = new double[RealDemands.numInstances()];
        double BackOrder[] = new double[RealDemands.numInstances() + 1];
        double BackOrderCost[] = new double[RealDemands.numInstances()];
        for (int month = 0; month < RealDemands.numInstances(); month++) {
            EstimatedDemands[month] = 0;
            InventoryLevel[month] = 0;
            InventoryCost[month] = 0;
            OrderAmount[month] = 0;
            BackOrder[month] = 0;
        }
        InventoryLevel[0] = 60;
        double TotalCost = 0;

        for (int month = 0; month < 119; month++) {
            //\/\/\/ BEGINGIN OF MONTH
            //\/\/\/ ESTIMATE DEMAND
            EstimatedDemands[month + 1] = estimateNextMonthDemand(month, classifier.classifier);
            if(EstimatedDemands[month + 1]==-1)
            {
                Result result = new Result();
                result.algorithmName = classifier.name+"_FAILED";
                return result;
            }
            //^^^ ESTIMATE DEMAND
            if (InventoryLevel[month] <= RealDemands.get(month).value(2) + BackOrder[month]) {
                BackOrder[month + 1] = (RealDemands.get(month).value(2) + BackOrder[month]) - InventoryLevel[month];
                InventoryLevel[month] = 0;
                OrderAmount[month] = EstimatedDemands[month + 1] + BackOrder[month + 1] - InventoryLevel[month];
            } else if (InventoryLevel[month] > RealDemands.get(month).value(2) + BackOrder[month]) {
                InventoryLevel[month] = InventoryLevel[month] - (RealDemands.get(month).value(2) + BackOrder[month]);
                BackOrder[month + 1] = 0;
                OrderAmount[month] = EstimatedDemands[month + 1] + BackOrder[month + 1] - InventoryLevel[month];
            }

            //^^^ BEGINGIN OF MONTH
            //\/\/\/ END OF MONTH
            if (InventoryLevel[month] < 90) {
                InventoryCost[month] = InventoryLevel[month] * 1;
                TotalCost = TotalCost + InventoryCost[month];
            } else {
                InventoryCost[month] = InventoryLevel[month] * 2;
                TotalCost = TotalCost + InventoryCost[month];
            }
            BackOrderCost[month] = BackOrder[month] * 3;
            TotalCost = TotalCost + BackOrderCost[month];
            InventoryLevel[month + 1] = OrderAmount[month];
            //^^^ END OF MONTH
        }
        Result result = new Result();
        result.EstimatedDemands = EstimatedDemands;
        result.BackOrder = BackOrder;
        result.InventoryCost = InventoryCost;
        result.totalCost = TotalCost;
        result.InventoryLevel = InventoryLevel;
        result.OrderAmount = OrderAmount;
        result.averageBackOrderCost = getMean(BackOrderCost);
        result.averageInventoryCost = getMean(InventoryCost);
        result.algorithmName = classifier.name;
        
        if(Double.isNaN(result.totalCost)==true)
        {
            result.algorithmName=classifier.name+"_FAILED";
            result.totalCost=1000000000;
        }

        return result;
    }
    
    public Result[] deepClone(Result inputResults[])
    {
        Result outputResults[]=new Result[inputResults.length];
        for(int i=0;i<inputResults.length;i++)
        {
            outputResults[i]=new Result(inputResults[i].algorithmName,inputResults[i].totalCost,inputResults[i].averageInventoryCost,inputResults[i].averageBackOrderCost,inputResults[i].EstimatedDemands,inputResults[i].InventoryLevel,inputResults[i].InventoryCost,inputResults[i].OrderAmount,inputResults[i].BackOrder);
        }
        return outputResults;
    }

    public void setupForcastingAlgorithms() {
        algorithms = new MyClassifier[37];
        for (int i = 0; i < algorithms.length; i++) {
            algorithms[i] = new MyClassifier();
        }
        algorithms[0].classifier = new GaussianProcesses();
        algorithms[0].name = "0 GaussianProcesses";

        algorithms[1].classifier = new LinearRegression();
        algorithms[1].name = "1 LinearRegression";

        algorithms[2].classifier = new MultilayerPerceptron();
        algorithms[2].name = "2 MultilayerPerceptron";

        algorithms[3].classifier = new SMOreg();
        algorithms[3].name = "3 SMOreg";

        algorithms[4].classifier = new IBk();
        algorithms[4].name = "4 IBk";

        algorithms[5].classifier = new KStar();
        algorithms[5].name = "5 KStar";

        algorithms[6].classifier = new LWL();
        algorithms[6].name = "6 LWL";

        algorithms[7].classifier = new AdditiveRegression();
        algorithms[7].name = "7 AdditiveRegression";

        algorithms[8].classifier = new Bagging();
        ((Bagging) algorithms[8].classifier).setClassifier(new BayesNet());
        algorithms[8].name = "8 Bagging+BayesNet";

        algorithms[9].classifier = new Bagging();
        ((Bagging) algorithms[9].classifier).setClassifier(new GaussianProcesses());
        algorithms[9].name = "9 Bagging+GaussianProcesses";

        algorithms[10].classifier = new Bagging();
        ((Bagging) algorithms[10].classifier).setClassifier(new LinearRegression());
        algorithms[10].name = "10 Bagging+LinearRegression";

        algorithms[11].classifier = new Bagging();
        ((Bagging) algorithms[11].classifier).setClassifier(new Logistic());
        algorithms[11].name = "11 Bagging+Logistic";

        algorithms[12].classifier = new Bagging();
        ((Bagging) algorithms[12].classifier).setClassifier(new MultilayerPerceptron());
        algorithms[12].name = "12 Bagging+MultilayerPerceptron";

        algorithms[13].classifier = new Bagging();
        ((Bagging) algorithms[13].classifier).setClassifier(new SGD());
        algorithms[13].name = "13 Bagging+SGD";

        algorithms[14].classifier = new Bagging();
        ((Bagging) algorithms[14].classifier).setClassifier(new SMO());
        algorithms[14].name = "14 Bagging+SMO";

        algorithms[15].classifier = new Bagging();
        ((Bagging) algorithms[15].classifier).setClassifier(new AdaBoostM1());
        algorithms[15].name = "15 Bagging+AdaBoostM1";

        algorithms[16].classifier = new Bagging();
        ((Bagging) algorithms[16].classifier).setClassifier(new LogitBoost());
        algorithms[16].name = "16 Bagging+LogitBoost";

        algorithms[17].classifier = new Bagging();
        ((Bagging) algorithms[17].classifier).setClassifier(new JRip());
        algorithms[17].name = "17 Bagging+JRip";

        algorithms[18].classifier = new Bagging();
        ((Bagging) algorithms[18].classifier).setClassifier(new OneR());
        algorithms[18].name = "18 Bagging+OneR";

        algorithms[19].classifier = new Bagging();
        ((Bagging) algorithms[19].classifier).setClassifier(new PART());
        algorithms[19].name = "19 Bagging+PART";

        algorithms[20].classifier = new RandomCommittee();
        ((RandomCommittee) algorithms[20].classifier).setClassifier(new RandomTree());
        algorithms[20].name = "20 RandomCommittee+RandomTree";

        algorithms[21].classifier = new RandomCommittee();
        ((RandomCommittee) algorithms[21].classifier).setClassifier(new GaussianProcesses());
        algorithms[21].name = "21 RandomCommittee+GaussianProcesses";

        algorithms[22].classifier = new RandomCommittee();
        ((RandomCommittee) algorithms[22].classifier).setClassifier(new LinearRegression());
        algorithms[22].name = "22 RandomCommittee+LinearRegression";

        algorithms[23].classifier = new RandomCommittee();
        ((RandomCommittee) algorithms[23].classifier).setClassifier(new Logistic());
        algorithms[23].name = "23 RandomCommittee+Logistic";

        algorithms[24].classifier = new RandomCommittee();
        ((RandomCommittee) algorithms[24].classifier).setClassifier(new MultilayerPerceptron());
        algorithms[24].name = "24 RandomCommittee+MultilayerPerceptron";

        algorithms[25].classifier = new Stacking();
        Classifier internalClassifiers[] = new Classifier[3];
        internalClassifiers[0] = new GaussianProcesses();
        internalClassifiers[1] = new LinearRegression();
        internalClassifiers[2] = new Logistic();
        ((Stacking) algorithms[25].classifier).setClassifiers(internalClassifiers);
        algorithms[25].name = "25 Stacking+GaussianProcesses+LinearRegression+Logistic";

        algorithms[26].classifier = new Stacking();
        internalClassifiers = new Classifier[2];
        internalClassifiers[0] = new RandomForest();
        internalClassifiers[1] = new GaussianProcesses();
        ((Stacking) algorithms[26].classifier).setClassifiers(internalClassifiers);
        algorithms[26].name = "26 Stacking+RandomForest+GaussianProcesses";

        algorithms[27].classifier = new Vote();
        internalClassifiers = new Classifier[3];
        internalClassifiers[0] = new GaussianProcesses();
        internalClassifiers[1] = new LinearRegression();
        internalClassifiers[2] = new Logistic();
        ((Vote) algorithms[27].classifier).setClassifiers(internalClassifiers);
        algorithms[27].name = "27 Vote+GaussianProcesses+LinearRegression+Logistic";

        algorithms[28].classifier = new Vote();
        internalClassifiers = new Classifier[2];
        internalClassifiers[0] = new RandomForest();
        internalClassifiers[1] = new GaussianProcesses();
        ((Vote) algorithms[28].classifier).setClassifiers(internalClassifiers);
        algorithms[28].name = "28 Vote+RandomForest+GaussianProcesses";

        algorithms[29].classifier = new DecisionTable();
        algorithms[29].name = "29 DecisionTable";

        algorithms[30].classifier = new M5Rules();
        algorithms[30].name = "30 M5Rules";

        algorithms[31].classifier = new ZeroR();
        algorithms[31].name = "31 ZeroR";

        algorithms[32].classifier = new DecisionStump();
        algorithms[32].name = "32 DecisionStump";

        algorithms[33].classifier = new M5P();
        algorithms[33].name = "33 M5P";

        algorithms[34].classifier = new RandomForest();
        algorithms[34].name = "34 RandomForest";

        algorithms[35].classifier = new RandomTree();
        algorithms[35].name = "35 RandomTree";

        algorithms[36].classifier = new REPTree();
        algorithms[36].name = "36 REPTree";
    }

    public double getMean(double input[]) {
        double output = 0;
        for (int i = 0; i < input.length; i++) {
            output = input[i];
        }
        return output / input.length;
    }
    
    

    public double estimateNextMonthDemand(int currentMonthIndex, Classifier classifer) {
//        boolean success = false;

        WekaForecaster forecaster = new WekaForecaster();
        TSLagMaker lagMaker = forecaster.getTSLagMaker();

        try {
            forecaster.setFieldsToForecast("Demand");
            forecaster.setCalculateConfIntervalsForForecasts(1);
            forecaster.setBaseForecaster(classifer);
            lagMaker.setTimeStampField("Month");
            lagMaker.setMinLag(1);
            lagMaker.setMaxLag(12);
            lagMaker.setAddMonthOfYear(true);
            lagMaker.setAddQuarterOfYear(true);
            Instances a = new Instances(RealDemands,0,currentMonthIndex+1);
            int knownPart=Math.max(currentMonthIndex+1, 18);
            forecaster.buildForecaster(new Instances(RealDemands,0,knownPart), System.out);
            forecaster.primeForecaster(new Instances(RealDemands,0,currentMonthIndex+1));

            int numStepsToForecast = 1;
            List<List<NumericPrediction>> forecast = forecaster.forecast(numStepsToForecast, System.out);
//            List<List<NumericPrediction>> forecast=forecaster.forecast(1, new Instances(RealDemands,0,currentMonthIndex+1));
            forecaster.primeForecaster(a);

            double forecastedValues[] = getPredictionForNextMonth(forecast, numStepsToForecast);
//            success = true;
//            System.out.println(success);
//            System.out.println(forecastedValues[1]);
            double rangeMidToHighConfidenceInterval=forecastedValues[0]-forecastedValues[1];
            double offset=rangeMidToHighConfidenceInterval/3.0;
            return forecastedValues[0]+offset;
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = ex.getMessage().toLowerCase();
            if (msg.indexOf("not in classpath") > -1) {
                return -1;
            }
        }

        return -1;
    }

    private double[] getPredictionForNextMonth(List<List<NumericPrediction>> preds, int steps) {
        double output[] = new double[3];

        for (int i = 0; i < steps; i++) {
            List<NumericPrediction> predsForTargetsAtStep = preds.get(i);

            for (int j = 0; j < predsForTargetsAtStep.size(); j++) {
                NumericPrediction p = predsForTargetsAtStep.get(j);
                double[][] limits = p.predictionIntervals();
                output[0] = p.predicted();
                output[1] = limits[0][0];
                output[2] = limits[0][1];
            }
        }
        return output;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}