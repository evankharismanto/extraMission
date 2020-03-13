package db.migration;

import lombok.SneakyThrows;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.flywaydb.core.api.migration.*;
import java.sql.PreparedStatement;
import com.opencsv.*;
import java.util.*;
import java.io.*;

@EnableAutoConfiguration
public class V2__insert_base_vehicle extends BaseJavaMigration {
    private String fileLocation = "classpath:assets/csv/vehicles.csv";
    private String sqlCommand = "insert into m_vehicle(form,brand,model,color,power,powunit) select ?,?,?,?,?,? from dual";
    @SneakyThrows
    @Override
    public Integer getChecksum() {
        int result = fileLocation.hashCode();
        InputStream stream = ((new ClassPathXmlApplicationContext()).getResource(fileLocation)).getInputStream();
        result = 31 * result + stream.hashCode();
        result = 31 * result + sqlCommand.hashCode();
        return result;
        //return super.getChecksum();
    }

    public void migrate(Context context) throws Exception {
        Resource resource = (new ClassPathXmlApplicationContext()).getResource(fileLocation);
        List<String[]> records;
        List<String> items;
        try (
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(resource.getInputStream()));
        ) {
            //CSVReader csvReader = new CSVReader(bufferedReader);
//            CSVParser parser = new CSVParserBuilder()
//                    .withSeparator(',')
//                    .withIgnoreQuotations(true)
//                    .build();

            CSVReader csvReader = new CSVReaderBuilder(bufferedReader)
                    .withSkipLines(1)
                    .build();
            records = csvReader.readAll();
        }

        for (String[] record:records) {
            items = Arrays.asList(record);
            String form = items.get(0);
            String brand = items.get(1);
            String model = items.get(2);
            String color = items.get(3);
            String power = items.get(4);
            String powunit = items.get(5);
            try (PreparedStatement preparedInsert = context.getConnection()
                    .prepareStatement(sqlCommand)) {
                preparedInsert.setString(1, form);
                preparedInsert.setString(2, brand);
                preparedInsert.setString(3, model);
                preparedInsert.setString(4, color);
                preparedInsert.setFloat(5, Float.parseFloat(power));
                preparedInsert.setString(6, powunit);
                preparedInsert.execute();
            }
        }
    }
}