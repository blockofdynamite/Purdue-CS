/**
 * A program to illustrate GUI lockup.  
 *
 * Set N to large enough for 10+/- second compute time.  Observe GUI freezing while running.
 *
 * @author jtk
 * @date 3/31/13
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        new View(model);
    }
}

class LockUpFix implements Runnable {
    final static long N = 50;  // controls the compute time

    private JLabel status;   // note: this is extremely bad coding practice -- the only reason this exists is for testing
    private JButton count;   // "
    private JButton compute; // "
    private long f = 0;
    private int counter = 0;
    private long t = 0;

    /**
     * Start the program, creating the GUI using the EDT.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LockUpFix());
    }

    /**
     * Create the GUI and provide the ActionListeners.
     */
    @Override
    public void run() {
        final LockUpFix view = this;

        // Create the top-level frame...
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Create the status area and buttons...
        status = new JLabel("Press a button...");
        count = new JButton("Count");
        compute = new JButton("Compute");

        // ActionListener for the "Count" button...
        count.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                updateStatus();
            }
        });

        // ActionListener for the "Compute" button...
        compute.addActionListener(new ActionListener() {
            @SuppressWarnings("rawtypes")
            @Override
            public void actionPerformed(ActionEvent e) {
                (new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        compute.setEnabled(false);
                        long t1 = System.currentTimeMillis();
                        f = fibonacci(N);
                        long t2 = System.currentTimeMillis();
                        t = t2 - t1;
                        return null;
                    }

                    @Override
                    protected void done() {
                        updateStatus();
                        compute.setEnabled(true);
                    }
                }).execute();
            }
        });

        // Create a border for the status area...
        Border b = BorderFactory.createTitledBorder("Status");
        status.setBorder(b);

        // Get the default (preferred) size of the status field (label), then multiply
        // width by 4 to allow it to expand for larger message...
        Dimension d = status.getPreferredSize();
        d.width *= 4;  // arbitrary choice to make it "big enough"
        status.setPreferredSize(d);

        // Structure layout with a panel, add to the frame, and display...
        JPanel panel = new JPanel();
        panel.add(count);
        panel.add(compute);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(status, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Compute the nth element of the Fibonacci sequence in a particularly slow (recursive) way.
     */
    long fibonacci(long n) {
        if (n <= 2)
            return 1;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * Update the status area of the display.  Must be run on the EDT.
     */
    void updateStatus() {
        status.setText(String.format("count = %d; fibonacci(%d) = %d (in %d ms)", counter, N, f, t));
    }
}

class Model {
    final static long N = 50;  // controls the compute time
    private long f = 0;
    private int counter = 0;
    private long t = 0;
}

class View implements Runnable {
    private JButton count;
    private JButton compute;
    private JLabel status;
    Model model;

    View(Model model) {
        this.model = model;
        SwingUtilities.invokeLater(this);
    }


    @Override
    public void run() {
        // Create the top-level frame...
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Create the status area and buttons...
        status = new JLabel("Press a button...");
        count = new JButton("Count");
        compute = new JButton("Compute");

        // Create a border for the status area...
        Border b = BorderFactory.createTitledBorder("Status");
        status.setBorder(b);

        // Get the default (preferred) size of the status field (label), then multiply
        // width by 4 to allow it to expand for larger message...
        Dimension d = status.getPreferredSize();
        d.width *= 4;  // arbitrary choice to make it "big enough"
        status.setPreferredSize(d);

        // Structure layout with a panel, add to the frame, and display...
        JPanel panel = new JPanel();
        panel.add(count);
        panel.add(compute);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(status, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}

class ComputeController extends SwingWorker<Void, Void> implements ActionListener {
    Model model;
    View view;

    ComputeController(Model m, View v) {
        model = m;
        view = v;
    }


    protected Object doInBackground() throws Exception {
        compute.setEnabled(false);
        long t1 = System.currentTimeMillis();
        f = fibonacci(N);
        long t2 = System.currentTimeMillis();
        t = t2 - t1;
        return null;
    }

    @Override
    protected void done() {
        updateStatus();
        compute.setEnabled(true);
    }
}).execute();

        }