package vm.fs;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author xmic
 */
public class FSGlobal {

    private static final Boolean ASK_FOR_EXISTENCE = true;

    private static String initRoot() {
//        return "h:\\TMP\\Similarity_search\\";
        String[] paths = new String[]{
            "/tmp/"
        };
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                return path;
            }
        }
        throw new IllegalArgumentException("Create the root file");
    }

    public static final String ROOT = initRoot();
    public static final Boolean UNIX = ROOT.contains("/");

    public static final String TRIALS_FOLDER = ROOT + "Trials\\";

    public static final String DATA_FOLDER = ROOT + "Dataset\\";
    public static final String DATASET_MAPDB_FOLDER = DATA_FOLDER + "Map_DB\\";
    public static final String DATASET_MVSTORAGE_FOLDER = DATA_FOLDER + "MV_storage\\";
    public static final String DATASET_FOLDER = DATA_FOLDER + "Dataset\\";
    public static final String DATASET_METADATA = DATA_FOLDER + "Metadata\\";
    public static final String PIVOT_FOLDER = DATA_FOLDER + "Pivot\\";
    public static final String QUERY_FOLDER = DATA_FOLDER + "Query\\";
    public static final String PRECOMPUTED_DISTS_FOLDER = DATA_FOLDER + "DistsToPivots";

    public static final String RESULT_FOLDER = ROOT + "Result\\";
    public static final String RESULT_STATS_FOLDER = "Processed_stats\\";
    public static final String GROUND_TRUTH_FOLDER = RESULT_FOLDER + "Ground_truth\\";

    public static final String AUXILIARY_FOR_DATA_TRANSFORMS = ROOT + "Auxiliary_for_transforms\\";
    public static final String AUXILIARY_FOR_SVD_TRANSFORMS = AUXILIARY_FOR_DATA_TRANSFORMS + "SVD\\";

    public static final String AUXILIARY_FOR_DATA_FILTERING = ROOT + "Auxiliary_for_filtering\\";
    public static final String VORONOI_PARTITIONING_STORAGE = AUXILIARY_FOR_DATA_FILTERING + "Voronoi_partitioning\\";
    public static final String GRAPPLE_PARTITIONING_STORAGE = AUXILIARY_FOR_DATA_FILTERING + "GRAPPLE_partitioning\\";
    public static final String SECONDARY_FILTERING_WITH_SKETCHES_AUXILIARY = AUXILIARY_FOR_DATA_FILTERING + "Secondary_filtering_with_sk_auxiliary\\";
    public static final String SECONDARY_FILTERING_WITH_SKETCHES_FINAL_MAPPING = AUXILIARY_FOR_DATA_FILTERING + "Secondary_filtering_with_sk_mapping\\";

    public static final String SMALLEST_DISTANCES = AUXILIARY_FOR_DATA_FILTERING + "Smallest_distances";
    public static final String AUXILIARY_FOR_TRIANGULAR_FILTERING_WITH_LIMITED_ANGLES = AUXILIARY_FOR_DATA_FILTERING + "Triangle_ineq_with_limited_angles\\";
    public static final String AUXILIARY_FOR_PTOLEMAIOS_WITH_LIMITED_ANGLES = AUXILIARY_FOR_DATA_FILTERING + "Ptolemaios_limited_angles\\";
    public static final String AUXILIARY_FOR_PTOLEMAIOS_COEFS_WITH_LIMITED_ANGLES = AUXILIARY_FOR_PTOLEMAIOS_WITH_LIMITED_ANGLES + "Simple_coefs\\";
    public static final String BINARY_SKETCHES = AUXILIARY_FOR_DATA_TRANSFORMS + "Sketches\\";

    public static final String SIMREL_TOMEGA_THRESHOLDS = AUXILIARY_FOR_DATA_FILTERING + "SimRel_tOmega_thresholds\\";

    private static final Logger LOG = Logger.getLogger(FSGlobal.class.getName());

    public static final File checkFileExistence(File file, boolean willBeDeleted) {
        Object[] options = new String[]{"Yes", "No"};
        file = new File(checkUnixPath(file.getAbsolutePath()));
        file.getParentFile().mkdirs();
        if (file.exists() && willBeDeleted && ASK_FOR_EXISTENCE) {
            LOG.log(Level.WARNING, "Former file deleted: {0}", file.getAbsolutePath());
            file.delete();
            LOG.log(Level.INFO, "File returned ({0})", file.getAbsolutePath());
            return file;
        } else {
            LOG.log(Level.INFO, "File pointer created ({0})", file.getAbsolutePath());
        }
        return file;
    }

    public static final File checkFileExistence(File file) {
        return checkFileExistence(file, true);
    }

    private static String checkUnixPath(String path) {
        if (UNIX) {
            return path.replace("\\", "/");
        }
        return path;
    }

}
