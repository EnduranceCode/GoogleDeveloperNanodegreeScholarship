package com.endurancecode.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    /* Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /* This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
        }
    };

    /**
     * Clean up the media player by releasing its resources
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            /* Regardless of the current state of the media player, release its resources
             * because we no longer need it.
             */
            mMediaPlayer.release();

            /* Set the media player back to null. For our code, we've decided that
             * setting the media player to null is an easy way to tell that the media player
             * is not configured to play an audio file at the moment.
             */
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        /* Create an ArrayList that stores objects of type Word. Those objects consist of pair
         * of words: one word is the Miwoki word and the other it's its default translation
         * The Word objects are instantiated with the Word class.
         */
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));

        /* Create a custom {@link WordAdapter}, whose data source is a list of Word objects. The
         * adapter uses list_item.xml layout to create layouts for each item in the list.
         * This list item layout contains two {@link TextView}, which the adapter will set to
         * display the two words contained by each Word object in the ArrayList words.
         */
        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_numbers);

        /* Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
         * There should be a {@link ListView} with the view ID called list, which is declared in the
         * word_list.xml file.
         */
        ListView listView = (ListView) findViewById(R.id.list);

        /* Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
         * {@link ListView} will display list items for each word in the list of words.
         * Do this by calling the setAdapter method on the {@link ListView} object and pass in
         */
        listView.setAdapter(wordAdapter);

        /* Set OnItemClickListener in the ListView items */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* Gets the selected word object */
                Word selectedWord = words.get(position);
                /* TODO Study the deference with
                 * Word selectedWord = (Word) parent.getItemAtPosition(position);
                 */

                /* Release MediaPlayer resources before initialize it to play a new audio */
                releaseMediaPlayer();

                /* Creates the MediaPlayer object */
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, selectedWord.getAudioResourceId());
                mMediaPlayer.start();

                /* Set an OnCompletionListener method to release MediaPlayer
                 * when audio is finishes playing
                 */
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        releaseMediaPlayer();
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        /* Release the MediaPlayer resources when the activity is stopped */
        releaseMediaPlayer();
    }
}
