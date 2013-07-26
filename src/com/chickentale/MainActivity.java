package com.chickentale;

import java.io.InputStream;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.story.Story;

/**
 * Main activity that waits for user swipes and updates stories accordingly
 */
public class MainActivity extends Activity
{
	// The current story to load
	private Story story;
	
	// Views that can change based on the current story
	private TextView storyView;
	private TextView firstActionView;
	private TextView firstActionTipView;
	private TextView secondActionView;
	private TextView secondActionTipView;
	private TextView instructionView;
	
	// Used to keep track of swipe direction
	private float previousX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Wire up our views after setting the layout
		storyView = (TextView) findViewById(R.id.story);
		firstActionView = (TextView) findViewById(R.id.firstAction);
		firstActionTipView = (TextView) findViewById(R.id.firstActionTip);
		secondActionView = (TextView) findViewById(R.id.secondAction);
		secondActionTipView = (TextView) findViewById(R.id.secondActionTip);
		instructionView = (TextView) findViewById(R.id.instruction);
		
		// Initialize our story
		resetStory();
		
		// Update the views
		updateTextViews();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(e.getActionMasked())
		{
			// Start tracking the X position of the mouse 
			case MotionEvent.ACTION_DOWN:
				previousX = e.getX();
				break;
				
			// Figure out the delta X and act accordingly
			case MotionEvent.ACTION_UP:
				// If the story is over, reset the story
				if (story.isTheEnd())
				{
					resetStory();
					break;
				}
				
				// Get the delta X and update the story
				float dx = e.getX() - previousX;
				story = dx <= 0 ? story.getFirstAction() : story.getSecondAction();
				break;
				
			default:
				return false;
		}
		
		updateTextViews();
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.quit:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Reinitializes the story
	 */
	private void resetStory()
	{
		try
		{
			// Initialize the story from a JSON file
			ObjectMapper mapper = new ObjectMapper();
			InputStream something = getAssets().open("Story.json");
			story = mapper.readValue(something, Story.class);
		}
		catch (Exception e)
		{
			Log.d("Fatal Error", "Unable to load the story.");
			finish();
		}
	}
	
	/**
	 * Updates the views with the current story's description and actions
	 */
	private void updateTextViews()
	{
		storyView.setText(story.getStoryDescription());
		firstActionView.setText(story.getFirstAction() != null ? story.getFirstAction().getActionDescription() : "");
		firstActionTipView.setText(story.getFirstAction() != null ? getString(R.string.first_action_tip) : "");
		secondActionView.setText(story.getSecondAction() != null ? story.getSecondAction().getActionDescription() : "");
		secondActionTipView.setText(story.getSecondAction() != null ? getString(R.string.second_action_tip) : "");
		instructionView.setText(story.isTheEnd() ? getString(R.string.the_end_instructions) : getString(R.string.story_instructions));
	}
}
