package vm.fs.main.objTransforms.learning;

import java.util.Map;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSGHPSketchesPivotPairsStorageImpl;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.metricSpace.Dataset;
import vm.objTransforms.learning.LearnSketchingGHP;
import vm.objTransforms.storeLearned.GHPSketchingPivotPairsStoreInterface;

/**
 *
 * @author xmic
 */
public class FSLearnGHPSketchingMain {

    public static void main(String[] args) {
        GHPSketchingPivotPairsStoreInterface sketchingTechStorage = new FSGHPSketchesPivotPairsStorageImpl();
        int[] sketchesLengths = new int[]{256, 192, 128, 64, 512};
        run(new FSDatasetInstanceSingularizator.DeCAFDataset(), sketchingTechStorage, sketchesLengths);
        System.gc();
        run(new FSDatasetInstanceSingularizator.FSMPEG7dataset(), sketchingTechStorage, sketchesLengths);
        System.gc();
        run(new FSDatasetInstanceSingularizator.RandomDataset20Uniform(), sketchingTechStorage, sketchesLengths);
        System.gc();
        run(new FSDatasetInstanceSingularizator.SIFTdataset(), sketchingTechStorage, sketchesLengths);
        System.gc();
    }

    private static void run(Dataset dataset, GHPSketchingPivotPairsStoreInterface sketchingTechStorage, int[] sketchesLengths) {
        int sampleSize = 100000; // 100000
        LearnSketchingGHP learn = new LearnSketchingGHP(dataset.getMetricSpace(), dataset.getMetricSpacesStorage(), sketchingTechStorage);
        String datasetName = dataset.getDatasetName();
        learn.evaluate(datasetName, datasetName, sampleSize, sketchesLengths, 0.5f);
    }
}
