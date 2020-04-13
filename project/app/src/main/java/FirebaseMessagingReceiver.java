import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size() > 0){
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
        if(remoteMessage.getNotification() != null){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }
    /*private*/
    public void showNotification(String title, String message){

    }
}
