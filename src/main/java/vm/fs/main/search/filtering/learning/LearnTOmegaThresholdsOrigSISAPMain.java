package vm.fs.main.search.filtering.learning;

import java.util.List;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.metricSpace.Dataset;
import vm.fs.store.filtering.FSSimRelThresholdsTOmegaStorage;
import vm.search.SearchingAlgorithm;
import vm.search.impl.SimRelSeqScanKNNCandSet;
import vm.simRel.impl.learn.SimRelEuclideanPCAForLearning;
import vm.simRel.impl.learn.ThresholdsTOmegaEvaluator;
import vm.simRel.impl.learn.storeLearnt.SimRelEuclidThresholdsTOmegaStorage;

/**
 *
 * @author Vlada
 */
public class LearnTOmegaThresholdsOrigSISAPMain {

    public static void main(String[] args) {
        Dataset[] fullDatasets = new Dataset[]{
//            new FSDatasetInstanceSingularizator.DeCAFDataset(), //            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_10M_Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_30M_Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_100M_Dataset()
        };
        Dataset[] pcaDatasets = new Dataset[]{
//            new FSDatasetInstanceSingularizator.DeCAF_PCA256Dataset(), //            new FSDatasetInstanceSingularizator.LAION_100k_PCA96Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_10M_PCA96Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_30M_PCA96Dataset(),
                    new FSDatasetInstanceSingularizator.LAION_100M_PCA96Dataset()
        };
        for (int i = 0; i < pcaDatasets.length; i++) {
            Dataset pcaDataset = pcaDatasets[i];
            Dataset fullDataset = fullDatasets[i];
            run(pcaDataset);
//            float[] learnTOmegaThresholdsOrig = learnTOmegaThresholdsOrig(pcaDataset, 100, 100000, 256, 100, 0.85f);
//            Tools.printArray(learnTOmegaThresholdsOrig, "\n");
        }
    }

    private static void run(Dataset<float[]> pcaDataset) {
        /* the name of the PCA-shortened dataset */
        int kPCA = 300;// DeCAF 100
        /* number of query objects to learn t(\Omega) thresholds. We use different objects than the pivots tested. */
        int querySampleCount = 100;//200
        /* size of the data sample to learn t(\Omega) thresholds, IS: 1M */
        int dataSampleCount = 100000; // 1000000 = 1M
        int pcaLength = 96;
        SimRelEuclidThresholdsTOmegaStorage simRelStorage = new FSSimRelThresholdsTOmegaStorage(querySampleCount, pcaLength, kPCA, dataSampleCount);
        ThresholdsTOmegaEvaluator evaluator = new ThresholdsTOmegaEvaluator(querySampleCount, kPCA);
        evaluator.learnTOmegaThresholds(pcaDataset, simRelStorage, dataSampleCount, pcaLength, FSSimRelThresholdsTOmegaStorage.PERCENTILES);
    }

    // IS paper: pcaDataset, 100, 100000, 256, 100, 0.85f
    private static float[] learnTOmegaThresholdsOrig(Dataset pcaDataset, int querySampleCount, int dataSampleCount, int pcaLength, int kPCA, float percentileWrong) {
        List<Object> querySamples = pcaDataset.getPivots(querySampleCount);
        List<Object> sampleOfDataset = pcaDataset.getSampleOfDataset(dataSampleCount);
        SimRelEuclideanPCAForLearning simRelLearn = new SimRelEuclideanPCAForLearning(pcaLength);
        SearchingAlgorithm alg = new SimRelSeqScanKNNCandSet(simRelLearn, kPCA, true);

        simRelLearn.resetLearning(pcaLength);
        for (int i = 0; i < querySamples.size(); i++) {
            Object queryObj = querySamples.get(i);
            simRelLearn.resetCounters(pcaLength);
            alg.candSetKnnSearch(pcaDataset.getMetricSpace(), queryObj, kPCA, sampleOfDataset.iterator());
            System.out.println("Learning tresholds with the query obj" + (i + 1));
        }
        int idx = SimRelEuclidThresholdsTOmegaStorage.percentileToArrayIdx(percentileWrong);
        float[] ret = simRelLearn.getDiffWhenWrong(percentileWrong)[idx];
        return ret;
    }

}
