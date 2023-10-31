import models.JDataFrame;
import models.JdfSelector;

public class Application {
    public static void main(String[] args) {
        // read csv file and turn it into JPandas object
        String path = "C:\\Users\\chenr\\IdeaProjects\\java-projects\\JPandas\\src\\csv\\AAPL-US-D-20231028.csv";
        JDataFrame df = JPandas.readCsv(path);

        // select columns
        JDataFrame df2 = df.selectColumns(new String[]{"date", "open", "high", "low", "close", "volume"});

        // select rows
        JDataFrame df3 = df2.selectRows(0, 50);

        // where clause using selector
        JdfSelector selector = new JdfSelector();
        selector.greaterThan("close", 150).lessThan("open", 150);
        JDataFrame df4 = df3.selectIf(selector);
        System.out.println(df4);
    }
}
