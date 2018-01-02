package tech.pursuiters.techpursuiters.uitsocieties.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Shubhi on 12/29/2017..
 */

public class UiTvFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN ="REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recent_token= FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);
    }

}
