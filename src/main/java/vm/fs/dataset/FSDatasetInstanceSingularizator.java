package vm.fs.dataset;

import java.util.Map;
import vm.fs.metricSpaceImpl.FSMetricSpaceImpl;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.dataToStringConvertors.SingularisedConvertors;

/**
 *
 * @author xmic
 */
public class FSDatasetInstanceSingularizator {

    public static class DeCAFDataset extends FSFloatVectorDataset {

        public DeCAFDataset() {
            super("decaf_1m");
        }

    }

    public static class RandomDataset20Uniform extends FSFloatVectorDataset {

        public RandomDataset20Uniform() {
            super("random_20dim_uniform_1m");
        }
    }

    public static class SIFTdataset extends FSFloatVectorDataset {

        public SIFTdataset() {
            super("sift_1m");
        }
    }

    public static class MPEG7dataset extends Dataset<Map<String, Object>> {

        public MPEG7dataset() {
            this.datasetName = "mpeg7_1m";
            this.metricSpace = new FSMetricSpaceImpl();
            this.metricSpacesStorage = new FSMetricSpacesStorage<>(metricSpace, SingularisedConvertors.MPEG7_SPACE);
        }
    }

    public static class DeCAF_PCA256Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA256Dataset() {
            super("decaf_1m_PCA256");
        }

    }

    public static class DeCAF_GHP_50_256Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_256Dataset() {
            super("decaf_1m_GHP_50_256");
        }

    }

    public static class DeCAF_GHP_50_192Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_192Dataset() {
            super("decaf_1m_GHP_50_192");
        }

    }

    public static class DeCAF_GHP_50_128Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_128Dataset() {
            super("decaf_1m_GHP_50_128");
        }

    }

    public static class DeCAF_GHP_50_64Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_64Dataset() {
            super("decaf_1m_GHP_50_64");
        }

    }

    private static class FSFloatVectorDataset extends Dataset<float[]> {

        public FSFloatVectorDataset(String datasetName) {
            this.datasetName = datasetName;
            this.metricSpace = new FSMetricSpaceImpl();
            this.metricSpacesStorage = new FSMetricSpacesStorage<>(metricSpace, SingularisedConvertors.FLOAT_VECTOR_SPACE);
        }
    }

    private static class FSHammingSpaceDataset extends Dataset<long[]> {

        public FSHammingSpaceDataset(String datasetName) {
            this.datasetName = datasetName;
            this.metricSpace = new FSMetricSpaceImpl();
            this.metricSpacesStorage = new FSMetricSpacesStorage<>(metricSpace, SingularisedConvertors.LONG_VECTOR_SPACE);
        }
    }

}
