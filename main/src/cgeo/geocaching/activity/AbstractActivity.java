package cgeo.geocaching.activity;

import cgeo.geocaching.R;
import cgeo.geocaching.Settings;
import cgeo.geocaching.cgBase;
import cgeo.geocaching.cgCache;
import cgeo.geocaching.cgeoapplication;
import cgeo.geocaching.compatibility.Compatibility;
import cgeo.geocaching.network.Network;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class AbstractActivity extends Activity implements IAbstractActivity {

    private String helpTopic;

    protected cgeoapplication app = null;
    protected Resources res = null;
    private boolean keepScreenOn = false;

    protected AbstractActivity() {
        this(null);
    }

    protected AbstractActivity(final String helpTopic) {
        this.helpTopic = helpTopic;
    }

    protected AbstractActivity(final String helpTopic, final boolean keepScreenOn) {
        this(helpTopic);
        this.keepScreenOn = keepScreenOn;
    }

    final public void goHome(final View view) {
        ActivityMixin.goHome(this);
    }

    public void goManual(final View view) {
        ActivityMixin.goManual(this, helpTopic);
    }

    final public void setTitle(final String title) {
        ActivityMixin.setTitle(this, title);
    }

    final public void showProgress(final boolean show) {
        ActivityMixin.showProgress(this, show);
    }

    final public void setTheme() {
        ActivityMixin.setTheme(this);
    }

    public final void showToast(String text) {
        ActivityMixin.showToast(this, text);
    }

    public final void showShortToast(String text) {
        ActivityMixin.showShortToast(this, text);
    }

    public final void helpDialog(final String title, final String message) {
        ActivityMixin.helpDialog(this, title, message);
    }

    public final void helpDialog(final String title, final String message, final Drawable icon) {
        ActivityMixin.helpDialog(this, title, message, icon);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init
        res = this.getResources();
        app = (cgeoapplication) this.getApplication();

        cgBase.initialize(app);

        // Restore cookie store if needed
        Network.restoreCookieStore(Settings.getCookieStore());

        ActivityMixin.keepScreenOn(this, keepScreenOn);
    }

    public void addVisitMenu(Menu menu, cgCache cache) {
        ActivityMixin.addVisitMenu(this, menu, cache);
    }

    protected static void disableSuggestions(final EditText edit) {
        Compatibility.disableSuggestions(edit);
    }

    protected void restartActivity() {
        Compatibility.restartActivity(this);
    }

    public void invalidateOptionsMenuCompatible() {
        ActivityMixin.invalidateOptionsMenu(this);
    }

    public static LinearLayout createStarRating(final float value, final int count, final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout starsContainer = new LinearLayout(context);
        starsContainer.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < count; i++) {
            ImageView star = (ImageView) inflater.inflate(R.layout.star, null);
            if (value - i >= 0.75) {
                star.setImageResource(R.drawable.star_on);
            } else if (value - i >= 0.25) {
                star.setImageResource(R.drawable.star_half);
            } else {
                star.setImageResource(R.drawable.star_off);
            }
            starsContainer.addView(star);
        }

        return starsContainer;
    }

    /**
     * insert text into the EditText at the current cursor position
     * 
     * @param editText
     * @param insertText
     * @param moveCursor
     *            place the cursor after the inserted text
     */
    public static void insertAtPosition(final EditText editText, final String insertText, final boolean moveCursor) {
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        final String content = editText.getText().toString();
        String completeText;
        if (start > 0 && !Character.isWhitespace(content.charAt(start - 1))) {
            completeText = " " + insertText;
        } else {
            completeText = insertText;
        }

        editText.getText().replace(start, end, completeText);
        int newCursor = moveCursor ? start + completeText.length() : start;
        editText.setSelection(newCursor, newCursor);
    }

}
