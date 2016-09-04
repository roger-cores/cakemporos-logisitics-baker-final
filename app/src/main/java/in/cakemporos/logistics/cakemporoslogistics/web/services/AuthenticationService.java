package in.cakemporos.logistics.cakemporoslogistics.web.services;


/**
 * Created by roger on 10/8/16.
 */



import android.app.Activity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Key;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Error;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.ValidateRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class AuthenticationService {

    public static void getMyInfo(final Activity activity,
                                 final Retrofit retrofit,
                                 final AuthenticationEndPoint authenticationEndPoint,
                                 final OnWebServiceCallDoneEventListener event){

        final Call<Baker> getMyInfoCall = authenticationEndPoint.getMyInfo(Utility.getKey(activity).getAccess());

        getMyInfoCall.enqueue(new Callback<Baker>() {
            @Override
            public void onResponse(Call<Baker> call, retrofit2.Response<Baker> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){
                    event.onContingencyError(0);
                }
                else {
                    Baker baker = response.body();
                    if(baker!=null) {
                        event.onDone(R.string.success, 0, baker);
                    } else {
                        event.onContingencyError(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Baker> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });

    }


    public static void validateAccessToken(final Activity activity,
                                           final Retrofit retrofit,
                                           final boolean validateRefresh,
                                           Key key,
                                           final AuthenticationEndPoint authenticationEndPoint,
                                           final OnWebServiceCallDoneEventListener event){

        if(key.getAccess().equals("")) {
            getTokenByRefresh(activity, retrofit, key, authenticationEndPoint, event);
            return;
        }

        final ValidateRequest validateRequest = new ValidateRequest(key.getAccess());
        final Key key1 = key;
        final Call<Response> validateCall = authenticationEndPoint.validateToken(validateRequest);

        validateCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {


                if(response != null && !response.isSuccessful() && response.errorBody() != null){
                    //Branch: Error
                    Converter<ResponseBody, Error> errorConverter =
                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                    try {
                        Error error = errorConverter.convert(response.errorBody());
                        switch (error.getError()){
                            case "Unauthorized":
                                //token expired
                                //try refresh token
                                if(validateRefresh) {
                                    getTokenByRefresh(activity, retrofit, key1, authenticationEndPoint, event);
                                }
                                else {
                                    event.onError(R.string.error_session, 1);
                                    //TODO end of chain
                                }
                                break;
                            case "Contingency":
                                event.onContingencyError(0);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        event.onContingencyError(0);
                    }
                    return;
                }


                Response validationResponse = response.body();
                if(validationResponse!=null && validationResponse.getCode() != null && validationResponse.getCode() == 1) {
                    //Branch: Success
                    //TODO end of chain
                    event.onDone(R.string.success, 0);
                } else {
                    //Branch: Unexpected Error
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {


                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }


    public static void getTokenByRefresh(final Activity activity,
                                         final Retrofit retrofit,
                                         final Key key,
                                         final AuthenticationEndPoint authenticationEndPoint,
                                         final OnWebServiceCallDoneEventListener event){

        if(key.getRefresh().equals("")) {
            //TODO end of chain - default
            event.onError(R.string.please_sign_in, -1);
            return;
        }



        AuthRequest loginRequest = new AuthRequest(key.getRefresh(), activity);
        Call<AuthResponse> refreshToken = authenticationEndPoint.getToken(loginRequest);

        refreshToken.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {

                if(response != null && !response.isSuccessful() && response.errorBody() != null) {

                    Converter<ResponseBody, Error> errorConverter =
                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                    try {
                        Error error = errorConverter.convert(response.errorBody());

                        switch (error.getError()){
                            case "invalid_request":
                            case "invalid_grant":
                                event.onError(R.string.error_session, 0);
                                //TODO end of chain
                                break;


                            case "unauthorized_client":
                            case "unsupported_grant_type":
                            case "invalid_scope":
                            case "server_error":
                            case "invalid_client":
                                event.onContingencyError(0);
                                break;
                        }


                    } catch (IOException e){
                        e.printStackTrace();
                        event.onContingencyError(0);
                    }

                    return;
                }

                if(response != null && response.body() != null){
                   Utility.updateKey(activity.getApplicationContext(), response.body().getAccessToken(), response.body().getRefreshToken());
                    //validate again
                    validateAccessToken(activity, retrofit, false, key, authenticationEndPoint, event);
                }  else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);

            }
        });
    }

    public static void getTokenByPassword(final Activity activity,
                                          final Retrofit retrofit,
                                          final AuthenticationEndPoint authenticationEndPoint,
                                          String email,
                                          String password,
                                          final OnWebServiceCallDoneEventListener event){

        AuthRequest loginRequest = new AuthRequest(email, password, activity);
        Call<AuthResponse> login = authenticationEndPoint.getToken(loginRequest);

        login.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){

                    //Branch: Error
                    Converter<ResponseBody, Error> errorConverter =
                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                    try {
                        Error error = errorConverter.convert(response.errorBody());
                        if(error != null && error.getErrorDescription() != null && error.getErrorDescription().equals(activity.getString(R.string.invalid_credentials))){
                            event.onError(R.string.invalid_user_credentials, 0);
                        } else event.onContingencyError(0);
                    } catch(IOException e){
                        e.printStackTrace();
                        event.onContingencyError(0);
                    }

                } else if(response != null && response.body() != null && response.body().getAccessToken() != null && response.body().getRefreshToken() != null){
                    //Branch: Success | Go to validate
                    Utility.updateKey(activity, response.body().getAccessToken(), response.body().getRefreshToken());
                    validateAccessToken(activity, retrofit, false, Utility.getKey(activity), authenticationEndPoint, event);
                } else {
                    //Branch: Unexpected Error
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });

    }
}
