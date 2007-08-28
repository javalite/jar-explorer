package org.jarexplorer;

import java.util.*;

/**
 * Brute force index of jar file entries.
 *
 * @author greg.tatham,
 * Date: Jul 27, 2007
 * @author Igor Polevoy
 */
public class EntryIndex
{

    private HashMap indexMap = new HashMap();


    /**
     *  Adds new entry to index.
     *
     * @param className - name of antry in the jar file
     * @param jarFileName - name of the jar file
     */
    public void addEntryName(String jarFileName, String className) 
    {
        //lightweight instance of ClassInformation:
        ClassInformation classInfo = new ClassInformation(jarFileName, className);
        
        //add array if not found
        if(!indexMap.containsKey(classInfo.getKey()))
        {
            indexMap.put(classInfo.getKey(), classInfo);
        }
    }

    /**
     * Performs a search for a name.
     *
     * @param name - name of a resource or class in format: <code>dir/dir/Resource.extension</code>, or any substring.
     * @return list of {@link ClassInformation} instances.
     */
    public ArrayList search(String name)
    {
        //not a real index, just a brute force linera search
        Iterator values = indexMap.values().iterator();

        ArrayList results  = new ArrayList();

        while (values.hasNext())
        {
            ClassInformation classInfo = (ClassInformation)values.next();

            if ( classInfo.getClassPath().indexOf(name) != -1 ) {

                results.add(classInfo);
            }
        }
        return results;
    }

    /**
     * Get all the classes in a given jar alphabetically sorted.
     *
     * @param jarName The Jar for which to return all the classes.
     * @return  List of {@link ClassInformation} instances.
     */
    public ArrayList getClassesInJar ( String jarName ) {

        Iterator values = indexMap.values().iterator();
        ArrayList results  = new ArrayList();

        while (values.hasNext())
        {
            ClassInformation classInfo = (ClassInformation)values.next();
            if ( classInfo.getJarFileName().equals(jarName) ) {

                results.add(classInfo);
            }
        }
        Collections.sort(results, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return results;
    }
}
