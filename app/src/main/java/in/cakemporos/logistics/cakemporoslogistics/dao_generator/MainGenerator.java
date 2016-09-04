package in.cakemporos.logistics.cakemporoslogistics.dao_generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.frostox.doughnuts.dbase");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity key = addKey(schema);


    }

    private static Entity addKey(final Schema schema) {
        Entity key = schema.addEntity("Key");
        key.addIdProperty().primaryKey().autoincrement();
        key.addStringProperty("access").notNull();
        key.addStringProperty("refresh").notNull();

        return key;
    }


}