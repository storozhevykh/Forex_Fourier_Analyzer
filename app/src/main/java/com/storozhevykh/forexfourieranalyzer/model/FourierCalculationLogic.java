package com.storozhevykh.forexfourieranalyzer.model;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.storozhevykh.forexfourieranalyzer.controller.ParametersOwner;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;

import java.util.ArrayList;
import java.util.List;

public class FourierCalculationLogic {

    public int iterations = 0;
    public int iterNumber = 0;

    private List<Quote> quoteList;
    private Double[] close;
    private Double[] approximation;
    private Double[] prediction;
    private List<Double[]> result;
    private Handler handler;

    private int numberOfModes;
    private int numberOfHarmonics;
    private int periodOfApproximation;
    private int predictedBars;
    private double minPeriods;
    private double maxPeriods;
    private int periodStep;
    private int amplitudeSteps;
    private double phaseStep;
    private int highFreqFilter;
    private boolean dynamicApproximation;

    private int minPeriod;
    private int maxPeriod;
    private int minT;
    private Integer[] modeLength;
    private int actualPeriodStep;

    public FourierCalculationLogic(List<Quote> quoteList, Handler handler) {
        this.quoteList = quoteList;
        this.handler = handler;
        loadParameters();
        close = new Double[periodOfApproximation];
        approximation = new Double[periodOfApproximation];
        prediction = new Double[predictedBars];
        result = new ArrayList<>();
        for (int i = 0; i < periodOfApproximation; i++) {
            close[i] = quoteList.get(i).getClose();
        }

        minPeriod = (int) Math.round(periodOfApproximation/maxPeriods);
        maxPeriod = (int) Math.round(periodOfApproximation/minPeriods);
        modeLength = new Integer[numberOfModes + 1];
    }

    public int getIterations() {
        actualPeriodStep = (int) Math.round(periodStep * (1.0 * maxPeriod / periodOfApproximation));
        return (int) Math.round(((maxPeriod - minPeriod) / actualPeriodStep) * amplitudeSteps * (2 * Math.PI / phaseStep) * numberOfHarmonics * numberOfModes);
    }

    public List<Double[]> calculate(double maxHigh, double minLow) {

        double prevDeviation = 0;
        minT = periodOfApproximation;
        double prevBestDeviation = 0;
        double bestNextModeDeviation = 0;
        double deviation = 0;
        double nextModeDeviation = 0;
        Double[][] amplitudeOpt = new Double[numberOfModes + 1][numberOfHarmonics + 1];
        Integer[][] tOpt = new Integer[numberOfModes + 1][numberOfHarmonics + 1];
        Double[][] phaseOpt = new Double[numberOfModes + 1][numberOfHarmonics + 1];
        double maxAmplitude = maxHigh - minLow;
        double minAmplitude = maxAmplitude / amplitudeSteps;
        double amplitudeStep = minAmplitude;
        double pi = Math.PI;
        double y = 0;
        int length = periodOfApproximation;

        iterations = getIterations();

        //BaseLine
        double kOpt = 0;
        double bOpt = 0;
        double bRange = maxHigh - minLow;
        double kRange = Math.abs(close[0] - close[periodOfApproximation - 1]) / periodOfApproximation;

        double bStep = bRange * 2.0 / 200;
        double kStep = kRange * 2.0 / 500;

        for (double b = close[0] - bRange; b < close[0] + bRange; b += bStep) {

            for (double k = -kRange; k <= kRange; k += kStep) {
                deviation = 0;
                for (int x = 0; x < periodOfApproximation; x++) {
                    y = k * x + b;
                    deviation = deviation + Math.abs(close[x] - y);
                }
                if (deviation < prevDeviation || prevDeviation == 0) {
                    prevDeviation = deviation;
                    kOpt = k;
                    bOpt = b;
                }
            }
        }


        for (int mode = 1; mode <= numberOfModes; mode++) {

            modeLength[mode] = minT;
            minPeriod = (int) Math.round(minT/maxPeriods);
            maxPeriod = (int) Math.round(minT/minPeriods);
            prevBestDeviation = bestNextModeDeviation;
            actualPeriodStep = (int) Math.round(periodStep * (1.0 * maxPeriod / periodOfApproximation));
            if (actualPeriodStep == 0)
                actualPeriodStep = 1;

            for (int harmonic = 1; harmonic <= numberOfHarmonics; harmonic++) {

                if (prevDeviation < prevBestDeviation || prevBestDeviation == 0)
                    prevBestDeviation = prevDeviation;
                prevDeviation = 0;
                amplitudeOpt[mode][harmonic] = 0.0;

                if (maxPeriod < highFreqFilter) {
                    tOpt[mode][harmonic] = 1;
                    amplitudeOpt[mode][harmonic] = 0.0;
                    phaseOpt[mode][harmonic] = 1.0;
                    continue;
                }

                if (minPeriod < highFreqFilter)
                    minPeriod = highFreqFilter;

                for (int T = maxPeriod; T >= minPeriod; T -= actualPeriodStep) {

                    for (double amplitude = minAmplitude; amplitude <= maxAmplitude; amplitude += amplitudeStep) {

                        for (double phase = 0; phase <= 2 * pi; phase += phaseStep) {
                            iterNumber++;
                            handler.sendEmptyMessage(iterNumber);
                            /*if(NormalizeDouble(IterNumber*IterPercent,0)>CurPercent)
                            {
                                CurPercent=(int)NormalizeDouble(IterNumber*IterPercent,0);
                                info="Harmonics calculation progress... " + CurPercent + "%";
                                ObjectSetText("Info3",info,12,font,DeepSkyBlue);
                                WindowRedraw();
                            }*/
                            nextModeDeviation = 0;
                            deviation = 0;

                            for(int x = 0; x < length; x++) {

                                if (mode == 1 && harmonic == 1) {
                                    y = kOpt * x + bOpt + amplitude * Math.sin((2 * pi / T) * x + phase);
                                }
                                else {
                                    int i = 0;
                                    int j = 0;
                                    if (harmonic == 1)
                                        i = 1;
                                    if(harmonic > 1)
                                        j = 1;
                                    y = kOpt * x + bOpt + amplitude * Math.sin((2 * pi / T) * x + phase);

                                    for (int M = 1; M <= mode - i; M++) {
                                        for (int H = 1; H <= harmonic - j; H++) {
                                            y = y + amplitudeOpt[M][H] * Math.sin((2 * pi / tOpt[M][H]) * x + phaseOpt[M][H]);
                                        }
                                    }
                                }

                                if(!dynamicApproximation) {
                                    deviation = deviation + Math.abs(close[x] - y);
                                    if (x <= T)
                                        nextModeDeviation = deviation;
                                }
                                if(dynamicApproximation) {
                                    deviation = deviation + Math.abs(close[x] - y) / Math.sqrt(Math.sqrt(x));
                                    if (x <= T)
                                        nextModeDeviation = deviation;
                                }
                            }

                            if (deviation < prevDeviation || prevDeviation == 0) {
                                prevDeviation = deviation;
                                tOpt[mode][harmonic] = T;
                                amplitudeOpt[mode][harmonic] = amplitude;
                                phaseOpt[mode][harmonic] = phase;
                                if (deviation >= prevBestDeviation && prevBestDeviation > 0)
                                    amplitudeOpt[mode][harmonic] = 0.0;
                                if (amplitudeOpt[mode][harmonic] > 0)
                                    bestNextModeDeviation = nextModeDeviation;
                            }
                        }
                    }
                }

                if (tOpt[mode][harmonic] < minT)
                    minT = tOpt[mode][harmonic];

                if (prevDeviation > prevBestDeviation && prevBestDeviation > 0) {
                    for (int harm = harmonic; harm <= numberOfHarmonics; harm++) {
                        /*Print ("Harmonic = ", Harm, ", ", "PrevDeviation = ", PrevDeviation);*/
                        /*IterNumber=IterNumber+(int)NormalizeDouble((MaxPeriod-MinPeriod)/PeriodStep,0)*AmplitudeSteps*NormalizeDouble(2*pi/PhaseStep,0);
                        if(NormalizeDouble(IterNumber*IterPercent,0)>CurPercent)
                        {
                            CurPercent=(int)NormalizeDouble(IterNumber*IterPercent,0);
                            info="Harmonics calculation progress... " + CurPercent + "%";
                            ObjectSetText("Info3",info,12,font,DeepSkyBlue);
                            WindowRedraw();
                        }*/
                        tOpt[mode][harm] = 1;
                        amplitudeOpt[mode][harm] = 0.0;
                        phaseOpt[mode][harm] = 1.0;
                    }
                    break;
                }
            }
            length = minT;
        }

        for (int i = 0; i < periodOfApproximation; i++) {
            y = kOpt * i + bOpt;
            for (int mode = 1; mode <= numberOfModes; mode++) {
                for (int harmonic = 1; harmonic <= numberOfHarmonics; harmonic++) {
                    y = y + amplitudeOpt[mode][harmonic] * Math.sin((2 * pi / tOpt[mode][harmonic]) * i + phaseOpt[mode][harmonic]);
                }
            }
            approximation[i] = y;
        }

        for (int i = -predictedBars; i < 0; i++) {
            y = kOpt * i + bOpt;
            for (int mode = 1; mode <= numberOfModes; mode++) {
                for (int harmonic = 1; harmonic <= numberOfHarmonics; harmonic++) {
                    y = y + amplitudeOpt[mode][harmonic] * Math.sin((2 * pi / tOpt[mode][harmonic]) * i + phaseOpt[mode][harmonic]);
                }
            }
            prediction[predictedBars + i] = y;
        }

        result.add(approximation);
        result.add(prediction);

        return result;
    }

    public void loadParameters() {
        numberOfModes = ParametersOwner.getInstance().getNumberOfModes();
        numberOfHarmonics = ParametersOwner.getInstance().getNumberOfHarmonics();
        periodOfApproximation = ParametersOwner.getInstance().getPeriodOfApproximation();
        predictedBars = ParametersOwner.getInstance().getPredictedBars();
        minPeriods = ParametersOwner.getInstance().getMinPeriods();
        maxPeriods = ParametersOwner.getInstance().getMaxPeriods();
        periodStep = ParametersOwner.getInstance().getPeriodStep();
        amplitudeSteps = ParametersOwner.getInstance().getAmplitudeSteps();
        phaseStep = ParametersOwner.getInstance().getPhaseStep();
        highFreqFilter = ParametersOwner.getInstance().getHighFreqFilter();
        dynamicApproximation = ParametersOwner.getInstance().isDynamicApproximation();
    }
}
