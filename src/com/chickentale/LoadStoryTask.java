package com.chickentale;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.story.Story;

/**
 * Async task to load stories from JSON files
 */
public class LoadStoryTask extends AsyncTask<Context, Integer, Story>
{
	// Weak reference to the activity 
	private WeakReference<StoryActivity> activity;
	
	/**
	 * Constructs a task to load stories for the given activity
	 * @param activity activity to load the story for
	 */
	LoadStoryTask(StoryActivity activity)
	{
		this.activity = new WeakReference<StoryActivity>(activity);
	}
	
	@Override
	protected void onPreExecute()
	{
		// Start the progress dialog
		activity.get().getProgressDialog().show();
	}
	
	@Override
	protected Story doInBackground(Context... params)
	{
		try
		{
			// Fake load time...
			for (int x = 0; x < 100; x++)
			{
				Thread.sleep(10);
				activity.get().getProgressDialog().setProgress(x);
			}
			
			// Initialize the story from a JSON file (shouldn't explicitly declare a file name here)
			ObjectMapper mapper = new ObjectMapper();
			InputStream something = params[0].getAssets().open("Story.json");
			return mapper.readValue(something, Story.class);
		}
		catch (Exception e)
		{
			Log.d("LoadStoryTask Error", "Unable to load the story.");
			return null;
		}
	}

	@Override
	protected void onPostExecute(Story result)
	{
		// Kill the progress dialog
		activity.get().getProgressDialog().dismiss();
		
		// If we didn't get a story, kill the app
		if (result == null)
		{
			activity.get().finish();
			return;
		}
		
		// Update data and views with loaded story
		activity.get().setStory(result);		
		activity.get().updateTextViews();
	}
}
