package vm.fs.main.objTransforms.learning;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSSVDStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.objTransforms.learning.LearnSVD;
import vm.objTransforms.storeLearned.SVDStoreInterface;

/**
 *
 * @author Vlada
 */
public class FSLearnSVDMain {

    public static void main(String[] args) {
//        int sampleCount = 100000;
        int sampleCount = 500000;
//        run(new FSDatasetInstanceSingularizator.SIFTdataset(), sampleCount);
//        run(new FSDatasetInstanceSingularizator.DeCAFDataset(), sampleCount);
        run(new FSDatasetInstanceSingularizator.LAION_1M_SampleDataset(), sampleCount);
//        run(new FSDatasetInstanceSingularizator.SIFTdataset(), sampleCount);
    }

    private static final List<Object> getTrivialSampleDataset() {
        List<Object> ret = new ArrayList<>();
        ret.add(new AbstractMap.SimpleEntry("1", new float[]{3, 2, 2}));
        ret.add(new AbstractMap.SimpleEntry("2", new float[]{2, 3, -2}));
        return ret;
    }

    private static void run(Dataset dataset, int sampleCount) {
        String datasetName = dataset.getDatasetName();
        AbstractMetricSpace sourceMetricSpace = dataset.getMetricSpace();
//
        List<Object> sampleOfDataset = dataset.getSampleOfDataset(sampleCount);
//        List<Object> sampleOfDataset = getTrivialSampleDataset();
        SVDStoreInterface pcaStorage = new FSSVDStorageImpl(datasetName, sampleCount, true);

        LearnSVD svd = new LearnSVD(sourceMetricSpace, pcaStorage, sampleOfDataset, datasetName, sampleCount);
        svd.execute();
    }
}
