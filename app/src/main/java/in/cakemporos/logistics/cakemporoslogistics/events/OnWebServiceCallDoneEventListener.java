package in.cakemporos.logistics.cakemporoslogistics.events;

/**
 * Created by roger on 10/8/16.
 */
public interface OnWebServiceCallDoneEventListener {

    /**
     * Triggered when a web service call returns a response
     * @param message_id carries information about the response of the call
     * @param code carries error code if it exists
     * @param args carries objects returned by the service call
     */
    void onDone(int message_id, int code, Object... args);

    /**
     * Displays contingency error message when unexpected error occurs
     * @param code
     */
    void onContingencyError(int code);

    /**
     * Displays an error message
     * @param message_id Resource id of the string message
     * @param code error code
     * @param args parameters for the string resource
     */
    void onError(int message_id, int code, String... args);
}
