package tech.tablesaw.api.ml.classification;

import com.google.common.base.Preconditions;
import smile.classification.KNN;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.CategoryColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.ShortColumn;
import tech.tablesaw.util.DoubleArrays;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 */
public class Knn extends AbstractClassifier {

    private final KNN<double[]> classifierModel;

    private Knn(KNN<double[]> classifierModel) {
        this.classifierModel = classifierModel;
    }

    public static Knn learn(int k, ShortColumn labels, NumericColumn... predictors) {
        KNN<double[]> classifierModel = KNN.learn(DoubleArrays.to2dArray(predictors), labels.toIntArray(), k);
        return new Knn(classifierModel);
    }

    public static Knn learn(int k, IntColumn labels, NumericColumn... predictors) {
        KNN<double[]> classifierModel = KNN.learn(DoubleArrays.to2dArray(predictors), labels.data().toIntArray(), k);
        return new Knn(classifierModel);
    }

    public static Knn learn(int k, BooleanColumn labels, NumericColumn... predictors) {
        KNN<double[]> classifierModel = KNN.learn(DoubleArrays.to2dArray(predictors), labels.toIntArray(), k);
        return new Knn(classifierModel);
    }

    public static Knn learn(int k, CategoryColumn labels, NumericColumn... predictors) {
        KNN<double[]> classifierModel = KNN.learn(DoubleArrays.to2dArray(predictors), labels.data().toIntArray(), k);
        return new Knn(classifierModel);
    }

    public int predict(double[] data) {
        return classifierModel.predict(data);
    }

    public ConfusionMatrix predictMatrix(ShortColumn labels, NumericColumn... predictors) {
        Preconditions.checkArgument(predictors.length > 0);

        SortedSet<Object> labelSet = new TreeSet<>(labels.asSet());
        ConfusionMatrix confusion = new StandardConfusionMatrix(labelSet);

        populateMatrix(labels.toIntArray(), confusion, predictors);
        return confusion;
    }

    public ConfusionMatrix predictMatrix(IntColumn labels, NumericColumn... predictors) {
        Preconditions.checkArgument(predictors.length > 0);

        SortedSet<Object> labelSet = new TreeSet<>(labels.asSet());
        ConfusionMatrix confusion = new StandardConfusionMatrix(labelSet);

        populateMatrix(labels.data().toIntArray(), confusion, predictors);
        return confusion;
    }

    public ConfusionMatrix predictMatrix(BooleanColumn labels, NumericColumn... predictors) {
        Preconditions.checkArgument(predictors.length > 0);

        SortedSet<Object> labelSet = new TreeSet<>(labels.asSet());
        ConfusionMatrix confusion = new StandardConfusionMatrix(labelSet);

        populateMatrix(labels.toIntArray(), confusion, predictors);
        return confusion;
    }

    public ConfusionMatrix predictMatrix(CategoryColumn labels, NumericColumn... predictors) {
        Preconditions.checkArgument(predictors.length > 0);

        SortedSet<String> labelSet = new TreeSet<>(labels.asSet());
        ConfusionMatrix confusion = new CategoryConfusionMatrix(labels, labelSet);

        populateMatrix(labels.data().toIntArray(), confusion, predictors);
        return confusion;
    }

    public int[] predict(NumericColumn... predictors) {
        Preconditions.checkArgument(predictors.length > 0);
        int[] predictedLabels = new int[predictors[0].size()];
        for (int row = 0; row < predictors[0].size(); row++) {
            double[] data = new double[predictors.length];
            for (int col = 0; col < predictors.length; col++) {
                data[row] = predictors[col].getFloat(row);
            }
            predictedLabels[row] = classifierModel.predict(data);
        }
        return predictedLabels;
    }

    @Override
    int predictFromModel(double[] data) {
        //TODO(lwhite): Better tests
/*
        if (data[0] == 5.0)
            System.out.println(Arrays.toString(data));
*/
        return classifierModel.predict(data);
    }
}
