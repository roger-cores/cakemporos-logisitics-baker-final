package in.cakemporos.logistics.cakemporoslogistics.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Roger Cores on 1/8/16.
 */
public class Utility {

    /**
     * Gets stored access token and refresh token if they exist
     * @return @{Key} object if it exists, else null
     */
    public static Key getKey(Context context){
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "key-db", null).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        KeyDao keyDao = daoSession.getKeyDao();
        Key key = null;
        List<Key> keys = keyDao.queryBuilder().limit(1).list();
        if(!keys.isEmpty()){
            key = keys.get(0);
        }
        if(key == null){
            Utility.updateKey(context, "", "");
            keys = keyDao.queryBuilder().limit(1).list();
            if(!keys.isEmpty()){
                key = keys.get(0);
            }
        }
        closeDb(daoMaster);
        return key;
    }

    /**
     * Update refresh token and access token
     * @param context
     * @param accessToken
     * @param refreshToken
     * @return true of successful, else false
     */
    public static boolean updateKey(Context context, String accessToken, String refreshToken){
        boolean result = false;
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "key-db", null).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        KeyDao keyDao = daoSession.getKeyDao();
        Key key;
        List<Key> keys = keyDao.queryBuilder().limit(1).list();
        if(!keys.isEmpty()){
            key = keys.get(0);
            key.setAccess(accessToken);
            key.setRefresh(refreshToken);
            keyDao.update(key);
            result = true;
        } else {
            key = new Key();
            key.setAccess(accessToken);
            key.setRefresh(refreshToken);
            keyDao.insert(key);
            result = true;
        }

        closeDb(daoMaster);
        return result;
    }


    /**
     * Insert keys (default)
     * Used to create the record at first app-run
     * @param context
     * @param accessToken
     * @param refreshToken
     */
    public static void insertKey(Context context, String accessToken, String refreshToken) {
        boolean result = false;
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "key-db", null).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        KeyDao keyDao = daoSession.getKeyDao();
        Key key = new Key();
        key.setAccess(accessToken);
        key.setRefresh(refreshToken);
        keyDao.insert(key);
        closeDb(daoMaster);
    }

    public static void closeDb(DaoMaster daoMaster){
        if(daoMaster.getDatabase()!=null && daoMaster.getDatabase().isOpen())
            daoMaster.getDatabase().close();
    }

}
