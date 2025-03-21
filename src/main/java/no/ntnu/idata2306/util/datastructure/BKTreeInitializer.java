package no.ntnu.idata2306.util.datastructure;

import no.ntnu.idata2306.exception.BKTreeException;

import java.util.List;
import java.util.function.Function;


/**
 * BKTreeInitializer is a utility class that provides a method to initialize a BKTree with a list of objects.
 * This class uses generics to allow the initialization of BKTree with any type of object.
 */
public class BKTreeInitializer {

    /**
     * Initializes a BKTree with the given list of objects.
     * This method creates a new BKTree instance and adds each object from the list to the tree.
     *
     * @param <T> the type of objects to be added to the BKTree.
     * @param objects the list of objects to be added to the BKTree.
     * @param extractor a function that extracts the string representation from the objects.
     *                  This function is used to convert each object in the list to a string that will be stored in the BKTree.
     *                  The extractor function should be provided based on how you want to represent the objects as strings.
     *                  For example, if you have a list of `Course` objects, you might use `Course::getCourseName` as the extractor.
     *                  This ensures that the BKTree contains meaningful string representations of the objects for efficient fuzzy searching.
     * @return a BKTree containing all the objects from the list.
     *
     * @throws BKTreeException if the objects list is null.
     */
    public static <T> BKTree<String> initializeBKTree(List<T> objects, Function<T, String> extractor) {
        if (objects == null) {
            throw new BKTreeException("Initialization failed: The provided list of objects is null. Please provide a valid list of objects to initialize the BKTree.");
        }

        BKTree<String> tree = new BKTree<>();
        for (T object : objects) {
            tree.add(extractor.apply(object));
        }
        return tree;
    }


    private BKTreeInitializer(){

    }
}
