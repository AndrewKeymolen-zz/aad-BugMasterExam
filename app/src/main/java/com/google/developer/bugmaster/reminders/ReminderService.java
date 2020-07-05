package com.google.developer.bugmaster.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.developer.bugmaster.QuizActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;

import java.util.ArrayList;
import java.util.Random;

import static com.google.developer.bugmaster.QuizActivity.ANSWER_COUNT;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;

public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create action intent
        Intent action = new Intent(this, QuizActivity.class);

        //Feature Task 10
        ArrayList<Insect> insects = new ArrayList<>();
        Cursor mCursor = DatabaseManager.getInstance(getApplicationContext()).queryAllInsects(null);
        int cursorSize = mCursor.getCount();
        final Random rand = new Random();
        int position;
        Insect insect;

        for (int i = 0; i < ANSWER_COUNT; i++) {
            position = rand.nextInt(cursorSize);
            mCursor.moveToPosition(position);
            insect = new Insect(mCursor);
            insects.add(insect);
        }

        action.putParcelableArrayListExtra(EXTRA_INSECTS, insects);
        action.putExtra(EXTRA_ANSWER, insects.get(rand.nextInt(ANSWER_COUNT)));

        //Debugging Task 3
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(QuizActivity.class);
        stackBuilder.addNextIntent(action);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_bug_empty)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);

    }
}
