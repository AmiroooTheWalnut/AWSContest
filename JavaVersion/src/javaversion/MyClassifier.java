/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaversion;

import arima.ARIMA;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;
import weka.filters.supervised.attribute.TSLagMaker;

/**
 *
 * @author user
 */
public class MyClassifier {

    String type;
    Classifier classifier;
    String name;

    static Double results[];

    public double estimateNextMonthDemandSeparately(Instances RealDemands, int currentMonthIndex,int initiallyKnownMonths,int warmupMonthsIndex) {
//        boolean success = false;
        if (type.equals("DM")) {
            WekaForecaster forecaster = new WekaForecaster();
            TSLagMaker lagMaker = forecaster.getTSLagMaker();

            try {
                forecaster.setFieldsToForecast("Demand");
                forecaster.setCalculateConfIntervalsForForecasts(1);
                forecaster.setBaseForecaster(classifier);
                lagMaker.setTimeStampField("Month");
                lagMaker.setMinLag(1);
                lagMaker.setMaxLag(12);
                lagMaker.setAddMonthOfYear(true);
                lagMaker.setAddQuarterOfYear(true);
                int knownPartWarmup = Math.max(currentMonthIndex + 1, warmupMonthsIndex);
                forecaster.buildForecaster(new Instances(RealDemands, 0, knownPartWarmup));
                forecaster.primeForecaster(new Instances(RealDemands, 0, currentMonthIndex + 1));

                int numStepsToForecast = 1;
                List<List<NumericPrediction>> forecast = forecaster.forecast(numStepsToForecast);
//            List<List<NumericPrediction>> forecast=forecaster.forecast(1, new Instances(RealDemands,0,currentMonthIndex+1));

                double forecastedValues[] = getPredictionForNextMonth(forecast, numStepsToForecast);
//            success = true;
//            System.out.println(success);
//            System.out.println(forecastedValues[1]);
                double rangeMidToHighConfidenceInterval = forecastedValues[0] - forecastedValues[1];
                double offset = rangeMidToHighConfidenceInterval / 1.0;
                return forecastedValues[0] + offset;
            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = ex.getMessage().toLowerCase();
                if (msg.indexOf("not in classpath") > -1) {
                    return -1;
                }
            }
        } else if (type.equals("ARMA")) {
            try {
                double[] dataArray = new double[currentMonthIndex];
                for (int d = 0; d < dataArray.length; d++) {
                    dataArray[d] = RealDemands.instance(d).value(2);
                }

                //System.out.println(arraylist.size());
                ARIMA arima = new ARIMA(dataArray);

                double range = 0;
                double counter = 0;
                for (int d = 0; d < 4; d++) {
                    range = Math.abs(dataArray[dataArray.length - 1 - d] - dataArray[dataArray.length - 1 - d - 1]);
                    counter = counter + 1;
                }
                range = range / counter;

                int[] model = arima.getARIMAmodel();
                return arima.aftDeal(arima.predictValue(model[0], model[1])) + range / 1.0;
            } catch (Exception ex) {
                return -1;
            }
        }

        return -1;
    }

    static public Double[] makeOneMonthEstimationForAllAlgorithms(Instances RealDemands, MyClassifier algorithms[], int currentMonthIndex, int numCPUs,int initiallyKnownMonths,int warmupMonthsIndex) {
        results = new Double[algorithms.length];

        Thread parallelRuns[] = new Thread[numCPUs];
        CPUUpperLowerBounds bounds[] = CPUUpperLowerBounds.spreadTasks(parallelRuns.length, algorithms.length);
        for (int c = 0; c < bounds.length; c++) {
            final int passed_c = c;
            parallelRuns[c] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = bounds[passed_c].startIndex; i < bounds[passed_c].endIndex; i++) {
                        if (algorithms[i].type.equals("DM")) {
                            WekaForecaster forecaster = new WekaForecaster();
                            TSLagMaker lagMaker = forecaster.getTSLagMaker();

                            try {
                                forecaster.setFieldsToForecast("Demand");
                                forecaster.setCalculateConfIntervalsForForecasts(1);
                                forecaster.setBaseForecaster(algorithms[i].classifier);
                                lagMaker.setTimeStampField("Month");
                                lagMaker.setMinLag(1);
                                lagMaker.setMaxLag(12);
                                lagMaker.setAddMonthOfYear(true);
                                lagMaker.setAddQuarterOfYear(true);
                                int knownPartWarmup = Math.max(currentMonthIndex + 1, warmupMonthsIndex);
                                forecaster.buildForecaster(new Instances(RealDemands, 0, knownPartWarmup));
                                forecaster.primeForecaster(new Instances(RealDemands, 0, currentMonthIndex + 1));

                                int numStepsToForecast = 1;
                                List<List<NumericPrediction>> forecast = forecaster.forecast(numStepsToForecast);
//            List<List<NumericPrediction>> forecast=forecaster.forecast(1, new Instances(RealDemands,0,currentMonthIndex+1));

                                double forecastedValues[] = getPredictionForNextMonth(forecast, numStepsToForecast);
//            success = true;
//            System.out.println(success);
//            System.out.println(forecastedValues[1]);
                                double rangeMidToHighConfidenceInterval = forecastedValues[0] - forecastedValues[1];
                                double offset = rangeMidToHighConfidenceInterval / 1.0;
                                results[i] = forecastedValues[0] + offset;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                String msg = ex.getMessage().toLowerCase();
                                if (msg.indexOf("not in classpath") > -1) {
                                    results[i] = new Double(-1);
                                }
                            }
                        } else if (algorithms[i].type.equals("ARMA")) {
                            try {
                                double[] dataArray = new double[RealDemands.numInstances()];
                                for (int d = 0; d < dataArray.length; d++) {
                                    dataArray[d] = RealDemands.instance(d).value(2);
                                }

                                //System.out.println(arraylist.size());
                                ARIMA arima = new ARIMA(dataArray);

                                double range = 0;
                                double counter = 0;
                                for (int d = 0; d < 4; d++) {
                                    range = Math.abs(dataArray[dataArray.length - d] - dataArray[dataArray.length - d - 1]);
                                    counter = counter + 1;
                                }
                                range = range / counter;

                                int[] model = arima.getARIMAmodel();
                                results[i] = arima.aftDeal(arima.predictValue(model[0], model[1])) + range / 1.0;
                            } catch (Exception ex) {
                                results[i] = new Double(-1);
                            }
                        }
                    }
                }
            });
            parallelRuns[c].start();
        }
        for (int c = 0; c < bounds.length; c++) {
            try {
                parallelRuns[c].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return results;

//        Double output[] = new Double[algorithms.length];
//        for (int i = 0; i < algorithms.length; i++) {
//            if (algorithms[i].type.equals("DM")) {
//                WekaForecaster forecaster = new WekaForecaster();
//                TSLagMaker lagMaker = forecaster.getTSLagMaker();
//
//                try {
//                    forecaster.setFieldsToForecast("Demand");
//                    forecaster.setCalculateConfIntervalsForForecasts(1);
//                    forecaster.setBaseForecaster(algorithms[i].classifier);
//                    lagMaker.setTimeStampField("Month");
//                    lagMaker.setMinLag(1);
//                    lagMaker.setMaxLag(12);
//                    lagMaker.setAddMonthOfYear(true);
//                    lagMaker.setAddQuarterOfYear(true);
//                    Instances a = new Instances(RealDemands, 0, currentMonthIndex + 1);
//                    int knownPart = Math.max(currentMonthIndex + 1, 18);
//                    forecaster.buildForecaster(new Instances(RealDemands, 0, knownPart), System.out);
//                    forecaster.primeForecaster(new Instances(RealDemands, 0, currentMonthIndex + 1));
//
//                    int numStepsToForecast = 1;
//                    List<List<NumericPrediction>> forecast = forecaster.forecast(numStepsToForecast, System.out);
////            List<List<NumericPrediction>> forecast=forecaster.forecast(1, new Instances(RealDemands,0,currentMonthIndex+1));
//
//                    double forecastedValues[] = getPredictionForNextMonth(forecast, numStepsToForecast);
////            success = true;
////            System.out.println(success);
////            System.out.println(forecastedValues[1]);
//                    double rangeMidToHighConfidenceInterval = forecastedValues[0] - forecastedValues[1];
//                    double offset = rangeMidToHighConfidenceInterval / 1;
//                    output[i] = forecastedValues[0] + offset;
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    String msg = ex.getMessage().toLowerCase();
//                    if (msg.indexOf("not in classpath") > -1) {
//                        output[i] = new Double(-1);
//                    }
//                }
//            } else if (algorithms[i].type.equals("ARMA")) {
//                try {
//                    double[] dataArray = new double[RealDemands.numInstances()];
//                    for (int d = 0; d < dataArray.length; d++) {
//                        dataArray[d] = RealDemands.instance(d).value(2);
//                    }
//
//                    //System.out.println(arraylist.size());
//                    ARIMA arima = new ARIMA(dataArray);
//
//                    double range = 0;
//                    double counter = 0;
//                    for (int d = 0; d < 4; d++) {
//                        range = Math.abs(dataArray[dataArray.length - d] - dataArray[dataArray.length - d - 1]);
//                        counter = counter + 1;
//                    }
//                    range = range / counter;
//
//                    int[] model = arima.getARIMAmodel();
//                    output[i] = arima.aftDeal(arima.predictValue(model[0], model[1])) + range / 1.0;
//                } catch (Exception ex) {
//                    output[i] = new Double(-1);
//                }
//
////                int[] model = arima.getARIMAmodel();
////                System.out.println("Best model is [p,q]=" + "[" + model[0] + " " + model[1] + "]");
////                System.out.println("Predict value=" + arima.aftDeal(arima.predictValue(model[0], model[1])));
////                System.out.println("Predict error=" + (arima.aftDeal(arima.predictValue(model[0], model[1])) - arraylist.get(arraylist.size() - 1)) / arraylist.get(arraylist.size() - 1) * 100 + "%");
//            }
//
//        }
//        return output;
    }

    static private double[] getPredictionForNextMonth(List<List<NumericPrediction>> preds, int steps) {
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

}
