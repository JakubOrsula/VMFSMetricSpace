package vm.fs.main.search.filtering.learning;

import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.metricSpace.Dataset;
import vm.simRel.impl.learn.storeLearnt.SimRelEuclidThresholdsTOmegaStorage;
import vm.fs.store.filtering.FSSimRelThresholdsTOmegaStorage;
import vm.simRel.impl.learn.ThresholdsTOmegaEvaluator;

/**
 *
 * @author Vlada
 */
public class LearnTOmegaThresholdsForSimRelEuclidMain {

    public static void main(String[] args) {
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.LAION_10M_PCA96Dataset(),
            new FSDatasetInstanceSingularizator.LAION_30M_PCA96Dataset(),
            new FSDatasetInstanceSingularizator.LAION_100M_PCA96Dataset(),
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    private static void run(Dataset<float[]> pcaDataset) {
        /* the name of the PCA-shortened dataset */
        int kPCA = 300;
        /* number of query objects to learn t(\Omega) thresholds. We use different objects than the pivots tested. */
        int querySampleCount = 200;//200
        /* size of the data sample to learn t(\Omega) thresholds, SISAP: 100K */
        int dataSampleCount = 1000000; // 1000000 = 1M
        /* percentile - defined in the paper. Defines the precision of the simRel */
        float percentile = 0.85f;
        SimRelEuclidThresholdsTOmegaStorage simRelStorage = new FSSimRelThresholdsTOmegaStorage();
        ThresholdsTOmegaEvaluator evaluator = new ThresholdsTOmegaEvaluator(querySampleCount, dataSampleCount, kPCA, percentile);
        evaluator.learnTOmegaThresholds(pcaDataset, simRelStorage);
    }

}
