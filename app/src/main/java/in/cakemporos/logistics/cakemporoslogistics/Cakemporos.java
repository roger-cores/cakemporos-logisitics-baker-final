package in.cakemporos.logistics.cakemporoslogistics;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Created by roger on 9/17/2016.
 */
public class Cakemporos extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException ex) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }
    }
}
