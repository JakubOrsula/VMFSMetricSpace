package vm.fs.main.objTransforms.learning;

import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSGHPSketchesPivotPairsStorageImpl;
import vm.metricSpace.Dataset;
import vm.objTransforms.learning.LearningSketchingGHP;
import vm.objTransforms.storeLearned.GHPSketchingPivotPairsStoreInterface;

/**
 *
 * @author xmic
 */
public class FSLearnGHPSketchingMain {

    public static void main(String[] args) {
        GHPSketchingPivotPairsStoreInterface sketchingTechStorage = new FSGHPSketchesPivotPairsStorageImpl();
        int[] sketchesLengths = new int[]{256, 192, 128, 64, 512};
//        run(new FSDatasetInstanceSingularizator.DeCAFDataset(), sketchingTechStorage, sketchesLengths);
//        System.gc();
        run(new FSDatasetInstanceSingularizator.MPEG7dataset(), sketchingTechStorage, sketchesLengths);
        System.gc();
        run(new FSDatasetInstanceSingularizator.RandomDataset20Uniform(), sketchingTechStorage, sketchesLengths);
        System.gc();
        run(new FSDatasetInstanceSingularizator.SIFTdataset(), sketchingTechStorage, sketchesLengths);
        System.gc();
    }

    private static void run(Dataset dataset, GHPSketchingPivotPairsStoreInterface sketchingTechStorage, int[] sketchesLengths) {
        int sampleSize = 100000; // 100000
        LearningSketchingGHP learn = new LearningSketchingGHP(dataset.getMetricSpace(), dataset.getMetricSpacesStorage(), sketchingTechStorage);
        learn.execute(dataset.getDatasetName(), dataset.getDatasetName(), sampleSize, sketchesLengths, 0.5f);
    }
}
