package com.chickentale;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.story.Story;

/**
 * Main activity that waits for user swipes and updates stories accordingly
 */
public class StoryActivity extends FragmentActivity
{
	// The current story to load
	private Story story;
	
	// The progress bar to show users
	private ProgressDialogFragment progressDialog;
	
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
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		ProgressDialogFragment dialog = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(getProgressDialogTag());
		if (dialog != null)
		{
			dialog.dismiss();	
		}
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
	
	public String getProgressDialogTag()
	{
		return getString(R.string.progress_dialog_fragment_tag);
	}
	
	/**
	 * Gets the story file path
	 * @return the story file path
	 */
	public String getStoryFilePath()
	{
		return getString(R.string.story_file_path);
	}
	
	/**
	 * Sets the story for this activity
	 * @param story the story to set for this activity
	 */
	public void setStory(Story story)
	{
		this.story = story;
	}
	
	/**
	 * Reinitializes the story
	 */
	private void resetStory()
	{
		// Initialize progress dialog
		progressDialog = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(getProgressDialogTag());
		if (progressDialog == null)
		{
			progressDialog = new ProgressDialogFragment();
			progressDialog.show(this.getSupportFragmentManager(), getProgressDialogTag());
		}
		
		new LoadStoryTask(this).execute(getAssets());
	}
	
	/**
	 * Updates the views with the current story's description and actions
	 */
	public void updateTextViews()
	{
		storyView.setText(story.getStoryDescription());
		firstActionView.setText(story.getFirstAction() != null ? story.getFirstAction().getActionDescription() : "");
		firstActionTipView.setText(story.getFirstAction() != null ? getString(R.string.first_action_tip) : "");
		secondActionView.setText(story.getSecondAction() != null ? story.getSecondAction().getActionDescription() : "");
		secondActionTipView.setText(story.getSecondAction() != null ? getString(R.string.second_action_tip) : "");
		instructionView.setText(story.isTheEnd() ? getString(R.string.the_end_instructions) : getString(R.string.story_instructions));
	}
}
