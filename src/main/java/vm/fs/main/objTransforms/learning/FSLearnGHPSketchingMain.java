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
        int[] sketchesLengths = new int[]{256};
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.LAION_100M_Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset, sketchingTechStorage, sketchesLengths);
            System.gc();
        }
    }

    private static void run(Dataset dataset, GHPSketchingPivotPairsStoreInterface sketchingTechStorage, int[] sketchesLengths) {
        int sampleSize = 1000000; // 100k - 1M, depends od the size of data and dist comp. cost
        int pivotCount = 1024; // min 512, max 1024 - RAM and time grow with the second power of this param!
        LearnSketchingGHP learn = new LearnSketchingGHP(dataset, sketchingTechStorage, pivotCount, 15000);
        String datasetName = dataset.getDatasetName();
        String pivotsName = dataset.getPivotSetName();
        // voluntary step and voluntary arguments - is the precomputed distances does not excist, that deals with it automatically
        FSPrecomputedDistancesMatrixLoaderImpl pd = new FSPrecomputedDistancesMatrixLoaderImpl();
        float[][] dists = pd.loadPrecomPivotsToObjectsDists(datasetName, pivotsName, pivotCount);
        Map<String, Integer> columnHeaders = pd.getColumnHeaders();
        Map<String, Integer> rowHeaders = pd.getRowHeaders();
        // voluntary step and voluntary arguments
        learn.evaluate(dataset, sampleSize, sketchesLengths, 0.5f, dists, columnHeaders, rowHeaders);
    }
}
