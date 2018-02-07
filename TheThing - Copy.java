import javax.swing.*;
import java.net.*;
import java.util.Scanner;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by deanl on 2/6/2018.
 */
public class TheThing {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Mining Rigs");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Wrapper stuff = null;
        frame.setVisible(true);
        frame.setResizable(false);
        try {
            stuff = getStuff();
        } catch (IOException lol) {
                System.out.println(lol.toString());
        }
        int numRigs = stuff.data.length;
        JLabel[][] Labels = new JLabel[numRigs+1][6];
        frame.setSize(595, numRigs * 30 + 120);
        generateLabels(frame);
        for (int i = 0; i < numRigs + 1; i++) {
            Labels[i][0] = new JLabel();
            Labels[i][0].setBounds(10,40 + i*30,100,30);
            frame.add(Labels[i][0]);

            Labels[i][1] = new JLabel();
            Labels[i][1].setBounds(70,40 + i*30,100,30);
            frame.add(Labels[i][1]);

            Labels[i][2] = new JLabel();
            Labels[i][2].setBounds(190,40 + i*30,100,30);
            frame.add(Labels[i][2]);

            Labels[i][3] = new JLabel();
            Labels[i][3].setBounds(310,40 + i*30,100,30);
            frame.add(Labels[i][3]);

            Labels[i][4] = new JLabel();
            Labels[i][4].setBounds(410,40 + i*30,100,30);
            frame.add(Labels[i][4]);

            Labels[i][5] = new JLabel();
            Labels[i][5].setBounds(510,40 + i*30,100,30);
            frame.add(Labels[i][5]);
        }
        frame.add(new JLabel());
        while (true) {
            long[] totals = new long[5];
            for (int i = 0; i < numRigs; i++) {
                RigData bar = stuff.data[i];
                totals[0] += bar.currentHashrate;
                totals[1] += bar.averageHashrate;
                totals[2] += bar.validShares;
                totals[3] += bar.staleShares;
                totals[4] += bar.invalidShares;
                Labels[i][0].setText(bar.worker);
                Labels[i][1].setText(String.format("%.1f MH/s",bar.currentHashrate/1000000));
                Labels[i][2].setText(String.format("%.1f MH/s",bar.averageHashrate/1000000));
                int totalShares = bar.validShares + bar.invalidShares + bar.staleShares;
                Labels[i][3].setText(String.format("%d%%",(int) (100.0 * bar.validShares / totalShares + 0.5)));
                Labels[i][4].setText(String.format("%d%%",(int) (100.0 * bar.staleShares / totalShares + 0.5)));
                Labels[i][5].setText(String.format("%d min ago", (int)((System.currentTimeMillis()/1000 - bar.lastSeen)/60.0+0.5)));
            }
            Labels[numRigs][0].setText("Total");
            Labels[numRigs][1].setText(String.format("%.1f MH/s",(float)totals[0]/1000000));
            Labels[numRigs][2].setText(String.format("%.1f MH/s",(float)totals[1]/1000000));
            long totalShares = totals[2] + totals[3] + totals[4];
            Labels[numRigs][3].setText(String.format("%d%%",(int) (100.0 * totals[2] / totalShares + 0.5)));
            Labels[numRigs][4].setText(String.format("%d%%",(int) (100.0 * totals[3] / totalShares + 0.5)));

            Thread.sleep(30000);

            try {
                stuff = getStuff();
            } catch (IOException lol) {
                System.out.println(lol.toString());
            }
        }
    }

    private static Wrapper getStuff() throws IOException {
        URL url = new URL("https://api.ethermine.org/miner/:0486eA95a1e54E512925Ed3D6E049Fb752Ac988f/workers");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.getResponseCode();
        String out = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
        connection.disconnect();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(out, Wrapper.class);
    }

    private static void generateLabels(JFrame frame) {
        JLabel foo;
        foo = new JLabel("Worker");
        foo.setBounds(10,10,100,30);
        frame.add(foo);

        foo = new JLabel("Current Hashrate");
        foo.setBounds(70,10,100,30);
        frame.add(foo);

        foo = new JLabel("Average Hashrate");
        foo.setBounds(190,10,110,30);
        frame.add(foo);

        foo = new JLabel("Valid Shares");
        foo.setBounds(310,10,100,30);
        frame.add(foo);

        foo = new JLabel("Stale Shares");
        foo.setBounds(410,10,100,30);
        frame.add(foo);

        foo = new JLabel("Last Seen");
        foo.setBounds(510,10,100,30);
        frame.add(foo);

        foo = new JLabel("");
        frame.add(foo);

        frame.validate();
    }
}
