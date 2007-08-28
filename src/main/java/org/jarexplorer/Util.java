package org.jarexplorer;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;


/**
 * org.jarexplorer.Util class
 *
 * @author Igor Polevoy
 */
public class Util
{

    /**
     * Reads resource from a jar file fully
     *
     * @param jarFileName jar file name
     * @param resourceName internal resource name
     * @return full content of resource
     * @throws IOException thrown in case of io problem
     */
    public static String readResourceAsString(String jarFileName, String resourceName) throws IOException
    {
        URL u = new URL("jar:file:" + jarFileName + "!/" + resourceName);
        InputStream in = u.openConnection().getInputStream();
        BufferedInputStream bin = new BufferedInputStream(in);

        StringBuffer stringBuffer = new StringBuffer(1024);
        for (int tmp = bin.read(); tmp != -1; tmp = bin.read())
        {
            stringBuffer.append((char) tmp);
        }
        return stringBuffer.toString();
    }

    /**
     * Reads resource from jar fully
     * @param jarFileName jar file path
     * @param resourceName resource path
     * @return bytes with resource content
     * @throws IOException in case of io error
     */
    public static byte[] readResourceAsBytes(String jarFileName, String resourceName) throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        URL u = new URL("jar:file:" + jarFileName + "!/" + resourceName);
        InputStream in = u.openConnection().getInputStream();
        BufferedInputStream bin = new BufferedInputStream(in);

        for (int tmp = bin.read(); tmp != -1; tmp = bin.read())
        {
            bout.write(tmp);
        }
        return bout.toByteArray();
    }

}