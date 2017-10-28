package org.jarexplorer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * @author Igor Polevoy
 */
public class JarExplorer extends JFrame {

    private static final long serialVersionUID = 3904974110947067038L;  // we don't want to serializable this, but silence warnings...

    private static final String CONFIG_LAST_FILE = "last.file";

    private static final String CONFIG_ZIP_EXTENSIONS = "zip.extensions";
  
    // this will clean a long running parse, if a Stop button is pressed
    public static boolean stop = false;

    /**
     * Toolbar controls
     */
    private JButton stopB, searchB, cleanB;
    private JTextField searchTF;

    /**
     * Progress bar at the bottom
     */
    public JProgressBar progressBar; // at the bottom of main frame

    /**
     * Main split pane
     */
    private JSplitPane mainSp;

    /**
     * Used to set divider location initially
     */
    private boolean init = false;

    /**
     * this is to display help initial text in the search text field
     */
    private boolean firstTime = true;

    /**
     * Previously opened directory or file
     */
    private File prevFile;

    /**
     * Last file menu item - on the File menu
     */
    private JMenuItem lastFileMI;

    /**
     * Panel to display list of jar files
     */
    private JarFilePanel jarFilePanel;

    /**
     * Panel to display search results
     */
    private SearchResultsPanel resultsPanel;

    private EntryIndex index = new EntryIndex();

    /**
     * Configuration stuff
     */
    private static Model model;
    private static String APP_NAME;

    /**
     * Actual GUI method
     */
    public JarExplorer() {
        super(APP_NAME);
        GUIUtil.setMainFrame(this);

        mainSp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                jarFilePanel = new JarFilePanel(),
                resultsPanel = new SearchResultsPanel());

        getContentPane().add(buildToolBar(), BorderLayout.NORTH);
        getContentPane().add(mainSp, BorderLayout.CENTER);
        getContentPane().add(progressBar = new JProgressBar(JProgressBar.HORIZONTAL), BorderLayout.SOUTH);
        progressBar.setStringPainted(true);

        buildMenu();
        addActionListeners();
    }

    /**
     * builds a tool bar
     *
     * @return toolbar instance with components added and wired with event listeners
     */
    private JToolBar buildToolBar() {

        JToolBar tb = new JToolBar();
        tb.addSeparator();
        tb.add(stopB = new JButton("Stop"));
        stopB.setCursor(Cursor.getDefaultCursor());
        stopB.setToolTipText("stops directory scan");
        tb.add(cleanB = new JButton("Clean"));
        cleanB.setToolTipText("cleans windows");
        tb.addSeparator();
        tb.add(new JLabel("Enter class to search:"));
        tb.addSeparator();
        tb.add(searchTF = new JTextField("enter class to search"));
        searchTF.setToolTipText("to start seach, provide a class name and hit 'Enter'");
        tb.addSeparator();
        tb.add(searchB = new JButton("Search"));
        searchB.setToolTipText("press to start search");
        tb.addSeparator();
        return tb;
    }

    /**
     * Overridden in order to cause a split panel resize when window shows initially
     *
     * @param g Graphic context passed from JVM
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!init) {
            mainSp.setDividerLocation(0.5);
            //subSplitPane.setDividerLocation(0.75);
            init = true;
        }

    }

    private void init() {
        stop = false;
        clean();
    }

    /**
     * Wires action listeners to action components
     */
    private void addActionListeners() {
        searchTF.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                if (firstTime) {
                    firstTime = false;
                    searchTF.setText("");
                }
            }
        });
        searchTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    search();
                }
            }

        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        stopB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop = true;

            }
        });
        cleanB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clean();
            }
        });

        jarFilePanel.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    String selectedJar = jarFilePanel.getSelectedJar();
                    if (selectedJar != null) {
                        showJarContent(selectedJar);
                    }

                }
            }
        });
    }

    /**
     * Shows contents of a jar file in the search results panel.
     *
     * @param jarFile - jar file name, should be a fully qualified path to a file
     */
    private void showJarContent(String jarFile) {
        ArrayList matchingClasses = this.index.getClassesInJar(jarFile);
        resultsPanel.setResults("Contents of " + jarFile, matchingClasses);
    }

    private void scanPath() {
        JFileChooser fc;
        if (prevFile != null) {
            fc = new JFileChooser(prevFile);
        } else {
            fc = new JFileChooser();
        }
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setApproveButtonText("Select");
        fc.setDialogTitle("Locate Root Directory or Jar File:");
        fc.setFileHidingEnabled(false);
        fc.showOpenDialog(JarExplorer.this);

        final File f = fc.getSelectedFile();

        if (f == null) // dialog cancelled
        {
            return;
        }

        if (!f.getPath().endsWith(".jar") && f.isFile()) {
            GUIUtil.messageBox(this, "Message:", "Can process jar files only");
            return;
        }

        prevFile = f;
        scanPath(f);
    }

    /**
     * builds a menu
     */
    private void buildMenu() {
        JMenu fileM = new JMenu("File");
        JMenuItem loadMI = new JMenuItem("Locate Root Directory or Jar File");
        loadMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scanPath();
            }
        });

        fileM.add(loadMI);

        JMenuItem exitMI = new JMenuItem("Exit");
        exitMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileM.add(exitMI);

        fileM.addSeparator();

        lastFileMI = new JMenuItem(Configuration.getProperty(CONFIG_LAST_FILE));
        lastFileMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String file = Configuration.getProperty(CONFIG_LAST_FILE);
                scanPath(new File(file));
            }
        });
        fileM.add(lastFileMI);

        JMenu helpM = new JMenu("Help");

        JMenuItem licenseMI = new JMenuItem("License");
        licenseMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LicenseDialog dialog = new LicenseDialog();
                dialog.setVisible(true);
            }
        });

        JMenuItem aboutMI = new JMenuItem("About");
        aboutMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIUtil.messageBox(JarExplorer.this, "About: ", "<html>" + APP_NAME
                        + "<br>by Igor Polevoy<br/><font size=\"-1\"><i>w/ addition from Greg Tatham</i></font></html>");
            }
        });

        helpM.add(licenseMI);
        helpM.add(aboutMI);

        JMenuBar mb = new JMenuBar();
        mb.add(fileM);
        mb.add(helpM);
        this.setJMenuBar(mb);
    }

    private void search() {
        int found = 0;
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            progressBar.setString("starting to search");
            progressBar.setIndeterminate(true);
            ArrayList results = index.search(searchTF.getText());
            found = results.size();

            if (found == 0) {
                results = index.search(Util.convertFqnToPath(searchTF.getText(), false));
                found = results.size();
            }

            if (found == 0) {
                results = index.search(Util.convertFqnToPath(searchTF.getText(), true));
                found = results.size();
            }
            resultsPanel.setResults("Found substring '" + searchTF.getText() + "' in all jars:", results);
            progressBar.setValue(0);
            progressBar.setIndeterminate(false);
            progressBar.setString("done path searching");
        } catch (Throwable e) {
            GUIUtil.messageBoxWithDetails(JarExplorer.this, "Exception", e);
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            progressBar.setString("done searching, found: " + found + " entries");
        }
    }

    /**
     * Recursive method. Lists content of directory. If it finds a jar, it adds it to the list, if is
     * finds a directory, it calls itself with that directory as argument
     *
     * @param f - file handle representing a directory
     * @param jarNameList - List to which we're adding Jar names
     * @throws java.io.IOException If problems processing Jars or directories
     */
    private void scanDirectory(File f, List jarNameList) throws IOException {
        File[] children = f.listFiles();

        for (File aChildren : children) {
            if (stop) {
                clean();
                return;
            }

            boolean javaArchive = false;
            String[] extensions = Configuration.getProperty(CONFIG_ZIP_EXTENSIONS).split(",");

            for (String extension : extensions) {
                if (aChildren.isFile() && aChildren.getName().endsWith("." + extension)) {
                    javaArchive = true;
                }
            }

            if (javaArchive) {
                String name = aChildren.getCanonicalPath();
                jarNameList.add(name);
                progressBar.setString("Processing: " + name);
                indexJarFile(name);
            } else if (aChildren.isDirectory()) {
                scanDirectory(aChildren, jarNameList);
            } else {
                //ignore
            }
        }
    }

    private void clean() {
        jarFilePanel.clean();
        resultsPanel.clean();
        index = new EntryIndex();
        GUIUtil.getMainFrame().setTitle(JarExplorer.APP_NAME);
    }

    /**
     * Indexes a jar file.
     *
     * @param canonicalPath - fully qualified path to file.
     * @throws IOException in case of io problem
     */
    private void indexJarFile(String canonicalPath) throws IOException {
        FileInputStream fin = new FileInputStream(canonicalPath);

        ZipInputStream jin = new ZipInputStream(fin);
        for (ZipEntry entryName = jin.getNextEntry(); entryName != null; entryName = jin.getNextEntry()) {
            if (!entryName.getName().endsWith("/")) //skip directories
            {
                this.index.addEntryName(canonicalPath, entryName.getName());
            }
        }
        fin.close();
    }

    /**
     * This is where indexing is done.
     *
     * @param topDirectory - root directory of the directory tree to be indexed.
     */
    private void scanPath(final File topDirectory) {
        String treeRoot;
        try {
            treeRoot = topDirectory.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        init();

        if (!topDirectory.exists()) {
            throw new RuntimeException("Path: '" + treeRoot + "' does not exist");
        }

        //run the parsing in a separate thread.
        final String treeRoot1 = treeRoot;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                progressBar.setIndeterminate(true);
                progressBar.setString("Parsing tree: " + treeRoot1);
                try {
                    ArrayList jarNameList = new ArrayList();
                    if (topDirectory.isFile()) {
                        jarNameList.add(topDirectory.getCanonicalPath());
                        indexJarFile(topDirectory.getCanonicalPath());
                    } else {
                        scanDirectory(topDirectory, jarNameList);

                    }
                    Collections.sort(jarNameList);
                    jarFilePanel.setJarList(jarNameList);
                    Configuration.setProperty(CONFIG_LAST_FILE, treeRoot1);
                    lastFileMI.setText(treeRoot1);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.setString("failed to parse path: " + treeRoot1);
                    GUIUtil.messageBoxWithDetails(JarExplorer.this, "Error Condition:", e);
                } finally {
                    progressBar.setValue(0);
                    progressBar.setIndeterminate(false);
                    progressBar.setString("done path parsing");
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    GUIUtil.getMainFrame().setTitle(JarExplorer.APP_NAME + " : " + treeRoot1);
                    GUIUtil.getMainFrame().repaint();
                }
            }
        };

        new Thread(r, "Parsing Thread").start();
    }

    private static void appInit() {

        // Application name and version from POM
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            model = reader.read(new FileReader("pom.xml"));
        }
        catch (Exception e) {
            // something went wrong with reading the pom, just use a default
            // e.printStackTrace();
            model = new Model();
            model.setName("JarExplorer");
            model.setVersion("unknown version");
        }
        APP_NAME = model.getName() + " " + model.getVersion();

        // Configuration setup
        Configuration.setFileName(System.getProperty("user.home") + "/" + ".JarExplorer.properties",
                model.getName());
        // Configuration defaults
        if (Configuration.getProperty(CONFIG_ZIP_EXTENSIONS) == null) {
            Configuration.setProperty(CONFIG_ZIP_EXTENSIONS, "jar,zip,war,ear,rar");
        }
    }

    public static void main(final String[] args) {
        appInit();

        final JarExplorer je = new JarExplorer();
        je.setSize(new Dimension(800, 600));
        je.repaint();
        je.setLocationRelativeTo(null);
        je.setVisible(true);

        if (args.length == 1) {
            je.scanPath(new File(args[0]));
        }
    }
}