/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.search.filtering.perform;

import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSGHPSketchesPivotPairsStorageImpl;
import vm.fs.store.filtering.FSSecondaryFilteringWithSketchesStorage;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.DistanceFunctionInterface;
import vm.metricSpace.distance.bounding.nopivot.impl.SecondaryFilteringWithSketches;
import vm.metricSpace.distance.bounding.nopivot.learning.LearningSecondaryFilteringWithSketches;
import vm.metricSpace.distance.bounding.nopivot.storeLearned.SecondaryFilteringWithSketchesStoreInterface;
import vm.objTransforms.objectToSketchTransformators.AbstractObjectToSketchTransformator;
import vm.objTransforms.objectToSketchTransformators.SketchingGHP;
import vm.objTransforms.storeLearned.GHPSketchingPivotPairsStoreInterface;
import vm.queryResults.recallEvaluation.RecallOfCandsSetsEvaluator;
import vm.search.SearchingAlgorithm;
import vm.search.impl.KNNSearchWithSketchSecondaryFiltering;

/**
 *
 * @author Vlada
 */
public class FSKNNQueriesSeqScanWithSecondaryFilteringWithSketches {

    private static final Logger LOG = Logger.getLogger(FSKNNQueriesSeqScanWithSecondaryFilteringWithSketches.class.getName());

    public static void main(String[] args) {
        float pCum = 0.75f;
        int sketchLength = 256;
        Dataset[] fullDatasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(),
            new FSDatasetInstanceSingularizator.LAION_30M_Dataset()
        };
        Dataset[] sketchesDatasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_256Dataset(),
            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_256Dataset(),
            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_256Dataset()
        };
        float[] distIntervalsForPX = new float[]{
            2,
            0.004f,
            0.004f
        };
        for (int i = 2; i < sketchesDatasets.length; i++) {
            Dataset fullDataset = fullDatasets[i];
            Dataset sketchesDataset = sketchesDatasets[i];
            float distIntervalForPX = distIntervalsForPX[i];
            run(fullDataset, sketchesDataset, distIntervalForPX, pCum, sketchLength);
        }
    }

    private static void run(Dataset fullDataset, Dataset sketchesDataset, float distIntervalForPX, float pCum, int sketchLength) {
        int k = 10;
        AbstractMetricSpace metricSpace = fullDataset.getMetricSpace();
        DistanceFunctionInterface df = fullDataset.getDistanceFunction();

        GHPSketchingPivotPairsStoreInterface storageOfPivotPairs = new FSGHPSketchesPivotPairsStorageImpl();
        List pivots = fullDataset.getPivots(-1);
        AbstractObjectToSketchTransformator sketchingTechnique = new SketchingGHP(df, metricSpace, pivots, false, fullDataset.getDatasetName(), 0.5f, sketchLength, storageOfPivotPairs);

        SecondaryFilteringWithSketchesStoreInterface storage = new FSSecondaryFilteringWithSketchesStorage();
        SecondaryFilteringWithSketches filter = new SecondaryFilteringWithSketches("", fullDataset.getDatasetName(), sketchesDataset, storage, pCum, LearningSecondaryFilteringWithSketches.SKETCHES_SAMPLE_COUNT_FOR_IDIM_PX, LearningSecondaryFilteringWithSketches.DISTS_COMPS_FOR_SK_IDIM_AND_PX, distIntervalForPX);

        SearchingAlgorithm alg = new KNNSearchWithSketchSecondaryFiltering(fullDataset, filter, sketchingTechnique);

        List queries = fullDataset.getMetricQueryObjects();

        TreeSet[] results = alg.completeKnnSearchOfQuerySet(metricSpace, queries, k, fullDataset.getMetricObjectsFromDataset());

        LOG.log(Level.INFO, "Storing statistics of queries");
        FSQueryExecutionStatsStoreImpl statsStorage = new FSQueryExecutionStatsStoreImpl(fullDataset.getDatasetName(), fullDataset.getQuerySetName(), k, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), filter.getTechFullName(), null);
        statsStorage.storeStatsForQueries(alg.getDistCompsPerQueries(), alg.getTimesPerQueries());
        statsStorage.saveFile();

        LOG.log(Level.INFO, "Storing results of queries");
        FSNearestNeighboursStorageImpl resultsStorage = new FSNearestNeighboursStorageImpl();
        resultsStorage.storeQueryResults(metricSpace, queries, results, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), filter.getTechFullName());

        LOG.log(Level.INFO, "Evaluating accuracy of queries");
        FSRecallOfCandidateSetsStorageImpl recallStorage = new FSRecallOfCandidateSetsStorageImpl(fullDataset.getDatasetName(), fullDataset.getQuerySetName(), k, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), filter.getTechFullName(), null);
        RecallOfCandsSetsEvaluator evaluator = new RecallOfCandsSetsEvaluator(new FSNearestNeighboursStorageImpl(), recallStorage);
        evaluator.evaluateAndStoreRecallsOfQueries(fullDataset.getDatasetName(), fullDataset.getQuerySetName(), k, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), filter.getTechFullName(), k);
        recallStorage.saveFile();
    }

}
